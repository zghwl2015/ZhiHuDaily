package com.example.zhihudaily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by hwl on 2017/7/10.
 */

public class WebViewFragment extends Fragment {

    private WebView mWebView;
    private FloatingActionButton mFloatingActionButton;
    private SwitchFragmentListener mSwitchFragmentListener;

    public void setCurrentNewId(int currentNewId) {
        this.currentNewId = currentNewId;
    }

    private int currentNewId;
//    private String mWebContent;
//
//    public WebViewFragment(){
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_fragment, container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchFragmentListener.switchFragment();
//                Intent intent = new Intent(getActivity(), TestActivity.class);
//                startActivity(intent);
            }
        });
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
}
