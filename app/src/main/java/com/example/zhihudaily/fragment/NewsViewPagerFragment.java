package com.example.zhihudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhihudaily.R;

/**
 * Created by hwl on 2017/8/3.
 */

public class NewsViewPagerFragment extends Fragment {

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_viewpager_fragment, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.news_viewpager);
//        mViewPager.setAdapter(new );
        return view;
    }
}
