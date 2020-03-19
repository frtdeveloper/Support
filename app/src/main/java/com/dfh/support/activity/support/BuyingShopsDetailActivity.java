package com.dfh.support.activity.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.adapter.DialogListAdapter;
import com.dfh.support.activity.adapter.SparePartsDetailAdapter;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.activity.widget.ListConfirmDialog;
import com.dfh.support.activity.widget.LoadingProgressDialog;
import com.dfh.support.entity.PictureVOData;
import com.dfh.support.entity.ServeData;
import com.dfh.support.http.HttpJsonAnaly;
import com.dfh.support.http.HttpJsonSend;
import com.dfh.support.utils.ActionBarUtil;
import com.dfh.support.utils.CallPhoneUtil;
import com.dfh.support.utils.FileUtils;
import com.dfh.support.utils.LogUtil;
import com.dfh.support.utils.TextUtils;
import com.dfh.support.utils.ToastUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BuyingShopsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack, mIvContactUs,mIvGps;
    private TextView mTvName;
    private TextView mTvContactName;
    private TextView mTvDistance;
    private TextView mTvAddress;
    private TextView mTvTime;
    private TextView mTvPhone;
    private TextView mTvServiceContent;
    private TextView mTvSpecialReminder;
    private ChildrenListView mLvPic;
    private ArrayList<PictureVOData> mPicList = new ArrayList<PictureVOData>();
    private SparePartsDetailAdapter mSparePartsDetailAdapter;


    private static final int SERVE_DETAIL_SUCCESS = 1;
    private static final int SERVE_DETAIL_FALSE = 2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SERVE_DETAIL_SUCCESS:
                    LoadingProgressDialog.Dissmiss();
                    //刷新内容
                    mTvName.setText(mServeData.getName());
                    if(!TextUtils.isEmpty(distance)) mTvDistance.setText(distance);
                    mTvAddress.setText(mServeData.getAddress());
                    mTvContactName.setText(getResources().getString(R.string.common_contact)+"："+mServeData.getContact());
                    mTvTime.setText(getResources().getString(R.string.service_time)+"："+mServeData.getTime());
                    mTvPhone.setText(getResources().getString(R.string.service_phone)+"："+mServeData.getTel());
                    mTvServiceContent.setText(mServeData.getScope());
                    mTvSpecialReminder.setText(mServeData.getTips());
                    mPicList = mServeData.getPictureVOData();
                    mSparePartsDetailAdapter.setList(mPicList);
                    break;
                case SERVE_DETAIL_FALSE:
                    LoadingProgressDialog.Dissmiss();
                    ToastUtils.shortToast(BuyingShopsDetailActivity.this,  HttpJsonAnaly.lastError);
                    break;
            }
        }
    };
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.transparencyBarTitleColor(this);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_buying_shops_detail);
        initView();
        initListener();
        LoadingProgressDialog.show(BuyingShopsDetailActivity.this, false, true, 30000);
        id = getIntent().getStringExtra("id");
        distance = getIntent().getStringExtra("distance");
        mServeIdDetailTask = new ServeIdDetailTask();
        mServeIdDetailTask.execute("");
    }


    private void initListener() {
        mIvContactUs.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvPhone.setOnClickListener(this);
        mIvGps.setOnClickListener(this);
    }

    private void initView() {
        mIvGps = (ImageView) findViewById(R.id.iv_gps);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvContactUs = (ImageView) findViewById(R.id.iv_contact_us);
        mLvPic = (ChildrenListView)findViewById(R.id.lv_pic);
        mSparePartsDetailAdapter = new SparePartsDetailAdapter(this,mPicList);
        mLvPic.setAdapter(mSparePartsDetailAdapter);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvContactName = (TextView) findViewById(R.id.tv_contact_name);
        mTvDistance = (TextView) findViewById(R.id.tv_distance);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvServiceContent = (TextView) findViewById(R.id.tv_service_content);
        mTvSpecialReminder = (TextView) findViewById(R.id.tv_special_reminder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_gps:
                if (mServeData != null) {
                    showMapListConfirmDialog();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_contact_us:
                Intent intent = new Intent(BuyingShopsDetailActivity.this, CallPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_phone:
                if(mServeData!=null) {
                    if(!TextUtils.isEmpty(mServeData.getTel())) {
                        showListConfirmDialog(mServeData.getTel());
                    }
                }
                break;
        }
    }
    private ServeData mServeData;
    private ServeIdDetailTask mServeIdDetailTask;
    private String id = "";
    private String distance = "";

    private class ServeIdDetailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mServeData =  HttpJsonSend.serveIdDetail(BuyingShopsDetailActivity.this,id);
            if (mServeData.isFlag()) {
                mHandler.sendEmptyMessage(SERVE_DETAIL_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(SERVE_DETAIL_FALSE);
            }
            LogUtil.printPushLog("httpGet serveIdDetail mServeData" + mServeData.toString());
            return null;
        }
    }


    private ListConfirmDialog mListConfirmDialog;

    private ListView mLvSelectRoles;
    private DialogListAdapter dialogListAdapter;
    private ArrayList<String> phoneList = new ArrayList<String>();

    public void showListConfirmDialog(String phone) {
        phoneList = new ArrayList<String>();
        if(phone.contains("/")){
            String[] phones = phone.split("/");
            for(int i = 0;i<phones.length;i++){
                String p = phones[i];
                phoneList.add(p);
            }
        }else {
            phoneList.add(phone);
        }
        mListConfirmDialog = new ListConfirmDialog(BuyingShopsDetailActivity.this,
                R.style.share_dialog);
        mListConfirmDialog.show();
        Window window = mListConfirmDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
        mLvSelectRoles = (ListView) window.findViewById(R.id.lv_select_roles);
        dialogListAdapter = new DialogListAdapter(BuyingShopsDetailActivity.this, phoneList);
        mLvSelectRoles.setAdapter(dialogListAdapter);
        mLvSelectRoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CallPhoneUtil.callPhone(BuyingShopsDetailActivity.this,phoneList.get(position));
                mListConfirmDialog.dismiss();
            }
        });
    }


    private ListConfirmDialog mListMapConfirmDialog;

    private ListView mLvMap;
    private DialogListAdapter dialogMapListAdapter;

    public void showMapListConfirmDialog() {
        try {
            final ArrayList<String> mapList = new ArrayList<String>();
            if (FileUtils.isInstalled(BuyingShopsDetailActivity.this, "com.autonavi.minimap")) {
                mapList.add("高德地图");
            }
            if (FileUtils.isInstalled(BuyingShopsDetailActivity.this, "com.baidu.BaiduMap")) {
                mapList.add("百度地图");
            }
            if (FileUtils.isInstalled(BuyingShopsDetailActivity.this, "com.tencent.map")) {
                mapList.add("腾讯地图");
            }
            if (mapList.size() == 0) {
                ToastUtils.shortToast(BuyingShopsDetailActivity.this, getResources().getString(R.string.no_map));
            } else if (mapList.size() == 1) {
                if (mapList.get(0).equals("高德地图"))
                    goToMapGaode(mServeData.getAddress(), Double.valueOf(mServeData.getLat()), Double.valueOf(mServeData.getLng()));
                if (mapList.get(0).equals("百度地图"))
                    goToBaiduMap(mServeData.getAddress(), Double.valueOf(mServeData.getLat()), Double.valueOf(mServeData.getLng()));
                if (mapList.get(0).equals("腾讯地图"))
                    goToMapTX(mServeData.getAddress(), Double.valueOf(mServeData.getLat()), Double.valueOf(mServeData.getLng()));
            } else {
                mListMapConfirmDialog = new ListConfirmDialog(BuyingShopsDetailActivity.this,
                        R.style.share_dialog);
                mListMapConfirmDialog.show();
                Window window = mListMapConfirmDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 1f;
                window.setAttributes(lp);
                mLvMap = (ListView) window.findViewById(R.id.lv_select_roles);
                dialogMapListAdapter = new DialogListAdapter(BuyingShopsDetailActivity.this, mapList);
                mLvMap.setAdapter(dialogMapListAdapter);
                mLvMap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String mapName = mapList.get(position);
                        if (mapName.equals("高德地图"))
                            goToMapGaode(mServeData.getAddress(), Double.valueOf(mServeData.getLat()), Double.valueOf(mServeData.getLng()));
                        if (mapName.equals("百度地图"))
                            goToBaiduMap(mServeData.getAddress(), Double.valueOf(mServeData.getLat()), Double.valueOf(mServeData.getLng()));
                        if (mapName.equals("腾讯地图"))
                            goToMapTX(mServeData.getAddress(), Double.valueOf(mServeData.getLat()), Double.valueOf(mServeData.getLng()));
                        mListMapConfirmDialog.dismiss();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 启动高德App进行导航
     *
     * @param address 目的地
     * @param lat     必填 纬度
     * @param lon     必填 经度
     */
    public void goToMapGaode(String address, double lat, double lon) {
        String dev = "0";
        String style = "0";

        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=").append("amap");
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon).append("&keywords=" + address)
                .append("&dev=").append(dev)
                .append("&style=").append(style);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }

    /**
     * 跳转百度地图
     *
     * @param address 目的地
     * @param lat     必填 纬度
     * @param lon     必填 经度
     */
    private void goToBaiduMap(String address, double lat, double lon) {
        /**
         * GCJ-02 坐标转换成 BD-09 坐标
         */
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;

        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?destination=latlng:"
                + tempLat + ","
                + tempLon + "|name:" + address + // 终点
                "&mode=driving" + // 导航路线方式
                "&src=" + getPackageName()));
        startActivity(intent); // 启动调用
    }


    /**
     * 启动腾讯地图App进行导航
     *
     * @param address 目的地
     * @param lat     必填 纬度
     * @param lon     必填 经度
     */
    public void goToMapTX(String address, double lat, double lon) {
        StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=drive")
                .append("&tocoord=").append(lat).append(",").append(lon).append("&to=" + address);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        startActivity(intent);
    }
}
