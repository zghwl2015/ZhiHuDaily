package com.example.zhihudaily.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hwl on 2017/8/2.
 */

public class SplashActivity extends Activity {

    private ImageView mSplashImage;
    private AlphaAnimation mAlphaAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //背景图与状态栏融为一体
//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        initView();
        //初始化动画
        initAnimation();
        requestSplashImage();
        initListener();
    }

    void initView(){
        setContentView(R.layout.splash_activity);
        mSplashImage =(ImageView) findViewById(R.id.splash_image);

    }

    private void initAnimation(){
        mAlphaAnimation = new AlphaAnimation(0f, 1f);//从透明到完全不透明
        mAlphaAnimation.setDuration(1500);
        mAlphaAnimation.setFillAfter(true);

        mSplashImage.setAnimation(mAlphaAnimation);
    }

    void initListener(){
        mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束跳转到MainActivity
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    void requestSplashImage(){

        HttpUtil.sendOkHttpRequest(Urls.BINGIMAGE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this, "请求失败=_=", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String launchImage = response.body().string();
//                final LaunchImage launchImage =  new Gson().fromJson(response.body().string(),
//                        LaunchImage.class);
                final boolean or = (response.isSuccessful());
//                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (launchImage != ""){
                            Glide.with(SplashActivity.this).load(launchImage)
                                    .into(mSplashImage);
                        } else{
                            mSplashImage.setBackground(getResources().getDrawable(
                                    R.drawable.splash, getTheme()));//动态加载背景
                            Toast.makeText(SplashActivity.this,
                                    "更新启动界面图失败=_=" + or, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
