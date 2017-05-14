package com.mindspree.days.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.engine.ClusterEngine;
import com.mindspree.days.engine.EngineDBInterface;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.Cluster_input;
import com.mindspree.days.model.Daily;
import com.mindspree.days.model.Location;
import com.mindspree.days.model.Photo;
import com.mindspree.days.view.AnimatingProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class DataBackupActivity extends BaseActivity {

    private DBWrapper mDBWrapper;
    private DatabaseReference mDatabase;
    private DatabaseReference mDataReference;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private StorageReference mPhotoReference;
    private AnimatingProgressBar mProgressBar;

    private HashMap<String, Object> mDataMap;
    private int mUploadedCount = 0;
    private String mStart = "";
    private String mEnd = "";

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private TextView mTextTitle;
    private int mPeriod = 100;
    private int mProgress = 0;
    private boolean mIsStarted = false;

    public static void startActivity(Context context, String start, String end) {
        Intent intent = new Intent(context, DataBackupActivity.class);
        intent.putExtra(AppConfig.IntentParam.START, start);
        intent.putExtra(AppConfig.IntentParam.END, end);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databackup);
        initData();
        initView();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mIsStarted == false){
            mIsStarted = true;

            mTextTitle.setText(getAppText(R.string.message_image_uploading));

            ExecuteBackup asyn = new ExecuteBackup();
            asyn.execute();
        }
    }

    @Override
    public void onBackPressed(){

    }

    private void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDataReference = mDatabase.child("users").child(mPreference.getUserUid());

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReferenceFromUrl(getAppText(R.string.firebase_bucket_name));
        mStart = getIntent().getStringExtra(AppConfig.IntentParam.START);
        mEnd = getIntent().getStringExtra(AppConfig.IntentParam.END);
    }

    private void initView() {
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        AQuery aq = new AQuery(this);
        mProgressBar = (AnimatingProgressBar)aq.id(R.id.pregress_bar).getView();
        mTextTitle = aq.id(R.id.text_title).getTextView();
        aq.dismiss();


    }

    // Async Task Class
    public class ExecuteBackup extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {

            try {
                mUploadedCount = 0;
                final ArrayList<Photo> photolist = mDBWrapper.getPhotoDatalist(mStart, mEnd);
                if(photolist.size() > 0) {
                    for (Photo item : photolist) {
                        File files = new File(item.file_location);
                        if (files.exists() == true) {
                            byte[] dataCompress = AppUtils.compressImageFile(item.file_location);
                            mPhotoReference = mStorageReference.child(String.format("photos/%s/%s", mPreference.getUserUid(), item.getDatetimeIndex()));

                            StorageMetadata metadata = new StorageMetadata.Builder()
                                    .setCustomMetadata("file_index", String.format("%d", item.file_index))
                                    .build();

                            UploadTask uploadTask = mPhotoReference.putBytes(dataCompress, metadata);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception exception) {
                                    showToast(getAppText(R.string.message_network_error));
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    StorageMetadata meta = taskSnapshot.getMetadata();
                                    String file_index = meta.getCustomMetadata("file_index");
                                    mDBWrapper.setPhoto(file_index, url);
                                    mUploadedCount++;
                                    if (mUploadedCount >= photolist.size() - 1) {
                                        requestFirebaseBackup();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    requestFirebaseBackup();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast(getAppText(R.string.message_backup_error));
                finish();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
        }


        @Override
        protected void onPostExecute(String file_url) {


        }
    }

    private void requestFirebaseBackup() {
        runOnUiThread(new Runnable() {
            public void run() {
                mTextTitle.setText(getAppText(R.string.message_data_uploading));
                mProgressBar.setProgress(mProgress%500);
            }
        });
        mDataMap = new HashMap<>();
        mDBWrapper.setLastTimeline();
        ArrayList<Photo> photolist = mDBWrapper.getPhotoDatalist(mStart, mEnd);
        for(Photo item : photolist){
            mDataMap.put("/photos/" + item.getDatetimeIndex(), item.toMap());
        }
        ArrayList<Location> locationlist = mDBWrapper.getLocationList(mStart, mEnd);
        for(Location item : locationlist){
            mDataMap.put("/locations/" + item.getDatetimeIndex(), item.toMap());
        }
        ArrayList<Daily> dailylist = mDBWrapper.getDailyDatalist(mStart, mEnd);
        for(Daily item : dailylist){
            mDataMap.put("/daily/" + item.getDatetimeIndex(), item.toMap());
        }
        mDataReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mDataReference.updateChildren(mDataMap);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                finish();
            }
        });
        mDataReference.addValueEventListener(mValueEventListener);

    }

    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            showToast(getAppText(R.string.message_network_error));
        }
    };

}
