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

    // DNN related classes
    //public DnnEngine mDnnengine ;
//    public String [] DNN_DB1 = {"Dutch oven","wok","caldron","frying pan","Crock Pot","plate","restaurant","groom","bakery"};
//    public String [] DNN_DB2 = {"lakeside","seashore","lakefront"};
//    public String [] DNN_DB3 = {"volcano","cliff","valley","mountainside","alp"};
//    public String [] DNN_DB4 = {"amusement park","playground"};

    public DnnModel dnnModel;

    public ArrayList DNN_result;
    public String hashString_DNN="";

    public DatelineModel(){
    }

    public DatelineModel(String updateDate, String photoGroup, String ids, String weather, String mood, int locationCount, int photoCount, String sentence, String poiGroup, String poiCRDatesGroup){
        mUpdateDate = updateDate;
        mPhotoGroup = photoGroup;
        mPhotoIds = ids;
        mWeather = weather;
        mMood =mood;
        mPoiGroup = poiGroup;
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
                return new ArrayList<String>(photoids.subList(0, 3));
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

    public String getSummarize() {

        dnnModel = new DnnModel();
        String DateInMomeent= getDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String DateToday = dateFormat.format(cal.getTime()); //your formatted date here
        cal.add(Calendar.DATE, -1);
        String DateYesterday = dateFormat.format(cal.getTime()); //your formatted date here

//        if(mSentence == null || mSentence.equals("") || DateInMomeent.equals(DateToday) ) {
        if(mSentence == null || mSentence.equals("")  ) {

            // Garbage collection before any heavy load work
            System.gc();

            Random generator = new Random();
            clusterEngine =new ClusterEngine();

            String [] DNN_path = MainActivity.DNN_path;
            DNN_result = new ArrayList();

            rear_cam_width = Integer.parseInt(readFromFile("rear_camera_setting.txt", AppApplication.getAppInstance().getApplicationContext())) ;
            front_cam_width = Integer.parseInt(readFromFile("front_camera_setting.txt", AppApplication.getAppInstance().getApplicationContext())) ;

            mDBWrapper = new DBWrapper(AppPreference.getInstance().getUserUid());

//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DATE, -1);
//            String DateToday = dateFormat.format(cal.getTime()); //your formatted date here
//            cal.add(Calendar.DATE, -1);
//            String DateYesterday = dateFormat.format(cal.getTime()); //your formatted date here

            doDayOfWeek();
            String hash_string ="";

            //1. Today is *** date\
            hash_string = hash_string + String.format("Today is %s. ", getDate());

            //2 Weather
            if(mWeather==null){
                hash_string = hash_string + "Not sure about the weather.\n";
            }else{
                hash_string = hash_string + String.format("It is %s. ", getWeatherEnglish());
                DNN_result.add(String.format("#%s",mWeather));
            }



            ArrayList poiList = getPoiList();
            ArrayList poiCRDatesList = getPoiCRDatesList();
            int photoID_size=0;

            if(poiList.size()>0)
            {

                // Get photo info retrieval
                ArrayList photoIDs = getDisplayPhotoIds();
                photoID_size= photoIDs.size();
                PhotoInfoModel[] photoinfos=null;
                Integer[] PhotoPoi_mapping_index=null;   // This contains POI index for each photo element

                if(photoID_size>0){
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
                        String temp = "ok";
                        if(PhotoPoi_mapping_index==null)
                        {
                            temp =null;
                        }


                        String temp2="";
                        for (int i=0;i<poiList.size();i++)
                        {
                            temp2=temp2 + String.format("#%s ", poiList.get(i).toString());
                        }
                        String temp3="";
                        for (int i=0;i<poiList.size();i++)
                        {
                            temp3=temp3 + String.format("#%s ", poiCRDatesList.get(i).toString());
                        }
                        String temp4="";
                        for (int i=0;i<photoID_size;i++)
                        {
                            temp4=temp4 + String.format("#%s ", photoinfos.toString());
                        }

                        hash_string = hash_string + temp2 + temp3+ temp4;
                        hash_string = hash_string + String.format("\n #%s #%s #%s ", DateInMomeent,DateToday, temp);



                        return hash_string;
                    }


                    int[] possibleNumbers = new int[100];
                    Map<Integer, Integer> result = new HashMap<Integer, Integer>();

                    for (int j = 0; j < PhotoPoi_mapping_index.length; ++j) {
                        possibleNumbers[PhotoPoi_mapping_index[j]] = possibleNumbers[PhotoPoi_mapping_index[j]] + 1;
                        result.put(PhotoPoi_mapping_index[j], possibleNumbers[PhotoPoi_mapping_index[j]]);
                    }

                    List<Integer> integerList = new ArrayList<>(uniqKeys);
                    Integer [] uniqKeysArray = new Integer[uniqKeys.size()];
                    for (int j = 0; j < uniqKeys.size(); j++)
                      uniqKeysArray[j] = integerList.get(j); //

                    //3. Place
                    // POI based sentence generation part
                    hash_string = POIbasedSentence(uniqKeysArray,poiList,hash_string);

                    // Photo based sentence generation part
                    int offset =0;
                    int size =0;
                    ArrayList photolist = getPhotoList();
                    String hash_string_face ="";
                    String hash_string_face_buf ="";
                    for (int m=0;m<uniqKeysArray.length;m++)
                    {
                        // key is unique poi index
                        Integer key_temp = uniqKeysArray[m];  //This is POI index which is key

                        size = result.get(key_temp);          // This is the number of photos taken in this POI index


                        //4. Face & Deep learning
                        hash_string_face=SentenceFromPhoto(offset,size,poiList.get(key_temp).toString(),photolist,front_cam_width,rear_cam_width, DNN_path);

                        if(hash_string_face.equals(hash_string_face_buf))
                            hash_string_face=""; // This is to prevent the duplication

                        hash_string =hash_string+hash_string_face;
                        hash_string_face_buf = hash_string_face;

                        offset=offset+size;
                    }


//
                        hashString_DNN="";
                        if(DNN_result.size()>0){
                            hashString_DNN =hashString_DNN + "\n";
                            for(int r=0;r<DNN_result.size();r++) {
                                if (r == 0)
                                    hashString_DNN = hashString_DNN + String.format("%s ", DNN_result.get(r).toString());
                                else {
                                    if (!DNN_result.get(r).toString().equals(DNN_result.get(r - 1).toString()))
                                        hashString_DNN = hashString_DNN + String.format("%s ", DNN_result.get(r).toString());
                                }
                            }

                        }


                }

                // This is how you get cluster size for the specific cluster ID
//                int temp = getClusterSize(String.valueOf(photoinfos[1].cluster_id));


                // Phot0 & POI mapping
                String temp = getCreateTime(poiList.get(0).toString());


                // 5. Measure how busy user was
                if(poiList.size() < 4) {
                    int n = generator.nextInt(dnnModel.dailysummary_nobusy.length);
                    hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_nobusy[n]);
                    DNN_result.add(String.format("#%s","Not busy"));

                } else if(poiList.size() >= 4 && poiList.size() <= 6) {
                    int n = generator.nextInt(dnnModel.dailysummary_lessbusy.length);
                    hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_lessbusy[n]);
                    DNN_result.add(String.format("#%s","A bit busy"));
                } else if (poiList.size() > 6) {
                    int n = generator.nextInt(dnnModel.dailysummary_busy.length);
                    hash_string = hash_string + String.format("%s ", dnnModel.dailysummary_busy[n]);
                    DNN_result.add(String.format("#%s","Busy"));
                }


                if(mMood!=null) {
                    hash_string = hash_string + String.format("\n I think today was %s day in general. ", mMood);
                    DNN_result.add(String.format("#%s",mMood));
                }

                if(DNN_result.size()>0){
                    for(int r=0;r<DNN_result.size();r++) {
                        if (r == 0)
                            hashString_DNN = hashString_DNN + String.format("%s ", DNN_result.get(r).toString());
                        else {
                            if (!DNN_result.get(r).toString().equals(DNN_result.get(r - 1).toString()))
                                hashString_DNN = hashString_DNN + String.format("%s ", DNN_result.get(r).toString());
                        }
                    }

                }

            }
            else{

                hash_string =hash_string + "I stayed at home whole day. I think I didn't do anything special. What a boring day. I will go out somewhere tomorrow";
            }

            hash_string = hash_string + hashString_DNN;


            // This saves sentense to DB


