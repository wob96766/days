package com.mindspree.days.model;

//import android.hardware.Camera;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.location.*;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.engine.ClusterEngine;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.ui.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.mindspree.days.engine.EngineDBInterface;

import static android.R.attr.defaultValue;
import static android.media.CamcorderProfile.get;
import static com.mindspree.days.model.DatelineModel.DnnEngineClassJNI;

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
    public Location mCurrentLocation = new Location("other");;

    private String mMeasureDate = "";
    private String mCreateDate = "";
    private String mPhotoGroup = "";
    private String mImageGroup = "";


    private String mMeasureTime ="";
    private String mMeasureTimeInHour ="";
    private ArrayList<TextView> mSelectedImages = new ArrayList<TextView>();
    private TimelineModel mTimelineModel;
    private DBWrapper mDBWrapper;

    public int weekend_days=0;
    public String strWeek;
    public int nWeek;

    public ClusterEngine clusterEngine;
    // DNN related classes
    //public DnnEngine mDnnengine ;

    public ArrayList DNN_result;
    public String hashString_DNN="";

    public String [] DNN_path ;

    private MainActivity mainActivity =new MainActivity();
    public DnnModel dnnModel = new DnnModel();

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
            SimpleDateFormat formatString = new SimpleDateFormat("HH:mm");
            mMeasureTime = formatString.format(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void setMeasureTimeInHour(String formatDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date updateDate= dateFormat.parse(formatDate);
            SimpleDateFormat formatString = new SimpleDateFormat("HH");
            mMeasureTimeInHour = formatString.format(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }





    private void writeToFile(String data, String filename, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String filename, Context context) {

        String ret = "0";

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

    public String getSummarize(Context context) {

        DNN_path = MainActivity.DNN_path;
        DNN_result = new ArrayList();
        clusterEngine =new ClusterEngine();
        double avg_PhotoCreateTime =0;
        ArrayList PhotoList = getPhotoList();  // getExtraFeatWithPhotoURL(PhotoString);


        // Read Caemra Information (defined in Main Activity)
        int rear_cam_width =5000;
        int front_cam_width =2500;

        rear_cam_width = Integer.parseInt(readFromFile("rear_camera_setting.txt", AppApplication.getAppInstance().getApplicationContext())) ;
        front_cam_width = Integer.parseInt(readFromFile("front_camera_setting.txt", AppApplication.getAppInstance().getApplicationContext())) ;

        if(rear_cam_width==0)
            rear_cam_width = 5000;

        if(front_cam_width==0)
            front_cam_width = 2500;

        mCurrentLocation.setLatitude(getLatitude());
        mCurrentLocation.setLongitude(getLongitude());

        String hash_string = "";
        // Check date
        doDayOfWeek();

        // 1. POI estimation
        String POI = mName;
        hash_string =String.format("#%s ", mName);



        // 2. Time
        setMeasureTime(mCreateDate);
        setMeasureTimeInHour(mCreateDate);
        hash_string =hash_string + String.format("#%s ", mMeasureTime);



        // 3. Location
        if(mName !=null)
        {
            String homeLocationText = AppUtils.getAppText(R.string.text_location_home);
            if(weekend_days==0){
                // Weekdays
                if(mName.equals(homeLocationText) || mName.contains(homeLocationText)){

                    if( Integer.parseInt(mMeasureTimeInHour) <9 ){
                        hash_string =hash_string + String.format("#%s ", "바쁜 아침");  // Busy morning
                    }else if ( Integer.parseInt(mMeasureTimeInHour) >= 10 && Integer.parseInt(mMeasureTimeInHour) <= 12){
                        hash_string =hash_string + String.format("#%s ", "여유있는 아침");   // Lazy morning
                    }else if ( Integer.parseInt(mMeasureTimeInHour) > 18 && Integer.parseInt(mMeasureTimeInHour) < 24){
                        hash_string =hash_string + String.format("#%s ", "다시 집");   // Lazy morning
                    }


                }
                else
                {
                    if( Integer.parseInt(mMeasureTimeInHour) <6 ) {
                        hash_string = hash_string + String.format("#%s ", "외박 ~");
                    }
                }

            }
            else{
                // Weekend
                if(mName.equals(homeLocationText) || mName.contains(homeLocationText)){

                    if( Integer.parseInt(mMeasureTimeInHour) <9 ){
                        hash_string =hash_string + String.format("#%s ", "이른 주말 아침");    // Early weekend moning
                    }else if ( Integer.parseInt(mMeasureTimeInHour) >= 9 && Integer.parseInt(mMeasureTimeInHour) <= 12){
                        hash_string =hash_string + String.format("#%s ", "한가로운 주말 아침");   // Cozy weekend mornign
                    }else if ( Integer.parseInt(mMeasureTimeInHour) > 18 && Integer.parseInt(mMeasureTimeInHour) < 24){
                        hash_string =hash_string + String.format("#%s ", "다시 집");   // Lazy morning
                    }

                }
                else
                {
                    // junyong - add "null checking"
                    if (mHomeLocation !=null && mCurrentLocation != null){
                        if( mHomeLocation.distanceTo(mCurrentLocation) <5000 ){
                            hash_string =hash_string + String.format("#%s ", "잠시 외출");   // Temporary out
                        }else if( mHomeLocation.distanceTo(mCurrentLocation) >= 5000 && mHomeLocation.distanceTo(mCurrentLocation) < 20000 ){
                            hash_string =hash_string + String.format("#%s ", "일상 탈출");      // Esapce from town
                        }else if( mHomeLocation.distanceTo(mCurrentLocation) > 20000 ){
                            hash_string =hash_string + String.format("#%s ", "오늘 외박 ~");     // Stay outside ~

                        }
                    }
                }
            }


            // 3. POI context based string.
            for (int i = 0; i < PhotoList.size(); i++){
                String timelinePhotoFile = PhotoList.get(i).toString();
                double temp[] = clusterEngine.timeFeatureExtract(timelinePhotoFile);
                avg_PhotoCreateTime=avg_PhotoCreateTime+temp[3];
            }
            avg_PhotoCreateTime=avg_PhotoCreateTime/PhotoList.size();
            hash_string=hash_string + dnnModel.getPOIhash(mName, dnnModel, avg_PhotoCreateTime);

            // 4. Face detectoion/ Smile score


//        hash_string = hash_string+hashFromFace(PhotoList, front_cam_width, rear_cam_width);
            hash_string = hash_string + dnnModel.hashFromFace(PhotoList,front_cam_width,rear_cam_width);



        }





        return String.format("%s", hash_string);

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


    public String hashFromFace(ArrayList PhotoList,  int front_cam_width, int rear_cam_width)
    {
        String hash_string = "";
        int photoCount = PhotoList.size();
        float Num_Face=0;
        float Smile_Prob=0;
        int selfie_cnt=0;
        int singlePhoto_cnt=0;
        int groupSelfie_cnt=0;
        int groupPhoto_cnt=0;
        int smile_cnt=0;
        EngineDBInterface engineDBInterface = new EngineDBInterface();

        if(photoCount > 0) {
            // Extract the name of the representing photo
            //String PhotoString = getPhotoString();
            // Extract timeline data



            //photoCount=1; // for debugging
            if(photoCount>3)
                photoCount=3;

            for (int i = 0; i < photoCount; i++){
                String timelinePhotoFile = PhotoList.get(i).toString();

                int Im_width=0;
                int Im_height=0;



                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(timelinePhotoFile, options);
                int imgHeight = options.outHeight;
                int imgWidth = options.outWidth;

                int sample_size =1;
                if(imgHeight > 1000 && imgWidth>1000)
                    sample_size =32;
                else
                    sample_size =1;

                BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
                bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap_options.inSampleSize = sample_size;
                Bitmap bMap_temp = BitmapFactory.decodeFile(timelinePhotoFile, bitmap_options);
                Im_width = bMap_temp.getWidth() * sample_size ;

                Num_Face =  engineDBInterface.getExtraFeatWithPhotoURL(timelinePhotoFile);
                Smile_Prob =  engineDBInterface.getWeightCoeffWithPhotoURL(timelinePhotoFile);

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

                }else {
                    //hash_string = hash_string + String.format("#%s ", "Oops");
                }

                if( Smile_Prob >= 0.6)
                    smile_cnt++;



                //4. Deep learning engine
                // It detects food, mountain, cliff, river, sea, seashore only
//                File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
//                String root = Environment.getExternalStorageDirectory().toString();
//                File myDir = new File(root + "/days_resample_images"); //
//                String days_moment_resample_image ;
//
//                days_moment_resample_image=resampleandsave_single(i, myDir, timelinePhotoFile);
//
////                double [] temp_time = clusterEngine.timeFeatureExtract(timelinePhotoFile);
//
//                // Run neural network
//                // This is for classification
//                String DNN_test_path_resample =days_moment_resample_image;
//                String[] jargv =new String[7];
//                jargv[0] ="classifier_Class";
//                jargv[1] ="predictCustom";  // This is for classification
//                jargv[2] =DNN_path[0];
//                jargv[3] =DNN_path[1];
//                jargv[4] =DNN_path[2];
//                jargv[5] =DNN_test_path_resample;
//                jargv[6] = outDir+"/";
//
//                String class_predict = DnnEngineClassJNI(jargv);
//                 System.gc();

            }

            // Selfie check
            if(selfie_cnt > 0){
                hash_string = hash_string + String.format("#%d %s ", selfie_cnt, "Selfie");
                hash_string = hash_string + String.format("#%s ", "So Handsome");
            }

            // Group photo, single photo check
            if(singlePhoto_cnt >0) {
                hash_string = hash_string + String.format("#%d %s ", singlePhoto_cnt, "Photos");
                hash_string = hash_string + String.format("#%s ", "Who is that nice guy in the picture ?");
            }

            if(groupPhoto_cnt > 0){
                hash_string = hash_string + String.format("#%d %s", groupPhoto_cnt, "Group photos");
                hash_string = hash_string + String.format("#%s ", "Best People !");
            }

            if(groupSelfie_cnt >0) {
                hash_string = hash_string + String.format("#%d %s ", selfie_cnt, "Group Selfie");
                hash_string = hash_string + String.format("#%s ", "Handsome guys");
            }

            // Smile detection
            if(smile_cnt ==1) {
                hash_string = hash_string + String.format("#%s ", "Beautifule Smile");
            }else if(smile_cnt >1) {
                hash_string = hash_string + String.format("#%s ", "Oh Happy Day ~");
            }


        } else {
//            hash_string = hash_string + String.format("#Nothing much ~");
        }


        return hash_string;
    }





    public String getDateFormat() {
        return AppUtils.getTime(mCreateDate);
    }


}
