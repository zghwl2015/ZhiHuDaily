package com.example.zhihudaily;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zhihudaily.adapter.MenuAdapter;
import com.example.zhihudaily.adapter.MyMenu;
import com.example.zhihudaily.json.Story;
import com.example.zhihudaily.json.ThemeContent;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
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

    public void setmWebViewContent(String mWebViewContent) {
        this.mWebViewContent = mWebViewContent;
    }

    WebViewFragment  mWebViewFragment = new WebViewFragment();
    private String mWebViewContent;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉默认title
        mToolbar.setTitle("首页");//设置标题
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_actionbar_menu);
        }

        initMenuIdMap();

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
                //切换到当前主题界面
//                RecyclerViewFragment fragment = new RecyclerViewFragment();
                int themeId = menuIdMap.get(myMenu.getThemeContent());
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fl_layout, fragment);//替换frameLayout
//                transaction.commit();
                recyclerViewFragment.requestThemeNewsFromServer(Urls.THEME_NEWS + themeId);

                mDrawerLayout.closeDrawers();//关闭滑动菜单

            }
        });

        mLinearLayout = (LinearLayout) findViewById(R.id.layout_home);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbar.getTitle() != "首页"){
                    mToolbar.setTitle("首页");
                    recyclerViewFragment.requestLatestNewsFromServer(Urls.LATEST_NEWS);
                }
                mDrawerLayout.closeDrawers();//关闭滑动菜单
            }
        });

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        recyclerViewFragment = new RecyclerViewFragment();
        transaction.replace(R.id.fl_layout, recyclerViewFragment);
        transaction.commit();
        recyclerViewFragment.requestLatestNewsFromServer(Urls.LATEST_NEWS);
        recyclerViewFragment.setmSwitchFragmentListener(new SwitchFragmentListener() {
            @Override
            public void switchFragment() {
//                if (mWebViewFragment.)
//                WebViewFragment  mWebViewFragment = new WebViewFragment();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (mWebViewFragment.isAdded()){
                    transaction.hide(recyclerViewFragment).show(mWebViewFragment).commitAllowingStateLoss();
                }else {
                    transaction.hide(recyclerViewFragment).add(R.id.fl_layout, mWebViewFragment).commitAllowingStateLoss();
                }

                fragmentManager.executePendingTransactions();
                mWebViewFragment.setContent(mWebViewContent);
                mWebViewFragment.setmSwitchFragmentListener(new SwitchFragmentListener() {
                    @Override
                    public void switchFragment() {
                        NewsCommentsFragment newsCommentsFragment = new NewsCommentsFragment();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if (newsCommentsFragment.isAdded()){
                            transaction.hide(mWebViewFragment).show(newsCommentsFragment).
                                    commitAllowingStateLoss();
                        }else {
                            transaction.hide(mWebViewFragment).add(R.id.fl_layout, newsCommentsFragment).
                                    commitAllowingStateLoss();
                        }

                        fragmentManager.executePendingTransactions();
                    }
                });
            }
        });



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
        myMenuList.add(new MyMenu("体育安全", R.drawable.menu_follow));
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
                                Toast.makeText(MainActivity.this, "成功初始化主题日报列表！",
                                        Toast.LENGTH_SHORT).show();
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return true;
    }
}
