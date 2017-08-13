package com.example.zhihudaily.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.activity.MainActivity;
import com.example.zhihudaily.adapter.GlideImageLoader;
import com.example.zhihudaily.adapter.NewsListAdapter;
import com.example.zhihudaily.json.BeforeNews;
import com.example.zhihudaily.json.LatestNews;
import com.example.zhihudaily.json.NewsContent;
import com.example.zhihudaily.json.Story;
import com.example.zhihudaily.json.ThemeNewsContent;
import com.example.zhihudaily.json.ThemeNewsStory;
import com.example.zhihudaily.json.TopStory;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;
import com.example.zhihudaily.util.Yesterday;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

//    private TextView textView;
    private List<Integer> newsIdList = new ArrayList<>();//banner轮播新闻内容ID集合
    private List<String> bannerImages = new ArrayList<>();;//banner轮播图片
    private List<String> imageTitles = new ArrayList<>();;//轮播图片标题

    public List<Story> getStoryList() {
        return storyList;
    }

    private List<Story> storyList = new ArrayList<>();//recyclerview新闻子项内容

    public List<Integer> newsIdHomeList = new ArrayList<>();//保存新闻ID链表

    public void setMenuIdMap(Map<String, Integer> menuIdMap) {
        this.menuIdMap = menuIdMap;
    }

    private Map<String, Integer> menuIdMap = new HashMap<>();

    private SwitchFragmentListener mSwitchFragmentListener;
    private NewsContentFragment mNewsContentFragment;

    private int previousItemNum = 0;//与Adapter中的isClicks链表有关，用于点击改变recyclerview子项文本颜色


//    //下面的变量与上拉自动加载有关
    private String currentDate = new String();
    //标识是否主页
    public boolean isHome = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.recycleview_fragment, container, false);
//        textView = (TextView) view.findViewById(R.id.text_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header, null);//这个方法为何？
//        mBanner =(Banner) header.findViewById(R.id.banner);
        //已在XML文件中设置Banner高度

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));//一定在设置适配器之前添加


//        myAdapter.setmHeaderView(null);


//        View titleView = LayoutInflater.from(getActivity()).
//                inflate(R.layout.recyclerview_title, null);
//        myAdapter.setmTitleView(titleView);

//        requestLatestNewsFromServer(Urls.LATEST_NEWS);
        initRecyclerView();
