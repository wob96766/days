package com.mindspree.days.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mindspree.days.engine.ClusterEngine;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static android.content.ContentValues.TAG;

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
    private String mCategoryGroup = "";
    private String mPoiCRDatesGroup = "";
    private int mLocationCount = 0;
    private int mPhotoCount = 0;
    private String mSentence = "";

    private DBWrapper mDBWrapper;
    private AppPreference mPreference;

    public int weekend_days=0;
    public String strWeek;
    public int nWeek;

    public int rear_cam_width =5000;
    public int front_cam_width =3000;


    public ClusterEngine clusterEngine;
    public DnnModel dnnModel;
    ArrayList<String> mPhotoList = null;

    public String mMood_kr = "";
    public String mMood_hash_kr = "";

    public ArrayList DNN_result_in;
    public ArrayList DNN_result;
    public String hashString_DNN="";

    public DatelineModel(){
    }

    public DatelineModel(String updateDate, String photoGroup, String ids, String weather, String mood, int locationCount, int photoCount, String sentence, String poiGroup, String poiCRDatesGroup, String categoryGroup){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mPhotoIds = ids;
        mWeather = weather;
        mMood =mood;
        mPoiGroup = poiGroup;
        mCategoryGroup = categoryGroup;
        mPoiCRDatesGroup = poiCRDatesGroup;
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
        if(mPhotoList == null) {
            mPhotoList = new ArrayList<String>();
            if (mPhotoGroup != null) {
                ArrayList<String> temp = new ArrayList<String>(Arrays.asList(mPhotoGroup.split(",")));
                for (int i = 0; i < temp.size(); i++) {
                    if (!temp.get(i).contains("http://") && !temp.get(i).contains("https://")) {
                        File file = new File(temp.get(i));
                        if (file.exists()) {
                            mPhotoList.add(temp.get(i));
                        }
                    } else {
                        mPhotoList.add(temp.get(i));
                    }
                }
            }
        }
        return mPhotoList;
    }
    //junyong - get image's "photo_index" to be displayed on the screen
    public ArrayList<String> getDisplayPhotoIds() {
        ArrayList<String> result = new ArrayList<String>();

        if(mPhotoGroup != null) {

            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(mPhotoGroup.split(",")));
            ArrayList<String> ids = new ArrayList<String>(Arrays.asList(mPhotoIds.split(",")));
            for(int i=0;i<temp.size() ; i++){
                if(!temp.get(i).contains("http://") && !temp.get(i).contains("https://")) {
                    File file = new File(temp.get(i));
                    if (file.exists()) {
                        result.add(ids.get(i));
                    }
                } else {
                    result.add(ids.get(i));
                }
            }
        }
        return result;
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

    public ArrayList<String> getCategoryList() {
//        if(mPhotoGroup != null) {
        if(mCategoryGroup != null) {
            return new ArrayList<String>(Arrays.asList(mCategoryGroup.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getPoiCRDatesList() {
//        if(mPhotoGroup != null) {
        if(mPoiCRDatesGroup != null) {
            return new ArrayList<String>(Arrays.asList(mPoiCRDatesGroup.split(",")));
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

    // sentence_mode
    // hash --> return only hash
    // return --> only sentence
    public String getSummarize(String sentence_mode) {


        dnnModel = new DnnModel();
        String DateInMomeent= getDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        String DateToday = dateFormat.format(cal.getTime()); //your formatted date here
        cal.add(Calendar.DATE, -1);
        String DateYesterday = dateFormat.format(cal.getTime()); //your formatted date here

//        if(mSentence == null || mSentence.equals("") || DateInMomeent.equals(DateToday) ) {   // This is only for debugging
//        if(DateInMomeent.equals(DateYesterday) ) {   // This is only for debugging
        if(mSentence == null || mSentence.equals("")  ) {
//        if(true) {

            if (sentence_mode.equals("hash"))
                return "";

            System.gc();
            Random generator = new Random();
            clusterEngine =new ClusterEngine();
            EngineDBInterface engineDBInterface = new EngineDBInterface();

            String [] DNN_path = MainActivity.DNN_path;
            DNN_result = new ArrayList();

            rear_cam_width = Integer.parseInt(readFromFile("rear_camera_setting.txt", AppApplication.getAppInstance().getApplicationContext())) ;
            front_cam_width = Integer.parseInt(readFromFile("front_camera_setting.txt", AppApplication.getAppInstance().getApplicationContext())) ;
            mDBWrapper = new DBWrapper(AppPreference.getInstance().getUserUid());

            doDayOfWeek();
            String hash_string ="";

            //1. Today is *** date
            hash_string = hash_string + String.format("%s. ", getDate());  //            hash_string = hash_string + String.format("Today is %s. ", getDate());

            //2 Weather
            if(mWeather==null){
                hash_string = hash_string + "날씨 잘 모르겠음.\n";
            }else{
                hash_string = hash_string + String.format("날씨 %s. ", getWeather());//                hash_string = hash_string + String.format("It is %s. ", getWeatherEnglish());
                DNN_result.add(String.format("#%s",mWeather));
            }

            ArrayList poiList = getPoiList();
            ArrayList poicatList = getCategoryList(); // Next step is to create Category LUT with POI list key
            ArrayList poiCRDatesList = getPoiCRDatesList();
            int photoID_size=0;

            if(poiList.size()>1)
            {

                // Get photo info retrieval
                ArrayList photoIDs = getDisplayPhotoIds();
                photoID_size= photoIDs.size();
                PhotoInfoModel[] photoinfos=null;
                Integer[] PhotoPoi_mapping_index=null;   // This contains POI index for each photo element

                if(photoID_size==0){

                    //1. Only POI based sentence. There is no photo
                    // Get the unique POIs index from all POIs
                    Integer [] uniqKeysArray = null;
                    int array_size = arraylistsize_nooverlap(poiList);
                    String [] poiList_nooverlap = new String[array_size];
                    poiList_nooverlap=arraylistTostringarray_nooverlap(poiList);

                    hash_string = dnnModel.POIbasedSentence(uniqKeysArray, DNN_result, poiList_nooverlap,poiList,hash_string);
                    DNN_result.addAll(dnnModel.DNN_result);

                }else if(photoID_size>0){
                    photoinfos = new PhotoInfoModel[photoID_size];

                    for (int i=0;i<photoID_size;i++)
                        photoinfos[i] = getPhotoInfo(photoIDs.get(i).toString());

                    // convert POI & Photo create time format to yyyy-MM-dd HH:mm:ss
                    PhotoPoi_mapping_index= PhotoPoi_mapping(photoID_size, photoinfos ,poiCRDatesList);

                    // Get the unique POI index
                    Set<Integer> uniqKeys = new TreeSet<Integer>();
                    try {
                        uniqKeys.addAll(Arrays.asList(PhotoPoi_mapping_index));
                    } catch (Exception e) {
                        Log.e("your app", e.toString());
                        hash_string = hash_string + String.format("\n #%s", "Error");
                        return hash_string;
                    }

                    int[] possibleNumbers = new int[100];
                    Map<Integer, Integer> result = new HashMap<Integer, Integer>();

                    for (int j = 0; j < PhotoPoi_mapping_index.length; ++j) {
                        possibleNumbers[PhotoPoi_mapping_index[j]] = possibleNumbers[PhotoPoi_mapping_index[j]] + 1;
                        result.put(PhotoPoi_mapping_index[j], possibleNumbers[PhotoPoi_mapping_index[j]]);
                    }

                    //3. POI based sentence
                    // Get the unique POIs index from all POIs
                    List<Integer> integerList = new ArrayList<>(uniqKeys);
                    Integer [] uniqKeysArray = new Integer[uniqKeys.size()];
                    for (int j = 0; j < uniqKeys.size(); j++)
                      uniqKeysArray[j] = integerList.get(j); //

                    int array_size = arraylistsize_nooverlap(poiList);
                    String [] poiList_nooverlap = new String[array_size];
                    poiList_nooverlap=arraylistTostringarray_nooverlap(poiList);
                    hash_string = dnnModel.POIbasedSentence(uniqKeysArray, DNN_result, poiList_nooverlap,poiList,hash_string);

                    //4. Face & Deep learning
                    int offset =0;
                    int size =0;
                    ArrayList photolist = getPhotoList();
                    String hash_string_face ="";
                    String hash_string_face_buf ="";
                    for (int m=0;m<uniqKeysArray.length;m++){
                        // key is unique poi index
                        Integer key_temp = uniqKeysArray[m];  //This is POI index which is key
                        size = result.get(key_temp);          // This is the number of photos taken in this POI index

                        hash_string_face= dnnModel.SentenceFromPhoto_korean(clusterEngine, offset,size, DNN_result, poiList.get(key_temp).toString(),photolist,front_cam_width,rear_cam_width, DNN_path, weekend_days);
                        DNN_result.addAll(dnnModel.DNN_result);

                        if(hash_string_face.equals(hash_string_face_buf))
                            hash_string_face=""; // This is to prevent the duplication

                        hash_string =hash_string+hash_string_face;
                        hash_string_face_buf = hash_string_face;

                        offset=offset+size;
                    }


                }


                // Phot0 & POI mapping
                String temp = getCreateTime(poiList.get(0).toString());

                // 5. Measure how busy user was
                if(poiList.size() < 4) {
                    int n = generator.nextInt(dnnModel.dailysummary_nobusy.length);
                    hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_nobusy[n]);
                    DNN_result.add(String.format("#%s","하나도 안 바쁨"));

                } else if(poiList.size() >= 4 && poiList.size() <= 6) {
                    int n = generator.nextInt(dnnModel.dailysummary_lessbusy.length);
                    hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_lessbusy[n]);
                    DNN_result.add(String.format("#%s","약간 바쁨"));
                } else if (poiList.size() > 6) {
                    int n = generator.nextInt(dnnModel.dailysummary_busy.length);
                    hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_busy[n]);
                    DNN_result.add(String.format("#%s","바쁨"));
                }


                if(mMood!=null) {

                    if(mMood.equals("Happy")){
                        mMood_kr="그럭저럭 행복한";  // For sentence
                        mMood_hash_kr="행복";     // For hash
                    }else if(mMood.equals("Angry")){
                        mMood_kr="별로 기분이 안 좋은";
                        mMood_hash_kr="화남";
                    }else if(mMood.equals("Sad")){
                        mMood_kr="슬픔";
                        mMood_hash_kr="행복";
                    }else if(mMood.equals("Busy")){
                        mMood_kr="많이 바쁜";
                        mMood_hash_kr="바쁨";
                    }

                    hash_string = hash_string + String.format("\n 오늘은 %s 하루였다. ", mMood_kr);
                    DNN_result.add(String.format("#%s",mMood_hash_kr));

                }

                if(DNN_result.size()>0){

                    // Get the unique hash result,  remove overlapping
                    int array_size = arraylistsize_nooverlap(DNN_result);
                    String [] DNN_result_nooverlap = new String[array_size];
                    DNN_result_nooverlap=arraylistTostringarray_nooverlap(DNN_result);

                    for(int r=0;r<DNN_result_nooverlap.length;r++)
                        hashString_DNN = hashString_DNN + DNN_result_nooverlap[r];

                }

            }
            else if(poiList.size()==1){

                int n = generator.nextInt(dnnModel.dailysummary_nopoi_kr.length);
                hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_nopoi_kr[n]);
            }

            // Save fianl sentence and hash to DB
            hash_string = hash_string + "\n" + hashString_DNN;
            mDBWrapper.setSentence(DateInMomeent,hash_string);
            return hash_string;


        } else {

            ArrayList sentenceHash;
            sentenceHash = new ArrayList<String>(Arrays.asList(mSentence.split("#")));
            String sentenceOnly = sentenceHash.get(0).toString();
            String hashOnly ="";

            if(sentenceHash.size()>=2)
            {
                for(int i=1;i<sentenceHash.size();i++)
                    hashOnly = hashOnly + "#" + sentenceHash.get(i).toString();
            }


            if (sentence_mode.equals("sentence")) {
                return sentenceOnly;
            }else{
                return hashOnly;
            }


        }



    }



    public String resampleandsave_single(int index, File myDir, String DNN_test_path_input) {

        String days_moment_resample_image=null;


            File files = new File(DNN_test_path_input);
            if (files.exists()) {
                Bitmap bm = AppUtils.downsampleImageFile(DNN_test_path_input, 122, 149);


                if(myDir.exists() && myDir.isDirectory()) {
                    // do nothing
                }else{
                    myDir.mkdirs();
                }

                String fname = "Days_Moment_" + index + ".days";
                File file = new File(myDir, fname);
                days_moment_resample_image=file.toString();

                if (file.exists())
                    file.delete();

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        return days_moment_resample_image;
    }






    public String [] arraylistTostringarray_nooverlap(ArrayList poiList){
        Set<String> uniqKeys2 = new TreeSet<String>();

        // Convert poiList arraylist to string array
        Object[] poiListStringArraytemp = poiList.toArray(new String[poiList.size()]);
        String[] poiListStringArray = (String[]) poiListStringArraytemp;

        try {
            uniqKeys2.addAll(Arrays.asList(poiListStringArray));
        } catch (Exception e) {
            e.printStackTrace();

        }

        List<String> stringList = new ArrayList<>(uniqKeys2);
        String [] uniqKeysArray2 = new String[uniqKeys2.size()];
        for (int j = 0; j < uniqKeys2.size(); j++)
            uniqKeysArray2[j] = stringList.get(j); //


        return uniqKeysArray2;

    }

    public int arraylistsize_nooverlap(ArrayList poiList){
        Set<String> uniqKeys2 = new TreeSet<String>();


        // Convert poiList arraylist to string array
        Object[] poiListStringArraytemp = poiList.toArray(new String[poiList.size()]);
        String[] poiListStringArray = (String[]) poiListStringArraytemp;

        try {
            uniqKeys2.addAll(Arrays.asList(poiListStringArray));
        } catch (Exception e) {
            e.printStackTrace();

        }

        return uniqKeys2.size();

    }


    public Integer[] PhotoPoi_mapping(int photoID_size, PhotoInfoModel[] photoinfos ,ArrayList poiCRDatesList)
    {
        Integer[] PhotoPoi_mapping_index=new Integer[photoID_size];

        Date now = new Date();
        Date poiCRDatesList_format1 =null;
        Date poiCRDatesList_format2 =null;

        if(poiCRDatesList.size() == 1){
            for(int i=0;i<photoID_size;i++)
                PhotoPoi_mapping_index[i]= 0; //

        }else{
            for(int k=0;k<photoID_size;k++){
                for(int j=0;j<poiCRDatesList.size();j++){


                    if(j>0 && j<poiCRDatesList.size()-1){
                        // POI create Date formatting
                        poiCRDatesList_format1 = AppUtils.StringToDate(now, poiCRDatesList.get(j).toString());
                        poiCRDatesList_format2 = AppUtils.StringToDate(now, poiCRDatesList.get(j + 1).toString());

                        // Photo Create date formatting
                        Date photoCRDatesList_format = AppUtils.StringToDate(now, photoinfos[k].update_date);
                        if( (photoCRDatesList_format.equals(poiCRDatesList_format1) || photoCRDatesList_format.after(poiCRDatesList_format1)) && photoCRDatesList_format.before(poiCRDatesList_format2))
                            PhotoPoi_mapping_index[k]= j;
                    }else if(j==0){
                        poiCRDatesList_format1 = AppUtils.StringToDate(now, poiCRDatesList.get(j).toString());
                        poiCRDatesList_format2 = AppUtils.StringToDate(now, poiCRDatesList.get(j+1).toString());

                        Date photoCRDatesList_format = AppUtils.StringToDate(now, photoinfos[k].update_date);
                        if(photoCRDatesList_format.before(poiCRDatesList_format2))
                            PhotoPoi_mapping_index[k]= 0;
                        else if( (photoCRDatesList_format.equals(poiCRDatesList_format1) || photoCRDatesList_format.after(poiCRDatesList_format1)) && photoCRDatesList_format.before(poiCRDatesList_format2))
                            PhotoPoi_mapping_index[k]= j;

                    }else{
                        // POI create Date formatting


                        poiCRDatesList_format1 = AppUtils.StringToDate(now, poiCRDatesList.get(j).toString());
                        poiCRDatesList_format2 = AppUtils.getTodayDateTime(now, "23:59:59");

                        // Photo Create date formatting
                        Date photoCRDatesList_format = AppUtils.StringToDate(now, photoinfos[k].update_date);
                        if(photoCRDatesList_format.equals(poiCRDatesList_format1) || photoCRDatesList_format.after(poiCRDatesList_format1))
                            PhotoPoi_mapping_index[k]= j;
                    }




                }
            }

        }


        return PhotoPoi_mapping_index;
    }



    private String readFromFile(String filename, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    public native static String DnnEngineClassJNI(String[] jargv);

    static {
        System.loadLibrary("DNN-jni");
    }



}
