package com.example.zhihudaily.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zhihudaily.R;
import com.example.zhihudaily.util.HttpUtil;
import com.example.zhihudaily.util.Urls;
import com.example.zhihudaily.util.Utility;
import com.example.zhihudaily.view.IconView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hwl on 2017/8/2.
 */

public class SplashActivity extends BaseActivity {

    private ImageView mSplashImage;
    private AlphaAnimation mAlphaAnimation;
    private ScaleAnimation mScaleAnimation;
    private RelativeLayout mLaunchBottomLayout;
    private IconView mIconView;
    private float mViewHeight;

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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Utility.transparentStatusBar(this);
        initView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //初始化动画
                initAnimation();
                requestSplashImage();
                initListener();
            }
        }, 2500);



    }

    void initView(){
        setContentView(R.layout.splash_activity);
        mSplashImage =(ImageView) findViewById(R.id.splash_image);
        mLaunchBottomLayout = (RelativeLayout) findViewById(R.id.launch_bottom_layout);
        mIconView = (IconView) findViewById(R.id.icon_view);
        mIconView.post(new Runnable() {
            @Override
            public void run() {
                mViewHeight = mIconView.getHeight();
//                //初始化动画
//                initAnimation();
//                requestSplashImage();
//                initListener();
                initTranslationAnimation();
            }
        });

    }

    private void initAnimation(){
        mAlphaAnimation = new AlphaAnimation(0f, 1f);//从透明到完全不透明

        mAlphaAnimation.setDuration(1000);
        mAlphaAnimation.setFillAfter(true);

        mScaleAnimation = new ScaleAnimation(1f, 1.1f, 1f, 1.1f,
                mSplashImage.getWidth() / 2f,
                mSplashImage.getHeight() / 2f);
        mScaleAnimation.setDuration(1000);
        mScaleAnimation.setFillAfter(true);

        mSplashImage.startAnimation(mAlphaAnimation);
        mSplashImage.startAnimation(mScaleAnimation);


    }

    private void initTranslationAnimation(){
        ObjectAnimator translationAnimation = ObjectAnimator.ofFloat(mLaunchBottomLayout,
                "translationY", mViewHeight, 0);
        translationAnimation.setDuration(1000);
        translationAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIconView.startDrawIconAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        translationAnimation.start();
    }

    void initListener(){
        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束跳转到MainActivity
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
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
                        if (launchImage != "" && launchImage != null){
//                            Glide.with(SplashActivity.this).load(launchImage)
//                                    .into(mSplashImage);
                            mSplashImage.setBackground(getResources().getDrawable(
                                    R.drawable.splash, getTheme()));
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
