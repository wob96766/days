package com.mindspree.days.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.mindspree.days.R;
import com.mindspree.days.interfaces.OnItemClickListener;
import com.mindspree.days.model.DatelineModel;


import java.util.ArrayList;


public class DatelineAdapter extends RecyclerView.Adapter<DatelineAdapter.MyViewHolders> {

    private ArrayList<DatelineModel> mDataSource = new ArrayList<DatelineModel>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public DatelineAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dateline, null);

        MyViewHolders rcv = new MyViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MyViewHolders holder, int position) {
        ArrayList<String> files = mDataSource.get(position).getPhotoList();
        Glide.with(mContext).load("").centerCrop().into(holder.mThumnail1);
        Glide.with(mContext).load("").centerCrop().into(holder.mThumnail2);
        Glide.with(mContext).load("").centerCrop().into(holder.mThumnail3);
        if(mDataSource.get(position).getPhotoList().size() > 0) {

            for(int i=0 ; i<files.size() ; i++){
                if(i == 1) {
                    Glide.with(mContext).load(files.get(i)).centerCrop().into(holder.mThumnail2);
                } else if(i == 2){
                    Glide.with(mContext).load(files.get(i)).centerCrop().into(holder.mThumnail3);
                } else {
                    Glide.with(mContext).load(files.get(i)).centerCrop().into(holder.mThumnail1);
                }
            }

        } else {
            // for default image
            /*Picasso.with(mContext).load(R.drawable.background_white_round).into(holder.mThumnail);*/
        }
        holder.mTextContent.setText(mDataSource.get(position).getSummarize());
//        holder.mTextTitle.setText(mDataSource.get(position).getDateFormat());
        holder.mTextDay.setText(mDataSource.get(position).getDayFormat());
        holder.mTextMonth.setText(mDataSource.get(position).getMonthFormat());
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public DatelineModel getItem(int position) {
        return mDataSource.get(position);
    }

    public void setDataSource(ArrayList<DatelineModel> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    public void add(DatelineModel map) {
        mDataSource.add(map);
    }

    public void remove(int _position) {
        mDataSource.remove(_position);
    }

    public void clear() {
        this.mDataSource.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class MyViewHolders extends RecyclerView.ViewHolder {
        public ImageView mThumnail1;
        public ImageView mThumnail2;
        public ImageView mThumnail3;
        public TextView mTextTitle;
        public TextView mTextContent;
        public TextView mTextDay;
        public TextView mTextMonth;
        public ViewGroup mViewPhoto;

        public TextView mLikeCount;
        public TextView mCommentCount;
        public View mImageContainer;
        public MyViewHolders(View itemView) {
            super(itemView);

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

            AQuery aq = new AQuery(itemView);
            mViewPhoto = (ViewGroup)aq.id(R.id.view_photo).getView();
            mThumnail1 = aq.id(R.id.image_thumnail1).width(metrics.widthPixels * 2 / 9, false).height(metrics.widthPixels * 2 / 9, false).clicked(mOnClickListener).getImageView();
            mThumnail2 = aq.id(R.id.image_thumnail2).width(metrics.widthPixels * 2 / 9, false).height(metrics.widthPixels * 2 / 9, false).clicked(mOnClickListener).getImageView();
            mThumnail3 = aq.id(R.id.image_thumnail3).width(metrics.widthPixels * 2 / 9, false).height(metrics.widthPixels * 2 / 9, false).clicked(mOnClickListener).getImageView();
            /*fit screen size
            if(contentHeight > 0) {
                aq.id(R.id.image_container).height(contentHeight);
                aq.id(R.id.image_content).height(contentHeight);
            }
            mTextTitle = aq.id(R.id.text_title).clicked(mOnClickListener).getTextView();
            */

            mTextDay = aq.id(R.id.text_day).clicked(mOnClickListener).getTextView();
            mTextMonth = aq.id(R.id.text_month).clicked(mOnClickListener).getTextView();
            mTextContent = aq.id(R.id.text_content).getTextView();
            mLikeCount = aq.id(R.id.text_like).getTextView();
            mCommentCount = aq.id(R.id.text_comment).getTextView();

            aq.id(R.id.cell).clicked(mOnClickListener);
            aq.dismiss();
        }
        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, getAdapterPosition());
                }
            }
        };
    }



}
