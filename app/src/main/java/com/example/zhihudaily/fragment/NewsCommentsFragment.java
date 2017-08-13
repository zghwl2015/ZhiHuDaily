package com.example.zhihudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhihudaily.R;
import com.example.zhihudaily.activity.MainActivity;
import com.example.zhihudaily.json.ExtraNewsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwl on 2017/7/10.
 * 用来显示评论界面
 */

public class NewsCommentsFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getNewsId() {
        return newsId;
    }

    private int newsId;

    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    public void setExtraNewsInfo(ExtraNewsInfo extraNewsInfo) {
        this.extraNewsInfo = extraNewsInfo;
    }

    private ExtraNewsInfo extraNewsInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_comments, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTitles.add("长评");
        mTitles.add("短评");

//        LongCommentsFragment longComments = new LongCommentsFragment();
//        LongCommentsFragment shortComments = new LongCommentsFragment();



        mFragments.add(new LongCommentsFragment());
        mFragments.add(new ShortCommentFragment());
        return view;
    }

    public void init(){





        //在fragment中给viewpager设置adapter需要使用getChildFragmentManager()
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager(), mTitles, mFragments));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);

//        longComments.setNewId(newsId);
//        shortComments.setNewId(newsId);
//        if (newsId != 0){
//            longComments.requestNewsConmentsFromServer(Urls.NEWS_COMMENTS + newsId + Urls.LONG_COMMENTS);
//            shortComments.requestNewsConmentsFromServer(Urls.NEWS_COMMENTS + newsId + Urls.SHORT_COMMENTS);
//        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        mViewPager.setCurrentItem(tab.getPosition());
        //更新ToolBar显示当前长/短评论数
        if (tab.getPosition() == 0){
            ((MainActivity)getActivity()).getmToolbar().setTitle(
                    extraNewsInfo.longComments + "条长评"
            );
        }else {
            ((MainActivity)getActivity()).getmToolbar().setTitle(
                    extraNewsInfo.shortComments + "条短评"
            );
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

class MyAdapter extends FragmentPagerAdapter {
    private List<String> mTitles ;
    private List<Fragment> mFragments ;

    public MyAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments){
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    //配置标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

}
