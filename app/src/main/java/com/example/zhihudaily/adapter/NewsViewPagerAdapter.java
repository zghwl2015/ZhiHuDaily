package com.example.zhihudaily.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.zhihudaily.fragment.NewsContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monlonwong on 2017/8/6.
 */

public class NewsViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public NewsContentFragment getCurrFragment() {
        return currFragment;
    }

    private NewsContentFragment currFragment;

    public NewsViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }




//    @Override
//    public Fragment getItem(int position) {
//        return mFragments.get(position);
//    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public NewsViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {

//        NewsContentFragment newsContentFragment = new NewsContentFragment();
//        mFragments.add(newsContentFragment);
//        return newsContentFragment;
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //由于是对应无线轮播，故要做循环处理
        int pos = position % mFragments.size();
        return super.instantiateItem(container, pos);
//        return mFragments.get(pos);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currFragment = (NewsContentFragment) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }


}
