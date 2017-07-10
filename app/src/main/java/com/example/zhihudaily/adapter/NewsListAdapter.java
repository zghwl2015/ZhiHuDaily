package com.example.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.json.Story;

import java.util.List;


/**
 * Created by hwl on 2017/7/3.
 * RecycleView适配器，内含顶部banner、新闻列表
 */

public class NewsListAdapter extends RecyclerView.Adapter {
    private static int TYPE_BANNER = 0;
    private static int TYPE_TITLE = 1;
    private static int TYPE_ITEM = 2;

    private List<Story> mStoryList;
    private View mHeaderView;
    private View mTitleView;
    private Context mContext;
    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    //新建recyclerview子项点击事件接口,用来调用父Fragment方法
    public interface RecyclerViewOnClickListener{
        void onClick(int id);
        int getThemeId(String title);
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener listener){
        mRecyclerViewOnClickListener = listener;
    }

    public void setmHeaderView(View headerView){
        if (headerView != null){
            mHeaderView = headerView;
        }
    }
    public void setmTitleView(View titleView){
        if (titleView != null){
            mTitleView = titleView;
        }
    }

    //
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
//        Button button;

        public ViewHolder(View view){
            super(view);

            if (view != null && view == mHeaderView){
                return;
            }
            textView = (TextView) view.findViewById(R.id.news_title);
            imageView = (ImageView) view.findViewById(R.id.new_image);
//            button = (Button) view.findViewById(R.id.button1);

        }


    }

     class TitleViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public TitleViewHolder(View view){

            super(view);

            textView =(TextView) view.findViewById(R.id.everyday_title);
        }


    }

    //利用构造器加载新闻数据
    public NewsListAdapter(List<Story> storyList, Context context){
        mStoryList = storyList;
        mContext = context;
    }

//    //初始化新闻数据
//    private void initData(){
//
//    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER){
            return new ViewHolder(mHeaderView);
        }
        if (viewType == TYPE_TITLE){
            return new TitleViewHolder(mTitleView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    //绑定时要判定该子项布局是哪一种，如果是mHeaderView就不用绑定了
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_BANNER)
            return;
        if (getItemViewType(position) == TYPE_TITLE){
            if (holder != null && holder instanceof TitleViewHolder){
                ((TitleViewHolder)holder).textView.setText("今日要闻");
            }
            return;
        }
//        else{
//            int count = getItemCount();
//            boolean f = (holder != null);

//        }
        final int pos = getRealPosition(holder);
        final Story story = mStoryList.get(pos);
//        final Story story = mStoryList.get(position - 2);

        if (holder != null && holder instanceof ViewHolder){
            //注册子项点击事件
            final int themeId = story.id;
            ((ViewHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerViewOnClickListener.onClick(themeId);
                }
            });

            ((ViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerViewOnClickListener.onClick(themeId);
                }
            });

            try {
                ((ViewHolder) holder).textView.setText(story.title);
                //使用Glide加载图片到imageView中
//                    ((ViewHolder) holder).button.setText("OK");
                Glide.with(mContext).load(story.images.get(0))
                        .into(((ViewHolder) holder).imageView);
                System.out.println("imageurl:" + story.images.get(0));
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(mContext, "无法下载图片！", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    //这是最为关键的一个方法，通过这种方法告知recyclerView为当前item加载哪个布局
    public int getItemViewType(int position) {
        if (mHeaderView == null )
            return TYPE_ITEM;
        if (position == 0)
            return TYPE_BANNER;
        if (position == 1)
            return  TYPE_TITLE;

        return TYPE_ITEM;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position: position - 2;
    }

    @Override
    //返回item的个数，一般情况下，mTitleView存在即意味着mStoryList不为空
    public int getItemCount() {
        return mHeaderView == null ? mStoryList.size() + 1 : mStoryList.size() + 2;

    }
}
