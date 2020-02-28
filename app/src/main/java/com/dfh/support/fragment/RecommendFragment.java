package com.dfh.support.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dfh.support.R;
import com.dfh.support.utils.LogUtil;

public class RecommendFragment extends Fragment {

    private View mFragmentView, mFragmentInfo;

    private static RecommendFragment s_instance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.printFragmentLog("RecommendFragment::onCreateView================");
        mFragmentView = inflater.inflate(R.layout.recommend_layout, null);
        mFragmentInfo = mFragmentView.findViewById(R.id.recommend_info);
        Toast.makeText(getActivity(), String.valueOf(mFragmentInfo.getId()), Toast.LENGTH_LONG).show();
        return mFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.printFragmentLog("RecommendFragment::onResume================");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.printFragmentLog("RecommendFragment::onDetach================");
    }

    public static RecommendFragment newInstance(){
        if (null == s_instance)
            s_instance = new RecommendFragment();

        return s_instance;
    }
}