package com.dfh.support.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.entity.AdvertisementData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class AdvertisementListItemAdapter extends BaseAdapter {

    private ArrayList<AdvertisementData> mList = new ArrayList<AdvertisementData>();
    protected LayoutInflater mInflater;
    protected Context cxt;
    private HashMap<Integer, Boolean> clickMap = new HashMap<Integer, Boolean>();
    private DisplayImageOptions options;

    public AdvertisementListItemAdapter(Context context, ArrayList<AdvertisementData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.bg_loading_bottom)
                .showImageForEmptyUri(R.mipmap.bg_load_false_bottom)
                .showImageOnFail(R.mipmap.bg_load_false_bottom).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
    }

    public void setList(ArrayList<AdvertisementData> list) {
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
            v = mInflater.inflate(R.layout.advertisement_list_item_adapter, null);
            holder = new HolderView();
            holder.ivPic = (ImageView) v.findViewById(R.id.iv_advertisement_center_icon);
            holder.tvTitle = (TextView) v.findViewById(R.id.tv_advertisement_center_title);
            holder.tvSee = (TextView) v.findViewById(R.id.tv_see);
            holder.tvZan = (TextView) v.findViewById(R.id.tv_zan);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        final AdvertisementData advertisementData = mList.get(position);
        holder.tvTitle.setText(advertisementData.getTitle());
        holder.tvSee.setText(advertisementData.getBrowses());
        holder.tvZan.setText(advertisementData.getLikes());
        ImageLoader.getInstance().displayImage(advertisementData.getIcon(),holder.ivPic,options);
        return v;
    }

    public class HolderView {
        private TextView tvTitle;
        private TextView tvSee;
        private TextView tvZan;
        private ImageView ivPic;
    }


}
