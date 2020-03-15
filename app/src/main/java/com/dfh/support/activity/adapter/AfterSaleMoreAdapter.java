package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.entity.ServeData;

import java.util.ArrayList;


public class AfterSaleMoreAdapter extends BaseAdapter {
    private ArrayList<ServeData> mList = new ArrayList<ServeData>();
    protected LayoutInflater mInflater;
    protected Context cxt;
    private boolean mHasLocation = false;

    public AfterSaleMoreAdapter(Context context, ArrayList<ServeData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<ServeData> list,boolean hasLocation) {
        mHasLocation = hasLocation;
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ServeData getItem(int position) {
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
            v = mInflater.inflate(R.layout.after_sale_more_adapter, null);
            holder = new HolderView();
            holder.tvName = (TextView) v.findViewById(R.id.tv_name);
            holder.tvDistance = (TextView) v.findViewById(R.id.tv_distance);
            holder.tvAddress = (TextView) v.findViewById(R.id.tv_address);
            holder.tvTime = (TextView) v.findViewById(R.id.tv_time);
            holder.tvPhone = (TextView) v.findViewById(R.id.tv_phone);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        ServeData serveData = mList.get(position);
        holder.tvName.setText(serveData.getName());
        if(mHasLocation) {
            holder.tvDistance.setVisibility(View.VISIBLE);
        }else{
            holder.tvDistance.setVisibility(View.GONE);
        }
        holder.tvDistance.setText(serveData.getDistance());
        holder.tvAddress.setText(serveData.getAddress());
        holder.tvTime.setText(serveData.getTime());
        holder.tvPhone.setText(serveData.getTel());
        return v;
    }


    public class HolderView {
        private TextView tvName;
        private TextView tvDistance;
        private TextView tvAddress;
        private TextView tvTime;
        private TextView tvPhone;
    }
}


