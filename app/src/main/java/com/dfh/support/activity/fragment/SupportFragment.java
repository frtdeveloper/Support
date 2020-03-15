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
import com.dfh.support.SupportApplication;
import com.dfh.support.activity.TitleWebViewActivity;
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.adapter.AfterSaleMoreAdapter;
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
import com.dfh.support.activity.widget.LoadingProgressDialog;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

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

    private RelativeLayout mRlSpareParts, mRlAboutUs, mRlAfterSales;
    private LinearLayout mLlSearch;
    private ImageView mIvContactUs, mIvTroubleShootingOne, mIvTroubleShootingTwo;

    private String mCity = "";
    private static final int POLICY_FIND_SUCCESS = 1;
    private static final int POLICY_FIND_FALSE = 2;
    private static final int DEBUG_FIND_SUCCESS = 3;
    private static final int DEBUG_FIND_FALSE = 4;
    private static final int GET_POLICY_NAME = 5;
    private static final int GET_FAULT_CLASSIFY = 6;
    private static final int SERVE_PAGER_SUCCESS = 7;
    private static final int SERVE_PAGER_FALSE = 8;
    private static final int GET_SERVE_PAGER = 9;
    private static final int REPEAT_TIME = 30 * 1000;
    private String mPolicyUrl = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POLICY_FIND_SUCCESS:
                    mPolicyUrl = mPolicyData.getUrl();
                    break;
                case POLICY_FIND_FALSE:
                    //ToastUtils.shortToast(getActivity(),  HttpJsonAnaly.lastError);
                    mHandler.sendEmptyMessageDelayed(GET_POLICY_NAME, REPEAT_TIME);
                    break;
                case DEBUG_FIND_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    if(mDebugMenuListData.getDebugMenuData().size()>0){
                        mIvTroubleShootingOne.setVisibility(View.VISIBLE);
                        final DebugMenuData debugMenuData = mDebugMenuListData.getDebugMenuData().get(0);
                        ImageLoader.getInstance().displayImage(debugMenuData.getIcon(),mIvTroubleShootingOne);
                        mIvTroubleShootingOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), DebuggingListActivity.class);
                                intent.putExtra("id", debugMenuData.getId());
                                startActivity(intent);
                            }
                        });
                    }
                    if(mDebugMenuListData.getDebugMenuData().size()>1){
                        mIvTroubleShootingTwo.setVisibility(View.VISIBLE);
                        final DebugMenuData debugMenuData = mDebugMenuListData.getDebugMenuData().get(1);
                        ImageLoader.getInstance().displayImage(debugMenuData.getIcon(),mIvTroubleShootingTwo);
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
                case SERVE_PAGER_SUCCESS:
                    //刷新list.根据type来。同时如果是type=1继续请求接口
                    if(type.equals(HttpJsonSend.SERVE_TYPE_SERVICE)){
                        LogUtil.printPushLog("CityData type.equals(HttpJsonSend.SERVE_TYPE_SERVICE)");
                        //售后服务列表
                        mAfterSaleServiceList = serveListData.getServeData();
                        mAfterSaleMoreAdapter.setList(mAfterSaleServiceList,hasLocation);
                        type = HttpJsonSend.SERVE_TYPE_BUYING;
                        mHandler.sendEmptyMessageDelayed(GET_SERVE_PAGER,2000);
                    }else{
                        LogUtil.printPushLog("CityData type.equals(HttpJsonSend.SERVE_TYPE_BUYING)");
                        //换购网点列表
                        mChangeOfPurchaseServiceList = serveListData.getServeData();
                        mChangeOfPurchaseMoreAdapter.setList(mChangeOfPurchaseServiceList,hasLocation);
                    }
                    break;
                case SERVE_PAGER_FALSE:
                    mHandler.sendEmptyMessageDelayed(GET_SERVE_PAGER, REPEAT_TIME);
                    break;
                case GET_SERVE_PAGER:
                    mServePagerTask = new ServePagerTask();
                    mServePagerTask.execute("");
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.printFragmentLog("SupportFragment::onCreateView================");
        mFragmentView = inflater.inflate(R.layout.support_layout, null);
        initView();
        initListener();
//        setGridView();
        mCityData = LogUtil.getGeo(getActivity());
        mHandler.sendEmptyMessage(GET_POLICY_NAME);
        mHandler.sendEmptyMessage(GET_FAULT_CLASSIFY);
        mHttpReceiver = new HttpReceiver();//广播接受者实例
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SupportApplication.ACTION_HTTP_RESULT);
        getActivity().registerReceiver(mHttpReceiver, intentFilter);
        mHandler.sendEmptyMessage(GET_SERVE_PAGER);
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
        mIvAfterSaleService = (ImageView) mFragmentView.findViewById(R.id.iv_more_after_sale_service);
        mIvChangeOfPurchaseService = (ImageView) mFragmentView.findViewById(R.id.iv_more_change_of_purchase_service);

        mLlSearch = (LinearLayout) mFragmentView.findViewById(R.id.ll_search);
        mRlSpareParts = (RelativeLayout) mFragmentView.findViewById(R.id.rl_spare_parts_management);
        mRlAboutUs = (RelativeLayout) mFragmentView.findViewById(R.id.rl_about_us);
        mRlAfterSales = (RelativeLayout) mFragmentView.findViewById(R.id.rl_after_sales_policy);
        mIvContactUs = (ImageView) mFragmentView.findViewById(R.id.iv_contact_us);
        mIvTroubleShootingOne = (ImageView) mFragmentView.findViewById(R.id.iv_troubleshooting_one);
        mIvTroubleShootingTwo = (ImageView) mFragmentView.findViewById(R.id.iv_troubleshooting_two);

        mCity = SettingSharedPerferencesUtil.GetSearchCityValueConfig(getActivity());
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

        mLvAfterSaleService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                intent.putExtra("id",mAfterSaleServiceList.get(i).getId());
                if(hasLocation){
                    intent.putExtra("distance",mAfterSaleServiceList.get(i).getDistance());
                }else{
                    intent.putExtra("distance","");
                }
                startActivity(intent);
            }
        });
        mLvChangeOfPurchaseService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), BuyingShopsDetailActivity.class);
                intent.putExtra("id",mChangeOfPurchaseServiceList.get(i).getId());
                if(hasLocation){
                    intent.putExtra("distance",mChangeOfPurchaseServiceList.get(i).getDistance());
                }else{
                    intent.putExtra("distance","");
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
    private ServePagerTask mServePagerTask;
    private ServeListData serveListData;
    private CityData mCityData;
    private String pageSize = "20";
    private String pageNo = "1";
    private String type = HttpJsonSend.SERVE_TYPE_SERVICE;
    private boolean hasLocation = false;

    private class ServePagerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            if(mCityData!=null) {
                hasLocation = true;
                LogUtil.printPushLog("CityData mCityData:" + mCityData.toString());
                HttpJsonSend.servePager(getActivity(), mCityData.getCityName(),
                        String.valueOf(mCityData.getLatitude()), String.valueOf(mCityData.getLongitude())
                        , pageSize, pageNo, type);
            }else{
                hasLocation = false;
                LogUtil.printPushLog("CityData mCityData=null ");
                HttpJsonSend.servePager(getActivity(), mCity,
                        "39.908692", "116.397477", pageSize, pageNo, type);
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
                        boolean is_success = intent.getBooleanExtra("is_success", false);
                        LogUtil.printPushLog("mHttpReceiver urL_serve_pager :" + is_success);
                        if (is_success) {
                            //解析data再发送结果
                            String data = intent.getStringExtra("data");
                            try {
                                serveListData = HttpJsonAnaly.servePager(data, getActivity());
                                if (serveListData.isFlag()) {
                                    mHandler.sendEmptyMessage(SERVE_PAGER_SUCCESS);
                                } else {
                                    mHandler.sendEmptyMessage(SERVE_PAGER_FALSE);
                                }
                                LogUtil.printPushLog("mHttpReceiver serveListData.toString :" + serveListData.toString());
                            } catch (Exception e) {
                                mHandler.sendEmptyMessage(SERVE_PAGER_FALSE);
                                e.printStackTrace();
                            }
                        } else {
                            mHandler.sendEmptyMessage(SERVE_PAGER_FALSE);
                        }
                    }
                }
            }
        }

    }
}
