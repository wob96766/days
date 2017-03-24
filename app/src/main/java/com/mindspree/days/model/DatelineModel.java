package com.mindspree.days.model;

import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;
import android.util.Log;

import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.ui.MainActivity;
import com.mindspree.days.ui.TimelineActivity;
import com.mindspree.days.ui.TodayFragment;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.SentenceModel;
import com.mindspree.days.model.WeatherModel;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    // DNN related classes
    boolean asynctask_flag = false;
    String[] jargv =new String[7];
    public DnnEngine mDnnengine ;
    public String DNN_result;
    public String hashString_DNN="";

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

    public String getWeatherEnglish(){
        if(mWeather.equals("Clear")){
            return "sunny";
        } else if(mWeather.equals("Clouds")){
            return "cloudy";
        } else if(mWeather.equals("Dust")){
            return "dusty";
        } else if(mWeather.equals("Haze")){
            return "hazy";
        } else if(mWeather.equals("Mist")){
            return "misty";
        } else if(mWeather.equals("Fog")){
            return "foggy";
        } else if(mWeather.equals("Rain")){
            return "rainy";
        }
        return mWeather;
    }

    //junyong - check the poi's where each photos was taken
    //junyong - check the size of the cluster for each photo

    public String getCreateTime(String name){
        DBWrapper dbWrapper = new DBWrapper(AppPreference.getInstance().getUserUid());
        return dbWrapper.getCreateTime(name);
    }

    public PhotoInfoModel getPhotoInfo(String file_index){
        DBWrapper dbWrapper = new DBWrapper(AppPreference.getInstance().getUserUid());
        return dbWrapper.getPhotoInfo(file_index);
    }

    public int getClusterSize(String cluster_id){
        DBWrapper dbWrapper = new DBWrapper(AppPreference.getInstance().getUserUid());
        return dbWrapper.getClusteredImageSize(cluster_id);
    }

    public ArrayList<String> getPoiList() {
//        if(mPhotoGroup != null) {
        if(mPoiGroup != null) {
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


        mDnnengine = new DnnEngine();


        String [] DNN_path = MainActivity.DNN_path;

        // Garbage collection before any heavy load work
        System.gc();

        File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        String DNN_test = "IMG_7543.JPG"; // Seashore
        String DNN_test_path = outDir + "/data/" + DNN_test;

        // This is for classification
        String[] jargv =new String[7];
        jargv[0] ="classifier_Class";
        jargv[1] ="predictCustom";  // This is for classification
        jargv[2] =DNN_path[0];
        jargv[3] =DNN_path[1];
        jargv[4] =DNN_path[2];
        jargv[5] =DNN_test_path;
        jargv[6] = outDir+"/";




        if(mSentence == null || mSentence.equals("")) {

            //mDnnengine.execute();  Async task is not used in this model yet.
//            DNN_result = DnnEngineClassJNI(jargv);






            String hash_string ="";

            //1. Today is *** date\
            hash_string = hash_string + String.format("Today is %s. ", getDate());

            //2. Place
            ArrayList poiList = getPoiList();
            if(poiList.size()>0)
            {

                // Get photo info retrieval
                ArrayList photoIDs = getDisplayPhotoIds();

                int photoID_size= photoIDs.size();
                PhotoInfoModel[] photoinfos=null;

                if(photoID_size>0){
                    photoinfos = new PhotoInfoModel[photoID_size];
                    for (int i=0;i<photoID_size;i++)
                        photoinfos[i] = getPhotoInfo(photoIDs.get(i).toString());
                }

                ;

                // This is how you get cluster size for the specific cluster ID
                //int temp = getClusterSize(String.valueOf(photoinfos[1].cluster_id));


                String temp = getCreateTime(poiList.get(0).toString());

                // Measure how busy user was
                if(poiList.size() < 4)
                    hash_string = hash_string + "Just one ordinary day. Nothing much.";
                else if(poiList.size() >= 4 && poiList.size() <= 6)
                    hash_string = hash_string + "It was a little bit busy day";
                else if (poiList.size() > 6)
                    hash_string = hash_string + "Oh shit ! It was a super busy day";



                // Describe where user went
                if(poiList.size() ==1) {
                    // POI size 1 means user stayed in one place
                    hash_string = hash_string + String.format("%s %s.", "I didn't go anywhere. Just stayed in", poiList.get(0).toString());

                }else if(poiList.size() ==2){
                    // POI size 2 means user went to one place
                    hash_string = hash_string + String.format("Today I just went to %s.", poiList.get(0).toString());

                }else{

                    hash_string = hash_string + "Today I went to ";

                    for (int i=1;i<poiList.size();i++){
                        if(i==poiList.size()-1)
                            hash_string = hash_string + String.format("and %s.", poiList.get(i).toString());
                        else
                            hash_string = hash_string + String.format("%s, ", poiList.get(i).toString());
                    }





                }



            }
            else{

//                mDnnengine.execute();

                hash_string =hash_string + "I stayed at home whole day. I think I didn't do anything special. What a boring day. I will go out somewhere tomorrow";
            }

//            if(DNN_result.equals(""))
//            {
//
//            }
//            else{
//                hash_string =hash_string+DNN_result;
//            }


            return hash_string;







        } else {

            return mSentence;
        }

    }





    // Async Task Class : This method is not currently used in this model.
    public class DnnEngine extends AsyncTask<String, String, String> {



        public String [] DNN_path = MainActivity.DNN_path;

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {



            super.onPreExecute();

            hashString_DNN ="";

            String [] DNN_path =MainActivity.DNN_path;

            // Garbage collection before any heavy load work
            System.gc();

            File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
            String DNN_test = "IMG_7543.JPG"; // Seashore
            String DNN_test_path = outDir + "/data/" + DNN_test;

            // This is for classification

            jargv[0] ="classifier_Class";
            jargv[1] ="predictCustom";  // This is for classification
            jargv[2] =DNN_path[0];
            jargv[3] =DNN_path[1];
            jargv[4] =DNN_path[2];
            jargv[5] =DNN_test_path;
            jargv[6] = outDir+"/";
            asynctask_flag = true;


        }


        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {

            try {




                DNN_result = DnnEngineClassJNI(jargv);



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);

            try {
                int percent = Integer.parseInt(progress[0]);
                Log.v("mp3dropboxsync", "Hi progressing - " + percent + "%");


            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        @Override
        protected void onPostExecute(String file_url) {

            hashString_DNN=DNN_result;


            asynctask_flag =false;

        }


    }

    public native static String DnnEngineClassJNI(String[] jargv);

    static {
        System.loadLibrary("DNN-jni");
    }



}
