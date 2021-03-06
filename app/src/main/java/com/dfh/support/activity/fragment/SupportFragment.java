package com.dfh.support.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dfh.support.R;
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.adapter.AfterSaleMoreAdapter;
import com.dfh.support.activity.adapter.BannerPagerAdapter;
import com.dfh.support.activity.adapter.ChangeOfPurchaseMoreAdapter;
import com.dfh.support.activity.adapter.TroubleShootingAdapter;
import com.dfh.support.activity.support.AboutUsActivity;
import com.dfh.support.activity.support.BuyingShopsDetailActivity;
import com.dfh.support.activity.support.BuyingShopsListActivity;
import com.dfh.support.activity.support.CallPhoneActivity;
import com.dfh.support.activity.support.DebuggingListActivity;
import com.dfh.support.activity.support.DebuggingMenuActivity;
import com.dfh.support.activity.support.SearchActivity;
import com.dfh.support.activity.support.ServiceDetailActivity;
import com.dfh.support.activity.support.ServiceListActivity;
import com.dfh.support.activity.support.SparePartsMenuActivity;
import com.dfh.support.activity.widget.ChildrenGridView;
import com.dfh.support.activity.widget.FixedSpeedScroller;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.AdvertisementListData;
import com.dfh.support.entity.CityData;
import com.dfh.support.entity.ClassifyListData;
import com.dfh.support.entity.DebugMenuData;
import com.dfh.support.entity.DebugMenuListData;
import com.dfh.support.entity.PolicyData;
import com.dfh.support.entity.ServeData;
import com.dfh.support.entity.ServeListData;
import com.dfh.support.http.HttpConfig;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.SettingSharedPerferencesUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SupportFragment extends Fragment implements View.OnClickListener {

    private View mFragmentView;
    private ChildrenGridView mGvTroubleShooting;
    private TroubleShootingAdapter mTroubleShootingAdapter;
    private ArrayList<String> mTroubleShootingList = new ArrayList<String>();

    private ListView mLvAfterSaleService;//lv_after_sale_service
    private AfterSaleMoreAdapter mAfterSaleMoreAdapter;
    private ArrayList<ServeData> mAfterSaleServiceList = new ArrayList<ServeData>();

    private ListView mLvChangeOfPurchaseService;//lv_change_of_purchase_service
    private ChangeOfPurchaseMoreAdapter mChangeOfPurchaseMoreAdapter;
    private ArrayList<ServeData> mChangeOfPurchaseServiceList = new ArrayList<ServeData>();

    private ImageView mIvTroubleShooting, mIvAfterSaleService, mIvChangeOfPurchaseService;
    private static SupportFragment s_instance;

    private RelativeLayout mRlSpareParts, mRlAboutUs, mRlAfterSales, mRlTroubleShooting;
    private LinearLayout mLlSearch;
    private ImageView mIvContactUs, mIvTroubleShootingOne, mIvTroubleShootingTwo;

    private String mCity = "";
    private static final int POLICY_FIND_SUCCESS = 1;
    private static final int POLICY_FIND_FALSE = 2;
    private static final int DEBUG_FIND_SUCCESS = 3;
    private static final int DEBUG_FIND_FALSE = 4;
    private static final int GET_POLICY_NAME = 5;
    private static final int GET_FAULT_CLASSIFY = 6;
    private static final int SERVICE_SERVE_PAGER_SUCCESS = 7;
    private static final int SERVICE_SERVE_PAGER_FALSE = 8;
    private static final int BUYING_SERVE_PAGER_SUCCESS = 9;
    private static final int BUYING_SERVE_PAGER_FALSE = 10;
    private static final int GET_SERVICE_SERVE_PAGER = 11;
    private static final int GET_BUYING_SERVE_PAGER = 12;
    private static final int GET_GEO = 13;
    private static final int REPEAT_TIME = 30 * 1000;
    private String mPolicyUrl = "";
    private ViewPager mVpBanner;
    private ArrayList<View> mViewList;
    private BannerPagerAdapter mBannerPagerAdapter;
    private List<ImageView> mDots;//定义一个集合存储五个dot
    private int oldPosition;//记录当前点的位置。
    private int mBannerPosition = 0;
    private static final int BANNER_START_SLITHER = 14;
    private static final int BANNER_STOP_SLITHER = 15;
    private static final int AD_BANNER_SUCCESS = 16;
    private static final int AD_BANNER_FALSE = 17;
    private static final int GET_BANNER_AD = 18;
    private static int slither_time = 5 * 1000;//5S
    private static final int REPECT_GET = 30 * 1000;
    private DisplayImageOptions options;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AD_BANNER_SUCCESS:
                    //刷新banner
                    //updateViewPage();
                    updateViewPage();
                    break;
                case AD_BANNER_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_BANNER_AD, REPECT_GET);
                    break;
                case GET_BANNER_AD:
                    mAdsFindCarouselAllTask = new AdsFindCarouselAllTask();
                    mAdsFindCarouselAllTask.execute("");
                    break;
                case BANNER_START_SLITHER:
                    if (mBannerPosition == mViewList.size() - 1) {
                        mBannerPosition = 0;
                    } else {
                        mBannerPosition = mBannerPosition + 1;
                    }
                    mVpBanner.setCurrentItem(mBannerPosition);
                    mHandler.sendEmptyMessageDelayed(BANNER_START_SLITHER, slither_time);
                    break;
                case POLICY_FIND_SUCCESS:
                    mPolicyUrl = mPolicyData.getUrl();
                    break;
                case POLICY_FIND_FALSE:
                    //ToastUtils.shortToast(getActivity(),  HttpJsonAnaly.lastError);
                    mHandler.sendEmptyMessageDelayed(GET_POLICY_NAME, REPEAT_TIME);
                    break;
                case DEBUG_FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    if (mDebugMenuListData.getDebugMenuData().size() > 0) {
                        mIvTroubleShootingOne.setVisibility(View.VISIBLE);
                        final DebugMenuData debugMenuData = mDebugMenuListData.getDebugMenuData().get(0);
                        ImageLoader.getInstance().displayImage(debugMenuData.getIcon(), mIvTroubleShootingOne);
                        mIvTroubleShootingOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), DebuggingListActivity.class);
                                intent.putExtra("id", debugMenuData.getId());
                                startActivity(intent);
                            }
                        });
                    }
                    if (mDebugMenuListData.getDebugMenuData().size() > 1) {
                        mIvTroubleShootingTwo.setVisibility(View.VISIBLE);
                        final DebugMenuData debugMenuData = mDebugMenuListData.getDebugMenuData().get(1);
                        ImageLoader.getInstance().displayImage(debugMenuData.getIcon(), mIvTroubleShootingTwo);
                        mIvTroubleShootingTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), DebuggingListActivity.class);
                                intent.putExtra("id", debugMenuData.getId());
                                startActivity(intent);
                            }
                        });
                    }
                    break;
                case DEBUG_FIND_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_FAULT_CLASSIFY, REPEAT_TIME);
                    //ToastUtils.shortToast(DebuggingMenuActivity.this,  HttpJsonAnaly.lastError);
                    break;
                case GET_POLICY_NAME:
                    mPolicyFindNameTask = new PolicyFindNameTask();
                    mPolicyFindNameTask.execute("");
                    break;
                case GET_FAULT_CLASSIFY:
                    mFaultFindClassifyTask = new FaultFindClassifyTask();
                    mFaultFindClassifyTask.execute("");
                    break;
                case SERVICE_SERVE_PAGER_SUCCESS:
                    //刷新list.根据type来。同时如果是type=1继续请求接口
                    LogUtil.printPushLog("CityData type.equals(HttpJsonSend.SERVE_TYPE_SERVICE)");
                    //售后服务列表
                    mAfterSaleServiceList = serviceServeListData.getServeData();
                    mAfterSaleMoreAdapter.setList(mAfterSaleServiceList, hasLocation);
                    break;
                case SERVICE_SERVE_PAGER_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_SERVICE_SERVE_PAGER, REPEAT_TIME);
                    break;
                case BUYING_SERVE_PAGER_SUCCESS:
                    LogUtil.printPushLog("CityData type.equals(HttpJsonSend.SERVE_TYPE_BUYING)");
                    //换购网点列表
                    mChangeOfPurchaseServiceList = buyingServeListData.getServeData();
                    mChangeOfPurchaseMoreAdapter.setList(mChangeOfPurchaseServiceList, hasLocation);
                    break;
                case BUYING_SERVE_PAGER_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_BUYING_SERVE_PAGER, REPEAT_TIME);
                    break;
                case GET_SERVICE_SERVE_PAGER:
                    mServiceServePagerTask = new ServiceServePagerTask();
                    mServiceServePagerTask.execute("");
                    break;
                case GET_BUYING_SERVE_PAGER:
                    mBuyingServePagerTask = new BuyingServePagerTask();
                    mBuyingServePagerTask.execute("");
                    break;
                case GET_GEO:
                    mHandler.sendEmptyMessage(GET_SERVICE_SERVE_PAGER);
                    mHandler.sendEmptyMessage(GET_BUYING_SERVE_PAGER);
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.printFragmentLog("SupportFragment::onCreateView================");
        mFragmentView = inflater.inflate(R.layout.support_layout, null);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.bg_loading_top)
                .showImageForEmptyUri(R.mipmap.bg_load_false_top)
                .showImageOnFail(R.mipmap.bg_load_false_top).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        initView();
        initListener();
        initViewPage();
        setViewPagerScrollSpeed();
