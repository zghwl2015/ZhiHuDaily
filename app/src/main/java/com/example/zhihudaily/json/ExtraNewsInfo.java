package com.example.zhihudaily.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hwl on 2017/3/13.
 */

public class ExtraNewsInfo {
    @SerializedName("long_comments")
    public int longComments;

    //点赞总数
    public int popularity;

    @SerializedName("short_comments")
    public int shortComments;

    public int comments;
}
