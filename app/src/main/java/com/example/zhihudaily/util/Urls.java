package com.example.zhihudaily.util;

/**
 * Created by hwl on 2017/7/3.
 * 存储一些常量
 */

public class Urls {

    public static String LATEST_NEWS = "http://news-at.zhihu.com/api/4/news/latest";

    public static String LAUNCH_IMAGE = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    //请求具体新闻内容
    public static String NEWS_ADDRESS_HEAD = "http://news-at.zhihu.com/api/4/news/";

    //请求时添加请求新闻的日期，如20170710
    public static String BEFOREE_NEWS = "http://news.at.zhihu.com/api/4/news/before/";

    //点赞数、评论数等信息
    public static String EXTRA_INFO = "http://news-at.zhihu.com/api/4/story-extra/";

    public static String NEWS_COMMENTS = " http://news-at.zhihu.com/api/4/story/";

    public static String SHORT_COMMENTS = "/short-comments";
    public static String LONG_COMMENTS = "/long-comments";

    //请求主题列表
    public static String THEME_NEWS = "http://news-at.zhihu.com/api/4/theme/";

    //请求主题新闻列表
    public static String THEME_LIST = "http://news-at.zhihu.com/api/4/themes";

    //获取必应每日一图
    public static String BINGIMAGE = "http://guolin.tech/api/bing_pic";



}
