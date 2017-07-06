package com.example.zhihudaily.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hwl on 2017/3/13.
 */

public class LatestNews {

    public String date;

    public List<Story> stories;

    @SerializedName("top_stories")
    public List<TopStory> topStories;
}
