package com.mindspree.days.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mindspree.days.R;
import com.mindspree.days.lib.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 19-10-2015.
 */
public class DatelineModel implements Parcelable {

    public String mMood = "";
    public String mCreated= "";
    public String mUpdateDate = "";
    private String mPhotoGroup = "";
    private int mLocationCount = 0;
    private int mPhotoCount = 0;
    private String mSentence = "";

    public DatelineModel(){
    }

    public DatelineModel(String updateDate, String photoGroup, int locationCount, int photoCount, String sentence){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mLocationCount = locationCount;
        mPhotoCount = photoCount;
        mSentence = sentence;
    }

    public DatelineModel(String updateDate, String photoGroup, int locationCount, int photoCount, String sentence, String mood, String created){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mLocationCount = locationCount;
        mPhotoCount = photoCount;
        mSentence = sentence;
        mMood = mood;
        mCreated = created;
    }

    public DatelineModel(Parcel in) {

        mUpdateDate			= in.readString();
        mPhotoGroup 		= in.readString();
        mLocationCount		= in.readInt();
        mPhotoCount			= in.readInt();
        mSentence 		= in.readString();
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public DatelineModel createFromParcel(Parcel src) {
            return new DatelineModel(src);
        }

        public DatelineModel[] newArray(int size) {
            return new DatelineModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUpdateDate);
        dest.writeString(mPhotoGroup);
        dest.writeInt(mLocationCount);
        dest.writeInt(mPhotoCount);
        dest.writeString(mSentence);
    }

    public ArrayList<String> getPhotoList() {
        if(mPhotoGroup != null) {
            return new ArrayList<String>(Arrays.asList(mPhotoGroup.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

    public String getPhotoString() {
        return mPhotoGroup;
    }

    public String getDateFormat() {
        return AppUtils.getDate(mUpdateDate);
    }

    public String getDayFormat() {
        return AppUtils.getDate(mUpdateDate, "EEE,d");
    }
    public String getMonthFormat() {
        return AppUtils.getDate(mUpdateDate, "MMM yyyy");
    }

    public String getDate() {
        return AppUtils.getDate(mUpdateDate, "yyyy-MM-dd");
    }

    public String getSummarize() {
        if(mSentence == null || mSentence.equals("")) {
            return String.format("%d군데의 장소에서 찍은 %d개의 사진이 있습니다.", mLocationCount, mPhotoCount);
        } else {
            return mSentence;
        }

    }


}
