package com.example.zhihudaily;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.zhihudaily.adapter.NewsListAdapter;
import com.example.zhihudaily.json.Story;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwl on 2017/7/5.
 */

public class TestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;//进度对话框
    private SwipeRefreshLayout swipeRefreshLayout;
    private Banner mBanner;

    private List<String> bannerImages = new ArrayList<>();//banner轮播图片
    private List<String> imageTitles = new ArrayList<>();//轮播图片标题
    private List<Story> storyList = new ArrayList<>();//recyclerview新闻子项内容
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_fragment);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        View header = LayoutInflater.from(this).inflate(R.layout.header, null);//这个方法为何？
        mBanner =(Banner) header.findViewById(R.id.banner);
        //已在XML文件中设置Banner高度

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        NewsListAdapter myAdapter = new NewsListAdapter(storyList, this);
        myAdapter.setmHeaderView(mBanner);

        View titleView = LayoutInflater.from(this).
                inflate(R.layout.recyclerview_title, null);
        myAdapter.setmTitleView(titleView);
        mRecyclerView.setAdapter(myAdapter);
        //启动mBanner
        initDataAndImages();
        mBanner.setImages(bannerImages)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setImageLoader(new GlideImageLoader1())
                .setBannerTitles(imageTitles)
                .isAutoPlay(true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        mBanner.start();
    }


    private void initDataAndImages(){
        bannerImages.add("http://img3.imgtn.bdimg.com/it/u=2758743658,581437775&fm=15&gp=0.jpg");
        bannerImages.add("http://img3.imgtn.bdimg.com/it/u=2105877023,3759180926&fm=15&gp=0.jpg");


        imageTitles.add("贝聿铭建筑1");
        imageTitles.add("贝聿铭建筑2");


        ArrayList<String> list = new ArrayList<>();
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
//        Story story = new Story("中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）", list);
//        storyList.add(story);
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));

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
            mProgressDialog = new ProgressDialog(TestActivity.this);
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

    class GlideImageLoader1 extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */


//            //Glide 加载图片简单用法，加载网络图片
//            Glide.with(context)
//                    .load(path)
//                    .into(imageView);

        //Picasso 加载图片简单用法
        Picasso.with(context).load((String)path).into(imageView);
//
//        //用fresco加载图片简单用法，记得要写下面的createImageView方法
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);
        }
    }

}