//            if (!DateInMomeent.equals(DateToday))
                mDBWrapper.setSentence(DateInMomeent,hash_string);


            return hash_string;


        } else {

            return mSentence;
        }



    }



    public ArrayList resampleandsave(int photoID_size, File myDir, ArrayList photolist) {

        ArrayList days_moment_resample_image  = new ArrayList();

        for (int t=0;t<photoID_size;t++) {
            String DNN_test_path_input = photolist.get(t).toString();
            File files = new File(DNN_test_path_input);
            if (files.exists()) {
                Bitmap bm = AppUtils.downsampleImageFile(DNN_test_path_input, 122, 149);


                if(myDir.exists() && myDir.isDirectory()) {
                    // do nothing
                }else{
                    myDir.mkdirs();
                }

                String fname = "Days_Moment_" + t + ".days";
                File file = new File(myDir, fname);
                days_moment_resample_image.add(file.toString());

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
        }

        return days_moment_resample_image;
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




    private static void SaveImage(Bitmap finalBitmap, String filename) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/resized_images");
        myDir.mkdirs();

        String fname = String.format("%s_resized.JPG", filename);
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String POIbasedSentence(Integer [] uniqKeysArray, ArrayList poiList , String hash_string){



        if(uniqKeysArray.length==1){
            if(uniqKeysArray[0].toString().contains(AppUtils.getAppText(R.string.text_location_home))) {
                hash_string = hash_string + String.format("%s %s. ", "I didn't go anywhere. Just stayed in my house", poiList.get(uniqKeysArray[0]).toString());
            }else{
                hash_string = hash_string + String.format("%s %s. ", "I didn't go anywhere. Just stayed in", poiList.get(uniqKeysArray[0]).toString());
            }

        } else if(uniqKeysArray.length==2){
//                        if( poiList.get(0).toString().equals(poiList.get(1).toString()) && poiList.size()==2)
//                            hash_string = hash_string + String.format("%s. ", "I just quickly went outside and came back home soon.");


                if(uniqKeysArray[0].toString().contains(AppUtils.getAppText(R.string.text_location_home)) && uniqKeysArray[1].toString().contains(AppUtils.getAppText(R.string.text_location_home))) {



                }else if(uniqKeysArray[0].toString().contains(AppUtils.getAppText(R.string.text_location_home)) || !uniqKeysArray[1].toString().contains(AppUtils.getAppText(R.string.text_location_home))){

                    hash_string = hash_string + String.format("I went to %s and ", poiList.get(uniqKeysArray[1]).toString());

                }else if(!uniqKeysArray[0].toString().contains(AppUtils.getAppText(R.string.text_location_home)) || !uniqKeysArray[1].toString().contains(AppUtils.getAppText(R.string.text_location_home))){

                    hash_string = hash_string + String.format("I went to %s and ", poiList.get(uniqKeysArray[0]).toString());

                }else{
                    for (int k=0;k<2;k++){
                        if(k==0)
                            hash_string = hash_string + String.format("I went to %s and ", poiList.get(uniqKeysArray[k]).toString());
                        else
                            hash_string = hash_string + String.format("%s. ", poiList.get(uniqKeysArray[k]).toString());
                    }
                }




        }else if(uniqKeysArray.length>2){

            // Later on, there might be some corner case such as home, home, home, school, school and home
            hash_string = hash_string + String.format("%s.", "I went to a couple of places. ");


            for (int l=0;l<uniqKeysArray.length;l++){
                Integer temp_poiIndex = uniqKeysArray[l];
                if(l==0) {

                    if(uniqKeysArray[0].toString().contains(AppUtils.getAppText(R.string.text_location_home))){
                        hash_string = hash_string + String.format("Here are some nice photos taken in my house");
                    }else{
                        hash_string = hash_string + String.format("Here are some nice photos taken in %s", poiList.get(uniqKeysArray[l]).toString());
                    }


                }else if(l<uniqKeysArray.length-1) {

                    if(uniqKeysArray[l].toString().equals(uniqKeysArray[l-1].toString())){
                        // Do nothing, overlapped .
                    }else{
                        hash_string = hash_string + String.format(", %s ", poiList.get(uniqKeysArray[l]).toString());
                    }


                }else if(l==uniqKeysArray.length-1) {

                    if(uniqKeysArray[l].toString().equals(uniqKeysArray[l-1].toString())){
                        // Do nothing, overlapped .
                    }else{
                        hash_string = hash_string + String.format("and %s. ", poiList.get(uniqKeysArray[l]).toString());
                    }


                }
            }
        }

        return hash_string;

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

                    // POI create Date formatting
                    poiCRDatesList_format1 = AppUtils.StringToDate(now, poiCRDatesList.get(j).toString());
                    if(j==poiCRDatesList.size()-1)
                        poiCRDatesList_format2 = AppUtils.getTodayDateTime(now, "11:59:59");
                    else
                        poiCRDatesList_format2 = AppUtils.StringToDate(now, poiCRDatesList.get(j+1).toString());


                    // Photo Create date formatting
                    Date photoCRDatesList_format = AppUtils.StringToDate(now, photoinfos[k].update_date);
                    if(photoCRDatesList_format.after(poiCRDatesList_format1) && photoCRDatesList_format.before(poiCRDatesList_format2)) {
                        PhotoPoi_mapping_index[k]= j; //
                    }


                }
            }

        }


        return PhotoPoi_mapping_index;
    }


    public String SentenceFromPhoto(int offset,int size,String poi_string, ArrayList PhotoList,  int front_cam_width, int rear_cam_width, String [] DNN_path)
    {
        String hash_string = "";
        String hash_string_DNN= "";
        String hash_string_POI= "";
        int photoCount = PhotoList.size();
        DnnModel dnnModel = new DnnModel();
        Random generator = new Random();
        double avg_PhotoCreateTime =0;

        EngineDBInterface engineDBInterface = new EngineDBInterface();


        // POI context based string generation
        //POI_DB1 : Coffe and tea
        //POI_DB2 : 식당, Restaurant
        //POI_DB3 : Park
        //POI_DB4 : Cinema
        //POI_DB5 : "shopping"
        //POI_DB6 : "놀이공원"
        for (int i = offset; i < offset + size; i++){
            String timelinePhotoFile = PhotoList.get(i).toString();
            double temp[] = clusterEngine.timeFeatureExtract(timelinePhotoFile);
            avg_PhotoCreateTime=avg_PhotoCreateTime+temp[3];
        }
        avg_PhotoCreateTime=avg_PhotoCreateTime/size;

        // POI context based sentence
//        boolean POI_DB1_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB1);
        boolean POI_DB2_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB2);
