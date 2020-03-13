package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.entity.DebugDetailData;

import java.util.ArrayList;


public class DebuggingListAdapter extends BaseAdapter {
    private ArrayList<DebugDetailData> mList = new ArrayList<DebugDetailData>();
    protected LayoutInflater mInflater;
    protected Context cxt;

    public DebuggingListAdapter(Context context, ArrayList<DebugDetailData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<DebugDetailData> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DebugDetailData getItem(int position) {
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
            v = mInflater.inflate(R.layout.debugging_list_adapter, null);
            holder = new HolderView();
            holder.tvContent = (TextView) v.findViewById(R.id.tv_content);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        DebugDetailData debugDetailData = mList.get(position);
        holder.tvContent.setText(debugDetailData.getTitle());
        return v;
    }


    public class HolderView {
        private TextView tvContent;
    }
}


