package com.example.zhihudaily.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hwl on 2017/3/13.
 */

public class ThemeNewsContent {

    public List<ThemeNewsStory> stories;

    public String description;

    public String background;

    public int color;

    public String name;

    public String image;

    public List<ThemeNewsEditor> editors;

    @SerializedName("image_source")
    public String imageSource;
}
