package com.dfh.support.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.dfh.support.R;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.adapter.AfterSaleMoreAdapter;
import com.dfh.support.activity.adapter.ChangeOfPurchaseMoreAdapter;
import com.dfh.support.activity.adapter.TroubleShootingAdapter;
import com.dfh.support.activity.support.AboutUsActivity;
import com.dfh.support.activity.support.BuyingShopsListActivity;
import com.dfh.support.activity.support.CallPhoneActivity;
import com.dfh.support.activity.support.DebuggingListActivity;
import com.dfh.support.activity.support.DebuggingMenuActivity;
import com.dfh.support.activity.support.SearchActivity;
import com.dfh.support.activity.support.ServiceListActivity;
import com.dfh.support.activity.support.SparePartsMenuActivity;
import com.dfh.support.activity.widget.ChildrenGridView;
import com.dfh.support.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class SupportFragment extends Fragment implements View.OnClickListener {

    private View mFragmentView;
    private ChildrenGridView mGvTroubleShooting;
    private TroubleShootingAdapter mTroubleShootingAdapter;
    private ArrayList<String> mTroubleShootingList = new ArrayList<String>();

    private ListView mLvAfterSaleService;//lv_after_sale_service
    private AfterSaleMoreAdapter mAfterSaleMoreAdapter;
    private ArrayList<String> mAfterSaleServiceList = new ArrayList<String>();

    private ListView mLvChangeOfPurchaseService;//lv_change_of_purchase_service
    private ChangeOfPurchaseMoreAdapter mChangeOfPurchaseMoreAdapter;
    private ArrayList<String> mChangeOfPurchaseServiceList = new ArrayList<String>();

    private ImageView mIvTroubleShooting, mIvAfterSaleService, mIvChangeOfPurchaseService;
    private static SupportFragment s_instance;

    private RelativeLayout mRlSpareParts, mRlAboutUs, mRlAfterSales;
    private LinearLayout mLlSearch;
    private ImageView mIvContactUs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.printFragmentLog("SupportFragment::onCreateView================");
        mFragmentView = inflater.inflate(R.layout.support_layout, null);
        initView();
        initListener();
//        setGridView();
        return mFragmentView;
    }

    private void initView() {
        mTroubleShootingList = new ArrayList<String>();
        mAfterSaleServiceList = new ArrayList<String>();
        mChangeOfPurchaseServiceList = new ArrayList<String>();
        mTroubleShootingList.add("");
        mTroubleShootingList.add("");
        mTroubleShootingList.add("");
        mTroubleShootingList.add("");
        mTroubleShootingList.add("");
        mAfterSaleServiceList.add("");
        mAfterSaleServiceList.add("");
        //mChangeOfPurchaseServiceList.add("");
        mChangeOfPurchaseServiceList.add("");

//        mGvTroubleShooting = (ChildrenGridView)mFragmentView.findViewById(R.id.gv_troubleshooting);

        mLvAfterSaleService = (ListView) mFragmentView.findViewById(R.id.lv_after_sale_service);
        mAfterSaleMoreAdapter = new AfterSaleMoreAdapter(getActivity(), mAfterSaleServiceList);
        mLvAfterSaleService.setAdapter(mAfterSaleMoreAdapter);

        mLvChangeOfPurchaseService = (ListView) mFragmentView.findViewById(R.id.lv_change_of_purchase_service);
        mChangeOfPurchaseMoreAdapter = new ChangeOfPurchaseMoreAdapter(getActivity(), mChangeOfPurchaseServiceList);
        mLvChangeOfPurchaseService.setAdapter(mChangeOfPurchaseMoreAdapter);

        mIvTroubleShooting = (ImageView) mFragmentView.findViewById(R.id.iv_more_troubleshooting);
        mIvAfterSaleService = (ImageView) mFragmentView.findViewById(R.id.iv_more_after_sale_service);
        mIvChangeOfPurchaseService = (ImageView) mFragmentView.findViewById(R.id.iv_more_change_of_purchase_service);

        mLlSearch = (LinearLayout) mFragmentView.findViewById(R.id.ll_search);
        mRlSpareParts = (RelativeLayout) mFragmentView.findViewById(R.id.rl_spare_parts_management);
        mRlAboutUs = (RelativeLayout) mFragmentView.findViewById(R.id.rl_about_us);
        mRlAfterSales = (RelativeLayout) mFragmentView.findViewById(R.id.rl_after_sales_policy);
        mIvContactUs = (ImageView) mFragmentView.findViewById(R.id.iv_contact_us);

    }

    private void initListener() {
        mIvTroubleShooting.setOnClickListener(this);
        mIvAfterSaleService.setOnClickListener(this);
        mIvChangeOfPurchaseService.setOnClickListener(this);
        mLlSearch.setOnClickListener(this);
        mRlSpareParts.setOnClickListener(this);
        mRlAboutUs.setOnClickListener(this);
        mRlAfterSales.setOnClickListener(this);
        mIvContactUs.setOnClickListener(this);
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
                intent.putExtra("title",getResources().getString(R.string.after_sales_policy));
                startActivity(intent);
                break;
            case R.id.iv_contact_us:
                intent = new Intent(getActivity(), CallPhoneActivity.class);
                startActivity(intent);
                break;
        }
    }
}
