package com.example.zhihudaily.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.zhihudaily.R;

/**
 * Created by monlonwong on 2017/8/11.
 */

public class IconView extends View {
    private Paint mPaint1;//用来画弧
    private Paint mPaint2;//用来画圆环正方形
    private RectF mRectF1;
    private RectF mRectF2;

    private float mBorderWidth1 = dipToPx(5f);
    private float mBorderWidth2 = dipToPx(1f);

    private int mDuration = 1500;
    private float mCurrRadian = 0f;


    public IconView(Context context) {
        this(context, null);
    }

    public IconView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public IconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }


    private void initPaint(){
        mPaint1 = new Paint();
        mPaint1.setColor(getResources().getColor(R.color.white, getContext().getTheme()));
        mPaint1.setAntiAlias(true);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeCap(Paint.Cap.ROUND);
        mPaint1.setStrokeWidth(mBorderWidth1);

        mPaint2 = new Paint();
        mPaint2.setColor(getResources().getColor(R.color.peacockBlue, getContext().getTheme()));
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.FILL);
//        mPaint2.setStrokeCap(Paint.Cap.ROUND);
        mPaint2.setStrokeWidth(mBorderWidth2);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT);

//        //RectF四个参数表示left,top,right,bottom
//        RectF rectF1 = new RectF(dipToPx(8f) + mBorderWidth1/2, dipToPx(8f) + mBorderWidth1/2,
//                getWidth() - dipToPx(8f) - mBorderWidth1/2,
//                getWidth() - dipToPx(8f) - mBorderWidth1/2);

        //RectF四个参数表示left,top,right,bottom
        mRectF1= new RectF(dipToPx(8) + mBorderWidth1/2, dipToPx(8) + mBorderWidth1/2,
                getWidth() - dipToPx(8) - mBorderWidth1/2,
                getWidth() - dipToPx(8) - mBorderWidth1/2);

        mRectF2 = new RectF(mBorderWidth2/2, mBorderWidth2/2,
                getWidth() - mBorderWidth2/2,
                getWidth() - mBorderWidth2/2);


//        RectF rectF2 = new RectF(mBorderWidth2/2, mBorderWidth2/2,
//                getWidth() - mBorderWidth2/2,
//                getWidth() - mBorderWidth2/2);
        //2、3参数表示圆环弧度
        canvas.drawRoundRect(mRectF2, dipToPx(8), dipToPx(8), mPaint2);
        canvas.drawArc(mRectF1, 90, mCurrRadian, false, mPaint1);
    }

    public void startDrawIconAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 270);
        valueAnimator.setDuration(mDuration);
        //添加插值器， 先加速后减速
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrRadian = (float) animation.getAnimatedValue();
                invalidate();//重复调用onDraw方法重绘，形成动画
            }
        });
        valueAnimator.start();
    }



    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
