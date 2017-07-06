package com.example.zhihudaily.adapter;

/**
 * Created by hwl on 2017/7/4.
 */

public class MyMenu {

    private String themeContent;

    private int imageId;

    public MyMenu(String themeContent, int imageId){
        this.themeContent = themeContent;
        this.imageId = imageId;
    }

    public String getThemeContent() {
        return themeContent;
    }

    public int getImageId() {
        return imageId;
    }


}
