package com.example.zhihudaily.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by monlonwong on 2017/8/10.
 */

public class BaseApplication extends Application {

    private static final String PARAM_IS_NIGHT_MODE = "PARAM_IS_NIGHT_MODE";

    private boolean mIsNightMode;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIsNightMode = mSharedPreferences.getBoolean(PARAM_IS_NIGHT_MODE, false);

    }

    public boolean isNightMode(){
        return mIsNightMode;
    }


    public void setIsNightMode(boolean isNightMode) {
        if (mIsNightMode == isNightMode)
            return;

        mIsNightMode = isNightMode;
    }


}
