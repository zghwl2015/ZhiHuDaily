package com.example.zhihudaily.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zhihudaily.R;
import com.example.zhihudaily.adapter.MenuAdapter;
import com.example.zhihudaily.adapter.MyMenu;
import com.example.zhihudaily.fragment.NewsCommentsFragment;
import com.example.zhihudaily.fragment.NewsViewPagerFragment;
import com.example.zhihudaily.fragment.RecyclerViewFragment;
import com.example.zhihudaily.fragment.SwitchFragmentListener;
import com.example.zhihudaily.json.NewsContent;
import com.example.zhihudaily.json.Story;
import com.example.zhihudaily.json.ThemeContent;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;
import com.example.zhihudaily.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
//    private ImageView testView;
    private LinearLayout mLinearLayout;

    private List<String> bannerImages = new ArrayList<>();;//banner轮播图片
    private List<String> imageTitles = new ArrayList<>();;//轮播图片标题
    private List<Story> storyList = new ArrayList<>();//recyclerview新闻子项内容

    private ArrayList<MyMenu> myMenuList = new ArrayList<>();
    private Map<String, Integer> menuIdMap = new HashMap<>();//存储主题日报对应请求id

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private RecyclerViewFragment recyclerViewFragment;

    private boolean isMapInited = false;

    //当前显示的fragment
    private Fragment mCurrentFragment;

    //TAG
    private static String TAG_HOME = "home";
    private static String TAG_CONTENT = "content";
    private static String TAG_COMMENT = "comment";

    public void setmWebViewContent(String mWebViewContent) {
        this.mWebViewContent = mWebViewContent;
    }

    NewsViewPagerFragment mNewsViewPagerFragment = new NewsViewPagerFragment();
//    NewsContentFragment mNewsContentFragment = new NewsContentFragment();
    NewsCommentsFragment newsCommentsFragment = new NewsCommentsFragment();
    private String mWebViewContent;

    public void setmNewsImage(String mNewsImage) {
        this.mNewsImage = mNewsImage;
    }

    private String mNewsImage;

    public void setmNewsContent(NewsContent mNewsContent) {
        this.mNewsContent = mNewsContent;
    }

    private NewsContent mNewsContent;

    private boolean isFirstAccess = false;//标识是否第一次点击进入新闻内容页面
    private boolean isHomeAsUp = true;

    public MainActivity() {
        super();
    }

    //对应滑动菜单中的我的收藏和夜间模式布局
    private LinearLayout myCollect;
    private LinearLayout nightMode;

    //我的收藏的新闻列表
    public List<Story> collectedStoryList = new ArrayList<>();
    public List<Boolean> isCollectedList = new ArrayList<>();
    public List<Boolean> isPraisedList = new ArrayList<>();

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //4.4以上系统实现沉浸式状态栏，需在XML文件中设置android:fitsSystemWindows="true"
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Utility.transparentStatusBar(this);

        mToolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉默认title
        mToolbar.setTitle("首页");//设置标题
//        toggleToolbarTitleColor();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.navigation_icon);
        }

        initMenuIdMap();
        //初始化我的收藏和夜间模式，并设置点击事件
        initTwoOption();


        //设置滑动菜单中的ListView的点击事件
        mListView = (ListView) findViewById(R.id.menu_listview);
        mListView.setDivider(null);//去掉分割线
        mListView.setVerticalScrollBarEnabled(false);//禁用ListView右侧普通滚动条
        mListView.setFastScrollEnabled(false);//禁用快速滚动条
        mListView.setAdapter (new MenuAdapter(MainActivity.this, R.layout.listview_item,
                initMenuData()));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu myMenu = myMenuList.get(position);
                mToolbar.setTitle(myMenu.getThemeContent());
                recyclerViewFragment.isHome = false;
                //切换到当前主题界面
//                RecyclerViewFragment fragment = new RecyclerViewFragment();
                int themeId = menuIdMap.get(myMenu.getThemeContent());
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fl_layout, fragment);//替换frameLayout
//                transaction.commit();
                recyclerViewFragment.requestThemeNewsFromServer(Urls.THEME_NEWS + themeId);
                if (!(mCurrentFragment instanceof RecyclerViewFragment)){
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.hide(mCurrentFragment).show(recyclerViewFragment);
                    ft.commit();

                }
                mDrawerLayout.closeDrawers();//关闭滑动菜单

            }
        });

        //滑动菜单首页点击事件切换
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_home);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbar.getTitle() != "首页"){
                    mToolbar.setTitle("首页");

                    recyclerViewFragment.requestLatestNewsFromServer(Urls.LATEST_NEWS);
                }
                if (!(mCurrentFragment instanceof RecyclerViewFragment)){
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.hide(mCurrentFragment).show(recyclerViewFragment);
                    ft.commit();

                }
