package com.dfh.support.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.LoadingActivity;
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.adapter.AdvertisementListAdapter;
import com.dfh.support.activity.adapter.BannerPagerAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.activity.widget.LoadListView;
import com.dfh.support.anmi.ZoomOutPageTransformer;
import com.dfh.support.compose.UmentBroadcastReceiver;
import com.dfh.support.entity.AdvertisementData;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment implements LoadListView.ILoadListener2 {

    private View mFragmentView;
    private ViewPager mVpBanner;
    private LoadListView mLvAdvertisement;//lv_advertisement
    private AdvertisementListAdapter mAdvertisementListAdapter;
    private ArrayList<AdvertisementData> mAdvertisementList;
    private ArrayList<View> mViewList;
    private BannerPagerAdapter mBannerPagerAdapter;
    private List<ImageView> mDots;//定义一个集合存储五个dot
    private int oldPosition;//记录当前点的位置。

    private static RecommendFragment s_instance;
    private static final int BANNER_START_SLITHER = 0;
    private static final int BANNER_STOP_SLITHER = 1;
    private static int slither_time = 6 * 1000;//5S
    private int mBannerPosition = 0;
    private static final int AD_BANNER_SUCCESS = 2;
    private static final int AD_BANNER_FALSE = 3;
    private static final int GET_BANNER_AD = 4;
    private static final int AD_LIST_SUCCESS = 5;
    private static final int AD_LIST_FALSE = 6;
    private static final int GET_LIST_AD = 7;
    private static final int REPECT_GET = 30*1000;
    private static final int RECEIVER_UPDATE = 8;
    private DisplayImageOptions options;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BANNER_START_SLITHER:
                    if(mBannerPosition == mViewList.size()-1){
                        mBannerPosition = 0;
                    }else{
                        mBannerPosition = mBannerPosition + 1;
                    }
                    mVpBanner.setCurrentItem(mBannerPosition);
                    mHandler.sendEmptyMessageDelayed(BANNER_START_SLITHER,slither_time);
                    break;
                case AD_BANNER_SUCCESS:
                    //刷新banner
                    //updateViewPage();
                    updateViewPage();
                    break;
                case AD_BANNER_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_BANNER_AD,REPECT_GET);
                    break;
                case GET_BANNER_AD:
                    mAdsFindCarouselAllTask = new AdsFindCarouselAllTask();
                    mAdsFindCarouselAllTask.execute("");
                    break;
                case AD_LIST_SUCCESS:
                    //刷新list
                    mAdvertisementList.addAll(adListData.getAdvertisementDatas());
                    mAdvertisementListAdapter.setList(mAdvertisementList);
                    mLvAdvertisement.loadComplete();
                    break;
                case AD_LIST_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_LIST_AD,REPECT_GET);
                    mLvAdvertisement.loadComplete();
                    break;
                case GET_LIST_AD:
                    //ToastUtils.shortToast(getActivity(), HttpJsonAnaly.lastError);
                    mAdsFindGeneralPagerTask = new AdsFindGeneralPagerTask();
                    mAdsFindGeneralPagerTask.execute("");
                    break;
                case RECEIVER_UPDATE:
                    mAdvertisementList = new ArrayList<AdvertisementData>();
                    pageNo = 1;
                    mHandler.sendEmptyMessage(GET_BANNER_AD);
                    mHandler.sendEmptyMessage(GET_LIST_AD);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.printFragmentLog("RecommendFragment::onCreateView================");
        mFragmentView = inflater.inflate(R.layout.recommend_layout, null);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.bg_loading_top)
                .showImageForEmptyUri(R.mipmap.bg_load_false_top)
                .showImageOnFail(R.mipmap.bg_load_false_top).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        initViewPage();
        initView();
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UmentBroadcastReceiver.INTENT_MSG_STR);
        intentFilter.addAction(SupportApplication.ACTION_HTTP_RESULT);
        getActivity().registerReceiver(mHttpReceiver, intentFilter);
        return mFragmentView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mHttpReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.printFragmentLog("RecommendFragment::onResume================");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.printFragmentLog("RecommendFragment::onDetach================");
    }

    public static RecommendFragment newInstance() {
        if (null == s_instance)
            s_instance = new RecommendFragment();

        return s_instance;
    }

    private void initViewPage() {
        mVpBanner = (ViewPager) mFragmentView.findViewById(R.id.vp_banner);
        mVpBanner.setPageTransformer(true,new ZoomOutPageTransformer());
        LayoutInflater inflater = getLayoutInflater();
        mViewList = new ArrayList<View>();
//        LogUtil.printPushLog("initViewPage advertisementListData.getAdvertisementDatas().size()"
//                + advertisementListData.getAdvertisementDatas().size());
//        for(int i = 0 ;i<advertisementListData.getAdvertisementDatas().size();i++) {
//            View view = inflater.inflate(R.layout.banner_layout, null);
//            ImageView ivBanner = (ImageView) view.findViewById(R.id.iv_banner);
//            //ivBanner.setImageResource(R.mipmap.show_banner);
//            ImageLoader.getInstance().displayImage(advertisementListData.getAdvertisementDatas().get(i).getIcon(),ivBanner);
//            final int finalI = i;
//            ivBanner.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                    intent.putExtra("url",HttpConfig.GetHttpPolicyAdress()
//                            +advertisementListData.getAdvertisementDatas().get(finalI).getLink()
//                            +"?links="+advertisementListData.getAdvertisementDatas().get(finalI).getLikes()
//                            +"&browses="+advertisementListData.getAdvertisementDatas().get(finalI).getBrowses());
//                    intent.putExtra("id",advertisementListData.getAdvertisementDatas().get(finalI).getId());
//                    getActivity().startActivity(intent);
//                }
//            });
//            mViewList.add(view);
//        }
        LogUtil.printPushLog("initViewPage mViewList.size()" + mViewList.size());
        mBannerPagerAdapter = new BannerPagerAdapter(mViewList);
        mVpBanner.setAdapter(mBannerPagerAdapter);
        mHandler.sendEmptyMessageDelayed(BANNER_START_SLITHER,slither_time);

        mDots = new ArrayList<ImageView>();
        ImageView dotOne = (ImageView) mFragmentView.findViewById(R.id.iv_banner_position_one);
        ImageView dotTwo = (ImageView) mFragmentView.findViewById(R.id.iv_banner_position_two);
        ImageView dotThree = (ImageView) mFragmentView.findViewById(R.id.iv_banner_position_three);
        ImageView dotFour = (ImageView) mFragmentView.findViewById(R.id.iv_banner_position_four);
        ImageView dotFive = (ImageView) mFragmentView.findViewById(R.id.iv_banner_position_five);
        mDots.add(dotOne);
        mDots.add(dotTwo);
        mDots.add(dotThree);
        mDots.add(dotFour);
        mDots.add(dotFive);
        oldPosition = 0;
        mDots.get(oldPosition).setImageResource(R.mipmap.dot_press);

        mVpBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mDots.get(oldPosition).setImageResource(R.mipmap.dot_normal);
                mDots.get(position).setImageResource(R.mipmap.dot_press);
                oldPosition = position;
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateViewPage() {
        LayoutInflater inflater = getLayoutInflater();
        mViewList = new ArrayList<View>();
        for(int a = 0; a<5;a++){
            mDots.get(a).setVisibility(View.GONE);
        }
        for(int i = 0 ;i<advertisementListData.getAdvertisementDatas().size();i++) {
            if(i<5) {
                mDots.get(i).setVisibility(View.VISIBLE);
                View view = inflater.inflate(R.layout.banner_layout, null);
                ImageView ivBanner = (ImageView) view.findViewById(R.id.iv_banner);
                //ivBanner.setImageResource(R.mipmap.show_banner);
                ImageLoader.getInstance().displayImage(advertisementListData.getAdvertisementDatas().get(i).getIcon(), ivBanner,options);
                final int finalI = i;
                ivBanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", HttpConfig.GetHttpPolicyAdress() +
                                advertisementListData.getAdvertisementDatas().get(finalI).getLink()
                                + "? links=" + advertisementListData.getAdvertisementDatas().get(finalI).getLikes()
                                + "&browses=" + advertisementListData.getAdvertisementDatas().get(finalI).getBrowses());
                        intent.putExtra("id", advertisementListData.getAdvertisementDatas().get(finalI).getId());
                        getActivity().startActivity(intent);
                    }
                });
                mViewList.add(view);
            }
        }
        mBannerPagerAdapter.setList(mViewList);
        mBannerPosition = 0;
    }

    private void initView(){
        mAdvertisementList = new ArrayList<AdvertisementData>();
        mLvAdvertisement = (LoadListView) mFragmentView.findViewById(R.id.lv_advertisement);
        mAdvertisementListAdapter = new AdvertisementListAdapter(getActivity(),mAdvertisementList);
        mLvAdvertisement.setAdapter(mAdvertisementListAdapter);
        mHandler.sendEmptyMessage(GET_BANNER_AD);
        mHandler.sendEmptyMessage(GET_LIST_AD);
    }

    private AdvertisementListData advertisementListData;
    private AdsFindCarouselAllTask mAdsFindCarouselAllTask;

    @Override
    public void onLoad() {
        pageNo = pageNo+1;
        mHandler.sendEmptyMessage(GET_LIST_AD);
    }

    private class AdsFindCarouselAllTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                advertisementListData = HttpJsonSend.adsFindCarouselAll(getActivity());
                if (advertisementListData.isFlag()) {
                    mHandler.sendEmptyMessage(AD_BANNER_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(AD_BANNER_FALSE);
                }
                LogUtil.printPushLog("httpGet adsFindCarouselAll advertisementListData" + advertisementListData.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    private AdsFindGeneralPagerTask mAdsFindGeneralPagerTask;
    private AdvertisementListData adListData;
    private String pageSize = "20";
    private int pageNo = 1;

    private class AdsFindGeneralPagerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpJsonSend.adsFindGeneralPager(getActivity(), pageSize,String.valueOf(pageNo));
            return null;
        }
    }

    private HttpReceiver mHttpReceiver;

    public class HttpReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.printPushLog("mHttpReceiver action :" + action);
            if (action.equals(SupportApplication.ACTION_HTTP_RESULT)) {
                //先判断url
                String url = intent.getStringExtra("url");
                if (url != null && !TextUtils.isEmpty(url)) {
                    if (url.contains(HttpConfig.url_ads_find_general_pager)) {
                        boolean is_success = intent.getBooleanExtra("is_success", false);
                        LogUtil.printPushLog("mHttpReceiver url_ads_find_general_pager :" + is_success);
                        if (is_success) {
                            //解析data再发送结果
                            String data = intent.getStringExtra("data");
                            LogUtil.printPushLog("mHttpReceiver url_ads_find_general_pager data:" + data);
                            try {
                                adListData = HttpJsonAnaly.adsFindGeneralPager(data, getActivity());
                                if (adListData.isFlag()) {
                                    mHandler.sendEmptyMessage(AD_LIST_SUCCESS);
                                } else {
                                    mHandler.sendEmptyMessage(AD_LIST_FALSE);
                                }
                                LogUtil.printPushLog("mHttpReceiver adListData.toString :" + adListData.toString());
                            } catch (Exception e) {
                                mHandler.sendEmptyMessage(AD_LIST_FALSE);
                                e.printStackTrace();
                            }
                        } else {
                            mHandler.sendEmptyMessage(AD_LIST_FALSE);
                        }
                    }
                }
            }else if(action.equals(UmentBroadcastReceiver.INTENT_MSG_STR)){
                //先判断url
                String message = intent.getStringExtra(UmentBroadcastReceiver.UMENG_MESSAGE);
                if (message != null && !TextUtils.isEmpty(message)) {
                    //推送刷新RECEIVER_UPDATE
                    LogUtil.printPushLog("SupportApplication::Recommend RECEIVER_UPDATE");
                    mHandler.sendEmptyMessage(RECEIVER_UPDATE);
                }
            }
        }

    }
}
