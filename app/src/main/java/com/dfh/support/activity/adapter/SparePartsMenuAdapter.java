package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dfh.support.R;
import com.dfh.support.entity.ClassifyData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class SparePartsMenuAdapter extends BaseAdapter {
    private ArrayList<ClassifyData> mList = new ArrayList<ClassifyData>();
    protected LayoutInflater mInflater;
    protected Context cxt;

    public SparePartsMenuAdapter(Context context, ArrayList<ClassifyData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<ClassifyData> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ClassifyData getItem(int position) {
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
            v = mInflater.inflate(R.layout.spare_parts_menu_adapter, null);
            holder = new HolderView();
            holder.ivPic = (ImageView) v.findViewById(R.id.iv_pic);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        ClassifyData classifyData = mList.get(position);
        ImageLoader.getInstance().displayImage(classifyData.getIcon(),holder.ivPic);
        return v;
    }


    public class HolderView {
        private ImageView ivPic;
    }
}


