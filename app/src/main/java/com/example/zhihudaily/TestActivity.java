package com.example.zhihudaily;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.zhihudaily.json.NewsContent;
import com.example.zhihudaily.json.Story;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;
import com.google.gson.Gson;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    private WebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_fragment);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int id = bundle.getInt("themeId");
        requestNewsContentFromServer(Urls.NEWS_ADDRESS_HEAD + id);

    }

    /*
  请求具体新闻内容
   */
    public void requestNewsContentFromServer(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestActivity.this, "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final NewsContent newsContent =  new Gson().fromJson(response.body().string(),
                        NewsContent.class);
                final boolean or = (response.isSuccessful());
//                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (newsContent != null)
                            handleInfo(newsContent);
                        else
                            Toast.makeText(TestActivity.this, "请求失败=_=" + or, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //将新闻内容解析成html，使用WebView加载
    public void handleInfo(NewsContent newsContent){
        String css ="<link rel=\"stylesheet\" href=\"" + newsContent.css.get(0) + "\" " +
                "type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsContent.body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html",
                "UTF-8", null);

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



}
