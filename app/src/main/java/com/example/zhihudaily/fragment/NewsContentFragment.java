package com.example.zhihudaily.fragment;

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

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.json.NewsContent;

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
    private RelativeLayout newsComment;
    private ImageView newsImage;
    private TextView imageSource;
    private TextView newsTitle;
//    private String mWebContent;
//
//    public NewsContentFragment(){
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_content_fragment, container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);

        newsComment = (RelativeLayout) view.findViewById(R.id.news_comment);
        newsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchFragmentListener.switchFragment();
//                Intent intent = new Intent(getActivity(), TestActivity.class);
//                startActivity(intent);
            }
        });

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        newsImage =(ImageView) view.findViewById(R.id.news_image);
        newsTitle = (TextView) view.findViewById(R.id.news_title);
        imageSource = (TextView) view.findViewById(R.id.image_source);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void setContent(String content){
        if (mWebView != null){
            mWebView.loadDataWithBaseURL("x-data://base", content, "text/html",
                    "UTF-8", null);
        }

    }

    public void loadNewsContent(NewsContent newsContent){
        Glide.with(getActivity()).load(newsContent.image).into(newsImage);
        imageSource.setText(newsContent.imageSource);
        newsTitle.setText(newsContent.title);

        nestedScrollView.fullScroll(View.FOCUS_UP);
    }
}
