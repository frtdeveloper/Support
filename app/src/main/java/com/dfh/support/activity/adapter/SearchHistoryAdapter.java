package com.dfh.support.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfh.support.R;
import com.dfh.support.activity.support.DebuggingSearchListActivity;
import com.dfh.support.activity.support.SearchActivity;
import com.dfh.support.activity.support.ServiceDetailActivity;

import java.util.ArrayList;


public class SearchHistoryAdapter extends BaseAdapter {
    private ArrayList<String> mList = new ArrayList<String>();
    protected LayoutInflater mInflater;
    protected Context cxt;

    public SearchHistoryAdapter(Context context, ArrayList<String> list) {
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
    public View getView(final int position, View v, ViewGroup parent) {
        HolderView holder;
        if (null == v) {
            v = mInflater.inflate(R.layout.search_history_adapter, null);
            holder = new HolderView();
            holder.tvHistory = (TextView) v.findViewById(R.id.tv_history);
            holder.rlHistory = (RelativeLayout) v.findViewById(R.id.rl_history);
            v.setTag(holder);
        } else {
            holder = (HolderView) v.getTag();
        }
        holder.tvHistory.setText(mList.get(position));
//        holder.rlHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(cxt, DebuggingSearchListActivity.class);
//                intent.putExtra("keyword",mList.get(position));
//                cxt.startActivity(intent);
//            }
//        });
        return v;
    }


    public class HolderView {
        private TextView tvHistory;
        private RelativeLayout rlHistory;
    }
}


