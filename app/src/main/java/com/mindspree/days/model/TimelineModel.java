package com.mindspree.days.model;

//import android.hardware.Camera;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.location.*;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.mindspree.days.engine.EngineDBInterface;

import static android.media.CamcorderProfile.get;

/**
 * Created by Admin on 19-10-2015.
 */
public class TimelineModel implements Parcelable {

    public int mLocationId = 0;
    public int mLock = 0;
    public String mName = "";
    public double mLatitude = 0;
    public double mLogitude = 0;
    public double mMeasureLatitude = 0;
    public double mMeasureLogitude = 0;

    public AppPreference mPreference;
    public Location mHomeLocation;

    private String mMeasureDate = "";
    private String mCreateDate = "";
    private String mPhotoGroup = "";
    private String mImageGroup = "";


    private ArrayList<TextView> mSelectedImages = new ArrayList<TextView>();
    private TimelineModel mTimelineModel;
    private DBWrapper mDBWrapper;



    public TimelineModel(){
    }

    public TimelineModel(int locationId, String name, double latitude, double longitude, String createDate, String photoGroup, String imageGroup){
        mLocationId = locationId;
        mName = name;
        mLatitude = latitude;
        mLogitude = longitude;
        mCreateDate = createDate;
        mPhotoGroup = photoGroup;
        mImageGroup = imageGroup;
    }

    protected TimelineModel(Parcel in) {
        mLocationId = in.readInt();
        mName = in.readString();
        mLatitude = in.readDouble();
        mLogitude = in.readDouble();
        mCreateDate = in.readString();
        mMeasureDate = in.readString();
        mPhotoGroup = in.readString();
        mImageGroup = in.readString();
    }

    public static final Creator<TimelineModel> CREATOR = new Creator<TimelineModel>() {
        @Override
        public TimelineModel createFromParcel(Parcel in) {
            return new TimelineModel(in);
        }

        @Override
        public TimelineModel[] newArray(int size) {
            return new TimelineModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(mLocationId);
        dest.writeString(mName);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLogitude);
        dest.writeString(mCreateDate);
        dest.writeString(mMeasureDate);
        dest.writeString(mPhotoGroup);
        dest.writeString(mImageGroup);
    }

    public ArrayList<String> getPhotoList() {
        if(mPhotoGroup != null) {
            return new ArrayList<String>(Arrays.asList(mPhotoGroup.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getImageList() {
        if(mPhotoGroup != null) {
            return new ArrayList<String>(Arrays.asList(mImageGroup.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

    public void setCreateDate(String formatDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date updateDate= dateFormat.parse(formatDate);
            SimpleDateFormat formatString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mCreateDate = formatString.format(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setMeasureDate(String formatDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date updateDate= dateFormat.parse(formatDate);
            SimpleDateFormat formatString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mMeasureDate = formatString.format(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public double getLongitude()
    {
        return mLogitude;
    }

    public double getMeasureLatitude()
    {
        return mMeasureLatitude;
    }

    public double getMeasureLogitude()
    {
        return mMeasureLogitude;
    }

    public String getCreateDate(){
        return mCreateDate;
    }

    public String getMeasureDate(){
        return mMeasureDate;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoString() {
        return mPhotoGroup;
    }

    public String getImageString() {
        return mImageGroup;
    }

    public String getSummarize() {



        // 1. POI estimation
        String POI = mName;


        // 2. Time
        String S_time = mCreateDate;


        EngineDBInterface engineDBInterface = new EngineDBInterface();
        float Num_Face=0;
        float Smile_Prob;

        int photoCount = getPhotoList().size();
        if(photoCount > 0) {

            // 3.  Extract the name of the representing photo
            //String PhotoString = getPhotoString();
            //images.size();

            // 4.  getExtraFeatWithPhotoURL(PhotoString);
            ArrayList PhotoList = getPhotoList();

            for (int i = 0; i < photoCount; i++){
                String timelinePhotoFile = PhotoList.get(0).toString();
                //String temp1 = "/storage/emulated/0/DCIM/Camera/20170219_095851.jpg";

                 Num_Face = engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
                 Smile_Prob = engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);
            }

            return String.format("%d개의 사진이 등록된 장소입니다. 뭘 했길래 사진을 이렇게 많이 찍었을까 ? ", getPhotoList().size());
        } else {


            // This is default message. Needs more data

            return String.format("볼일만 간단히 보고 휘리릭 ~~~ 오늘도 즐겁고 신나는 하루. 오늘은 어떤 일이 기다리고 있을까  ");
        }
    }

    public String getDateFormat() {
        return AppUtils.getTime(mCreateDate);
    }


}
