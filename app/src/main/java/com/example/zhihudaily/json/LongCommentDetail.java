package com.example.zhihudaily.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hwl on 2017/3/13.
 */

public class LongCommentDetail {

    public String author;

    public String comment;

    public String avatar;

    public int time;

    @SerializedName("reply_to")
    public ReplyInfo replyInfo;

    public class ReplyInfo{

        public String comment;

        public int status;

        public int id;

        public String author;
    }

    public int id;

    public int likes;
}
