package com.mindspree.days;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mindspree.days.data.DBHelper;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Admin on 21-10-2015.
 */
public class AppApplication extends Application   {

    public final String GA_UID = "UA-00000000-1";

    private static AppApplication context;
    public static int ColumnNumber = 3;
    private static DBHelper dbHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_NAME = "PassengerApp";

    private Tracker mTracker;

    public String networkFile = null;
    public String svmFile1 = null;
    public String svmFile2 = null;
    public String svmFile3 = null;
    public String svmFile4 = null;
    public String TEST = null;

    public String SoloFile = null;
    public String CoupleFile = null;
    public String GroupFile = null;
    public String DocumentsFile = null;

    private static final String TAG = "GooglyEyes";

    private static final int RC_HANDLE_GMS = 9001;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    public static DBHelper getDbHelper() {
        if(dbHelper ==  null) {
            dbHelper = new DBHelper(AppApplication.getAppInstance());
            try {
                dbHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbHelper.openDataBase();
        }
        return dbHelper;
    }


    public static AppApplication getAppInstance() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        //
        FacebookSdk.sdkInitialize(getApplicationContext());
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //initialize();
        //initImageLoader(getApplicationContext());
        //initDeepBelief();

        //        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //
        //        if (rc == PackageManager.PERMISSION_GRANTED) {
        //        }
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(GA_UID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(GA_UID)
                    : analytics.newTracker(GA_UID);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    /*
        private CameraSource mCameraSource = null;
        private CameraSourcePreview mPreview;
        private GraphicOverlay mGraphicOverlay;
        private boolean mIsFrontFacing = true;


        public static DBHelper getDbHelper() {
            return dbHelper;
        }






        @Override
        public void onTerminate() {
            super.onTerminate();
            if (dbHelper != null) {
                dbHelper.close();
            }
        }

        public void initialize() {
            AdobeAuthManager manager = AdobeAuthManager.sharedAuthManager();
            manager.initWithApplicationContext(PicnpicsApplication.this);
            try {
                manager.setAuthenticationParameters("225aa5f99c484b4fbc374e5ff9d71d31", "7530790f-5450-4101-9e44-4f94309b1bbe", null);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }



        //Initiate Image Loader Configuration
        public static void initImageLoader(Context context) {
            ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                    context);
            config.threadPriority(Thread.NORM_PRIORITY - 2);
            config.denyCacheImageMultipleSizesInMemory();
            config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
            config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
            config.tasksProcessingOrder(QueueProcessingType.LIFO);
            config.writeDebugLogs(); // Remove for release app

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config.build());

        }



        void initDeepBelief() {

            AssetManager am = ctx.getAssets();
            String svmFilename4 = "svmDocuments.txt";

            String Solo_1 = "Solo_1.jpg";
            String Couple_1 = "Couple_1.jpg";
            String Group_1 = "Group_1.jpg";
            String Documents_1 = "Documents_1.jpg";

            String dataDir = ctx.getFilesDir().getAbsolutePath();

            svmFile4 = dataDir + "/" + svmFilename4;

            SoloFile = dataDir + "/" + Solo_1;
            CoupleFile = dataDir + "/" + Couple_1;
            GroupFile = dataDir + "/" + Group_1;
            DocumentsFile = dataDir + "/" + Documents_1;

            copyAsset(am, svmFilename4, svmFile4);

            copyAsset(am, Solo_1, SoloFile);
            copyAsset(am, Couple_1, CoupleFile);
            copyAsset(am, Group_1, GroupFile);
            copyAsset(am, Documents_1, DocumentsFile);


        }



        private static boolean copyAsset(AssetManager assetManager,
                                         String fromAssetPath, String toPath) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(fromAssetPath);
                new File(toPath).createNewFile();
                out = new FileOutputStream(toPath);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private static void copyFile(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
        }



        @Override
        public String getBillingKey() {
            return ""; // leave it blank
        }

        @Override
        public String getClientID() {
            return "82d892978b3e403a93d41c4a2bc1c91d";
        }

        @Override
        public String getClientSecret() {
            return "4d665945-f7c3-4fd1-a197-8ed0ec5c7f94";
        }

    */
    public boolean getPrefFlag(String preferenceKey) {
        return sharedPreferences.getBoolean(preferenceKey, false);
    }

    public void setPrefFlag(String preferenceKey, boolean PassengerId) {
        editor.putBoolean(preferenceKey, PassengerId);
        editor.commit();
    }

    public int getPrefRatio(String preferenceKey) {
        return sharedPreferences.getInt(preferenceKey, 0);
    }

    public void setPrefRatio(String preferenceKey, int preIntValue) {
        editor.putInt(preferenceKey, preIntValue);
        editor.commit();
    }





    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            //mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
