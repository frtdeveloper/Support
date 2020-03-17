package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.entity.PictureVOData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class SparePartsDetailAdapter extends BaseAdapter {
    private ArrayList<PictureVOData> mList = new ArrayList<PictureVOData>();
    protected LayoutInflater mInflater;
    protected Context cxt;
    private DisplayImageOptions options;

    public SparePartsDetailAdapter(Context context, ArrayList<PictureVOData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.bg_loading_top)
                .showImageForEmptyUri(R.mipmap.bg_load_false_top)
                .showImageOnFail(R.mipmap.bg_load_false_top).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
    }

    public void setList(ArrayList<PictureVOData> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PictureVOData getItem(int position) {
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
            holder.ivPic = (ImageView) v.findViewById(R.id.iv_pic);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        ImageLoader.getInstance().displayImage(mList.get(position).getSource(),holder.ivPic,options);
        return v;
    }


    public class HolderView {
        private ImageView ivPic;
    }
}


