package com.dfh.support.activity.fragment;

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
import com.dfh.support.activity.adapter.AdvertisementListAdapter;
import com.dfh.support.activity.adapter.BannerPagerAdapter;
import com.dfh.support.anmi.ZoomOutPageTransformer;
import com.dfh.support.utils.LogUtil;

import java.util.ArrayList;

public class RecommendFragment extends Fragment {

    private View mFragmentView, mFragmentInfo;
    private ViewPager mVpBanner;
    private ListView mLvAdvertisement;//lv_advertisement
    private AdvertisementListAdapter mAdvertisementListAdapter;
    private ArrayList<String> mAdvertisementList;
    private ArrayList<View> mViewList;
    private BannerPagerAdapter mBannerPagerAdapter;

    private static RecommendFragment s_instance;
    private static final int BANNER_START_SLITHER = 0;
    private static final int BANNER_STOP_SLITHER = 1;
    private static int slither_time = 5 * 1000;//5S
    private int mBannerPosition = 0;

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
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.printFragmentLog("RecommendFragment::onCreateView================");
        mFragmentView = inflater.inflate(R.layout.recommend_layout, null);
        mFragmentInfo = mFragmentView.findViewById(R.id.recommend_info);
        Toast.makeText(getActivity(), String.valueOf(mFragmentInfo.getId()), Toast.LENGTH_LONG).show();
        initViewPage();
        initView();
        return mFragmentView;
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
        View view1 = inflater.inflate(R.layout.banner_layout, null);
        ImageView ivBanner1 = (ImageView) view1.findViewById(R.id.iv_banner);
        ivBanner1.setImageResource(R.mipmap.bg_loading);

        View view2 = inflater.inflate(R.layout.banner_layout, null);
        ImageView ivBanner2 = (ImageView) view2.findViewById(R.id.iv_banner);
        ivBanner2.setImageResource(R.mipmap.ic_launcher);

        View view3 = inflater.inflate(R.layout.banner_layout, null);
        ImageView ivBanner3 = (ImageView) view3.findViewById(R.id.iv_banner);
        ivBanner3.setImageResource(R.mipmap.ic_launcher_round);

        mViewList = new ArrayList<View>();
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mBannerPagerAdapter = new BannerPagerAdapter(mViewList);
        mVpBanner.setAdapter(mBannerPagerAdapter);
        mHandler.sendEmptyMessageDelayed(BANNER_START_SLITHER,slither_time);
    }

    private void initView(){
        mAdvertisementList = new ArrayList<String>();
        mAdvertisementList.add("");
        mAdvertisementList.add("");
        mAdvertisementList.add("");
        mAdvertisementList.add("");
        mAdvertisementList.add("");
        mAdvertisementList.add("");
        mAdvertisementList.add("");
        mLvAdvertisement = (ListView) mFragmentView.findViewById(R.id.lv_advertisement);
        mAdvertisementListAdapter = new AdvertisementListAdapter(getActivity(),mAdvertisementList);
        mLvAdvertisement.setAdapter(mAdvertisementListAdapter);
    }
}
