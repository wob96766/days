package com.mindspree.days.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.engine.ClusterEngine;
import com.mindspree.days.engine.EngineDBInterface;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.Cluster_input;
import com.mindspree.days.model.FoursquareModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.RequestCode;
import com.mindspree.days.services.LocationLoggingService;
import com.mindspree.days.services.LocationServiceReciever;
import com.mindspree.days.view.LoadingImageDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

/**
 * Created by Admin on 21-10-2015.
 * <p>
 * This activity is creating the dynamic grid and dynamic view
 * for supporting the drag and drop funtion.
 */

public class MainActivity extends BaseActivity {

    final int MAX_TAB_NUM = 4;

    private FirebaseAuth mAuth;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View mViewToday;
    private View mViewSummarize;
    private View mViewJournal;
    private View mViewSetting;

    boolean asynctask_flag = false;
    boolean asynctask_again_flag = false;

    private ExecuteEngines mExecuteEngines;
    private ExecuteEnginesAgain mExecuteEnginesAgain;
    private LocationServiceReciever mLocationServiceReciever;

    private LoadingImageDialog mImageLoadingDialog;
    private boolean isShowLocation = false;
    private long backKeyPressedTime = 0;
    private DBWrapper mDBWrapper;
    private boolean mLockScreenLoaded = false;



    public int rear_cam_width ;
    public int front_cam_width ;


    public FaceDetector fdetector;
    // DNN related : Added by Mindspree
    public static String [] DNN_path=null;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
        setContentView(R.layout.activity_main);