//        boolean POI_DB3_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB3);
//        boolean POI_DB4_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB4);
//        boolean POI_DB5_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB5);
        boolean POI_DB6_DETECT= poiclassDetect(poi_string,dnnModel.POI_DB6);



        hash_string_POI = getPOIstring(poi_string, dnnModel, avg_PhotoCreateTime);
        hash_string=hash_string+hash_string_POI;


        int sentence_cnt =0;

            for (int i = offset; i < offset + size; i++){
                String timelinePhotoFile = PhotoList.get(i).toString();

                int Im_width=0;
                int Im_height=0;

                float Num_Face=0;
                float Smile_Prob=0;
                int selfie_cnt=0;
                int singlePhoto_cnt=0;
                int groupSelfie_cnt=0;
                int groupPhoto_cnt=0;
                int nonhumanPhoto_cnt=0;
                int smile_cnt=0;

                int n = 0;


                // Face detectioin : Face number. Eye close. Smile probability

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(timelinePhotoFile, options);
                int sample_size =32;

                BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
                bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap_options.inSampleSize = sample_size;
                Bitmap bMap_temp = BitmapFactory.decodeFile(timelinePhotoFile, bitmap_options);
                Im_width = bMap_temp.getWidth() * sample_size ;

                Num_Face =  engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
                Smile_Prob =  engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);


                // Selfie, solo , group
                if( Num_Face == 1) {
                    if( Math.abs(front_cam_width - Im_width) < 500)
                        selfie_cnt++;
                    else if ( Math.abs(rear_cam_width - Im_width) < 500)
                        singlePhoto_cnt ++;
                }
                else if( Num_Face >1) {

                    if( Math.abs(front_cam_width - Im_width) < 500)
                        groupSelfie_cnt++;
                    else if ( Math.abs(rear_cam_width - Im_width) < 500)
                        groupPhoto_cnt++;

                }

                if( Smile_Prob >= 0.6)
                    smile_cnt++;


                String connection="";
                if(size==1)
                    connection=".";
                else if(size==2 && (i-offset)==0)
                    connection="and";
                else if(size==2 && (i-offset)==1)
                    connection=".";
                else if(size>2 && (i-offset)<size-2)
                    connection=",";
                else if(size>2 && (i-offset)==size-2)
                    connection="and";
                else if(size>2 && (i-offset)==size-1)
                    connection=".";
                else
                    connection="";


                    // Selfie check
                    if (selfie_cnt > 0) {
                        // Smile detection
                        n = generator.nextInt(dnnModel.FaceBasedPool_selfie_smile.length);

                        if (smile_cnt > 0) {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_selfie_smile[n], connection);
                            DNN_result.add(String.format("#%s", "Selfie with smile"));
                        } else {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_selfie_nosmile[n], connection);
                            DNN_result.add(String.format("#%s", "Selfie"));
                        }

                    }


                    // Group photo, single photo check
                    if (singlePhoto_cnt > 0) {

                        // Smile detection
                        n = generator.nextInt(dnnModel.FaceBasedPool_single_smile.length);

                        if (smile_cnt > 0) {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_single_smile[n], connection);
                        } else {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_single_nosmile[n], connection);

                        }
                    }

                    if (groupPhoto_cnt > 0) {

                        // Smile detection
                        n = generator.nextInt(dnnModel.FaceBasedPool_group_smile.length);

                        if (smile_cnt > 0) {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_group_smile[n], connection);
                        } else {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_group_nosmile[n], connection);
                        }
                        DNN_result.add(String.format("#%s", "Group photo"));

                    }

                    if (groupSelfie_cnt > 0) {
                        // Smile detection
                        n = generator.nextInt(dnnModel.FaceBasedPool_group_selfie.length);

                        if (smile_cnt > 0) {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_group_selfie[n], connection);
                            DNN_result.add(String.format("#%s", "Group selfie with smile"));
                        } else {
                            hash_string = hash_string + String.format("%s %s ", dnnModel.FaceBasedPool_group_noselfie[n], connection);
                            DNN_result.add(String.format("#%s", "Group selfie"));
                        }

                    }


                    // This is to interpret non humand photos such as food and landscape
                        // Do nothing at this point
                        //4. Deep learning engine
                        // It detects food, mountain, cliff, river, sea, seashore only
                        File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/days_resample_images"); //
                        String days_moment_resample_image ;

                        days_moment_resample_image=resampleandsave_single(i, myDir, timelinePhotoFile);



                        // Run neural network
                        // This is for classification
                        String DNN_test_path_resample =days_moment_resample_image;
                        String[] jargv =new String[7];
                        jargv[0] ="classifier_Class";
                        jargv[1] ="predictCustom";  // This is for classification
                        jargv[2] =DNN_path[0];
                        jargv[3] =DNN_path[1];
                        jargv[4] =DNN_path[2];
                        jargv[5] =DNN_test_path_resample;
                        jargv[6] = outDir+"/";

                        String class_predict = DnnEngineClassJNI(jargv);
                        System.gc();


                        Boolean foodClass = classDetect(class_predict, dnnModel.DNN_DB1);
                        Boolean WaterClass = classDetect(class_predict, dnnModel.DNN_DB2);
                        Boolean MounatainClass = classDetect(class_predict, dnnModel.DNN_DB3);
                        Boolean PlayClass = classDetect(class_predict, dnnModel.DNN_DB4);


                        double [] temp_time = clusterEngine.timeFeatureExtract(timelinePhotoFile);
                        double pic_time = temp_time[3];

                        if(foodClass && pic_time!=1997 && !POI_DB2_DETECT)
                        {

                            if(pic_time>05 && pic_time< 10){
                                //Breakfast
                                hash_string_DNN = "I had breakfast. ";
                                class_predict =String.format("#%s #%s","breakfast", poi_string);
                            }else if(pic_time>=11 && pic_time< 14){
                                //Lunch
                                hash_string_DNN = String.format("%s %s ", "I had a lunch in ", poi_string);
                                class_predict =String.format("#%s #%s","lunch", poi_string);
                            }else if(pic_time>=17 && pic_time< 19){
                                //dinner
                                hash_string_DNN = String.format("%s %s ", "I had a dinner in ", poi_string);

                                class_predict =String.format("#%s #%s","dinner", poi_string);
                            }else if(pic_time>=19) {
                                //Party
                                hash_string_DNN = String.format("%s %s %s", "I had party with my friends and colleagues in ", poi_string, ". Awesome food. ");

                                class_predict =String.format("#%s #%s","dinner party", poi_string);
                            }

                            DNN_result.add(class_predict);

                        }else if(WaterClass){

                            hash_string_DNN = "I went outside today and had fun in the water. ";
                            class_predict =String.format("#%s #%s","fun in the water", poi_string);
                            DNN_result.add(class_predict);


                        }else if(MounatainClass){

                            hash_string_DNN = "I went to mountain today. It was great. ";
                            class_predict =String.format("#%s #%s","hiking", poi_string);
                            DNN_result.add(class_predict);
                        }else if(PlayClass && !POI_DB6_DETECT){

                            hash_string_DNN = "I went to amusement park today. It was so fun with my family.";
                            class_predict =String.format("#%s #%s","amusement park", poi_string);
                            DNN_result.add(class_predict);
                        }


                        sentence_cnt++;





            }



                    hash_string = hash_string + hash_string_DNN;


        return hash_string;
    }


    public String  getPOIstring(String poi_string, DnnModel dnnModel, double avg_PhotoCreateTime){

        // POI context based string generation
        //POI_DB1 : Coffe and tea
        //POI_DB2 : 식당, Restaurant
        //POI_DB3 : Park
        //POI_DB4 : Cinema
        //POI_DB5 : "shopping"
        //POI_DB6 : "놀이공원"

        String hash_string_POI="";



            if(poiclassDetect(poi_string,dnnModel.POI_DB1)){
                hash_string_POI =  String.format("In %s ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","I had some coffee");
                DNN_result.add(String.format("#%s #%s", poi_string, "Coffee"));
            } else if(poiclassDetect(poi_string,dnnModel.POI_DB2)){
                hash_string_POI =  String.format("In %s ",poi_string);

                if(avg_PhotoCreateTime>05 && avg_PhotoCreateTime< 10){
                    //Breakfast
                    hash_string_POI = hash_string_POI + String.format("%s ", "I had a breakfast. ");
                    DNN_result.add(String.format("#%s #%s", poi_string, "Breakfast"));
                }else if(avg_PhotoCreateTime>=11 && avg_PhotoCreateTime< 14){
                    //Lunch
                    hash_string_POI = hash_string_POI +String.format("%s ", "I had a lunch. ");
                    DNN_result.add(String.format("#%s #%s", poi_string, "Lunch"));
                }else if(avg_PhotoCreateTime>=10 && avg_PhotoCreateTime< 11){
                    //Lunch
                    hash_string_POI =hash_string_POI + String.format("%s ", "I had a brunch. ");
                    DNN_result.add(String.format("#%s #%s", poi_string, "Brunch"));
                }else if(avg_PhotoCreateTime>=17 && avg_PhotoCreateTime< 19){
                    //dinner
                    hash_string_POI = hash_string_POI +String.format("%s ", "I had a dinner. ");
                    DNN_result.add(String.format("#%s #%s", poi_string, "Dinner"));
                }else if(avg_PhotoCreateTime>=19) {
                    //Party
                    hash_string_POI = hash_string_POI +String.format("%s ", "I had a party with my friends and colleagues");
                    DNN_result.add(String.format("#%s #%s", poi_string, "Party"));
                }

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB3)){
                hash_string_POI =  String.format("In %s ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","I walked with my friends and took some rest");
                DNN_result.add(String.format("#%s #%s", poi_string, "Walking"));

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB4)){

                hash_string_POI =  String.format("In %s ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","I watched movie with my friend");
                DNN_result.add(String.format("#%s #%s", poi_string, "Movie"));

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB5)){

                hash_string_POI =  String.format("In %s ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","I did some shopping");
                DNN_result.add(String.format("#%s #%s", poi_string, "Shopping"));

            } else if(poiclassDetect(poi_string,dnnModel.POI_DB6)){

                hash_string_POI =  String.format("In %s ",poi_string);
                hash_string_POI= hash_string_POI + String.format("%s. ","I went to amusement park");
                DNN_result.add(String.format("#%s #%s", poi_string, "Amusement park"));

            }





        return hash_string_POI;
    }



    public boolean classDetect(String class_predict, String [] Class_DB){

        boolean result=false;

        for(int i=0;i<Class_DB.length;i++){
            String temp = Class_DB[i];
            if(class_predict.equals(Class_DB[i])){
                result = true;
                break;
            }else {
                result = false;

            }

        }

        return result;

    }

    public boolean poiclassDetect(String poi_String, String [] Class_DB){

        boolean result=false;

        for(int i=0;i<Class_DB.length;i++){
            String temp = Class_DB[i];
            if(poi_String.contains(Class_DB[i])){
                result = true;
                break;
            }else {
                result = false;

            }

        }

        return result;

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
