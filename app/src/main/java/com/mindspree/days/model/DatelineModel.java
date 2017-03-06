package com.mindspree.days.model;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;
import android.util.Log;

import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.adapter.DatelineAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.engine.EngineDBInterface;
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

    String[] jargv =new String[7];

    boolean asynctask_flag = false;
    boolean asynctask_again_flag = false;

    // DNN related classes
    public DnnEngine mDnnengine ;

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


        mDnnengine = new DnnEngine();


        String [] DNN_path =MainActivity.DNN_path;

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


        //bmp.recycle();  --> If there is any bmp object created

        if(mSentence == null || mSentence.equals("")) {

            //mDnnengine.execute();

            String hash_string ="";

            //1. Today is *** date\
            hash_string = hash_string + String.format("Today is %s. ", getDate());

            //2. Place
            ArrayList poiList = getPoiList();

            if(poiList.size()>0)
            {




                if(poiList.size() < 4)
                    hash_string = hash_string + "Just one ordinary day. Nothing much.";
                else if(poiList.size() >= 4 && poiList.size() <= 6)
                    hash_string = hash_string + "It was a little bit busy day";
                else if (poiList.size() > 6)
                    hash_string = hash_string + "Oh shit ! It was a super busy day";



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

             //3. Photos
                // 3.1 : get the 3 representing photo URL
                // 3.2 : check the poi's where each photos was taken
                // 3.3 : check the size of the cluster for each photo





            }else{

//                String test ;
//                test = DnnEngineClassJNI(jargv);
                mDnnengine.execute();


                hash_string =hash_string + "I stayed at home whole day. I think I didn't do anything special. What a boring day. I will go out somewhere tomorrow";
            }



            return hash_string;


            // return String.format("%d군데의 장소에서 찍은 %d개의 사진이 있습니다.", mLocationCount, mPhotoCount);

        } else {

            return mSentence;
        }

    }


    public static long getUsedMemorySize() {


        long availHeapSizeInMB=0L;

        try {
            final Runtime runtime = Runtime.getRuntime();
            final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
            final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
            availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return availHeapSizeInMB;

    }



    // Async Task Class
    public class DnnEngine extends AsyncTask<String, String, String> {



        public String [] DNN_path =MainActivity.DNN_path;

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //showLoadingDialog();
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

            String test ="";
            try {




                test = DnnEngineClassJNI(jargv);



            } catch (Exception e) {
                e.printStackTrace();
            }
            String test_2 = test;
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

            //dismissLoadingDialog();
            asynctask_flag =false;
        }


    }



    public native static String DnnEngineClassJNI(String[] jargv);

    static {
        System.loadLibrary("DNN-jni");
    }


}