        // Get camera information
        rear_cam_width=getCamerainfo(0);
        initDNN();
        initData();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            SearchActivity.startActivity(getContext());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showToast(getAppText(R.string.message_app_finish));
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mPreference.getLockOnOff() && mPreference.getLockScreen() == true){
            mLockScreenLoaded = true;
            LockpinActivity.startActivity(getContext());
        }
        if (!isLocationEnabled() && !isShowLocation) {
            isShowLocation = true;
            showLocationDialog(this);
        }

        asynctask_flag = false;
        asynctask_again_flag =false;

        ArrayList<TimelineModel> timelineList = mDBWrapper.getNonameTimelineList();
        for(int i=0 ; i<timelineList.size() ; i++) {
            TimelineModel item = timelineList.get(i);

            mHttpClient.RequestPOI(getContext(), item.getLatitude(), item.getLongitude(), getAppText(R.string.foursquare_key), item, mAsyncHttpResponse);
            if(i >= timelineList.size()-1){
                sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
            }
        }

        fdetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        rear_cam_width=getCamerainfo(0);

        mExecuteEnginesAgain = new ExecuteEnginesAgain();
        mExecuteEnginesAgain.execute();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mLocationServiceReciever != null)
            unregisterReceiver(mLocationServiceReciever);
        mPreference.setLockScreen(true);
    }

    @Override
    public  void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuth != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void initData() {
        mAuth = FirebaseAuth.getInstance();
        mDBWrapper = new DBWrapper(mPreference.getUserUid());

        fdetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();


        //getCamerainfo();


        if (!mPreference.getLaunchHistory().equals(mPreference.getUserUid())) {
            TutorialActivity.startActivity(getContext());
            mPreference.setLaunchHistory(mPreference.getUserUid());
            mExecuteEngines = new ExecuteEngines();
            mExecuteEngines.execute();
        }
        mLocationServiceReciever = new LocationServiceReciever();
        Intent intent = new Intent(MainActivity.this, LocationLoggingService.class);
        IntentFilter filter = new IntentFilter(AppConfig.AppService.LOCATIONLOGGING);
        registerReceiver(mLocationServiceReciever, filter);
        startService(intent);
    }

    private void copyAssets(String path, String outPath) {
        AssetManager assetManager = this.getAssets();
        String assets[];
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path, outPath);
            } else {
                String fullPath = outPath + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    if (!dir.mkdir()) Log.e("ERROR", "No create external directory: " + dir);

                for (String asset : assets) {
                    copyAssets(path + "/" + asset, outPath);
                }
            }
        } catch (IOException ex) {
            Log.e("ERROR", "I/O Exception", ex);
        }
    }
    private void copyFile(String filename, String outPath) {
        AssetManager assetManager = this.getAssets();

        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(filename);
            String newFileName = outPath + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    String[] load_DNN_resource(String DNN_dataset,String DNN_cfg,String DNN_weight) {

        File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        String DNN_dataset_path = outDir + "/cfg/" + DNN_dataset;
        String DNN_cfg_path = outDir + "/cfg/" + DNN_cfg;
        String DNN_weight_path = outDir + "/weight/" + DNN_weight;

        File file1 = new File(DNN_dataset_path);
        File file2 = new File(DNN_cfg_path);
        File file3 = new File(DNN_weight_path);

        if(file1.exists() && file2.exists() && file3.exists())
        {
            // Asset already copied. Do nothing.
        }
        else
        {
            copyAssets("data",outDir.toString());
            copyAssets("cfg",outDir.toString());
            copyAssets("weight",outDir.toString());

        }


//        String DNN_dataset_path = outDir + "/cfg/" + DNN_dataset;
//        String DNN_cfg_path = outDir + "/cfg/" + DNN_cfg;
//        String DNN_weight_path = outDir + "/weight/" + DNN_weight;

        String [] path_array = {DNN_dataset_path, DNN_cfg_path, DNN_weight_path};

        return path_array;
    }

    private void initDNN() {
        // For clasification
        String DNN_dataset = "imagenet1k.data";
        String DNN_cfg = "tiny.cfg";
        String DNN_weight = "tiny.weights";

//        String DNN_dataset = "cifar100.data";
//        String DNN_cfg = "cifar_custom100.cfg";
//        String DNN_weight = "cifar_custom100.weights";

        DNN_path = load_DNN_resource(DNN_dataset,DNN_cfg,DNN_weight);
    }

    private void initView() {
        AQuery aq = new AQuery(this);

        Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mViewToday = aq.id(R.id.view_today).clicked(mOnClickListener).getView();
        mViewSummarize = aq.id(R.id.view_summarize).clicked(mOnClickListener).getView();
        mViewJournal = aq.id(R.id.view_journal).clicked(mOnClickListener).getView();
        mViewSetting = aq.id(R.id.view_setting).clicked(mOnClickListener).getView();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) aq.id(R.id.container).getView();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(MAX_TAB_NUM);

        aq.dismiss();

    }

    public void setCurrentTab(int position) {

        switch (position) {
            case 0:
                mViewToday.setSelected(true);
                mViewSummarize.setSelected(false);
                mViewJournal.setSelected(false);
                mViewSetting.setSelected(false);
                mViewPager.setCurrentItem(position);
                break;
            case 1:
                mViewToday.setSelected(false);
                mViewSummarize.setSelected(true);
                mViewJournal.setSelected(false);
                mViewSetting.setSelected(false);
                mViewPager.setCurrentItem(position);
                break;
            case 2:
                mViewToday.setSelected(false);
                mViewSummarize.setSelected(false);
                mViewJournal.setSelected(true);
                mViewSetting.setSelected(false);
                mViewPager.setCurrentItem(position);
                break;
            case 3:
                mViewToday.setSelected(false);
                mViewSummarize.setSelected(false);
                mViewJournal.setSelected(false);
                mViewSetting.setSelected(true);
                mViewPager.setCurrentItem(position);
                //if(AppConfig.IS_BETA)
                    sqliteExport();
                break;
        }
    }




    public static void showLocationDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(AppUtils.getAppText(R.string.message_locationservice_title))
                .setMessage(AppUtils.getAppText(R.string.message_locationservice))
                .setPositiveButton(AppUtils.getAppText(R.string.text_setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton(AppUtils.getAppText(R.string.text_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.view_today:
                    setCurrentTab(0);
                    break;
                case R.id.view_summarize:
                    setCurrentTab(1);
                    break;
                case R.id.view_journal:
                    setCurrentTab(2);
                    break;
                case R.id.view_setting:
                    setCurrentTab(3);
                    break;
            }
        }
    };

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TodayFragment();
                case 1:
                    return new SummarizedFragment();
                case 2:
                    return new JournalFragment();
                case 3:
                    return new SettingFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return MAX_TAB_NUM;
        }
    }

    public int getCamerainfo(int rear_front) {

        // rear_front ==0   -> Rear cameare width
        // rear_front ==1   -> Front cameare width

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics characteristics =null;


        try {
            String [] camera_ID = manager.getCameraIdList();
            characteristics = manager.getCameraCharacteristics(camera_ID[0]);

            StreamConfigurationMap config =characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            android.util.Size[] temp1 =   config.getOutputSizes(32);
            rear_cam_width = temp1[0].getWidth();


            characteristics = manager.getCameraCharacteristics(camera_ID[1]);
            StreamConfigurationMap config2 =characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            android.util.Size[] temp2 =   config2.getOutputSizes(32);
            front_cam_width = temp2[0].getWidth();



            if(temp1[0].getWidth() > temp2[0].getWidth()) {
                rear_cam_width = temp1[0].getWidth();
                front_cam_width = temp2[0].getWidth();
            }
            else
            {
                rear_cam_width = temp2[0].getWidth();
                front_cam_width = temp1[0].getWidth();
            }
            //android.util.Size[] temp4 =   config2.getOutputSizes(4);

            writeToFile(String.valueOf(rear_cam_width), "rear_camera_setting.txt" , getContext());
            writeToFile(String.valueOf(front_cam_width), "front_camera_setting.txt" , getContext());

        } catch (CameraAccessException e) {
            e.printStackTrace();
            rear_cam_width =5000;
            front_cam_width=2500;
            writeToFile(String.valueOf(rear_cam_width), "rear_camera_setting.txt" , getContext());
            writeToFile(String.valueOf(front_cam_width), "front_camera_setting.txt" , getContext());

        }





        if(rear_front ==0) {

            return rear_cam_width;
        }
        else {

            return front_cam_width;
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


    /**
     * Method for get the all images of device.
     */
    private Cursor getMediaImages() {
        Cursor cursor = null;
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_MODIFIED};
        final String orderBy = MediaStore.Images.Media.DATE_MODIFIED;
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        return cursor;
    }

    /**
     * Method to get all images list
     * here we get the all images list in device.
     */
    private void insertAllImages(Cursor cursor) {
        //Create an array to store path to all the images
        String[] arrPath = new String[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            //Log.i("PATH", arrPath[i]);

            File file = new File(arrPath[i]);
            Date lastModified = new Date(file.lastModified());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
            String formattedDateString = formatter.format(lastModified);
            //Log.i("PATH modify_date", "PATH modify_date : " + formattedDateString);

            String filename = arrPath[i].substring(arrPath[i].lastIndexOf("/") + 1);
            //Log.i("PATH modify_date", "PATH modify_name : " + filename);

            int photo_size = (int) file.length();

            if (mDBWrapper.isAvailable(arrPath[i])) {
                //Log.i("PATH modify_date", "PATH already there OLD : " + arrPath[i]);
                File fileExists = new File(arrPath[i]);
                //Log.i("PATH modify_date", "PATHa already there file Exist : " + fileExists.exists());
                if (!fileExists.exists()) {
                    mDBWrapper.deleteOld(arrPath[i]);
                }
            } else {
                //Log.i("PATH modify_date", "PATH already there NEW : " + arrPath[i]);
                mDBWrapper.insertNew(arrPath[i], filename, formattedDateString, photo_size);
            }
        }
    }

    private void showImageLoadingDialog() {
        if (mImageLoadingDialog != null){
            mImageLoadingDialog.dismiss();
        }else {
            mImageLoadingDialog = new LoadingImageDialog(this, R.style.TransparentDialog);
        }
        mImageLoadingDialog.show();
    }

    private void dismissImageLoadingDialog()
    {
        if(mImageLoadingDialog != null)
            mImageLoadingDialog.dismiss();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }



    // Async Task Class
    public class ExecuteEngines extends AsyncTask<String, String, String> {

        EngineDBInterface engineDBInterface = null;
        private int numberOfAllPhotos = 0;
        private int numberOfClusteredPhotos = 0;
        int progress_cnt=0;
        int index_scroll =0;



        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showImageLoadingDialog();
            //m_gridviewAdapter.updateList(imagesList);

            //4. add adapter to gridview
            // Shows Progress Bar Dialog and then call doInBackground method
            engineDBInterface = new EngineDBInterface();
            numberOfAllPhotos = engineDBInterface.getNumberOfAllPhotos();
            asynctask_flag = true;
        }


        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            Cursor cursor = getMediaImages();
            insertAllImages(cursor);

            try {

                // Return file list that needs to be clustered.
                EngineDBInterface engineDBInterface= new EngineDBInterface();
                ArrayList fileArrayList = engineDBInterface.getPhotoListWithoutClusterId();
                Cluster_input cluster_input = new Cluster_input();
                cluster_input.createVar(fileArrayList);
                ClusterEngine myM2 = new ClusterEngine();
                myM2.clusteringTest6Initialize();
                int cluster=0;
                double c=1;

                int cnt =0;
                int cluster_buf=0;

                for (int i=0; i < fileArrayList.size(); i++) {

                    cnt ++;

                    cluster = ClusterEngine.runClusterEngine(this, null, engineDBInterface, fileArrayList, cluster_input, c, myM2, i, fdetector);
                    Integer Cnt_class= new Integer (cnt);
                    Integer Cluster_class = new Integer(cluster);

                    if (cluster != cluster_buf && (cluster % 3 == 1))
                        publishProgress(Cluster_class.toString());


                    cluster_buf = cluster;

                }


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

                if(progress_cnt==0)
                    //gvfirst.setVisibility(View.GONE);

                progress_cnt++;

                // Update grid item and sort
                //gridItems = getClusteredImagesDataList();

                //AddDataInGrid(m_gridviewAdapter);
                //index_scroll  = gv.getFirstVisiblePosition();
                //displayDataInGrid();



            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        @Override
        protected void onPostExecute(String file_url) {
            //hideProgressDialog();
            dismissImageLoadingDialog();
            asynctask_flag =false;
        }

        public void updatedProgress(){
            numberOfClusteredPhotos = engineDBInterface.getNumberOfClusteredPhotos();
            float progress = (float)( (float) numberOfClusteredPhotos / (float) numberOfAllPhotos * 100);
            //progressBar.setProgress((int) progress);
        }
    }

    // Async Task Class
    public class ExecuteEnginesAgain extends AsyncTask<String, String, String> {

        private EngineDBInterface engineDBInterface = null;
        private int numberOfAllPhotos = 0;
        private int numberOfClusteredPhotos = 0;

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showImageLoadingDialog();
            engineDBInterface = new EngineDBInterface();
            ArrayList photoArrayList = engineDBInterface.getPhotoListWithoutClusterId() ;
            //if(photoArrayList.size() > 0 )
            //    showProgressDialog();
            numberOfAllPhotos = engineDBInterface.getNumberOfAllPhotos();
            asynctask_again_flag =true;
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            Cursor cursor = getMediaImages();
            insertAllImages(cursor);

            try {

                // Return file list that needs to be clustered.
                EngineDBInterface engineDBInterface= new EngineDBInterface();
                ArrayList fileArrayList = engineDBInterface.getPhotoListWithoutClusterId();

                Cluster_input cluster_input =new Cluster_input();
                cluster_input.createVar(fileArrayList);
                ClusterEngine myM2 = new ClusterEngine();
                myM2.clusteringTest6Initialize();
                int cluster=0;
                double c=1;

                int cnt =0;
                int cluster_buf=0;

                for (int i=0; i < fileArrayList.size(); i++) {

                    cnt ++;
                    cluster = ClusterEngine.runClusterEngine(null, this, engineDBInterface, fileArrayList, cluster_input, c, myM2, i, fdetector);
                    Integer Cnt_class= new Integer (cnt);
                    Integer Cluster_class = new Integer(cluster);

                    if(cluster != cluster_buf && (cluster%3 ==1)) {
                        publishProgress(Cluster_class.toString());
                    }


                    cluster_buf = cluster;

                }
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

                // Update Grid item and Sort

                //displayDataInGrid();


            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        @Override
        protected void onPostExecute(String file_url) {
            dismissImageLoadingDialog();
            //hideProgressDialog();
            //gvfirst.setVisibility(View.GONE);

            //gridItems = getAllImagesDataList();
            //displayDataInGridAgain();


            //hideProgressDialog();
            asynctask_again_flag =false;
            sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
        }

        public void updatedProgress(){
            numberOfClusteredPhotos = engineDBInterface.getNumberOfClusteredPhotos();
            float progress = (float)( (float) numberOfClusteredPhotos / (float) numberOfAllPhotos * 100);
            //progressBar.setProgress((int)progress);
        }
    }

    public void sqliteExport(){
        try {
            File sdDir = Environment.getExternalStorageDirectory();
            File dataDir = Environment.getDataDirectory();

            if(sdDir.canWrite()){
                //String crDBPath = "/data/data/com.mindspree.picsnpics/databases/PicnpicsDB.db";
                String crDBPath = "//data//com.mindspree.days//databases//PicnpicsDB.db";
                String bkDBPath = "PicnpicsDB.sqlite";

                File crDB = new File(dataDir, crDBPath);
                File bkDB = new File(sdDir, bkDBPath);

                if(crDB.exists()){
                    FileChannel src = new FileInputStream(crDB).getChannel();
                    FileChannel dst = new FileOutputStream(bkDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

                /*if(bkDB.exists()){
                    Toast.makeText(this, "DB file Export Success!", Toast.LENGTH_SHORT).show();
                }*/

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {

            } else {
                // User is signed out
                finish();
                SplashActivity.startActivity(getContext());
            }
        }
    };

    // 서버 응답 처리
    AsyncHttpResponse mAsyncHttpResponse = new AsyncHttpResponse()
    {
        @Override
        public void onHttpStart(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_POI:
                    break;
            }
        }

        @Override
        public void onHttpFinish(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_POI:
                    break;
            }
        }

        @Override
        public void onHttpSuccess(int reqCode, int code, Header[] headers, byte[] response, Object tag) {
            String data = new String(response);
            try {
                switch(reqCode) {
                    case RequestCode.GET_POI:
                        try {
                            if(tag instanceof TimelineModel) {
                                FoursquareModel model = FoursquareModel.parseData(data);
                                TimelineModel timeline = (TimelineModel)tag;
                                mDBWrapper.setLocation(timeline.mLocationId, model.mName);
                            }
                        } catch (Exception e) {
                            //showToast(getString(R.string.message_network_error));
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onHttpFailure(int reqCode, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            try {
                switch(reqCode)
                {
                    case RequestCode.GET_POI:
                        //showToast(getString(R.string.message_network_error));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };


}
