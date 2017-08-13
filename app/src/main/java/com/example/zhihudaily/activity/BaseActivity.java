package com.example.zhihudaily.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.zhihudaily.R;
import com.example.zhihudaily.application.BaseApplication;

/**
 * Created by monlonwong on 2017/8/10.
 */

public class BaseActivity extends AppCompatActivity {

    private BaseApplication mBaseApp = null;
    private WindowManager mWindowManager = null;
    private View mNightView = null;
    private LayoutParams mNightViewParam;

    protected boolean ismIsAddedView() {
        return mIsAddedView;
    }

    private boolean mIsAddedView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mBaseApp = (BaseApplication) getApplication();
        if (mBaseApp.isNightMode())
            setTheme(R.style.AppTheme_night);
        else
            setTheme(R.style.AppTheme_day);
        super.onCreate(savedInstanceState);
        mIsAddedView = false;

        if (mBaseApp.isNightMode()){
            initNightView();
            mNightView.setBackgroundResource(R.color.night_mask);
        }


    }

    @Override
    protected void onDestroy() {
        if (mIsAddedView) {
            mBaseApp = null;
            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
        super.onDestroy();
    }

    public BaseApplication getApp() {
        return mBaseApp;
    }

    protected void ChangeToDay() {
        mBaseApp.setIsNightMode(false);
//        mNightView.setBackgroundResource(android.R.color.transparent);
    }

    protected void setTransparent(){
        mNightView.setBackgroundResource(android.R.color.transparent);
    }

    protected void ChangeToNight() {
        mBaseApp.setIsNightMode(true);
//        initNightView();
//        mNightView.setBackgroundResource(R.color.night_mask);
    }

    /**
     * 在onResume方法完成前等待一段时间
     */
    public void recreateOnResume() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }

    private void initNightView() {
        if (mIsAddedView == true)
            return;
        mNightViewParam = new LayoutParams(
                LayoutParams.TYPE_APPLICATION,
                LayoutParams.FLAG_NOT_TOUCHABLE | LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, mNightViewParam);
        mIsAddedView = true;
    }


}
