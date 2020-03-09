package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dfh.support.R;

import java.util.ArrayList;
import java.util.HashMap;


public class AdvertisementListItemAdapter extends BaseAdapter {

    private ArrayList<String> mList = new ArrayList<String>();
    protected LayoutInflater mInflater;
    protected Context cxt;
    private HashMap<Integer, Boolean> clickMap = new HashMap<Integer, Boolean>();

    public AdvertisementListItemAdapter(Context context, ArrayList<String> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setMap(HashMap<Integer, Boolean> map) {
        clickMap = map;
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
            v = mInflater.inflate(R.layout.advertisement_list_item_adapter, null);
            holder = new HolderView();
//            holder.tvContent = (TextView) v.findViewById(R.id.tv_content_title);
//            holder.ivHead = (ImageView) v.findViewById(R.id.iv_head);
//            holder.rlBg = (RelativeLayout) v.findViewById(R.id.rl_test_bg);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        return v;
    }

    public class HolderView {
//        private TextView tvContent;
//        private ImageView ivHead;
//        private RelativeLayout rlBg;
    }


}
