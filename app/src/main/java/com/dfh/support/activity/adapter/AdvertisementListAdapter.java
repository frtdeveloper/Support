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

import java.util.ArrayList;
import java.util.HashMap;


public class AdvertisementListAdapter extends BaseAdapter {

    private ArrayList<String> mList = new ArrayList<String>();
    private ArrayList<String> mItemList = new ArrayList<String>();
    protected LayoutInflater mInflater;
    protected Context cxt;
    private AdvertisementListItemAdapter adapter;

    public AdvertisementListAdapter(Context context, ArrayList<String> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
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
//            holder.ivHead = (ImageView) v.findViewById(R.id.iv_head);
//            holder.rlBg = (RelativeLayout) v.findViewById(R.id.rl_test_bg);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        mItemList = new ArrayList<String>();
        mItemList.add("");
        mItemList.add("");
        mItemList.add("");
        adapter = new AdvertisementListItemAdapter(cxt, mItemList);
        holder.lvItem.setAdapter(adapter);
        holder.rlAdTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cxt, WebViewActivity.class);
                cxt.startActivity(intent);
            }
        });
        holder.lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(cxt, WebViewActivity.class);
                cxt.startActivity(intent);
            }
        });
        return v;
    }

    public class HolderView {
        private ChildrenListView lvItem;
        private RelativeLayout rlAdTop;
//        private ImageView ivHead;
//        private RelativeLayout rlBg;
    }


}
