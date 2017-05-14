package com.mindspree.days.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.Daily;
import com.mindspree.days.model.Location;
import com.mindspree.days.model.Photo;
import com.mindspree.days.view.AnimatingProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataRestoreActivity extends BaseActivity {
    public ImageView mLoadingImage = null;
    public TextView mLoadingStatus = null;

    private DatabaseReference mDatabase;
    private DatabaseReference mDataReference;
    private DBWrapper mDBWrapper;
    private HashMap<String, Object> mDataMap;

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private TextView mTextTitle;
    private int mPeriod = 100;
    private int mProgress = 0;
    private boolean mIsStarted = false;
    private AnimatingProgressBar mProgressBar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DataRestoreActivity.class);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datarestore);
        initData();
        initView();



    }

    @Override
    public void onResume(){
        super.onResume();
        if(mIsStarted == false){
            mIsStarted = true;
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if(mIsStarted == false) {
                        mIsStarted = true;
                    }
                    mProgress += 30;
                    mProgressBar.setProgress(mProgress%1000);
                    mHandler.postDelayed(mRunnable, mPeriod);
                }
            };
            mHandler.postDelayed(mRunnable, mPeriod);
            requestFirebaseRestore();
        }
    }

    @Override
    public void onBackPressed(){

    }

    private void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDataReference = mDatabase.child("users").child(mPreference.getUserUid());
    }

    private void initView() {
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        AQuery aq = new AQuery(this);
        mProgressBar = (AnimatingProgressBar)aq.id(R.id.pregress_bar).getView();
        mTextTitle = aq.id(R.id.text_title).getTextView();
        aq.dismiss();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mIsStarted == false) {
                    mIsStarted = true;
                }
                mProgress += 30;
                mProgressBar.setProgress(mProgress%1000);
                mHandler.postDelayed(mRunnable, mPeriod);
            }
        };
        mHandler.postDelayed(mRunnable, 1);
    }

    private void requestFirebaseRestore() {
        mDataReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        DataSnapshot locationData = dataSnapshot.child("locations");
                        DataSnapshot dailyData = dataSnapshot.child("daily");
                        DataSnapshot photoData = dataSnapshot.child("photos");

                        restoreDatabase(locationData, dailyData, photoData);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showToast(getAppText(R.string.message_network_error));
                    }
                });

    }

    private void restoreDatabase(DataSnapshot locations, DataSnapshot daily, DataSnapshot photos) {
        try {
            mDBWrapper.restoreLocation(locations);
            mDBWrapper.restoreDaily(daily);
            mDBWrapper.restorePhoto(photos);
            sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
            sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_CALENDAR));
            finish();
        }catch (Exception e){
            e.printStackTrace();
            showToast(getAppText(R.string.message_restore_error));
            finish();
        }
    }
}
