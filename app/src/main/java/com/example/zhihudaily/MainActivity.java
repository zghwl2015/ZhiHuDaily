package com.example.zhihudaily;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.zhihudaily.adapter.MenuAdapter;
import com.example.zhihudaily.adapter.MyMenu;
import com.example.zhihudaily.json.Story;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ImageView testView;

    private List<String> bannerImages = new ArrayList<>();;//banner轮播图片
    private List<String> imageTitles = new ArrayList<>();;//轮播图片标题
    private List<Story> storyList = new ArrayList<>();//recyclerview新闻子项内容


    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉默认title
        mToolbar.setTitle("首页");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_actionbar_menu);
        }
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

            }
        });

//        testView = (ImageView) findViewById(R.id.test_image_view);
//        Glide.with(this).load("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg")
//                .into(testView);

        //下面是测试用的banner代码
//        Banner banner =(Banner) findViewById(R.id.banner_main);
//        initDataAndImages();
//        banner.setImages(bannerImages)
//                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
//                .setImageLoader(new GlideImageLoader(){
//
//                    @Override
//                    public void displayImage(Context context, Object path, ImageView imageView) {
//                        Glide.with(MainActivity.this).load(path)
//                                .into(imageView);
//                    }
//                })
//                .setBannerTitles(imageTitles)
//                .setBannerAnimation(CubeOutTransformer.class)
//                .isAutoPlay(true)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//        banner.start();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fl_layout, new RecyclerViewFragment());
//        transaction.commit();
//        Button button =(Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TestActivity.class);
//                startActivity(intent);
//            }
//        });



    }

    private void initDataAndImages(){
        bannerImages.add("http://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png");
        bannerImages.add("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg");
        bannerImages.add("https://pic4.zhimg.com//v2-cf58ba2a7d9c5e13dbd79d2546900c43.jpg");

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

    //初始化ListView中的数据
    public List<MyMenu> initMenuData(){
        ArrayList<MyMenu> list = new ArrayList<>();
        list.add(new MyMenu("日常心理学", R.drawable.menu_follow));
        list.add(new MyMenu("用户推荐日报", R.drawable.menu_follow));
        list.add(new MyMenu("电影日报", R.drawable.menu_follow));
        list.add(new MyMenu("不许无聊", R.drawable.menu_follow));
        list.add(new MyMenu("设计日报", R.drawable.menu_follow));
        list.add(new MyMenu("大公司日报", R.drawable.menu_follow));
        list.add(new MyMenu("财经日报", R.drawable.menu_follow));
        list.add(new MyMenu("互联网安全", R.drawable.menu_follow));

        list.add(new MyMenu("日常心理学", R.drawable.menu_follow));
        list.add(new MyMenu("用户推荐日报", R.drawable.menu_follow));
        list.add(new MyMenu("电影日报", R.drawable.menu_follow));
        list.add(new MyMenu("不许无聊", R.drawable.menu_follow));
        list.add(new MyMenu("设计日报", R.drawable.menu_follow));
        list.add(new MyMenu("大公司日报", R.drawable.menu_follow));
        list.add(new MyMenu("财经日报", R.drawable.menu_follow));
        list.add(new MyMenu("互联网安全", R.drawable.menu_follow));
        return list;

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
