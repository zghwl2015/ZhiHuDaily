package com.example.zhihudaily.json;

import java.util.List;

/**
 * Created by hwl on 2017/3/13.
 */

public class ShortCommentsDetail {

    public List<ShortCommentDetail> comments;

    public class ShortCommentDetail{

        public String author;

        public int id;

        public String comment;

        public int likes;

        public int time;

        public String avatar;

    }
}
