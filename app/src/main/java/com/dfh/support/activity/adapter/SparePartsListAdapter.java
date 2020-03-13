package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.entity.PartsData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class SparePartsListAdapter extends BaseAdapter {
    private ArrayList<PartsData> mList = new ArrayList<PartsData>();
    protected LayoutInflater mInflater;
    protected Context cxt;

    public SparePartsListAdapter(Context context, ArrayList<PartsData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<PartsData> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PartsData getItem(int position) {
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
            v = mInflater.inflate(R.layout.spare_parts_list_adapter, null);
            holder = new HolderView();
            holder.tvName = (TextView) v.findViewById(R.id.tv_name);
            holder.tvValue = (TextView) v.findViewById(R.id.tv_value);
            holder.tvContent = (TextView) v.findViewById(R.id.tv_content);
            holder.tvText = (TextView) v.findViewById(R.id.tv_text);
            holder.tvRemark = (TextView) v.findViewById(R.id.tv_remark);
            holder.ivPic = (ImageView) v.findViewById(R.id.iv_pic);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        PartsData partsData = mList.get(position);
        holder.tvName.setText(partsData.getName());
        holder.tvValue.setText(partsData.getPpPrice());
        holder.tvContent.setText(partsData.getSpecs());
        holder.tvText.setText(partsData.getIntro());
        holder.tvRemark.setText(partsData.getTips());
        ImageLoader.getInstance().displayImage(partsData.getIcon(),holder.ivPic);
        return v;
    }


    public class HolderView {
        private ImageView ivPic;
        private TextView tvName;
        private TextView tvValue;
        private TextView tvContent;
        private TextView tvText;
        private TextView tvRemark;
    }
}


