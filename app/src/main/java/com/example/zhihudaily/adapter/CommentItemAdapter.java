package com.example.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihudaily.R;
import com.example.zhihudaily.json.ShortCommentDetail;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hwl on 2017/7/12.
 */

public class CommentItemAdapter extends RecyclerView.Adapter{
    private static int TYPE_ITEM = 1;

    public void setCommentsDetailList(List<ShortCommentDetail> commentsDetailList) {
        this.commentsDetailList = commentsDetailList;
    }

    private List<ShortCommentDetail> commentsDetailList = new ArrayList<>();
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView author;
        ImageView vote;
        TextView likes;
        TextView content;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);

            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            author = (TextView) itemView.findViewById(R.id.comment_author);
            vote = (ImageView) itemView.findViewById(R.id.vote);
            likes = (TextView) itemView.findViewById(R.id.likes);
            content = (TextView) itemView.findViewById(R.id.comment_content);
            time = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }

    public CommentItemAdapter(Context context){
//        commentsDetailList = commentsDetails;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                    parent, false);
            return new ViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShortCommentDetail commentsDetail = commentsDetailList.get(position);
        //bind
        if (holder != null && holder instanceof ViewHolder){
            try {
                Glide.with(mContext).load(commentsDetail.avatar)
                        .into(((ViewHolder) holder).avatar);
//                ((ViewHolder) holder).avatar.setImageURI(Uri.parse(commentsDetail.avatar));
                ((ViewHolder) holder).content.setText(commentsDetail.content);
                ((ViewHolder) holder).author.setText(commentsDetail.author);
                ((ViewHolder) holder).likes.setText(commentsDetail.likes + "");
                //转化时间
//                Date date = new Date(Long.valueOf(commentsDetail.time));
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                ((ViewHolder) holder).time.setText(formatter.format(date));
            }catch (Exception e){
                e.printStackTrace();
//                Toast.makeText(mContext, "无法下载图片！", Toast.LENGTH_SHORT);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return commentsDetailList.size();
    }
}
