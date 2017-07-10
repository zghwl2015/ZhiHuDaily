package com.example.zhihudaily.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hwl on 2017/3/13.
 */

public class NewsContent {

    public String body;

    @SerializedName("image_source")
    public String imageSource;

    public String title;

    public String image;

    @SerializedName("share_url")
    public String shareUrl;

    public List<Object> js;

    public List<Recommender> recommenders;

    public class Recommender{

        public String avatar;

    }

    public String ga_prefix;

    public Section section;

    public class Section{

        public String thumbnail;

        public int id;

        public String name;

    }

    public int type;

    public int id;

    public List<String> css;

}
