package com.dfh.support.activity.fragment;

import android.content.Intent;
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
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.adapter.AdvertisementListAdapter;
import com.dfh.support.activity.adapter.BannerPagerAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.anmi.ZoomOutPageTransformer;
import com.dfh.support.utils.LogUtil;

import java.util.ArrayList;

public class RecommendFragment extends Fragment {

    private View mFragmentView;
    private ViewPager mVpBanner;
    private ChildrenListView mLvAdvertisement;//lv_advertisement
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
        mViewList = new ArrayList<View>();
        for(int i = 0 ;i<3;i++) {
            View view = inflater.inflate(R.layout.banner_layout, null);
            ImageView ivBanner = (ImageView) view.findViewById(R.id.iv_banner);
            ivBanner.setImageResource(R.mipmap.show_banner);
            ivBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            mViewList.add(view);
        }
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
        mLvAdvertisement = (ChildrenListView) mFragmentView.findViewById(R.id.lv_advertisement);
        mAdvertisementListAdapter = new AdvertisementListAdapter(getActivity(),mAdvertisementList);
        mLvAdvertisement.setAdapter(mAdvertisementListAdapter);
    }
}
