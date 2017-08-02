package com.example.zhihudaily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhihudaily.adapter.CommentItemAdapter;
import com.example.zhihudaily.json.ShortCommentDetail;
import com.example.zhihudaily.json.ShortCommentsDetail;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hwl on 2017/8/2.
 */

public class ShortCommentFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CommentItemAdapter commentItemAdapter;

    public void setNewId(int newsId) {
        this.newsId = newsId;
    }

    private int newsId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.comments_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        commentItemAdapter = new CommentItemAdapter();
        mRecyclerView.setAdapter(commentItemAdapter);
        return view;
    }

    public void initRecyclerView(){

    }

    /*
  请求具体新闻内容
   */
    public void requestNewsCommentsFromServer(String address){
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
//                String string = response.body().string();
                final ShortCommentsDetail shortCommentsDetail =  new Gson().fromJson(response.body().string(),
                        ShortCommentsDetail.class);
                final boolean or = (response.isSuccessful());
//                final String string = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (shortCommentsDetail != null)
                            handleInfo(shortCommentsDetail);
                        else
                            Toast.makeText(getActivity(), "请求失败=_=" + or, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //将新闻内容解析成html，使用WebView加载
    public void handleInfo(ShortCommentsDetail shortCommentsDetail){
        List<ShortCommentDetail> details = shortCommentsDetail.comments;
        commentItemAdapter.setCommentsDetailList(details);
        commentItemAdapter.notifyDataSetChanged();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        newsId = ((NewsCommentsFragment) this.getParentFragment()).getNewsId();
        requestNewsCommentsFromServer(Urls.NEWS_COMMENTS + newsId + Urls.SHORT_COMMENTS);
    }
}
