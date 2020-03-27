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
import com.dfh.support.utils.TextUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class SparePartsListAdapter extends BaseAdapter {
    private ArrayList<PartsData> mList = new ArrayList<PartsData>();
    protected LayoutInflater mInflater;
    protected Context cxt;
//    private DisplayImageOptions options;


    public SparePartsListAdapter(Context context, ArrayList<PartsData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.bg_loading_bottom)
//                .showImageForEmptyUri(R.mipmap.bg_loading_bottom)
//                .showImageOnFail(R.mipmap.bg_loading_bottom).cacheInMemory(true)
//                .cacheOnDisk(true).considerExifParams(true).build();
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
            holder.ivLine = (View)v.findViewById(R.id.view_line);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        PartsData partsData = mList.get(position);
        if (!TextUtils.isEmpty(partsData.getName())) {
            holder.tvName.setVisibility(View.VISIBLE);
            holder.tvName.setText(cxt.getResources().getString(R.string.common_name) + partsData.getName());
        } else {
            holder.tvName.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(partsData.getPpPrice())) {
            holder.tvValue.setVisibility(View.VISIBLE);
            holder.tvValue.setText(cxt.getResources().getString(R.string.common_value) + partsData.getPpPrice());
        } else {
            holder.tvValue.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(partsData.getSpecs())) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(cxt.getResources().getString(R.string.common_specs) + partsData.getSpecs());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(partsData.getIntro())) {
            holder.tvText.setVisibility(View.VISIBLE);
            holder.tvText.setText(cxt.getResources().getString(R.string.common_explain) + partsData.getIntro());
        } else {
            holder.tvText.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(partsData.getTips())) {
            holder.tvRemark.setVisibility(View.VISIBLE);
            holder.ivLine.setVisibility(View.VISIBLE);
            holder.tvRemark.setText(cxt.getResources().getString(R.string.common_remark) + partsData.getTips());
        } else {
            holder.tvRemark.setVisibility(View.GONE);
            holder.ivLine.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(partsData.getIcon(), holder.ivPic);
        return v;
    }


    public class HolderView {
        private ImageView ivPic;
        private TextView tvName;
        private TextView tvValue;
        private TextView tvContent;
        private TextView tvText;
        private TextView tvRemark;
        private View ivLine;
    }
}


