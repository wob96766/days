package com.mindspree.days.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;

import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.ui.TimelineActivity;
import com.mindspree.days.ui.TodayFragment;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.SentenceModel;
import com.mindspree.days.model.WeatherModel;
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
    private String mPoiGroup = "";
    private int mLocationCount = 0;
    private int mPhotoCount = 0;
    private String mSentence = "";




    public DatelineModel(){
    }

    public DatelineModel(String updateDate, String photoGroup, int locationCount, int photoCount, String sentence, String poiGroup){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mPoiGroup = poiGroup;
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

    public ArrayList<String> getPoiList() {
        if(mPhotoGroup != null) {
            return new ArrayList<String>(Arrays.asList(mPoiGroup.split(",")));
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


            String hash_string ="";


            //1. Today is *** date\
            hash_string = hash_string + String.format("Today is %s. ", getDate());



            ArrayList poiList = getPoiList();
            if(poiList.size()>0)
            {
                hash_string ="Today I went to ";
                for (int i=0;i<poiList.size();i++){
                    if(i==poiList.size()-1)
                        hash_string = hash_string + String.format("and %s.", poiList.get(i).toString());
                    else
                        hash_string = hash_string + String.format("%s, ", poiList.get(i).toString());
                }

                if(poiList.size() < 4)
                    hash_string = hash_string + "Just one ordinary day. Nothing much.";
                else if(poiList.size() >= 4 && poiList.size() <= 6)
                    hash_string = hash_string + "It was a busy day";
                else if (poiList.size() > 6)
                    hash_string = hash_string + "Oh shit ! It was a super busy day";


            }
            else{
                hash_string ="I stayed at home whole day. What a boring day. I will go out somewhere tomorrow";
            }



            return hash_string;


            // return String.format("%d군데의 장소에서 찍은 %d개의 사진이 있습니다.", mLocationCount, mPhotoCount);

        } else {

            return mSentence;
        }

    }


}
