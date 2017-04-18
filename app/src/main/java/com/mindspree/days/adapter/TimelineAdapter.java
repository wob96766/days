package com.mindspree.days.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.ui.TimelineActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolders> {

    private ArrayList<TimelineModel> mDataSource = new ArrayList<TimelineModel>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public TimelineAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timeline, null);

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
                if (i == 0)  {
                    Glide.with(mContext).load(files.get(i)).centerCrop().into(holder.mThumnail1);
                } else if(i == 1) {
                    Glide.with(mContext).load(files.get(i)).centerCrop().into(holder.mThumnail2);
                } else if(i == 2){
                    Glide.with(mContext).load(files.get(i)).centerCrop().into(holder.mThumnail3);
                }
            }

        }
        if(position == 0){
            holder.mTopLine.setVisibility(View.INVISIBLE);
            if(position >= mDataSource.size()-1 ){
                holder.mBottomLine.setVisibility(View.INVISIBLE);
            } else {
                holder.mBottomLine.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mTopLine.setVisibility(View.VISIBLE);
            if(position >= mDataSource.size()-1 ){
                holder.mBottomLine.setVisibility(View.INVISIBLE);
            } else {
                holder.mBottomLine.setVisibility(View.VISIBLE);
            }
        }



        holder.mTextTitle.setText(mDataSource.get(position).getDateFormat());
        holder.mTextSubTitle.setText(mDataSource.get(position).getName());
        holder.mTextContent.setText(mDataSource.get(position).getSummarize(mContext));



    }





    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public TimelineModel getItem(int position) {
        return mDataSource.get(position);
    }

    public void setDataSource(ArrayList<TimelineModel> dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

    public void add(TimelineModel map) {
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
        public TextView mTextSubTitle;
        public TextView mTextContent;

        public TextView mTextLocationcount;
        public TextView mTextPhotocount;

        public ImageView mImageLocation;
        public ViewGroup mViewPhoto;

        public View mTopLine;
        public View mBottomLine;

        public TextView mLikeCount;
        public TextView mCommentCount;
        public View mImageContainer;
        public MyViewHolders(View itemView) {
            super(itemView);

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

            AQuery aq = new AQuery(itemView);
            mViewPhoto = (ViewGroup)aq.id(R.id.view_photo).getView();
            mTopLine = aq.id(R.id.view_topline).getView();
            mBottomLine = aq.id(R.id.view_bottomline).getView();
            mThumnail1 = aq.id(R.id.image_thumnail1).width(metrics.widthPixels * 2 / 8, false).height(metrics.widthPixels * 2 / 8, false).clicked(mOnClickListener).getImageView();
            mThumnail2 = aq.id(R.id.image_thumnail2).width(metrics.widthPixels * 2 / 8, false).height(metrics.widthPixels * 2 / 8, false).clicked(mOnClickListener).getImageView();
            mThumnail3 = aq.id(R.id.image_thumnail3).width(metrics.widthPixels * 2 / 8, false).height(metrics.widthPixels * 2 / 8, false).clicked(mOnClickListener).getImageView();
            /*fit screen size
            if(contentHeight > 0) {
                aq.id(R.id.image_container).height(contentHeight);
                aq.id(R.id.image_content).height(contentHeight);
            }*/
            mImageLocation = aq.id(R.id.image_location).clicked(mOnClickListener).getImageView();
            mTextTitle = aq.id(R.id.text_title).getTextView();
            mTextSubTitle= aq.id(R.id.text_subtitle).getTextView();
            mTextContent = aq.id(R.id.text_content).getTextView();
            mLikeCount = aq.id(R.id.text_like).getTextView();
            mCommentCount = aq.id(R.id.text_comment).getTextView();

            mTextLocationcount = aq.id(R.id.textLocationcount).getTextView();
            mTextPhotocount = aq.id(R.id.textPhotocount).getTextView();

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