//        addScrollListener();
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                int lastItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
//                        .findLastVisibleItemPosition();
//                if (lastItemPosition == ((LinearLayoutManager) mRecyclerView.getLayoutManager())
//                        .getItemCount() - 1){
//                    loadMoreNews();
//                }
//            }
//        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        return view;
    }

    void initRecyclerView(){
        myAdapter = new NewsListAdapter(storyList, getActivity(), ((MainActivity)getActivity())
        .getApp().isNightMode());
        setmHeaderViewAndTitleView(mRecyclerView);
        mRecyclerView.setAdapter(myAdapter);
    }

    //上拉加载更多
    void addScrollListener(){
        final String title = ((MainActivity)getActivity()).mToolbar.getTitle().toString();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isHome){
                    int lastItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();
                    if (lastItemPosition == ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                            .getItemCount() - 1){
                        loadMoreNews();
                    }
                }
            }
        });
    }

    /*
    用于上拉加载更多
     */
    public void loadMoreNews(){
        if (Yesterday.findYesterday(currentDate) != ""){
            requestBeforeNewsFromServer(Urls.BEFOREE_NEWS + Yesterday.findYesterday(currentDate));
            currentDate = Yesterday.findYesterday(currentDate);
        }

    }
    /*
    完成一些与父活动相关的初始化操作
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestLatestNewsFromServer(Urls.LATEST_NEWS);
        menuIdMap = ((MainActivity)getActivity()).getMenuIdMap();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgressDialog();
                String toolbarTitle = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getTitle().toString();
                if ( toolbarTitle == "首页"){
                    requestLatestNewsFromServer(Urls.LATEST_NEWS);

                }else {
                    int themeId = menuIdMap.get(toolbarTitle);
                    requestThemeNewsFromServer(Urls.THEME_NEWS + themeId);
                }
                swipeRefreshLayout.setRefreshing(false);
                closeProgressDialog();


            }
        });
        menuIdMap = ((MainActivity) getActivity()).getMenuIdMap();
        myAdapter.setRecyclerViewOnClickListener(new NewsListAdapter.RecyclerViewOnClickListener() {
            @Override
            public void onClick(int newsId, int pos) {
//                requestNewsContentFromServer(Urls.NEWS_ADDRESS_HEAD + id, id);
//                Intent intent = new Intent(getActivity(), TestActivity.class);
//                intent.putExtra("themeId", id);
//                getActivity().startActivity(intent);

                mSwitchFragmentListener.switchFragment(newsId, pos);
            }

            @Override
            public int getThemeId(String title) {
                if (menuIdMap.get(title) != null){
                    return menuIdMap.get(title);
                }
                return -1;

            }
        });
    }

    public void setmHeaderViewAndTitleView(RecyclerView view){
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header,view, false);//这个方法为何？
        mBanner =(Banner) header.findViewById(R.id.banner);
        myAdapter.setmHeaderView(mBanner);

        View titleView = LayoutInflater.from(getActivity()).
                inflate(R.layout.recyclerview_title, view, false);
        myAdapter.setmTitleView(titleView);

    }

    public void startBanner(){
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
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                        requestNewsContentFromServer(Urls.NEWS_ADDRESS_HEAD + newsIdList.get(position),
                                newsIdList.get(position));
                    }
                });
        mBanner.start();
    }

    //测试用的初始化数据方法，主要测试recyclerView是否正常显示
    private void initDataAndImages(){
        bannerImages.add("https://pic3.zhimg.com/v2-5853b2c39fd0316b4d2db748e74c4f6e.jpg");
//        bannerImages.add("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg");
//        bannerImages.add("http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg");
//        bannerImages.add(R.drawable.a);
//        bannerImages.add(R.drawable.b);
//        bannerImages.add(R.drawable.c);

        imageTitles.add("贝聿铭建筑1");
//        imageTitles.add("贝聿铭建筑2");
//        imageTitles.add("贝聿铭建筑3");


//        requestFromServer();

        ArrayList<String> list = new ArrayList<>();
        list.add("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg");
//        Story story = new Story("中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）", list);
//        storyList.add(story);
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));
//        storyList.add(new Story("长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？", new ArrayList<String>(list)));

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

//    public void parseJson(String res){
//        Log.d("LatestNews","res: " + res);
//        LatestNews latestNews = new Gson().fromJson(res, LatestNews.class);
//        Log.d("LatestNews","story title is : " + latestNews.stories.get(0).title);
//        Log.d("LatestNews","story image url is : " + latestNews.stories.get(0).images.get(0));
//        for (Story story : latestNews.stories)
//            storyList.add(story);
//        for (TopStory topStory : latestNews.topStories){
//            bannerImages.add(topStory.image);
//            imageTitles.add(topStory.title);
//            newsIdList.add(topStory.id);
//        }
//        startBanner();
//        myAdapter.notifyDataSetChanged();
//        swipeRefreshLayout.setRefreshing(false);
//
//    }

    /*
    请求最新消息，先查看本地数据库有无缓存，有则直接从本地获取；无则向服务器请求
     */
    public void requestLatestNews(){

    }

    /*
    请求主题日报，先查看本地数据库有无缓存，有则直接从本地获取；无则向服务器请求
     */
    public void requestThemeNews(){

    }


    /*
    向远程服务端请求内容
     */
    public void requestLatestNewsFromServer(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final LatestNews latestNews =  new Gson().fromJson(response.body().string(), LatestNews.class);;
//                final String string = response.body().toString();

//                final ThemeNewsContent themeNewsContent = Utility.handleThemeNewsResponse(response.body().toString());
//                final boolean or = (response.isSuccessful());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (latestNews != null)
                            handleInfo(latestNews);
                        else
                            Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
//                        parseJson(string);
                    }
                });
            }
        });
    }

    /*
    向远程服务端请求历史内容
     */
    public void requestBeforeNewsFromServer(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final BeforeNews beforeNews =  new Gson().fromJson(response.body().string(), BeforeNews.class);;
//                final String string = response.body().toString();

//                final ThemeNewsContent themeNewsContent = Utility.handleThemeNewsResponse(response.body().toString());
//                final boolean or = (response.isSuccessful());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (beforeNews != null)
                            handleInfo(beforeNews);
                        else
                            Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
//                        parseJson(string);
                    }
                });
            }
        });
    }


    /*
    请求具体消息内容
     */
    public void requestThemeNewsFromServer(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ThemeNewsContent themeNewsContent =  new Gson().fromJson(response.body().string(),
                        ThemeNewsContent.class);
                final boolean or = (response.isSuccessful());
//                final String string = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (themeNewsContent != null)
                            handleInfo(themeNewsContent);
                        else
                            Toast.makeText(getActivity(), "请求失败=_=" + or, Toast.LENGTH_SHORT).show();
//                        parseJson(string);
//                        textView.setText(string);
                    }
                });
            }
        });
    }



    /*
    处理返回信息，查看有无更新数据，有更新则通知adapter刷新；无则返回，不用刷新。
     */
    public void handleInfo(LatestNews latestNews){
        //先清空
        storyList.clear();
        bannerImages.clear();
        imageTitles.clear();

        currentDate = latestNews.date;

        addScrollListener();
        for (Story story : latestNews.stories){
            storyList.add(story);
            newsIdHomeList.add(story.id);
            ((MainActivity)getActivity()).isCollectedList.add(false);
            ((MainActivity)getActivity()).isPraisedList.add(false);
        }

        for (TopStory topStory : latestNews.topStories){
            bannerImages.add(topStory.image);
            imageTitles.add(topStory.title);
            newsIdList.add(topStory.id);
        }
        extendIsclicks(previousItemNum, storyList.size());
        previousItemNum = storyList.size();
        startBanner();
        myAdapter.notifyDataSetChanged();

    }

    //和点击新闻颜色变淡操作有关
    void extendIsclicks(int preLen, int currLen){
        for (int i =preLen; i < currLen; i++){
            myAdapter.isClicks.add(false);
        }
    }

    public void handleInfo(BeforeNews beforeNews){

        for (Story story : beforeNews.stories){
            storyList.add(story);
            newsIdHomeList.add(story.id);
            ((MainActivity)getActivity()).isCollectedList.add(false);
            ((MainActivity)getActivity()).isPraisedList.add(false);
        }

//        for (TopStory topStory : latestNews.topStories){
//            bannerImages.add(topStory.image);
//            imageTitles.add(topStory.title);
//        }
//        startBanner();
        extendIsclicks(previousItemNum, storyList.size());
        previousItemNum = storyList.size();
        myAdapter.notifyDataSetChanged();


    }

    public void handleInfo(ThemeNewsContent themeNewsContent){

        storyList.clear();
        for (ThemeNewsStory themeNewsStory : themeNewsContent.stories)
            storyList.add(new Story(themeNewsStory.title, themeNewsStory.images, themeNewsStory.id));
        bannerImages.clear();
        imageTitles.clear();
        bannerImages.add(themeNewsContent.background);
        imageTitles.add(themeNewsContent.description);
        //更新isClicks
        myAdapter.isClicks.clear();
        extendIsclicks(0, storyList.size());
        startBanner();
        myAdapter.notifyDataSetChanged();
//        swipeRefreshLayout.setRefreshing(false);
////        closeProgressDialog();

    }

    /*
   请求具体新闻内容
    */
    public void requestNewsContentFromServer(String address, final int newId){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final NewsContent newsContent =  new Gson().fromJson(response.body().string(),
                        NewsContent.class);
                final boolean or = (response.isSuccessful());
//                final String string = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (newsContent != null)
                            handleInfo(newsContent, newId);
                        else
                            Toast.makeText(getActivity(), "请求失败=_=" + or, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //将新闻内容解析成html，使用WebView加载
    public void handleInfo(NewsContent newsContent, int newId){
        String css ="<link rel=\"stylesheet\" href=\"" + newsContent.css.get(0) + "\" " +
                "type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsContent.body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
//        mNewsContentFragment = new NewsContentFragment();//不能在Fragment中实例化，而应该放到Activity中实例化

        ((MainActivity) getActivity()).setmWebViewContent(html);
        ((MainActivity) getActivity()).setmNewsContent(newsContent);
        mSwitchFragmentListener.switchFragment(newId);

    }

//    public interface SwitchFragmentListener{
//        void switchFragment();
//    }

    public void setmSwitchFragmentListener(SwitchFragmentListener listener){
        mSwitchFragmentListener = listener;
    }

//

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

    //    private Handler handler = new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            closeProgressDialog();
//            parseJson((String) msg.obj);
//        }
//    };
//    public void showResponse(final String response){
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                closeProgressDialog();
////                parseJson(response);
//                handleInfo(new Gson().fromJson(response, LatestNews.class));
//            }
//        });
//    }
//
//
//
//
//
//    /*
//    向服务器端请求
//     */
//    private void requestFromServer(){
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient okHttpClient = new OkHttpClient();
//                    Request request = new Request.Builder().url(Urls.LATEST_NEWS).build();
//                    String res = okHttpClient.newCall(request).execute().body().string();
//                    showResponse(res);
////                    Message message = new Message();
////                    message.obj = res;
////                    handler.sendMessage(message);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}
