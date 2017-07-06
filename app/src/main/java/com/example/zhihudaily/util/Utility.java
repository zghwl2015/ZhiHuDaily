package com.example.zhihudaily.util;

import com.example.zhihudaily.json.LatestNews;
import com.example.zhihudaily.json.LaunchImage;
import com.google.gson.Gson;

/**
 * Created by hwl on 2017/7/4.
 */

public class Utility {

    /*
    将返回的json数据解析为LaunchImage实体类
     */
    public LaunchImage handleLaunchImageResponse(String response){
        try {
            return new Gson().fromJson(response, LaunchImage.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LatestNews handleLatestNewsResponse(String response){
        try {
            return new Gson().fromJson(response, LatestNews.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
