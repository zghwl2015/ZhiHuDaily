package com.example.zhihudaily.fragment;

/**
 * Created by hwl on 2017/7/10.
 */

public interface SwitchFragmentListener {

    void switchFragment();

    void switchFragment(int newsId);
    void switchFragment(int newsId, int startPos);
}
