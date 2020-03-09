package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dfh.support.R;

import java.util.ArrayList;


public class SparePartsDetailAdapter extends BaseAdapter {
    private ArrayList<String> mList = new ArrayList<String>();
    protected LayoutInflater mInflater;
    protected Context cxt;

    public SparePartsDetailAdapter(Context context, ArrayList<String> list) {
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
    public View getView(int position, View v, ViewGroup parent) {
        HolderView holder;
        if (null == v) {
            v = mInflater.inflate(R.layout.spare_parts_detail_adapter, null);
            holder = new HolderView();
//            holder.tvContent = (TextView) v.findViewById(R.id.tv_content_title);
//            holder.vLine = (View) v.findViewById(R.id.v_chooes_line);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        return v;
    }


    public class HolderView {
//        private TextView tvContent;
//        private View vLine;
    }
}


