package com.dfh.support.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dfh.support.R;
import com.dfh.support.activity.WebViewActivity;
import com.dfh.support.activity.widget.ChildrenListView;
import com.dfh.support.entity.AdvertisementData;
import com.dfh.support.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class AdvertisementListAdapter extends BaseAdapter {

    private ArrayList<AdvertisementData> mList = new ArrayList<AdvertisementData>();
    //private ArrayList<AdvertisementData> mItemList = new ArrayList<AdvertisementData>();
    protected LayoutInflater mInflater;
    protected Context cxt;
    private AdvertisementListItemAdapter adapter;

    public AdvertisementListAdapter(Context context, ArrayList<AdvertisementData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<AdvertisementData> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AdvertisementData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        HolderView holder;
        if (null == v) {
            v = mInflater.inflate(R.layout.advertisement_list_adapter, null);
            holder = new HolderView();
            holder.lvItem = (ChildrenListView) v.findViewById(R.id.lv_item);
            holder.rlAdTop = (RelativeLayout) v.findViewById(R.id.rl_advertisement_top);
            holder.ivPic = (ImageView) v.findViewById(R.id.iv_pic);
            holder.tvTitle = (TextView) v.findViewById(R.id.tv_advertisement_top_title);
            holder.tvSee = (TextView) v.findViewById(R.id.tv_see);
            holder.tvZan = (TextView) v.findViewById(R.id.tv_zan);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        final AdvertisementData advertisementData = mList.get(position);
        final ArrayList<AdvertisementData> mItemList = advertisementData.getAdsVOList();
        LogUtil.printPushLog("mItemList mItemList.size()" + mItemList.size());
        adapter = new AdvertisementListItemAdapter(cxt, mItemList);
        holder.lvItem.setAdapter(adapter);
        holder.rlAdTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cxt, WebViewActivity.class);
                intent.putExtra("url",advertisementData.getLink());
                intent.putExtra("id",advertisementData.getId());
                cxt.startActivity(intent);
            }
        });
        holder.lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtil.printPushLog("mItemList i" + i);
                Intent intent = new Intent(cxt, WebViewActivity.class);
                intent.putExtra("url",mItemList.get(i).getLink());
                intent.putExtra("id",mItemList.get(i).getId());
                cxt.startActivity(intent);
            }
        });
        holder.tvTitle.setText(advertisementData.getTitle());
        holder.tvSee.setText(advertisementData.getBrowses());
        holder.tvZan.setText(advertisementData.getLikes());
        ImageLoader.getInstance().displayImage(advertisementData.getIcon(),holder.ivPic);
        return v;
    }

    public class HolderView {
        private ChildrenListView lvItem;
        private RelativeLayout rlAdTop;
        private TextView tvTitle;
        private TextView tvSee;
        private TextView tvZan;
        private ImageView ivPic;
    }


}
