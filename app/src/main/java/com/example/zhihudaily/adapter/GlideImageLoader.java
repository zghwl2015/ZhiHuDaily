package com.example.zhihudaily.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by hwl on 2017/7/4.
 * 自定义图片加载器
 */

public class GlideImageLoader extends ImageLoader {
//    private Context mContext;
//    public GlideImageLoader(Context context){
//        context
//
//    }
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */


        //Glide 加载图片简单用法，加载网络图片
        Glide.with(context).load(path)
                .into(imageView);
//        RequestOptions myOptions = new RequestOptions()
//                .placeholder(R.drawable.holder)
//                .diskCacheStrategy(DiskCacheStrategy.NONE);
//        Glide.with(context).
//                load(path).
//                apply(myOptions)
//                .transition(new DrawableTransitionOptions().crossFade(2000)).
//                into(imageView);
//
//        //Picasso 加载图片简单用法
//        Picasso.with(context).load((Integer) path).into(imageView);
//
//        //用fresco加载图片简单用法，记得要写下面的createImageView方法
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);
    }
}
