package com.dfh.support.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.support.DebuggingListActivity;
import com.dfh.support.activity.support.DebuggingMenuActivity;
import com.dfh.support.entity.DebugMenuData;
import com.dfh.support.entity.DebugMenuListData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class DebuggingMenuAdapter extends BaseAdapter {
    private ArrayList<DebugMenuListData> mList = new ArrayList<DebugMenuListData>();
    protected LayoutInflater mInflater;
    protected Context cxt;

    public DebuggingMenuAdapter(Context context, ArrayList<DebugMenuListData> list) {
        cxt = context;
        mInflater = LayoutInflater.from(this.cxt);
        mList = list;
    }

    public void setList(ArrayList<DebugMenuListData> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DebugMenuListData getItem(int position) {
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
            v = mInflater.inflate(R.layout.debugging_menu_adapter, null);
            holder = new HolderView();
            holder.rlOne = (RelativeLayout) v.findViewById(R.id.rl_one);
            holder.rlTwo = (RelativeLayout) v.findViewById(R.id.rl_two);
            holder.ivOne = (ImageView) v.findViewById(R.id.iv_one);
            holder.ivTwo = (ImageView) v.findViewById(R.id.iv_two);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        holder.ivOne.setVisibility(View.GONE);
        holder.ivTwo.setVisibility(View.GONE);
        DebugMenuListData debugMenuListData = mList.get(position);
        ArrayList<DebugMenuData> debugMenuDataList = debugMenuListData.getDebugMenuData();
        if (debugMenuDataList.size() > 0) {
            final DebugMenuData debugMenuData = debugMenuDataList.get(0);
            holder.ivOne.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(debugMenuData.getIcon(), holder.ivOne);
            holder.rlOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cxt, DebuggingListActivity.class);
                    intent.putExtra("id", debugMenuData.getId());
                    intent.putExtra("title",debugMenuData.getMainTitle());
                    cxt.startActivity(intent);
                }
            });
        }
        if (debugMenuDataList.size() > 1) {
            final DebugMenuData debugMenuData = debugMenuDataList.get(1);
            holder.ivTwo.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(debugMenuData.getIcon(), holder.ivTwo);
            holder.rlTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cxt, DebuggingListActivity.class);
                    intent.putExtra("id", debugMenuData.getId());
                    intent.putExtra("title",debugMenuData.getMainTitle());
                    cxt.startActivity(intent);
                }
            });
        }
        return v;
    }


    public class HolderView {
        private RelativeLayout rlOne;
        private RelativeLayout rlTwo;
        private ImageView ivOne;
        private ImageView ivTwo;
    }
}


