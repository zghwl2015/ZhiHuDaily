package com.example.zhihudaily.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hwl on 2017/3/13.
 */

//新闻详情数据
public class Story implements Serializable{

    public String title;

    public String ga_prefix;

    public List<String> images;

    public int type;

    public int id;

    public Story(String mTitle, List<String> mImages, int mId){
        title = mTitle;
        images = mImages;
        id = mId;
    }

}
