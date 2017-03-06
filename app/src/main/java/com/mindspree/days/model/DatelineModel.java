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

    private String mWeather = "";
    public String mMood = "";
    public String mCreated= "";
    public String mUpdateDate = "";
    private String mPhotoGroup = "";
    private String mPhotoIds = "";
    private String mPoiGroup = "";
    private int mLocationCount = 0;
    private int mPhotoCount = 0;
    private String mSentence = "";

    public DatelineModel(){
    }

    public DatelineModel(String updateDate, String photoGroup, String ids, String weather, int locationCount, int photoCount, String sentence, String poiGroup){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mPhotoIds = ids;
        mWeather = weather;
        mPoiGroup = poiGroup;
        mLocationCount = locationCount;
        mPhotoCount = photoCount;
        mSentence = sentence;
    }

    public DatelineModel(String updateDate, String photoGroup, String ids, int locationCount, int photoCount, String sentence, String mood, String created){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mPhotoIds = ids;
        mLocationCount = locationCount;
        mPhotoCount = photoCount;
        mSentence = sentence;
        mMood = mood;
        mCreated = created;
    }

    public DatelineModel(Parcel in) {

        mUpdateDate			= in.readString();
        mPhotoGroup 		= in.readString();
        mPhotoIds 	    	=   in.readString();
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
        dest.writeString(mPhotoIds);
        dest.writeInt(mLocationCount);
        dest.writeInt(mPhotoCount);
        dest.writeString(mSentence);
    }
    //junyong  - get the 3 representing photo URL
    public ArrayList<String> getPhotoList() {
        if(mPhotoGroup != null) {
            return new ArrayList<String>(Arrays.asList(mPhotoGroup.split(",")));
        } else {
            return new ArrayList<>();
        }
    }
    //junyong - get image's "photo_index" to be displayed on the screen
    public ArrayList<String> getDisplayPhotoIds() {
        if(mPhotoGroup != null) {
            ArrayList<String> photoids = new ArrayList<String>(Arrays.asList(mPhotoIds.split(",")));
            if(photoids.size() > 3){
                return new ArrayList<String>(photoids.subList(0, 2));
            } else {
                return new ArrayList<String>(Arrays.asList(mPhotoIds.split(",")));
            }
        } else {
            return new ArrayList<>();
        }
    }
    //junyong - get weather
    public String getWeather(){
        if(mWeather.equals("Clear")){
            return AppUtils.getAppText(R.string.text_weather_clear);
        } else if(mWeather.equals("Clouds")){
            return AppUtils.getAppText(R.string.text_weather_clouds);
        } else if(mWeather.equals("Dust")){
            return AppUtils.getAppText(R.string.text_weather_dust);
        } else if(mWeather.equals("Haze")){
            return AppUtils.getAppText(R.string.text_weather_haze);
        } else if(mWeather.equals("Mist")){
            return AppUtils.getAppText(R.string.text_weather_mist);
        } else if(mWeather.equals("Fog")){
            return AppUtils.getAppText(R.string.text_weather_fog);
        } else if(mWeather.equals("Rain")){
            return AppUtils.getAppText(R.string.text_weather_rain);
        }
        return mWeather;
    }
    //junyong - check the poi's where each photos was taken
    //junyong - check the size of the cluster for each photo
    public PhotoInfoModel getPhotoInfo(String file_index){
        DBWrapper dbWrapper = new DBWrapper(AppPreference.getInstance().getUserUid());
        return dbWrapper.getPhotoInfo(file_index);
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