//        setGridView();
        mHandler.sendEmptyMessage(GET_POLICY_NAME);
        //mHandler.sendEmptyMessage(GET_FAULT_CLASSIFY);
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SupportApplication.ACTION_HTTP_RESULT);
        getActivity().registerReceiver(mHttpReceiver, intentFilter);
        mGetGeoTask = new GetGeoTask();
        mGetGeoTask.execute("");
        mHandler.sendEmptyMessage(GET_BANNER_AD);
        return mFragmentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mHttpReceiver);
    }

    private void initView() {
        mTroubleShootingList = new ArrayList<String>();
        mAfterSaleServiceList = new ArrayList<ServeData>();
        mChangeOfPurchaseServiceList = new ArrayList<ServeData>();

//        mGvTroubleShooting = (ChildrenGridView)mFragmentView.findViewById(R.id.gv_troubleshooting);

        mLvAfterSaleService = (ListView) mFragmentView.findViewById(R.id.lv_after_sale_service);
        mAfterSaleMoreAdapter = new AfterSaleMoreAdapter(getActivity(), mAfterSaleServiceList);
        mLvAfterSaleService.setAdapter(mAfterSaleMoreAdapter);

        mLvChangeOfPurchaseService = (ListView) mFragmentView.findViewById(R.id.lv_change_of_purchase_service);
        mChangeOfPurchaseMoreAdapter = new ChangeOfPurchaseMoreAdapter(getActivity(), mChangeOfPurchaseServiceList);
        mLvChangeOfPurchaseService.setAdapter(mChangeOfPurchaseMoreAdapter);

        mIvTroubleShooting = (ImageView) mFragmentView.findViewById(R.id.iv_more_troubleshooting);
        mRlTroubleShooting = (RelativeLayout) mFragmentView.findViewById(R.id.rl_common_troubleshooting);
        mIvAfterSaleService = (ImageView) mFragmentView.findViewById(R.id.iv_more_after_sale_service);
        mIvChangeOfPurchaseService = (ImageView) mFragmentView.findViewById(R.id.iv_more_change_of_purchase_service);

        mLlSearch = (LinearLayout) mFragmentView.findViewById(R.id.ll_search);
        mRlSpareParts = (RelativeLayout) mFragmentView.findViewById(R.id.rl_spare_parts_management);
        mRlAboutUs = (RelativeLayout) mFragmentView.findViewById(R.id.rl_about_us);
        mRlAfterSales = (RelativeLayout) mFragmentView.findViewById(R.id.rl_after_sales_policy);
        mIvContactUs = (ImageView) mFragmentView.findViewById(R.id.iv_contact_us);
        mIvTroubleShootingOne = (ImageView) mFragmentView.findViewById(R.id.iv_troubleshooting_one);
        mIvTroubleShootingTwo = (ImageView) mFragmentView.findViewById(R.id.iv_troubleshooting_two);

    }

    private void initListener() {
        mIvTroubleShooting.setOnClickListener(this);
        mRlTroubleShooting.setOnClickListener(this);
        mIvAfterSaleService.setOnClickListener(this);
        mIvChangeOfPurchaseService.setOnClickListener(this);
        mLlSearch.setOnClickListener(this);
        mRlSpareParts.setOnClickListener(this);
        mRlAboutUs.setOnClickListener(this);
        mRlAfterSales.setOnClickListener(this);
        mIvContactUs.setOnClickListener(this);

        mLvAfterSaleService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                intent.putExtra("id", mAfterSaleServiceList.get(i).getId());
                if (hasLocation) {
                    intent.putExtra("distance", mAfterSaleServiceList.get(i).getDistance());
                } else {
                    intent.putExtra("distance", "");
                }
                startActivity(intent);
            }
        });
        mLvChangeOfPurchaseService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), BuyingShopsDetailActivity.class);
                intent.putExtra("id", mChangeOfPurchaseServiceList.get(i).getId());
                if (hasLocation) {
                    intent.putExtra("distance", mChangeOfPurchaseServiceList.get(i).getDistance());
                } else {
                    intent.putExtra("distance", "");
                }
                startActivity(intent);
            }
        });
    }

    /**
     * 设置GirdView参数，绑定数据
     */
