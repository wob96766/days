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
import java.util.Calendar;
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


    private String mMeasureTime ="";
    private ArrayList<TextView> mSelectedImages = new ArrayList<TextView>();
    private TimelineModel mTimelineModel;
    private DBWrapper mDBWrapper;

    public int weekend_days=0;
    public String strWeek;
    public int nWeek;

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

    public void setMeasureTime(String formatDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date updateDate= dateFormat.parse(formatDate);
            SimpleDateFormat formatString = new SimpleDateFormat("HH/mm");
            mMeasureTime = formatString.format(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void doDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        strWeek = null;

        nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (nWeek == 1) {
            strWeek = "일요일";
        } else if (nWeek == 2) {
            strWeek = "월요";
        } else if (nWeek == 3) {
            strWeek = "화요일";
        } else if (nWeek == 4) {
            strWeek = "수요일";
        } else if (nWeek == 5) {
            strWeek = "목요일";
        } else if (nWeek == 6) {
            strWeek = "금요일";
        } else if (nWeek == 7) {
            strWeek = "토요일";
        }

        if( nWeek>=2 && nWeek<=6)
            weekend_days =0;  // Weekdays
        else
            weekend_days =1;  // Weekend


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



        String hash_string = null;
        // Check date
        doDayOfWeek();

        // 1. POI estimation
        String POI = mName;
        hash_string =String.format("#%s ", mName);

        // 2. Time

        setMeasureTime(mCreateDate);

        hash_string =hash_string + String.format("#%s ", mMeasureTime);




        // 2.5
        if(weekend_days==0){
            // Weekdays
            // POI 가 집 -->  9시 이전이면 #출근 전,  9시 이후이면 # 여유있는 아침


        }
        else{
            // Weekend
            // POI 가 집 -->  9시 이전이면 #이른 주말 아침, 9시 이후 12 시 이전 이면 # 한가로운 주말 오전

        }

        /*
            if( Weekdays and time < 10:00 am)
                add "Before going to the work"
            elese
                add " "   // Nothing
        */



        // 4. Face detectoion/ Smile score
        EngineDBInterface engineDBInterface = new EngineDBInterface();
        float Num_Face=0;
        float Smile_Prob=0;

        int photoCount = getPhotoList().size();
        if(photoCount > 0) {

            // Extract the name of the representing photo
            //String PhotoString = getPhotoString();

            ArrayList PhotoList = getPhotoList();  // getExtraFeatWithPhotoURL(PhotoString);

            for (int i = 0; i < photoCount; i++){
                String timelinePhotoFile = PhotoList.get(0).toString();
                //String temp1 = "/storage/emulated/0/DCIM/Camera/20170219_095851.jpg";

                 Num_Face = Num_Face + engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
                 Smile_Prob = Smile_Prob + engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);

//                if( Num_Face == 1) {
//                    if("selfie resolution")
//                        hash_string = hash_string + String.format("#%s ", "셀피");
//                    else
//                        hash_string = hash_string + String.format("#%s ", "사랑하는 사람들");
//
//
//                    if(Smile_Prob > 0.6)
//                        hash_string =hash_string + String.format("#%s ", "행복한 미소");
//                }
                if( Num_Face >= 1) {
                    hash_string = hash_string + String.format("#%s ", "사랑하는 사람들");
                }

            }
            Smile_Prob = Smile_Prob / photoCount;
            if( Smile_Prob >= 0.6) {
                hash_string = hash_string + String.format("#%s ", "아름다운 미소");
            }

        } else {
            /*
            if( Weekdays and time < 10:00 am)
                add "Busy morning"
            else if( Weekdays and time > 12:00 am)
                add " "   // Nothing

            else if( Weekend and time < 10:00 am)
                add "게으른 아침"

            else if( Weekend and time > 12:00 am)
                add "방콕"
            */
            // This is default message. Needs more data
            hash_string = hash_string + String.format("#Busy");

        }

        return String.format(" %s ", hash_string);

    }

    public String getDateFormat() {
        return AppUtils.getTime(mCreateDate);
    }


}
