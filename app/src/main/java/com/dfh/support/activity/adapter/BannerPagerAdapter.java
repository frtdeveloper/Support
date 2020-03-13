package com.dfh.support.activity.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class BannerPagerAdapter extends PagerAdapter {

    ArrayList<View> viewList = new ArrayList<View>();
    public BannerPagerAdapter(ArrayList<View> viewList) {
        this.viewList = viewList;

    }

    public void setList(ArrayList<View> viewList) {
        this.viewList = viewList;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
}