//    private void setGridView() {
//        int size = mTroubleShootingList.size();
//        int length = 138;
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//        int gridviewWidth = (int) (size * (length + 4) * density);
//        int itemWidth = (int) (length * density );
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
//        mGvTroubleShooting.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
//        mGvTroubleShooting.setColumnWidth(itemWidth); // 设置列表项宽
//        mGvTroubleShooting.setHorizontalSpacing(5); // 设置列表项水平间距
//        mGvTroubleShooting.setStretchMode(GridView.NO_STRETCH);
//        mGvTroubleShooting.setNumColumns(size); // 设置列数量=列表集合数
//
//        mTroubleShootingAdapter = new TroubleShootingAdapter(getActivity().getApplicationContext(),
//                mTroubleShootingList);
//        mGvTroubleShooting.setAdapter(mTroubleShootingAdapter);
//    }
    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(GET_SERVICE_SERVE_PAGER);
        mHandler.sendEmptyMessage(GET_BUYING_SERVE_PAGER);
        LogUtil.printFragmentLog("SupportFragment::onResume================");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.printFragmentLog("SupportFragment::onDetach================");
    }

    public static SupportFragment newInstance() {
        if (null == s_instance)
            s_instance = new SupportFragment();

        return s_instance;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more_troubleshooting:
                Intent intent = new Intent(getActivity(), DebuggingMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_common_troubleshooting:
                intent = new Intent(getActivity(), DebuggingMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_more_after_sale_service:
                intent = new Intent(getActivity(), ServiceListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_more_change_of_purchase_service:
                intent = new Intent(getActivity(), BuyingShopsListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_spare_parts_management:
                intent = new Intent(getActivity(), SparePartsMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about_us:
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_after_sales_policy:
                intent = new Intent(getActivity(), TitleWebViewActivity.class);
                intent.putExtra("title", mPolicyData.getTitle());
                intent.putExtra("url", mPolicyData.getUrl());
                intent.putExtra("id", mPolicyData.getId());
                startActivity(intent);
                break;
            case R.id.iv_contact_us:
                intent = new Intent(getActivity(), CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }

    private GetGeoTask mGetGeoTask;

    private class GetGeoTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mCityData = LogUtil.getGeo(getActivity());
            mHandler.sendEmptyMessage(GET_GEO);
            LogUtil.printPushLog("httpGet getGeo mCityData" + mCityData.toString());
            return null;
        }
    }

    private PolicyData mPolicyData;
    private PolicyFindNameTask mPolicyFindNameTask;
    private String policyName = "售后政策";

    private class PolicyFindNameTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mPolicyData = HttpJsonSend.policyFindName(getActivity(), policyName);
            if (mPolicyData.isFlag()) {
                mHandler.sendEmptyMessage(POLICY_FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(POLICY_FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet policyFindName mPolicyData" + mPolicyData.toString());
            return null;
        }
    }

    private DebugMenuListData mDebugMenuListData;
    private FaultFindClassifyTask mFaultFindClassifyTask;

    private class FaultFindClassifyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mDebugMenuListData = HttpJsonSend.faultFindClassify(getActivity());
            if (mDebugMenuListData.isFlag()) {
                mHandler.sendEmptyMessage(DEBUG_FIND_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(DEBUG_FIND_FALSE);
            }
            LogUtil.printPushLog("httpGet faultFindClassify mDebugMenuListData" + mDebugMenuListData.toString());
            return null;
        }
    }

    private ServiceServePagerTask mServiceServePagerTask;
    private ServeListData serviceServeListData;
    private CityData mCityData;
    private String pageSize = "20";
    private int pageNo = 1;
    private boolean hasLocation = false;

    private class ServiceServePagerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mCity = SettingSharedPerferencesUtil.GetSearchCityValueConfig(getActivity());
            if (mCityData != null && mCityData.getLatitude() != 0 && mCityData.getLongitude() != 0) {
                hasLocation = true;
                LogUtil.printPushLog("CityData mCityData:" + mCityData.toString());
                if (!TextUtils.isEmpty(mCity)) {
                    HttpJsonSend.servePager(getActivity(), mCity,
                            String.valueOf(mCityData.getLatitude()), String.valueOf(mCityData.getLongitude()), pageSize,
                            String.valueOf(pageNo), HttpJsonSend.SERVE_TYPE_SERVICE, HttpJsonSend.COME_FROM_MAIN);
                } else {
                    HttpJsonSend.servePager(getActivity(), mCityData.getCityName(),
                            String.valueOf(mCityData.getLatitude()), String.valueOf(mCityData.getLongitude())
                            , pageSize, String.valueOf(pageNo), HttpJsonSend.SERVE_TYPE_SERVICE, HttpJsonSend.COME_FROM_MAIN);
                }
            } else {
                if (TextUtils.isEmpty(mCity)) {
                    if (getActivity() != null) {
                        mCity = getActivity().getResources().getString(R.string.common_default_city);
                    } else {
                        mCity = "北京";
                    }
                }
                hasLocation = false;
                LogUtil.printPushLog("CityData mCityData=null ");
                HttpJsonSend.servePager(getActivity(), mCity,
                        "39.908692", "116.397477", pageSize,
                        String.valueOf(pageNo), HttpJsonSend.SERVE_TYPE_SERVICE, HttpJsonSend.COME_FROM_MAIN);

            }
            return null;
        }
    }

    private BuyingServePagerTask mBuyingServePagerTask;
    private ServeListData buyingServeListData;

    private class BuyingServePagerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            if(isAdded()){
                mCity = SettingSharedPerferencesUtil.GetSearchCityValueConfig(getActivity());
                if (mCityData != null && mCityData.getLatitude() != 0 && mCityData.getLongitude() != 0) {
                    hasLocation = true;
                    LogUtil.printPushLog("CityData mCityData:" + mCityData.toString());
                    if (!TextUtils.isEmpty(mCity)) {
                        HttpJsonSend.servePager(getActivity(), mCity,
                                String.valueOf(mCityData.getLatitude()), String.valueOf(mCityData.getLongitude()), pageSize,
                                String.valueOf(pageNo), HttpJsonSend.SERVE_TYPE_BUYING, HttpJsonSend.COME_FROM_MAIN);
                    } else {
                        HttpJsonSend.servePager(getActivity(), mCityData.getCityName(),
                                String.valueOf(mCityData.getLatitude()), String.valueOf(mCityData.getLongitude())
                                , pageSize, String.valueOf(pageNo), HttpJsonSend.SERVE_TYPE_BUYING, HttpJsonSend.COME_FROM_MAIN);
                    }
                } else {
                    if (TextUtils.isEmpty(mCity))
                        mCity = getResources().getString(R.string.common_default_city);
                    hasLocation = false;
                    LogUtil.printPushLog("CityData mCityData=null ");
                    HttpJsonSend.servePager(getActivity(), mCity,
                            "39.908692", "116.397477", pageSize,
                            String.valueOf(pageNo), HttpJsonSend.SERVE_TYPE_BUYING, HttpJsonSend.COME_FROM_MAIN);
                }
            }

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
                    if (url.contains(HttpConfig.urL_serve_pager)) {
                        int type = intent.getIntExtra("type", 0);
                        if (type == HttpJsonSend.TYPE_MAIN_SERVICE) {
                            boolean is_success = intent.getBooleanExtra("is_success", false);
                            LogUtil.printPushLog("mHttpReceiver urL_serve_pager :" + is_success);
                            if (is_success) {
                                //解析data再发送结果
                                String data = intent.getStringExtra("data");
                                try {
                                    serviceServeListData = HttpJsonAnaly.servePager(data, getActivity());
                                    if (serviceServeListData.isFlag()) {
                                        mHandler.sendEmptyMessage(SERVICE_SERVE_PAGER_SUCCESS);
                                    } else {
                                        mHandler.sendEmptyMessage(SERVICE_SERVE_PAGER_FALSE);
                                    }
                                    LogUtil.printPushLog("mHttpReceiver serviceServeListData.toString :" + serviceServeListData.toString());
                                } catch (Exception e) {
                                    mHandler.sendEmptyMessage(SERVICE_SERVE_PAGER_FALSE);
                                    e.printStackTrace();
                                }
                            } else {
                                mHandler.sendEmptyMessage(SERVICE_SERVE_PAGER_FALSE);
                            }
                        } else if (type == HttpJsonSend.TYPE_MAIN_BUYING) {
                            boolean is_success = intent.getBooleanExtra("is_success", false);
                            LogUtil.printPushLog("mHttpReceiver urL_serve_pager :" + is_success);
                            if (is_success) {
                                //解析data再发送结果
                                String data = intent.getStringExtra("data");
                                try {
                                    buyingServeListData = HttpJsonAnaly.servePager(data, getActivity());
                                    if (buyingServeListData.isFlag()) {
                                        mHandler.sendEmptyMessage(BUYING_SERVE_PAGER_SUCCESS);
                                    } else {
                                        mHandler.sendEmptyMessage(BUYING_SERVE_PAGER_FALSE);
                                    }
                                    LogUtil.printPushLog("mHttpReceiver serveListData.toString :" + buyingServeListData.toString());
                                } catch (Exception e) {
                                    mHandler.sendEmptyMessage(BUYING_SERVE_PAGER_FALSE);
                                    e.printStackTrace();
                                }
                            } else {
                                mHandler.sendEmptyMessage(BUYING_SERVE_PAGER_FALSE);
                            }
                        }
                    }
                }
            }
        }

    }

    //viewpager的动画播放速度修改
    private void setViewPagerScrollSpeed() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mVpBanner.getContext(),
                    new AccelerateInterpolator());
            field.set(mVpBanner, scroller);
            scroller.setmDuration(300);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private void initViewPage() {
        mVpBanner = (ViewPager) mFragmentView.findViewById(R.id.vp_banner);
        //mVpBanner.setPageTransformer(true,new ZoomOutPageTransformer());
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
        LogUtil.printPushLog("SinitViewPage mViewList.size()" + mViewList.size());
        mBannerPagerAdapter = new BannerPagerAdapter(mViewList);
        mVpBanner.setAdapter(mBannerPagerAdapter);
        mHandler.sendEmptyMessageDelayed(BANNER_START_SLITHER, slither_time);

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
        LogUtil.printPushLog("SinitViewPage mDots.size()" + mDots.size());
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
        for (int a = 0; a < 5; a++) {
            mDots.get(a).setVisibility(View.GONE);
        }
        for (int i = 0; i < advertisementListData.getAdvertisementDatas().size(); i++) {
            if (i < 5) {
                mDots.get(i).setVisibility(View.VISIBLE);
                View view = inflater.inflate(R.layout.banner_layout, null);
                ImageView ivBanner = (ImageView) view.findViewById(R.id.iv_banner);
                //ivBanner.setImageResource(R.mipmap.show_banner);
                ImageLoader.getInstance().displayImage(advertisementListData.getAdvertisementDatas().get(i).getIcon(), ivBanner, options);
                final int finalI = i;
                ivBanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", HttpConfig.GetHttpPolicyAdress() +
                                advertisementListData.getAdvertisementDatas().get(finalI).getLink()
                                + "?links=" + advertisementListData.getAdvertisementDatas().get(finalI).getLikes()
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

    private AdvertisementListData advertisementListData;
    private AdsFindCarouselAllTask mAdsFindCarouselAllTask;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