//                TextView title =(TextView) mLinearLayout.findViewById(R.id.news_title);
//                title.setTextColor(getResources().getColor(R.color.gray));
//                if (title.getCurrentTextColor() == )
                mDrawerLayout.closeDrawers();//关闭滑动菜单
            }
        });

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        recyclerViewFragment = new RecyclerViewFragment();
        transaction.replace(R.id.fl_layout, recyclerViewFragment, TAG_HOME);
        transaction.commit();

        fragmentManager.executePendingTransactions();

        mCurrentFragment = recyclerViewFragment;//赋值
//        recyclerViewFragment.requestLatestNewsFromServer(Urls.LATEST_NEWS);
//        recyclerViewFragment.addScrollListener();
        //替换fragment方法，可以直接放在Activity方法中，有待优化代码结构
        recyclerViewFragment.setmSwitchFragmentListener(new SwitchFragmentListener() {
            @Override
            public void switchFragment() {

            }

            @Override
            public void switchFragment(final int newsId) {


            }

            @Override
            public void switchFragment(final int newsId, int startPos) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (mNewsViewPagerFragment.isAdded()){
                    transaction.hide(recyclerViewFragment).show(mNewsViewPagerFragment).commitAllowingStateLoss();
                }else {
                    transaction.hide(recyclerViewFragment).add(R.id.fl_layout, mNewsViewPagerFragment, TAG_CONTENT)
                            .commitAllowingStateLoss();
                }

                fragmentManager.executePendingTransactions();


                mToolbar.setTitle("新闻详情");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
                isHomeAsUp = false;
                //初始化滑动翻页监听事件和设置首页
                if (isFirstAccess){
                    mNewsViewPagerFragment.setStartPos(startPos);
                    mNewsViewPagerFragment.initNewsContent();

                }else {
                    mNewsViewPagerFragment.setStartPos(startPos);

                    mNewsViewPagerFragment.setNewsId(newsId);

                    mNewsViewPagerFragment.setRecyclerViewFragment(recyclerViewFragment);
                    mNewsViewPagerFragment.setLoadMoreNewsListener(new NewsViewPagerFragment.LoadMoreNewsListener() {
                        @Override
                        public void onLoad() {
                            recyclerViewFragment.loadMoreNews();
                        }
                    });
//                    Activity activity =  mNewsViewPagerFragment.getActivity();

                    mNewsViewPagerFragment.initOnPageChangeListener();
                    mNewsViewPagerFragment.initNewsContent();
                    isFirstAccess = true;
                }


//                mNewsContentFragment.setNewsImage(mNewsImage);
//                mNewsContentFragment.loadNewsContent(mNewsContent);
//                mNewsContentFragment.setContent(mWebViewContent);
//                mNewsContentFragment.setCurrentNewId(newsId);


                mCurrentFragment = mNewsViewPagerFragment;
                mNewsViewPagerFragment.setmSwitchFragmentListener(new SwitchFragmentListener() {
                    @Override
                    public void switchFragment() {
//                        NewsCommentsFragment newsCommentsFragment = new NewsCommentsFragment();
                        newsCommentsFragment.setNewsId(mNewsViewPagerFragment.getCurrentNewsId());
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if (newsCommentsFragment.isAdded()){
                            transaction.hide(mNewsViewPagerFragment).show(newsCommentsFragment).
                                    commitAllowingStateLoss();
                        }else {
                            transaction.hide(mNewsViewPagerFragment).add(R.id.fl_layout, newsCommentsFragment, TAG_COMMENT).
                                    commitAllowingStateLoss();
                        }

                        fragmentManager.executePendingTransactions();
                        newsCommentsFragment.init();
                        newsCommentsFragment.setExtraNewsInfo(mNewsViewPagerFragment
                        .getmExtraNewsInfo());
                        mCurrentFragment = newsCommentsFragment;
//                        newsCommentsFragment.requestNewsConmentsFromServer(Urls.NEWS_COMMENTS + newsId
//                        + Urls.SHORT_COMMENTS);
                    }

                    @Override
                    public void switchFragment(int newsId) {

                    }

                    @Override
                    public void switchFragment(int newsId, int startPos) {

                    }


                });
            }
        });
        if (ismIsAddedView())
            setTransparent();

    }

    private void initTwoOption(){
        myCollect = (LinearLayout) findViewById(R.id.my_collection);
        nightMode = (LinearLayout) findViewById(R.id.day_night_mode);
        myCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCollectionActivity.class);
                intent.putExtra("My_Collection", (Serializable) collectedStoryList);
                startActivity(intent);
            }
        });

        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeToggleTansitionAnimation();
                changeViewMode();
                mDrawerLayout.closeDrawers();

            }
        });
    }

    /*
    根据主题模式切换toolbar标题颜色
     */
    private void toggleToolbarTitleColor(){
        getmToolbar().setTitleTextColor( getApp().isNightMode() ? Color.GRAY : Color.WHITE);
    }




    //夜间模式切换
    void changeViewMode() {
        boolean isNight = getApp().isNightMode();
        if (isNight)
            ChangeToDay();
        else
            ChangeToNight();

        recreate();
//        startActivity(new Intent(this, MainActivity.class));
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        finish();
    }

    private void modeToggleTansitionAnimation(){
        final View decorView = getWindow().getDecorView();
        Bitmap screenShot = getScreenShot(decorView);
        if (decorView instanceof ViewGroup && screenShot != null){
            //将截屏覆于界面最上方，并添加一个透明度渐隐动画
            final View topView = new View(this);
            topView.setBackground(new BitmapDrawable(getResources(), screenShot));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            );
            ((ViewGroup)decorView).addView(topView, params);
//            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).addView(topView,
//                    new WindowManager.LayoutParams(
//                            WindowManager.LayoutParams.TYPE_APPLICATION,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                            PixelFormat.TRANSPARENT));
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(topView, "alpha",
                    1f, 0f);
            alphaAnimator.setDuration(6000);
            alphaAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //移除截屏View
                    ((ViewGroup)decorView).removeView(topView);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            alphaAnimator.start();
        }

    }

    /*
    获得截屏图片
     */
    private Bitmap getScreenShot(View view){
        view.setDrawingCacheEnabled(true);//设置可缓存View内容
        view.buildDrawingCache(true);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap screenShot;
        if (drawingCache != null){
            screenShot = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        }else {
            screenShot = null;
        }
        return screenShot;

    }

    /*
    返回键按压事件处理
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawers();
        }else {
            if (mCurrentFragment instanceof RecyclerViewFragment){
                super.onBackPressed();
            }
            if (mCurrentFragment instanceof NewsViewPagerFragment){
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.show(recyclerViewFragment)
                        .hide(mCurrentFragment);
                mCurrentFragment = recyclerViewFragment;
                ft.commit();
                mToolbar.setTitle("首页");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_actionbar_menu);
                isHomeAsUp = true;
            }
            if (mCurrentFragment instanceof NewsCommentsFragment){
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.show(mNewsViewPagerFragment)
                        .hide(mCurrentFragment);
                mCurrentFragment = mNewsViewPagerFragment;
                ft.commit();
                mToolbar.setTitle("新闻详情");
            }

        }

    }

    //初始化ListView中的数据
    public List<MyMenu> initMenuData(){


        myMenuList.add(new MyMenu("用户推荐日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("电影日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("不许无聊", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("设计日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("日常心理学", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("大公司日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("财经日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("互联网安全", R.drawable.menu_follow));

        myMenuList.add(new MyMenu("开始游戏", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("音乐日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("动漫日报", R.drawable.menu_follow));
        myMenuList.add(new MyMenu("体育日报", R.drawable.menu_follow));
        return myMenuList;

    }

    //初始化主题日报列表对应id
    private void initMenuIdMap(){
        HttpUtil.sendOkHttpRequest(Urls.THEME_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "初始化主题日报列表失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("others");//获取键为"others"的值
                        String ThemeContents = jsonArray.toString();
                        List<ThemeContent> themeContentList = new Gson().fromJson(ThemeContents,
                                new TypeToken<List<ThemeContent>>(){}.getType());
                        for (ThemeContent themeContent : themeContentList){
                            menuIdMap.put(themeContent.name, themeContent.id);
                        }
                        isMapInited = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(MainActivity.this, "成功初始化主题日报列表！",
//                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //用于fragment中获得父Activity的MenuIdMap
    public Map<String, Integer> getMenuIdMap(){
        return menuIdMap;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理HomeAsUp按钮点击事件
        switch (item.getItemId()){
            case android.R.id.home:
                if (isHomeAsUp){
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }else {
                    onBackPressed();
                }

                break;
            case R.id.setting:
                modeToggleTansitionAnimation();
                changeViewMode();
//                mDrawerLayout.closeDrawers();
                Toast.makeText(this, "设置功能待上线！敬请期待！", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
