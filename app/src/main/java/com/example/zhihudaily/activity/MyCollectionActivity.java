package com.example.zhihudaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.zhihudaily.R;
import com.example.zhihudaily.adapter.NewsListAdapter;
import com.example.zhihudaily.json.Story;
import com.example.zhihudaily.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monlonwong on 2017/8/9.
 */

public class MyCollectionActivity extends BaseActivity {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private NewsListAdapter mNewsListAdapter;//复用Adapter
    private List<Story> collectedStoryList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collection);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Utility.transparentStatusBar(this);
        //设置标题栏
        setUpToolbar();
        //设置滑动标题栏
        setUpCollapsingToolbar();
        //设置recyclerView
        setUpRecyclerView();
    }

    private void setUpToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.my_collection_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉默认title
    }

    private void setUpCollapsingToolbar(){
        collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
    }

    private void setUpRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_collection_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        Intent intentGet = getIntent();
        collectedStoryList =(List<Story>) intentGet.getSerializableExtra("My_Collection");
        mNewsListAdapter = new NewsListAdapter(collectedStoryList, this, getApp().isNightMode());
        mNewsListAdapter.setmHeaderView(null);
        mNewsListAdapter.setmTitleView(null);
        mRecyclerView.setAdapter(mNewsListAdapter);

    }
}
