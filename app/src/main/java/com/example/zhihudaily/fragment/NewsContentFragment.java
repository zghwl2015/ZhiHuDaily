package com.example.zhihudaily.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.activity.MainActivity;
import com.example.zhihudaily.json.ExtraNewsInfo;
import com.example.zhihudaily.json.NewsContent;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.IconUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hwl on 2017/7/10.
 */

public class NewsContentFragment extends Fragment {

    private WebView mWebView;
    private FloatingActionButton mFloatingActionButton;
    private SwitchFragmentListener mSwitchFragmentListener;


    public void setCurrentNewId(int currentNewId) {
        this.currentNewId = currentNewId;
    }

    private int currentNewId;

    private NestedScrollView nestedScrollView;
//    private RelativeLayout newsComment;
    private ImageView newsImage;
    private TextView imageSource;
    private TextView newsTitle;
    //底部标题栏相关控件
    private TextView praiseNum;
    private TextView commentNum;
    private ImageView collectImage;
    private ImageView praiseImage;
    private ImageView shareImage;
    private ImageView commentImage;
    private RelativeLayout newsPraise;
    private RelativeLayout newsComment;

    private MainActivity parentActivity;
    private NewsViewPagerFragment parentFragment;
    private Activity mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_content_fragment, container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);



        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        newsImage =(ImageView) view.findViewById(R.id.news_image);
        newsTitle = (TextView) view.findViewById(R.id.news_title);
        imageSource = (TextView) view.findViewById(R.id.image_source);

        praiseNum = (TextView) view.findViewById(R.id.praise_num);
        commentNum = (TextView) view.findViewById(R.id.comment_num);
        collectImage = (ImageView) view.findViewById(R.id.collect);

        praiseImage = (ImageView) view.findViewById(R.id.praise_image);
        shareImage = (ImageView) view.findViewById(R.id.share_image);
        commentImage = (ImageView) view.findViewById(R.id.comment_image);
        newsPraise = (RelativeLayout) view.findViewById(R.id.news_praise);

        newsComment = (RelativeLayout) view.findViewById(R.id.news_comment);
        newsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewsViewPagerFragment)getParentFragment()).
                        getmSwitchFragmentListener().switchFragment();
//                Intent intent = new Intent(getActivity(), TestActivity.class);
//                startActivity(intent);
            }
        });

        //给ImageView图标着色
        IconUtil.setImageViewColor(collectImage, R.color.gray);
        IconUtil.setImageViewColor(praiseImage, R.color.gray);
        IconUtil.setImageViewColor(shareImage, R.color.gray);
        IconUtil.setImageViewColor(commentImage, R.color.gray);
//        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSwitchFragmentListener.switchFragment();
//                Intent intent = new Intent(getActivity(), TestActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }
    public void setmSwitchFragmentListener(SwitchFragmentListener listener){
        mSwitchFragmentListener = listener;
    }

    //


    //获得上下文
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentFragment = (NewsViewPagerFragment) getParentFragment();
        //点击事件设置动画效果
        newsPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!parentActivity.isPraisedList.get(parentFragment.getCurrentPos())){
                    int currNum = Integer.parseInt(praiseNum.getText().toString());
                    praiseNum.setText(String.valueOf(currNum + 1));
                    praiseNum.setTextColor(getResources()
                            .getColor(R.color.peacockBlue));
                    IconUtil.setImageViewColor(praiseImage, R.color.peacockBlue);
                    parentActivity.isPraisedList.set(parentFragment.getCurrentPos(), true);
                }else {
                    int currNum = Integer.parseInt(praiseNum.getText().toString());
                    praiseNum.setText(String.valueOf(currNum - 1));
                    praiseNum.setTextColor(getResources()
                            .getColor(R.color.black));
                    IconUtil.setImageViewColor(praiseImage, R.color.gray);
                    parentActivity.isPraisedList.set(parentFragment.getCurrentPos(), false);
                }
            }
        });

        collectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!parentActivity.isCollectedList.get(parentFragment.getCurrentPos())){
                    IconUtil.setImageViewColor(collectImage, R.color.peacockBlue);
                    parentActivity.isCollectedList.set(parentFragment.getCurrentPos(), true);
                    //将当前新闻ID添加进MainActivity的collectedStoryList中
                    parentActivity.collectedStoryList.add(
                            parentFragment.getRecyclerViewFragment().getStoryList()
                            .get(parentFragment.getCurrentPos())
                    );
                    Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    IconUtil.setImageViewColor(collectImage, R.color.gray);
                    parentActivity.isCollectedList.set(parentFragment.getCurrentPos(), false);
                    parentActivity.collectedStoryList.remove(
                            parentFragment.getRecyclerViewFragment().getStoryList()
                                    .get(parentFragment.getCurrentPos())
                    );
                    Toast.makeText(mContext, "取消收藏！", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

//    public void setContent(String content){
//        if (mWebView != null){
//            mWebView.loadDataWithBaseURL("x-data://base", content, "text/html",
//                    "UTF-8", null);
//        }
//
//    }

    public void loadNewsContent(NewsContent newsContent){
        Glide.with(getActivity()).load(newsContent.image).into(newsImage);
        imageSource.setText(newsContent.imageSource);
        newsTitle.setText(newsContent.title);

        nestedScrollView.fullScroll(View.FOCUS_UP);
    }

    /*
   请求具体新闻内容
    */
    public void requestNewsContentFromServer(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mContext.runOnUiThread(new Runnable() {
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
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (newsContent != null){
                            handleInfo(newsContent);
                            loadNewsContent(newsContent);
                        } else {
                            Toast.makeText(getActivity(), "请求失败=_=" + or, Toast.LENGTH_SHORT).show();
                        }
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
//        mNewsContentFragment = new NewsContentFragment();//不能在Fragment中实例化，而应该放到Activity中实例化
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html",
                "UTF-8", null);
//        ((MainActivity) getActivity()).setmWebViewContent(html);
//        ((MainActivity) getActivity()).setmNewsContent(newsContent);
//        mSwitchFragmentListener.switchFragment(newId);

    }

    /*
 请求具体新闻内容
  */
    public void requestExtraNewsInfoFromServer(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ExtraNewsInfo extraNewsInfo =  new Gson().fromJson(response.body().string(),
                        ExtraNewsInfo.class);
                final boolean or = (response.isSuccessful());
//                final String string = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (extraNewsInfo != null){
                            handleInfo(extraNewsInfo);
                        } else {
                            Toast.makeText(getActivity(), "请求失败=_=" + or, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //将数据加载到底部标题栏中
    public void handleInfo(ExtraNewsInfo extraNewsInfo){
        praiseNum.setText(String.valueOf(extraNewsInfo.popularity));
        commentNum.setText(String.valueOf(extraNewsInfo.comments));
        ((NewsViewPagerFragment)getParentFragment()).setmExtraNewsInfo(extraNewsInfo);
    }



}
