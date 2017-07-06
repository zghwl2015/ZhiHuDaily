package com.example.zhihudaily;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.adapter.GlideImageLoader;
import com.example.zhihudaily.adapter.NewsListAdapter;
import com.example.zhihudaily.json.Story;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwl on 2017/7/3.
 * 新闻列表碎片，里面向服务器请求数据，然后本地缓存。当需要展示新闻时，首先从本地加载，
 * 如果没有则向服务器请求。将数据装入NewsListAdapter，加载到recycleView中。使用
 * 碎片方便以后复用。
 */

public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsListAdapter myAdapter;
    private ProgressDialog mProgressDialog;//进度对话框
    private SwipeRefreshLayout swipeRefreshLayout;
    private Banner mBanner;

    private List<Integer> bannerImages = new ArrayList<>();;//banner轮播图片
    private List<String> imageTitles = new ArrayList<>();;//轮播图片标题
    private List<Story> storyList = new ArrayList<>();//recyclerview新闻子项内容





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.recycleview_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header, null);//这个方法为何？
//        mBanner =(Banner) header.findViewById(R.id.banner);
        //已在XML文件中设置Banner高度

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        myAdapter = new NewsListAdapter(storyList, getActivity());
        setmHeaderViewAndTitleView(mRecyclerView);
//        myAdapter.setmHeaderView(null);

//        View titleView = LayoutInflater.from(getActivity()).
//                inflate(R.layout.recyclerview_title, null);
//        myAdapter.setmTitleView(titleView);
        mRecyclerView.setAdapter(myAdapter);
        //启动mBanner
        initDataAndImages();
        mBanner.setImages(bannerImages)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setImageLoader(new GlideImageLoader(){

                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(RecyclerViewFragment.this).load(path)
                                .into(imageView);
                    }
                })
                .setBannerTitles(imageTitles)
                .setBannerAnimation(Transformer.DepthPage)
                .isAutoPlay(true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        mBanner.start();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        return view;
    }

    public void setmHeaderViewAndTitleView(RecyclerView view){
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header,view, false);//这个方法为何？
        mBanner =(Banner) header.findViewById(R.id.banner);
        myAdapter.setmHeaderView(mBanner);

        View titleView = LayoutInflater.from(getActivity()).
                inflate(R.layout.recyclerview_title, view, false);
        myAdapter.setmTitleView(titleView);

    }

    private void initDataAndImages(){
//        bannerImages.add("http://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png");
//        bannerImages.add("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg");
//        bannerImages.add("http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg");
        bannerImages.add(R.drawable.a);
        bannerImages.add(R.drawable.b);
        bannerImages.add(R.drawable.c);

        imageTitles.add("贝聿铭建筑1");
        imageTitles.add("贝聿铭建筑2");
        imageTitles.add("贝聿铭建筑3");

        ArrayList<String> list = new ArrayList<>();
        list.add("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg");
        Story story = new Story("中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）", list);
        storyList.add(story);
        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));

//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<Integer>(list)));
//        storyList.add(new Story("中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）", new ArrayList<Integer>(list)));


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /*
    请求最新消息
     */
    private void requestLatestNews(){

    }


    /*
    请求具体消息内容
     */
    private void requestNewsContent(){

    }

    /*
    向服务器端请求
     */
    private void requestFromServer(){

    }

    /*
    显示进度对话框
     */
    private void showProgressDialog(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }
    /*
    关闭进度对话框
     */
    private void closeProgressDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}
