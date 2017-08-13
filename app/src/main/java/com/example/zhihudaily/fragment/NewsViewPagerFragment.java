package com.example.zhihudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhihudaily.R;
import com.example.zhihudaily.adapter.NewsViewPagerAdapter;
import com.example.zhihudaily.json.ExtraNewsInfo;
import com.example.zhihudaily.util.Urls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwl on 2017/8/3.
 */

public class NewsViewPagerFragment extends Fragment {

    private ViewPager mViewPager;
    private List<Fragment> mFragments;

    public SwitchFragmentListener getmSwitchFragmentListener() {
        return mSwitchFragmentListener;
    }

    private SwitchFragmentListener mSwitchFragmentListener;

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    private int newsId;

    public void setNewsIdHomeList(List<Integer> newsIdHomeList) {
        this.newsIdHomeList = newsIdHomeList;
    }

    private List<Integer> newsIdHomeList = new ArrayList<>();


    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    private int startPos;//点击进入新闻位置

    public void setRecyclerViewFragment(RecyclerViewFragment recyclerViewFragment) {
        this.recyclerViewFragment = recyclerViewFragment;
    }

    public RecyclerViewFragment getRecyclerViewFragment() {
        return recyclerViewFragment;
    }

    private RecyclerViewFragment recyclerViewFragment;

    public void setLoadMoreNewsListener(LoadMoreNewsListener loadMoreNewsListener) {
        this.loadMoreNewsListener = loadMoreNewsListener;
    }

    private LoadMoreNewsListener loadMoreNewsListener;
    public interface LoadMoreNewsListener{
        void onLoad();
    }

    private NewsViewPagerAdapter newsViewPagerAdapter;

    public int getCurrentNewsId() {
        return currentNewsId;
    }

    private int currentNewsId;//保存当前显示页面新闻ID，以传给NewsCommentFragment

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    private int currentPos;//保存当前页面对应位置

    public ExtraNewsInfo getmExtraNewsInfo() {
        return mExtraNewsInfo;
    }

    public void setmExtraNewsInfo(ExtraNewsInfo mExtraNewsInfo) {
        this.mExtraNewsInfo = mExtraNewsInfo;
    }

    private ExtraNewsInfo mExtraNewsInfo;


//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden){
//            initNewsContent();
//            initOnPageChangeListener();
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_viewpager_fragment, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.news_viewpager);
        initFragments();
        newsViewPagerAdapter =
                new NewsViewPagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(newsViewPagerAdapter);
        return view;
    }

    void  initFragments(){
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new NewsContentFragment());
        mFragments.add(new NewsContentFragment());
        mFragments.add(new NewsContentFragment());
        mFragments.add(new NewsContentFragment());

    }

    public void setmSwitchFragmentListener(SwitchFragmentListener listener){
        mSwitchFragmentListener = listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setStartPos(startPos);
//
//        setNewsId(newsId);
//
//        setRecyclerViewFragment(recyclerViewFragment);
//        setLoadMoreNewsListener(new NewsViewPagerFragment.LoadMoreNewsListener() {
//            @Override
//            public void onLoad() {
//                recyclerViewFragment.loadMoreNews();
//            }
//        });


    }

    public void initOnPageChangeListener(){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //找到当前显示的fragment
                setCurrentPos(position);
                int pos = position % mFragments.size();
                NewsContentFragment currFragment =(NewsContentFragment) mFragments.get(pos);
                if(recyclerViewFragment.newsIdHomeList.size() < position + 2){
                    loadMoreNewsListener.onLoad();
                }
                currFragment.requestExtraNewsInfoFromServer(Urls.EXTRA_INFO +
                        recyclerViewFragment.newsIdHomeList.get(position));
                currentNewsId = recyclerViewFragment.newsIdHomeList.get(position);
                currFragment.requestNewsContentFromServer(Urls.NEWS_ADDRESS_HEAD +
                        recyclerViewFragment.newsIdHomeList.get(position));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public void initNewsContent(){
        mViewPager.setCurrentItem(startPos, false);
        NewsContentFragment firstPage =(NewsContentFragment) mFragments
                .get(startPos % mFragments.size());
        firstPage.requestExtraNewsInfoFromServer(Urls.EXTRA_INFO +
                recyclerViewFragment.newsIdHomeList.get(startPos));
        currentNewsId = recyclerViewFragment.newsIdHomeList.get(startPos);
        firstPage.requestNewsContentFromServer(Urls.NEWS_ADDRESS_HEAD +
                recyclerViewFragment.newsIdHomeList.get(startPos));
        setCurrentPos(startPos);

//        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View page, float position) {
//                int pageWidth = view.getWidth();
//                int pageHeight =view.getHeight();
//
//                if (position < -1)
//                {
//                    // [-Infinity,-1)
//                    // This page is way off-screen to the left.
//                    view.setAlpha(0);
//
//                }
//                else if (position <= 0)
//                {
//                    // [-1,0]
//                    // Use the default slide transition when moving to the left page
//                    view.setAlpha(1);
//                    view.setTranslationX(0);
//                    view.setScaleX(1);
//                    view.setScaleY(1);
//                }
//                else if (position <= 1)
//                {
//                    // (0,1]
//
//                    // Fade the page out.
//                    view.setAlpha(1 - position);
////
////          // Counteract the default slide transition
////          view.setAlpha(1);
//                    view.setTranslationX(pageWidth * -position);
////
////          // Scale the page down (between MIN_SCALE and 1)
//                    float scaleFactor = MIN_SCALE
//                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
//                    view.setScaleX(scaleFactor);
//                    view.setScaleY(scaleFactor);
//                }
//                else
//                {
//                    // (1,+Infinity]
//                    // This page is way off-screen to the right.
//                    view.setAlpha(0);
//                }
//            }
//        });
    }
}
