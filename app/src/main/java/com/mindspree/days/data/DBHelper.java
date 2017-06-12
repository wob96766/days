package com.mindspree.days.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.model.Daily;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.DatelineModel;
import com.mindspree.days.model.Location;
import com.mindspree.days.model.Photo;
import com.mindspree.days.model.PhotoInfoModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.PhotosGridModel;
import com.mindspree.days.model.SentenceModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 22-08-2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    private final int VERSION = 2;
    private Context context;
    private SQLiteDatabase database;
    private String DATABASE_PATH = "";
    private String DB_NAME;


    private final String TABLE_PHOTOS = "PHOTOS";
    private final String TABLE_LOCATIONS = "LOCATIONS";
    private final String TABLE_DAILY = "DAILY";

    private String COLUMN_FILE_INDEX = "file_index";
    private String COLUMN_FILE_LOCATION = "file_location";
    private String COLUMN_FILE_URL = "file_location_url";
    private String COLUMN_FILE_NAME = "file_name";
    private String COLUMN_CLUSER_ID = "cluster_id";
    private String COLUMN_IS_BEST = "is_best";
    private String COLUMN_QUALITY_RANK = "quality_rank";
    private String COLUMN_BLURRING_INDEX = "blurring_index";
    private String COLUMN_PSNR_INDEX = "psnr_index";
    private String COLUMN_FACE_AREA = "face_area";
    private String COLUMN_FACE_DISTANCE = "face_distance";
    private String COLUMN_EXTRA_FEAT = "extra_feat";  // We will use this for the classification
    private String COLUMN_WEIGHT_COEFF = "weight_coeff";
    private String COLUMN_FINAL_SCORE = "final_score";
    private String COLUMN_DELETE_CHECK = "delete_check";
    private String COLUMN_UPDATE_DATE = "update_date";

    private String COLUMN_PHOTO_SIZE = "photo_size";
    private String COLUMN_SORTSEQ = "sortseq";

    // 2016-12-15 added column names
    private String COLUMN_USER_ID = "user_id";
    private String COLUMN_NAME = "name";
    private String COLUMN_LATITUDE = "latitude";
    private String COLUMN_LONGITUDE = "longitude";
    private String COLUMN_MEASURELATITUDE = "measure_latitude";
    private String COLUMN_MEASURELONGITUDE = "measure_longitude";
    private String COLUMN_MEASURE_DATE = "measure_date";
    private String COLUMN_DISTANCE = "distance";
    private String COLUMN_LOCATION_INDEX = "location_index";
    private String COLUMN_CREATE_DATE = "create_date";
    private String COLUMN_CREATED = "created";
    private String COLUMN_CATEGORY = "category";
    private String COLUMN_FLAG = "flag";

    private String COLUMN_DAILY_INDEX = "daily_index";
    private String COLUMN_MOOD = "mood";
    private String COLUMN_LOCK = "lock";

    private String COLUMN_SENTENCE = "sentence";
    private String COLUMN_WEATHER = "weather";
    private String COLUMN_TEMP_MINIMUM = "mintemp";
    private String COLUMN_TEMP_MAXIMUM = "maxtemp";


    @SuppressLint("SdCardPath")
    public DBHelper(Context context) {
        super(context, context.getResources().getString(R.string.db_name), null, 2);
        this.context = context;
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        DB_NAME = context.getResources().getString(R.string.db_name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PHOTO_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + COLUMN_FILE_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FILE_URL + " TEXT,"
                + COLUMN_FILE_LOCATION + " TEXT NOT NULL,"
                + COLUMN_FILE_NAME + " TEXT NOT NULL,"
                + COLUMN_CLUSER_ID + " INTEGER,"
                + COLUMN_IS_BEST + " INTEGER,"
                + COLUMN_QUALITY_RANK + " FLOAT,"
                + COLUMN_BLURRING_INDEX + " FLOAT,"
                + COLUMN_PSNR_INDEX + " FLOAT,"
                + COLUMN_FACE_AREA + " FLOAT,"
                + COLUMN_FACE_DISTANCE + " FLOAT,"
                + COLUMN_EXTRA_FEAT + " FLOAT,"
                + COLUMN_WEIGHT_COEFF + " FLOAT,"
                + COLUMN_FINAL_SCORE + " FLOAT,"
                + COLUMN_DELETE_CHECK + " INTEGER,"
                + COLUMN_UPDATE_DATE + " TEXT,"
                + COLUMN_PHOTO_SIZE + " TEXT,"
                + COLUMN_FLAG + " INT DEFAULT 1,"
                + COLUMN_SORTSEQ + " INTEGER DEFAULT 100,"
                + COLUMN_USER_ID + " TEXT DEFAULT '1'" + ")";
        db.execSQL(CREATE_PHOTO_TABLE);

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + COLUMN_LOCATION_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " TEXT DEFAULT '1',"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_LATITUDE + " DOUBLE,"
                + COLUMN_LONGITUDE + " DOUBLE,"
                + COLUMN_MEASURELATITUDE + " DOUBLE,"
                + COLUMN_MEASURELONGITUDE+ " DOUBLE,"
                + COLUMN_DISTANCE + " FLOAT,"
                + COLUMN_LOCK + " INT,"
                + COLUMN_CATEGORY  + " TEXT,"
                + COLUMN_MEASURE_DATE + " TEXT,"
                + COLUMN_CREATE_DATE + " TEXT,"
                + COLUMN_UPDATE_DATE + " TEXT)";
        db.execSQL(CREATE_LOCATION_TABLE);

        String CREATE_DAILY_TABLE = "CREATE TABLE " + TABLE_DAILY + "("
                + COLUMN_DAILY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " TEXT DEFAULT '1',"
                + COLUMN_CREATE_DATE + " TEXT,"
                + COLUMN_WEATHER + " TEXT,"
                + COLUMN_SENTENCE + " TEXT,"
                + COLUMN_TEMP_MINIMUM + " DOUBLE,"
                + COLUMN_TEMP_MAXIMUM + " DOUBLE,"
                + COLUMN_MOOD + " TEXT)";
        db.execSQL(CREATE_DAILY_TABLE);
        // insert sample location data
        if(AppConfig.IS_BETA == true) {
            insertTest(db);
        }

    }

    private void insertTest(SQLiteDatabase db) {
        String user_uid = AppPreference.getInstance().getUserUid();
        /*final ContentValues values = new ContentValues();


        values.put(COLUMN_USER_ID, user_uid);
        values.put(COLUMN_LATITUDE, 37.5618288);
        values.put(COLUMN_LONGITUDE, 126.7997334);
        values.put(COLUMN_MEASURELATITUDE, 37.5618288);
        values.put(COLUMN_MEASURELONGITUDE, 126.7997334);
        values.put(COLUMN_CREATE_DATE, "2017-01-24 09:00:00.000");
        values.put(COLUMN_UPDATE_DATE, "2017-01-24 23:59:59.000");

        db.insert(TABLE_LOCATIONS, null, values);
*/
        final ContentValues values3 = new ContentValues();

        values3.put(COLUMN_USER_ID, user_uid);
        values3.put(COLUMN_LATITUDE, 37.5618288);
        values3.put(COLUMN_LONGITUDE, 126.7997334);
        values3.put(COLUMN_MEASURELATITUDE, 37.5618288);
        values3.put(COLUMN_MEASURELONGITUDE, 126.7997334);
        values3.put(COLUMN_CREATE_DATE, "2017-01-23 23:00:00.000");
        values3.put(COLUMN_UPDATE_DATE, "2017-01-23 23:59:59.000");
        db.insert(TABLE_LOCATIONS, null, values3);

       /* final ContentValues values2 = new ContentValues();

        values2.put(COLUMN_USER_ID, user_uid);
        values2.put(COLUMN_LATITUDE, 37.5618288);
        values2.put(COLUMN_LONGITUDE, 126.7997334);
        values2.put(COLUMN_MEASURELATITUDE, 37.5618288);
        values2.put(COLUMN_MEASURELONGITUDE, 126.7997334);
        values2.put(COLUMN_CREATE_DATE, "2017-01-25 09:00:00.000");

        db.insert(TABLE_LOCATIONS, null, values2);
        */
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1 :
                try {
                    db.beginTransaction();
                    db.execSQL("ALTER TABLE " + TABLE_LOCATIONS + " ADD COLUMN " + COLUMN_CATEGORY + " TEXT DEFAULT ''");
                    db.execSQL("ALTER TABLE " + TABLE_PHOTOS + " ADD COLUMN " + COLUMN_FLAG + " INT DEFAULT 1");
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                };
                break;
        }
    }

    /**
     * This method will create database in application package /databases
     * directory when first time application launched
     */
    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
//            try {
//                copyDataBase();
//            } catch (IOException mIOException) {
//                mIOException.printStackTrace();
//                throw new Error("Error copying database");
//            } finally {
//                this.close();
//            }
        }
    }

    /**
     * This method checks whether database is exists or not *
     */
    private boolean checkDataBase() {
        try {
            final String mPath = DATABASE_PATH + DB_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method will copy database from /assets directory to application
     * package /databases directory
     */
    private void copyDataBase() throws IOException {
        try {
            InputStream mInputStream = context.getAssets().open(DB_NAME);
            String outFileName = DATABASE_PATH + DB_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method open database for operations *
     */
    public void openDataBase() throws SQLException {
        String mPath = DATABASE_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * This method close database connection and released occupied memory *
     */
    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    public void clearAllDatabase() {
        database.delete(TABLE_PHOTOS, null, null);
    }

    public void clearDatabase() {
        database.delete(TABLE_PHOTOS, null, null);
        database.delete(TABLE_LOCATIONS, null, null);
        database.delete(TABLE_DAILY, null, null);
    }



    public long insertNew(final String userUid, final String fileLocation, final String fileName,
                          final String fileUpdateDate, final int photo_size) {
        final ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, userUid);
        values.put(COLUMN_FILE_LOCATION, fileLocation);
        values.put(COLUMN_FILE_NAME, fileName);
        values.put(COLUMN_UPDATE_DATE, fileUpdateDate);
        values.put(COLUMN_PHOTO_SIZE, photo_size);
        values.put(COLUMN_FLAG, 0);

        return database.insert(TABLE_PHOTOS, null, values);
    }

    public int getNewPhotoCount(final String user_uid) {
        int photoCount = 0;

        Cursor cursor = database.query(TABLE_PHOTOS, null, String.format(" %s = 0 and %s = ? ", COLUMN_FLAG, COLUMN_USER_ID), new String[]{user_uid}, null, null, COLUMN_UPDATE_DATE+ " DESC", null);
        try {
            photoCount = cursor.getCount();
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoCount;
    }

    public int updateNewPhotoFlag(final String userUid) {

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FLAG, 1);
        return database.update(TABLE_PHOTOS, cv,  String.format(" %s = 0 and %s = ? ", COLUMN_FLAG, COLUMN_USER_ID), new String[]{userUid});
    }

    public long updateLocation(final String user_uid, final double latitude, final double longitude) {
        int locationIndex = 0;

        Cursor cursor = database.query(TABLE_LOCATIONS, null, String.format(" %s is null and  %s is null and %s = ? ", COLUMN_UPDATE_DATE, COLUMN_LOCK, COLUMN_USER_ID), new String[]{user_uid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    locationIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    ContentValues cv = new ContentValues();
                    cv.putNull(COLUMN_NAME);
                    cv.put(COLUMN_LATITUDE, latitude);
                    cv.put(COLUMN_LONGITUDE, longitude);
                    database.update(TABLE_LOCATIONS, cv, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationIndex)});
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationIndex;
    }

    public long updateMeasureLocation(final String user_uid, final double latitude, final double longitude) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        int locationIndex = 0;

        Cursor cursor = database.query(TABLE_LOCATIONS, null,  String.format(" %s is null and %s = ? ", COLUMN_UPDATE_DATE, COLUMN_USER_ID), new String[]{user_uid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    locationIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    ContentValues cv = new ContentValues();
                    //cv.putNull(COLUMN_NAME);
                    //cv.put(COLUMN_LATITUDE, latitude);
                    //cv.put(COLUMN_LONGITUDE, longitude);
                    cv.put(COLUMN_MEASURELATITUDE, latitude);
                    cv.put(COLUMN_MEASURELONGITUDE, longitude);
                    cv.put(COLUMN_MEASURE_DATE, dateFormat.format(date));
                    database.update(TABLE_LOCATIONS, cv, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationIndex)});
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationIndex;
    }

    public long insertLocation(final String user_uid, final double latitude, final double longitude) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();

        Cursor cursor = database.query(TABLE_LOCATIONS, null, null, null, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int locationIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_UPDATE_DATE, dateFormat.format(date));
                    database.update(TABLE_LOCATIONS, cv, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationIndex)});
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        final ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, user_uid);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_MEASURELATITUDE, latitude);
        values.put(COLUMN_MEASURELONGITUDE, longitude);
        values.put(COLUMN_CREATE_DATE, dateFormat.format(date));
        values.put(COLUMN_MEASURE_DATE, dateFormat.format(date));

        return database.insert(TABLE_LOCATIONS, null, values);
    }

    public long insertLocationAminute(final String user_uid, final double latitude, final double longitude) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        // 2 minutes before
        cal.add(Calendar.MINUTE, -1);


        Cursor cursor = database.query(TABLE_LOCATIONS, null, null, null, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int locationIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_UPDATE_DATE, dateFormat.format(cal.getTime()));
                    database.update(TABLE_LOCATIONS, cv, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationIndex)});
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        final ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, user_uid);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_MEASURELATITUDE, latitude);
        values.put(COLUMN_MEASURELONGITUDE, longitude);
        values.put(COLUMN_CREATE_DATE, dateFormat.format(cal.getTime()));
        values.put(COLUMN_MEASURE_DATE, dateFormat.format(cal.getTime()));

        return database.insert(TABLE_LOCATIONS, null, values);
    }

    public void setMood(final String userUid, final String mood) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        Cursor cursor = database.query(TABLE_DAILY, null,  String.format("date(%s) = date('now', 'localtime') and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        int dailyIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_DAILY_INDEX));
                        ContentValues cv = new ContentValues();
                        cv.put(COLUMN_MOOD, mood);
                        database.update(TABLE_DAILY, cv, COLUMN_DAILY_INDEX + " =? ", new String[]{String.format("%d", dailyIndex)});
                        break;
                    } while (cursor.moveToNext());
                }

            } else {
                final ContentValues values = new ContentValues();

                values.put(COLUMN_USER_ID, userUid);
                values.put(COLUMN_MOOD, mood);
                values.put(COLUMN_CREATE_DATE, dateFormat.format(date));

                database.insert(TABLE_DAILY, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWeather(final String userUid, final String weather) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        Cursor cursor = database.query(TABLE_DAILY, null,  String.format("date(%s) = date('now') and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        int dailyIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_DAILY_INDEX));
                        ContentValues cv = new ContentValues();
                        cv.put(COLUMN_WEATHER, weather);
                        database.update(TABLE_DAILY, cv, COLUMN_DAILY_INDEX + " =? ", new String[]{String.format("%d", dailyIndex)});
                        break;
                    } while (cursor.moveToNext());
                }

            } else {
                final ContentValues values = new ContentValues();

                values.put(COLUMN_USER_ID, userUid);
                values.put(COLUMN_WEATHER, weather);
                values.put(COLUMN_CREATE_DATE, dateFormat.format(date));

                database.insert(TABLE_DAILY, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWeatherToday(final String userUid) {
        String weather = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        Cursor cursor = database.query(TABLE_DAILY, null,  String.format("date(%s) = date('now') and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        weather = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER));

                        break;
                    } while (cursor.moveToNext());
                }

            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weather;
    }

    public void setSentence(final String userUid, final String sentence) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        Cursor cursor = database.query(TABLE_DAILY, null,  String.format("date(%s) = date('now', 'localtime') and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        int dailyIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_DAILY_INDEX));
                        ContentValues cv = new ContentValues();
                        cv.put(COLUMN_SENTENCE, sentence);
                        database.update(TABLE_DAILY, cv, COLUMN_DAILY_INDEX + " =? ", new String[]{String.format("%d", dailyIndex)});
                        break;
                    } while (cursor.moveToNext());
                }

            } else {
                final ContentValues values = new ContentValues();

                values.put(COLUMN_USER_ID, userUid);
                values.put(COLUMN_SENTENCE, sentence);
                values.put(COLUMN_CREATE_DATE, dateFormat.format(date));

                database.insert(TABLE_DAILY, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSentence(final String userUid, final String dateString, final String sentence) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        Cursor cursor = database.query(TABLE_DAILY, null,  String.format("date(%s) = date(?) and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{dateString, userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        int dailyIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_DAILY_INDEX));

                        ContentValues cv = new ContentValues();
                        cv.put(COLUMN_SENTENCE, sentence);
                        database.update(TABLE_DAILY, cv, COLUMN_DAILY_INDEX + " =? ", new String[]{String.format("%d", dailyIndex)});
                        //break;
                    } while (cursor.moveToNext());
                }

            } else {
                final ContentValues values = new ContentValues();

                values.put(COLUMN_USER_ID, userUid);
                values.put(COLUMN_SENTENCE, sentence);
                values.put(COLUMN_CREATE_DATE, dateString + " 00:00:00.000");

                database.insert(TABLE_DAILY, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageSequence(final String user_uid, final String dateString, final String filename, final int sortseq) {

        try {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_SORTSEQ, sortseq);
                database.update(TABLE_PHOTOS, cv, String.format("%s =? ", COLUMN_FILE_LOCATION), new String[]{String.format(filename)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocation(String userUid, int locationId, String title) {
        final ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, title);

        database.update(TABLE_LOCATIONS, values, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationId)});
    }

    public void setLocation(String userUid, int locationId, String title, String category) {
        final ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, title);
        values.put(COLUMN_CATEGORY, category);

        database.update(TABLE_LOCATIONS, values, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationId)});
    }


    public void setLocationLog(String userUid, int islock, int locationId) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_LOCK, islock);
        values.put(COLUMN_MEASURE_DATE, dateFormat.format(date));

        database.update(TABLE_LOCATIONS, values, COLUMN_LOCATION_INDEX + " =? and user_id =? ", new String[]{String.format("%d", locationId), userUid});
    }

    public void setLocationLog(int locationId, String userUid, String name, double latitude, double longitude) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MEASURELATITUDE, latitude);
        values.put(COLUMN_MEASURELONGITUDE, longitude);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_LOCK, 1);
        values.put(COLUMN_MEASURE_DATE, dateFormat.format(date));

        database.update(TABLE_LOCATIONS, values, COLUMN_LOCATION_INDEX + " =? and user_id =? ", new String[]{String.format("%d", locationId), userUid});
    }

    public void setLocation(String userUid, int locationId, String title, double latitude, double longitude) {
        final ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, userUid);
        values.put(COLUMN_LOCATION_INDEX, locationId);
        values.put(COLUMN_NAME, title);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_LOCK, 1);

        database.update(TABLE_LOCATIONS, values, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationId)});
    }

    public long insertAviary(final String fileLocation, final String fileName,
                             final String fileUpdateDate, int clusterId, int photo_size) {
        final ContentValues values = new ContentValues();

        values.put(COLUMN_CLUSER_ID, clusterId);
        values.put(COLUMN_FILE_LOCATION, fileLocation);
        values.put(COLUMN_FILE_NAME, fileName);
        values.put(COLUMN_UPDATE_DATE, fileUpdateDate);
        values.put(COLUMN_PHOTO_SIZE, photo_size);

        return database.insert(TABLE_PHOTOS, null, values);
    }

    public int deleteOld(String userUid, String filePath) {
        return database.delete(TABLE_PHOTOS, String.format(" %s =? and %s =?", COLUMN_FILE_LOCATION, COLUMN_USER_ID), new String[]{filePath, userUid});
    }

    public long updateClusterId(final String sourcefileClusterId, final String destinationfileClusterId) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CLUSER_ID, sourcefileClusterId);
        cv.put(COLUMN_IS_BEST, 0);
        return database.update(TABLE_PHOTOS, cv, COLUMN_CLUSER_ID + " =? ", new String[]{destinationfileClusterId});
    }

    public boolean isAvailable(String userUid, String photoPath) {
        boolean isAvailable = false;
        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_FILE_LOCATION + "=?", new String[]{photoPath}, null, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                isAvailable = true;
            } else {
                isAvailable = false;
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAvailable;
    }

//    * Query the given table, returning a {@link Cursor} over the result set.
//            *
//            * @param table The table name to compile the query against.
//    * @param columns A list of which columns to return. Passing null will
//    *            return all columns, which is discouraged to prevent reading
//    *            data from storage that isn't going to be used.
//            * @param selection A filter declaring which rows to return, formatted as an
//    *            SQL WHERE clause (excluding the WHERE itself). Passing null
//            *            will return all rows for the given table.
//    * @param selectionArgs You may include ?s in selection, which will be
//    *         replaced by the values from selectionArgs, in order that they
//    *         appear in the selection. The values will be bound as Strings.
//    * @param groupBy A filter declaring how to group rows, formatted as an SQL
//    *            GROUP BY clause (excluding the GROUP BY itself). Passing null
//            *            will cause the rows to not be grouped.
//            * @param having A filter declare which row groups to include in the cursor,
//    *            if row grouping is being used, formatted as an SQL HAVING
//    *            clause (excluding the HAVING itself). Passing null will cause
//    *            all row groups to be included, and is required when row
//    *            grouping is not being used.
//    * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
//    *            (excluding the ORDER BY itself). Passing null will use the
//    *            default sort order, which may be unordered.
//    * @param limit Limits the number of rows returned by the query,
//    *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
//            * @return A {@link Cursor} object, which is positioned before the first entry. Note that
//    * {@link Cursor}s are not synchronized, see the documentation for more details.
//            * @see Cursor
//    */
//    public Cursor query(String table, String[] columns, String selection,
//                        String[] selectionArgs, String groupBy, String having,
//                        String orderBy, String limit) {



    public ArrayList<PhotosGridModel> getClusteredImages(String userUid) {
        ArrayList<PhotosGridModel> items = new ArrayList<PhotosGridModel>();
        final Cursor cursor = database.query(TABLE_PHOTOS, null, "cluster_id is not null", null, null, null, COLUMN_UPDATE_DATE + " ASC", null);

        try {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                        float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                        //Log.i("DB HELPER", "DATA FROM THE DB HELPER COLUMN_CLUSER_ID : " + ClusterId);
                        items.add(new PhotosGridModel(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)), false, cursor.getString(cursor.getColumnIndex(COLUMN_FILE_NAME)), null, "0", ClusterId, QualityRank, cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX)), cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE)), cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST))));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }


    public ArrayList<PhotosGridModel> getAllImages() {
        ArrayList<PhotosGridModel> items = new ArrayList<PhotosGridModel>();
        final Cursor cursor = database.query(TABLE_PHOTOS, null, null, null, null, null, COLUMN_UPDATE_DATE + " DESC", null);
        try {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                        float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                        //Log.i("DB HELPER", "DATA FROM THE DB HELPER COLUMN_CLUSER_ID : " + ClusterId);
                        items.add(new PhotosGridModel(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)), false, cursor.getString(cursor.getColumnIndex(COLUMN_FILE_NAME)), null, "0", ClusterId, QualityRank, cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX)), cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE)), cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST))));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public SentenceModel getSentence(String userUid) {
        SentenceModel model =  new SentenceModel();
        try {
            Cursor cursor = database.rawQuery(
                    "select (select mood from DAILY where date(create_date) = date('now', 'localtime') and user_id = '" + userUid + "') as mood," +
                            "(select sentence from DAILY where date(create_date) = date('now', 'localtime') and user_id = '" + userUid + "') as sentence," +
                    "(select count(*) from LOCATIONS where date(create_date) = date('now', 'localtime') and user_id = '" + userUid + "') as location_count," +
                            "(select count(*) from PHOTOS  where date(update_date) = date('now', 'localtime') and user_id = '" + userUid + "') as photo_count " +
                            " limit 1"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    model.mMood = cursor.getString(cursor.getColumnIndex("mood"));
                    model.mLocationCount = cursor.getInt(cursor.getColumnIndex("location_count"));
                    model.mPhotoCount = cursor.getInt(cursor.getColumnIndex("photo_count"));
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public SentenceModel getSentence(String userUid, String dateString) {
        SentenceModel model =  new SentenceModel();
        try {
            Cursor cursor = database.rawQuery(
                    "select mood," +
                            "sentence," +
                            "(select count(*) from LOCATIONS where date(create_date) = date(a.create_date) and user_id = a.user_id) as location_count," +
                            "(select count(*) from PHOTOS  where date(update_date) = date(a.create_date) and user_id = a.user_id) as photo_count " +
                            " from DAILY a " +
                            " where date(a.create_date) = '" + dateString  +"'  and a.user_id = '"+ userUid+ "'"+
                            " limit 1"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    model.mLocationCount = cursor.getInt(cursor.getColumnIndex("location_count"));
                    model.mPhotoCount = cursor.getInt(cursor.getColumnIndex("photo_count"));
                    model.mMood = cursor.getString(cursor.getColumnIndex("mood"));
                    model.mSentence = cursor.getString(cursor.getColumnIndex("sentence"));
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }



//    // This is currently used in DatelineModel
//    public ArrayList<DatelineModel> getDatelineList(String userUid, int nextpage) {
//        int start = nextpage * 20;
//        int end = nextpage+1 * 20;
//        ArrayList<DatelineModel> list =  new ArrayList<DatelineModel>();
//        try {
//            Cursor cursor = database.rawQuery(
//                    "select a.create_date, "
//                            + "(select group_concat(ifnull(b.file_location, b.file_location_url)) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' group by cluster_id order by quality_rank desc) b )  as files,"
//                            + "(select group_concat(b.file_index) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' group by cluster_id order by quality_rank desc) b )  as ids,"
//                            + "(select count(*) from PHOTOS where date(a.create_date) = date(update_date) and user_id = '" + userUid + "')  as photo_count, "
//                            + "(select count(*) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as location_count,"
//                            + "(select sentence from DAILY where date(create_date) = date(a.create_date)) as sentence ,"
//                            + "(select weather from DAILY where date(create_date) = date(a.create_date)) as weather ,"
//                            + "(select mood from DAILY where date(create_date) = date(a.create_date)) as mood ,"
//                            + "(select group_concat(replace(ifnull(name,'none'),',','.')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as pois ,"
//                            + "(select group_concat(ifnull(category,'none')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as categories ,"
//                            + "(select group_concat(create_date) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as poisCRDates"
//                            + " from LOCATIONS a"
//                            + " where user_id = '" + userUid + "' and date(a.create_date) < date('now','localtime') "
//                            + " group by date(a.create_date) order by a.create_date desc limit " + String.format("%d",start) + ", " + String.format("%d",end)
//                    , null);
//            if (cursor.moveToFirst()) {
//                do {
//                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
//                    String files = cursor.getString(cursor.getColumnIndex("files"));
//                    String ids = cursor.getString(cursor.getColumnIndex("ids"));
//                    String weather = cursor.getString(cursor.getColumnIndex("weather"));
//                    String mood = cursor.getString(cursor.getColumnIndex("mood"));
//                    String categoryGroup = cursor.getString(cursor.getColumnIndex("categories"));
//                    String poiGroup = cursor.getString(cursor.getColumnIndex("pois"));
//                    String poiCRDatesGroup = cursor.getString(cursor.getColumnIndex("poisCRDates"));
//                    int locationCount = cursor.getInt(cursor.getColumnIndex("location_count"));
//                    int photoCount = cursor.getInt(cursor.getColumnIndex("photo_count"));
//                    String sentence = cursor.getString(cursor.getColumnIndex("sentence"));
//                    list.add(new DatelineModel(create_date, files, ids, weather,mood, locationCount, photoCount, sentence, poiGroup, poiCRDatesGroup, categoryGroup));
//                } while (cursor.moveToNext());
//            }
//            if (cursor != null)
//                cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }





    public ArrayList<TimelineModel> getTimelinelist(String userUid) {
        ArrayList<TimelineModel> list =  new ArrayList<TimelineModel>();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT a.location_index, a.name, a.latitude, a.longitude, a.create_date," +
                            "(SELECT group_concat(ifnull(x.file_location, x.file_location_url))" +
                            "   FROM (SELECT c.file_location, c.file_location_url" +
                            "           FROM (SELECT cluster_id, MAX(quality_rank) as quality_rank FROM PHOTOS GROUP BY  cluster_id, sortseq ) b" +
                            "               INNER JOIN PHOTOS c ON b.cluster_id = c.cluster_id AND b.quality_rank = c.quality_rank " +
                            "          WHERE c.update_date between ifnull(a.create_date, date('now','localtime')) and ifnull(a.update_date, datetime('now', 'localtime'))" +
                            "          ORDER BY  c.sortseq asc, c.quality_rank desc) x) files," +
                            "(SELECT group_concat(ifnull(c.file_location, c.file_location_url))" +
                            "   FROM PHOTOS c " +
                            "  WHERE c.update_date between ifnull(a.create_date, date('now','localtime')) and ifnull(a.update_date, datetime('now', 'localtime'))" +
                            "  ORDER BY  c.sortseq asc, c.quality_rank desc) images" +
                            "  from LOCATIONS a" +
                            " where date(a.create_date) = date('now', 'localtime') and user_id = '" + userUid + "'"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    int locationId = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    String files = cursor.getString(cursor.getColumnIndex("files"));
                    String images = cursor.getString(cursor.getColumnIndex("images"));
                    list.add(new TimelineModel(locationId, name, latitude, longitude, create_date, files, images));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<TimelineModel> getNonameTimelineList(String userUid) {
        ArrayList<TimelineModel> list =  new ArrayList<TimelineModel>();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT a.location_index, a.name, a.latitude, a.longitude, a.create_date" +
                            "  from LOCATIONS a" +
                            " where a.name is null and user_id = '" + userUid + "'"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    int locationId = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                    double logitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    list.add(new TimelineModel(locationId, name, latitude, logitude, create_date, "", ""));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public ArrayList<TimelineModel> getTimelinelist(String userUid, String dateString) {
        ArrayList<TimelineModel> list =  new ArrayList<TimelineModel>();
        try {
            Cursor cursor = database.rawQuery(
                    /*"SELECT a.location_index, a.name, a.latitude, a.longitude, a.create_date," +
                            "(SELECT group_concat(ifnull(x.file_location, x.file_location_url))" +
                            "   FROM (SELECT c.file_location, c.file_location_url" +
                            "           FROM (SELECT cluster_id, MAX(quality_rank) as quality_rank FROM PHOTOS GROUP BY  cluster_id ) b" +
                            "               INNER JOIN PHOTOS c ON b.cluster_id = c.cluster_id AND b.quality_rank = c.quality_rank " +
                            "          WHERE c.update_date between ifnull(a.create_date, date('now','localtime')) and ifnull(a.update_date, datetime('now', 'localtime'))" +
                            "          ORDER BY  c.sortseq asc, c.quality_rank desc) x) files, " +
                            "(SELECT group_concat(ifnull(c.file_location, c.file_location_url))" +
                            "   FROM PHOTOS c " +
                            "  WHERE c.update_date between ifnull(a.create_date, date('now','localtime')) and ifnull(a.update_date, datetime('now', 'localtime'))" +
                            "  ORDER BY  c.sortseq asc, c.quality_rank desc) images" +
                            "  from LOCATIONS a" +
                            " where date(a.create_date) = '" + dateString + "' and user_id = '" + userUid + "'"
                            , null);*/
                        "SELECT a.location_index, a.name, a.latitude, a.longitude, a.create_date," +
                                "(SELECT group_concat(ifnull(x.file_location, x.file_location_url))" +
                                "   FROM (SELECT c.file_location, c.file_location_url" +
                                "           FROM (SELECT cluster_id, MAX(quality_rank) as quality_rank FROM PHOTOS GROUP BY  cluster_id, sortseq ) b" +
                                "               INNER JOIN PHOTOS c ON b.cluster_id = c.cluster_id AND b.quality_rank = c.quality_rank " +
                                "          WHERE c.update_date between ifnull(a.create_date, date('now','localtime')) and ifnull(a.update_date, datetime('now', 'localtime'))" +
                                "          ORDER BY  c.sortseq asc, c.quality_rank desc) x) files," +
                                "(SELECT group_concat(ifnull(c.file_location, c.file_location_url))" +
                                "   FROM PHOTOS c " +
                                "  WHERE c.update_date between ifnull(a.create_date, date('now','localtime')) and ifnull(a.update_date, datetime('now', 'localtime'))" +
                                "  ORDER BY  c.sortseq asc, c.quality_rank desc) images" +
                                "  from LOCATIONS a" +
                                " where date(a.create_date) = '" + dateString + "' and user_id = '" + userUid + "'"

                    , null);
            if (cursor.moveToFirst()) {
                do {
                    int locationId = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                    double logitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    String files = cursor.getString(cursor.getColumnIndex("files"));
                    String images = cursor.getString(cursor.getColumnIndex("images"));
                    list.add(new TimelineModel(locationId, name, latitude, logitude, create_date, files, images));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    // This is currently used in DatelineModel
    public ArrayList<DatelineModel> getDatelineList(String userUid, int nextpage) {
        int start = nextpage * 20;
        int end = nextpage+1 * 20;
        ArrayList<DatelineModel> list =  new ArrayList<DatelineModel>();
        try {
            Cursor cursor = database.rawQuery(
                    "select a.create_date, "
                            + "(select group_concat(ifnull(b.file_location, b.file_location_url)) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' group by cluster_id order by quality_rank desc) b )  as files,"
                            + "(select group_concat(b.file_index) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' group by cluster_id order by quality_rank desc) b )  as ids,"
                            + "(select count(*) from PHOTOS where date(a.create_date) = date(update_date) and user_id = '" + userUid + "')  as photo_count, "
                            + "(select count(*) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as location_count,"
                            + "(select sentence from DAILY where date(create_date) = date(a.create_date)) as sentence ,"
                            + "(select weather from DAILY where date(create_date) = date(a.create_date)) as weather ,"
                            + "(select mood from DAILY where date(create_date) = date(a.create_date)) as mood ,"
                            + "(select group_concat(replace(ifnull(name,'none'),',','.')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as pois ,"
                            + "(select group_concat(ifnull(category,'none')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as categories ,"
                            + "(select group_concat(create_date) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as poisCRDates"
                            + " from LOCATIONS a"
                            + " where user_id = '" + userUid + "' and date(a.create_date) < date('now','localtime') "
                            + " group by date(a.create_date) order by a.create_date desc limit " + String.format("%d",start) + ", " + String.format("%d",end)
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    String files = cursor.getString(cursor.getColumnIndex("files"));
                    String ids = cursor.getString(cursor.getColumnIndex("ids"));
                    String weather = cursor.getString(cursor.getColumnIndex("weather"));
                    String mood = cursor.getString(cursor.getColumnIndex("mood"));
                    String categoryGroup = cursor.getString(cursor.getColumnIndex("categories"));
                    String poiGroup = cursor.getString(cursor.getColumnIndex("pois"));
                    String poiCRDatesGroup = cursor.getString(cursor.getColumnIndex("poisCRDates"));
                    int locationCount = cursor.getInt(cursor.getColumnIndex("location_count"));
                    int photoCount = cursor.getInt(cursor.getColumnIndex("photo_count"));
                    String sentence = cursor.getString(cursor.getColumnIndex("sentence"));
                    list.add(new DatelineModel(create_date, files, ids, weather,mood, locationCount, photoCount, sentence, poiGroup, poiCRDatesGroup, categoryGroup));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public ArrayList<DatelineModel> getDatelineList(String userUid) {
        ArrayList<DatelineModel> list =  new ArrayList<DatelineModel>();
        try {
            Cursor cursor = database.rawQuery(
                    "select a.create_date ,"
                            + "(select mood from DAILY where date(create_date) = date(a.create_date)) mood"
                            + " from LOCATIONS a"
                            + " where a.user_id = '" + userUid + "' "
                            + " group by date(a.create_date) order by a.create_date asc"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    DatelineModel dateline = new DatelineModel();
                    dateline.mUpdateDate = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    dateline.mMood = cursor.getString(cursor.getColumnIndex(COLUMN_MOOD));
                    list.add(dateline);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public DatelineModel getDateline(String userUid, String dateString) {
        DatelineModel dateline =  new DatelineModel();
        try {
            Cursor cursor = database.rawQuery(
                    "select a.create_date, "
                            + "(select group_concat(ifnull(b.file_location, b.file_location_url)) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' group by cluster_id order by quality_rank desc) b )  as files,"
                            + "(select count(*) from PHOTOS where date(a.create_date) = date(update_date) and user_id = '" + userUid + "')  as photo_count, "
                            + "(select count(*) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as location_count,"
                            + "(select sentence from DAILY where date(create_date) = date(a.create_date)) as sentence,"
                            + "(select weather from DAILY where date(create_date) = date(a.create_date)) as weather ,"
                            + "(select mood from DAILY where date(create_date) = date(a.create_date)) as mood ,"
                            + "(select group_concat(replace(ifnull(name,'none'),',','.')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as pois ,"
                            + "(select group_concat(ifnull(category,'none')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as categories ,"
                            + "(select group_concat(create_date) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as poisCRDates"
                            + " from LOCATIONS a"
                            + " where user_id = '" + userUid + "' and date(a.create_date) = '" + dateString + "' "
                            + " group by date(a.create_date) order by a.create_date desc "
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    String files = cursor.getString(cursor.getColumnIndex("files"));
                    String ids = cursor.getString(cursor.getColumnIndex("ids"));
                    String weather = cursor.getString(cursor.getColumnIndex("weather"));
                    String mood = cursor.getString(cursor.getColumnIndex("mood"));
                    String poiGroup = cursor.getString(cursor.getColumnIndex("pois"));
                    String poiCRDatesGroup = cursor.getString(cursor.getColumnIndex("poisCRDates"));
                    String categoryGroup = cursor.getString(cursor.getColumnIndex("categories"));
                    int locationCount = cursor.getInt(cursor.getColumnIndex("location_count"));
                    int photoCount = cursor.getInt(cursor.getColumnIndex("photo_count"));
                    String sentence = cursor.getString(cursor.getColumnIndex("sentence"));
                    return new DatelineModel(create_date, files, ids, weather, mood,locationCount, photoCount, sentence, poiGroup,poiCRDatesGroup, categoryGroup);
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateline;
    }

    public ArrayList<DatelineModel> getDatelineList(String userUid, String searchText, int nextpage) {
        int start = nextpage * 20;
        int end = nextpage+1 * 20;
        ArrayList<DatelineModel> list =  new ArrayList<DatelineModel>();
        try {
            Cursor cursor = database.rawQuery(
                    "select a.create_date, "
                            + "(select group_concat(ifnull(b.file_location, b.file_location_url)) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' group by cluster_id order by quality_rank desc) b )  as files,"
                            + "(select group_concat(b.file_index) from (select * from PHOTOS where date(update_date) = date(a.create_date) and user_id = '" + userUid + "' order by quality_rank desc) b )  as ids,"
                            + "(select count(*) from PHOTOS where date(a.create_date) = date(update_date) and user_id = '" + userUid + "')  as photo_count, "
                            + "(select count(*) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as location_count,"
                            + "(select sentence from DAILY where date(create_date) = date(a.create_date)) as sentence,"
                            + "(select weather from DAILY where date(create_date) = date(a.create_date)) as weather ,"
                            + "(select mood from DAILY where date(create_date) = date(a.create_date)) as mood ,"
                            + "(select group_concat(ifnull(category, 'none')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as pois ,"
                            + "(select group_concat(replace(ifnull(name,'none'),',','.')) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as categories ,"
                            + "(select group_concat(create_date) from LOCATIONS where date(a.create_date) = date(create_date) and user_id = '" + userUid + "')  as poisCRDates"
                            + " from LOCATIONS a  LEFT OUTER JOIN DAILY b ON date(a.create_date) = date(b.create_date)"
                            + " where   ( (a.name like '%'||'"+searchText+"'||'%') OR (b.mood like '%'||'"+searchText+"'||'%') OR (b.sentence like '%'||'"+searchText+"'||'%') ) "
                            + " and a.user_id = '" + userUid + "' and date(a.create_date) < date('now','localtime') "
                            + " group by date(a.create_date) order by a.create_date desc limit " + String.format("%d",start) + ", " + String.format("%d",end)
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    String files = cursor.getString(cursor.getColumnIndex("files"));
                    String ids = cursor.getString(cursor.getColumnIndex("ids"));
                    String weather = cursor.getString(cursor.getColumnIndex("weather"));
                    String mood = cursor.getString(cursor.getColumnIndex("mood"));
                    String poiGroup = cursor.getString(cursor.getColumnIndex("pois"));
                    String poiCRDatesGroup = cursor.getString(cursor.getColumnIndex("poisCRDates"));
                    String categoryGroup = cursor.getString(cursor.getColumnIndex("categories"));
                    int locationCount = cursor.getInt(cursor.getColumnIndex("location_count"));
                    int photoCount = cursor.getInt(cursor.getColumnIndex("photo_count"));
                    String sentence = cursor.getString(cursor.getColumnIndex("sentence"));
                    list.add(new DatelineModel(create_date, ids, weather, mood,files, locationCount, photoCount, sentence, poiGroup, poiCRDatesGroup, categoryGroup));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public TimelineModel getLastTimeline(String userUid) {
        TimelineModel model = new TimelineModel();
        final Cursor cursor = database.query(TABLE_LOCATIONS, null, String.format("%s =? and date(%s) = date('now','localtime')", COLUMN_USER_ID, COLUMN_CREATE_DATE), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    model.mLatitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_MEASURELATITUDE));
                    model.mLogitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_MEASURELONGITUDE));
                    model.setCreateDate(cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE)));
                    model.setMeasureDate(cursor.getString(cursor.getColumnIndex(COLUMN_MEASURE_DATE)));
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            model.mMeasureLatitude = -1;
            model.mMeasureLogitude = -1;
        }
        return model;
    }

    public TimelineModel getLastTimelineToday(String userUid) {
        TimelineModel model = new TimelineModel();
        final Cursor cursor = database.query(TABLE_LOCATIONS, null, String.format("date(%s) = date('now', 'localtime') and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    model.mLocationId = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    model.mLock = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCK));
                    model.mLatitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                    model.mLogitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                    model.mMeasureLatitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_MEASURELATITUDE));
                    model.mMeasureLogitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_MEASURELONGITUDE));
                    model.setCreateDate(cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE)));
                    model.setMeasureDate(cursor.getString(cursor.getColumnIndex(COLUMN_MEASURE_DATE)));
                    break;
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public void setLastTimeline(String userUid) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        final Cursor cursor = database.query(TABLE_LOCATIONS, null, String.format("%s =? and update_date is null", COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int locationIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                    String dateString = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                    Date created = formatter.parse(dateString);
                    String lastString = dateFormatter.format(created) + " 23:59:59.000" ;
                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_UPDATE_DATE, formatter.format(lastString));
                    database.update(TABLE_LOCATIONS, cv, COLUMN_LOCATION_INDEX + " =? ", new String[]{String.format("%d", locationIndex)});
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<DailyModel> getDailyList(String userUid) {
        ArrayList<DailyModel> list =  new ArrayList<DailyModel>();
        final Cursor cursor = database.query(TABLE_DAILY, null, String.format("%s = ? ", COLUMN_USER_ID), new String[]{userUid}, null, null, COLUMN_CREATE_DATE+ " ASC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String mood = cursor.getString(cursor.getColumnIndex(COLUMN_MOOD));;
                    String create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));;
                    list.add(new DailyModel(mood, create_date));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public ArrayList<PhotosGridModel> getAllImagesFirst() {
        ArrayList<PhotosGridModel> items = new ArrayList<PhotosGridModel>();
        final Cursor cursor = database.query(TABLE_PHOTOS, null, null, null, null, null, COLUMN_UPDATE_DATE + " ASC", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                    float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                    //Log.i("DB HELPER", "DATA FROM THE DB HELPER COLUMN_CLUSER_ID : " + ClusterId);
                    items.add(new PhotosGridModel(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)), false, "1", null, "0", ClusterId, QualityRank, cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX)), cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE)), cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST))));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }


    public void deleteAllData() {
        database.delete(TABLE_PHOTOS, null, null);
    }

    /**
     * Method for get the image with same ClusterId
     *
     * @param clusterId
     * @return
     */

    public ArrayList<PhotosGridModel> getSameImages(String clusterId) {
        ArrayList<PhotosGridModel> items = new ArrayList<PhotosGridModel>();
        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_CLUSER_ID + "=?", new String[]{"" + clusterId}, null, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST)) == 1) {

                        } else {
                            int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                            float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                            items.add(new PhotosGridModel(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)), false, "1", null, "0", ClusterId, QualityRank, cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX)), cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE)), cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST))));
                        }
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }


    /*public ArrayList<DynGridViewItemData> getFolderImagesForShare(String clusterId) {
        ArrayList<DynGridViewItemData> items = new ArrayList<DynGridViewItemData>();
        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_CLUSER_ID + "=?", new String[]{"" + clusterId}, null, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                        float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                        items.add(new DynGridViewItemData(
                                "texts", // item string name if we have the all images name and want to display the we will use this
                                "0",//Counter of the Folder
                                "0",//isFolder flag "0" for Folder and "1" for the image
                                120, 120, 2, // sizes: item w, item h, item padding
                                R.drawable.item,// item background image
                                R.drawable.ic_check_dyn,// it will be use as checked if reburied
                                R.drawable.imageview_selection_shape,// it will be use as unchecked if reburied
                                true, // favorite state, if favs are enabled
                                false, // favs disabled
                                true,//this flag is for drag
                                R.drawable.pic2,// item image res id
                                1,  // item id
                                cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)),// path of the folder
                                1
                        ));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }*/

    public PhotosGridModel getBestImages(String clusterId) {
        PhotosGridModel photosGridModel = null;
        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_CLUSER_ID + "=?" + " AND " + COLUMN_IS_BEST + "=?", new String[]{"" + clusterId, "1"}, null, null, null, null);
//        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_CLUSER_ID + "=?", new String[]{"" + clusterId}, null, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                        float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                        photosGridModel = new PhotosGridModel(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)), false, "1", null, "0", ClusterId, QualityRank, cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX)), cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE)), cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST)));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photosGridModel;
    }

    public PhotosGridModel getBlurryImages() {
        PhotosGridModel photosGridModel = null;
        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_BLURRING_INDEX + "=?", null, null, null, COLUMN_UPDATE_DATE + " ASC", null);
//        final Cursor cursor = database.query(TABLE_PHOTOS, null, COLUMN_CLUSER_ID + "=?", new String[]{"" + clusterId}, null, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int ClusterId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                        float QualityRank = cursor.getFloat(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                        photosGridModel = new PhotosGridModel(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION)), false, "1", null, "0", ClusterId, QualityRank, cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX)), cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE)), cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST)));
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photosGridModel;
    }


    public long updateBlurry(final String sourceIndex, final int isBlurry) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BLURRING_INDEX, isBlurry);
        return database.update(TABLE_PHOTOS, cv, COLUMN_FILE_INDEX + " =? ", new String[]{sourceIndex});
    }

    public long updateBest(final String sourceIndex, final int isBest) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IS_BEST, isBest);
        return database.update(TABLE_PHOTOS, cv, COLUMN_FILE_INDEX + " =? ", new String[]{sourceIndex});
    }


    public int deleteItemBypath(String path) {
        return database.delete(TABLE_PHOTOS, COLUMN_FILE_LOCATION + "=?", new String[]{path});
    }

    // methods for Firebase Backup & Restore
    public ArrayList<Photo> getPhotoDatalist(final String userUid, final String start, final String end) {

        ArrayList<Photo> list = new ArrayList<Photo>();
        //Cursor cursor = database.query(TABLE_LOCATIONS, null,  String.format("%s = ? and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{location.create_date, userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        Cursor cursor = database.query(TABLE_PHOTOS, null, String.format("date(%s) >= ? and date(%s) <= ?", COLUMN_UPDATE_DATE, COLUMN_UPDATE_DATE), new String[]{start, end}, null, null, COLUMN_UPDATE_DATE+ " ASC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {

                        Photo model = new Photo();
                        model.file_index = cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_INDEX));
                        model.file_url = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_URL));
                        model.file_location = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION));
                        model.file_name = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_NAME));
                        model.is_best = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BEST));
                        model.cluster_id = cursor.getInt(cursor.getColumnIndex(COLUMN_CLUSER_ID));
                        model.quality_rank = cursor.getString(cursor.getColumnIndex(COLUMN_QUALITY_RANK));
                        model.blurring_index = cursor.getString(cursor.getColumnIndex(COLUMN_BLURRING_INDEX));
                        model.psnr_index = cursor.getString(cursor.getColumnIndex(COLUMN_PSNR_INDEX));
                        model.face_area = cursor.getString(cursor.getColumnIndex(COLUMN_FACE_AREA));
                        model.face_distance = cursor.getString(cursor.getColumnIndex(COLUMN_FACE_DISTANCE));
                        model.extra_feat = cursor.getString(cursor.getColumnIndex(COLUMN_EXTRA_FEAT));
                        model.weight_coeff = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT_COEFF));
                        model.final_score = cursor.getString(cursor.getColumnIndex(COLUMN_FINAL_SCORE));
                        model.delete_check = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETE_CHECK));
                        model.update_date = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE));
                        model.photo_size = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_SIZE));
                        model.sortseq = cursor.getInt(cursor.getColumnIndex(COLUMN_SORTSEQ));
                        model.user_id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                        model.flag = cursor.getInt(cursor.getColumnIndex(COLUMN_FLAG));

                        list.add(model);
                    } while (cursor.moveToNext());
                }

            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Daily> getDailyDatalist(final String userUid, String start, String end) {

        ArrayList<Daily> list = new ArrayList<Daily>();
        Cursor cursor = database.query(TABLE_DAILY, null, String.format("%s between ? and ?", COLUMN_CREATE_DATE), new String[]{start, end}, null, null, COLUMN_CREATE_DATE+ " ASC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {

                        Daily model = new Daily();
                        model.daily_index = cursor.getInt(cursor.getColumnIndex(COLUMN_DAILY_INDEX));
                        model.mood = cursor.getString(cursor.getColumnIndex(COLUMN_MOOD));
                        model.weather = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER));
                        model.user_id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                        model.create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                        model.sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));

                        list.add(model);
                    } while (cursor.moveToNext());
                }

            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Location> getLocationDatalist(final String userUid, String start, String end) {

        ArrayList<Location> list = new ArrayList<Location>();
        Cursor cursor = database.query(TABLE_LOCATIONS, null, String.format("%s between ? and ?", COLUMN_CREATE_DATE), new String[]{start, end}, null, null, COLUMN_CREATE_DATE+ " ASC", null);
        try {
            if(cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        Location model = new Location();
                        model.location_index = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_INDEX));
                        model.user_id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                        model.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                        model.latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                        model.longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                        model.lock = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCK));
                        model.measure_latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_MEASURELATITUDE));
                        model.measure_longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_MEASURELONGITUDE));
                        model.measure_date = cursor.getString(cursor.getColumnIndex(COLUMN_MEASURE_DATE));
                        model.create_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_DATE));
                        model.update_date = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE));
                        model.category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                        list.add(model);
                    } while (cursor.moveToNext());
                }

            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public int setPhoto(String mUserUid, String fileIndex, String imageUrl) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FILE_URL, imageUrl);
        return database.update(TABLE_PHOTOS, cv, String.format(" %s =? and %s =? ", COLUMN_FILE_INDEX, COLUMN_USER_ID), new String[]{fileIndex, mUserUid});
    }

    public void restoreLocation(String userUid, Location location) {
        Cursor cursor = database.query(TABLE_LOCATIONS, null,  String.format("%s = ? and %s =? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{location.create_date, userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
            } else {
                final ContentValues values = new ContentValues();

                //values.put(COLUMN_LOCATION_INDEX, userUid);
                values.put(COLUMN_USER_ID, location.user_id);
                values.put(COLUMN_NAME, location.name);
                values.put(COLUMN_LATITUDE, location.latitude);
                values.put(COLUMN_LONGITUDE, location.longitude);
                values.put(COLUMN_LOCK, location.lock);
                values.put(COLUMN_MEASURELATITUDE, location.measure_latitude);
                values.put(COLUMN_MEASURELONGITUDE, location.measure_longitude);
                values.put(COLUMN_MEASURE_DATE, location.measure_date);
                values.put(COLUMN_CREATE_DATE, location.create_date);
                values.put(COLUMN_UPDATE_DATE, location.update_date);
                values.put(COLUMN_CATEGORY, location.category);

                database.insert(TABLE_LOCATIONS, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreDaily(String userUid, Daily item) {
        Cursor cursor = database.query(TABLE_DAILY, null,  String.format("date(%s) = ? and %s = ? ", COLUMN_CREATE_DATE, COLUMN_USER_ID), new String[]{item.create_date, userUid}, null, null, COLUMN_CREATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
            } else {
                final ContentValues values = new ContentValues();

                values.put(COLUMN_MOOD, item.mood);
                values.put(COLUMN_WEATHER, item.weather);
                values.put(COLUMN_USER_ID, item.user_id);
                values.put(COLUMN_CREATE_DATE, item.create_date);
                values.put(COLUMN_SENTENCE, item.sentence);

                database.insert(TABLE_DAILY, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restorePhoto(String userUid, Photo item) {
        Cursor cursor = database.query(TABLE_PHOTOS, null,  String.format("%s = ? and %s =? ", COLUMN_UPDATE_DATE, COLUMN_USER_ID), new String[]{item.update_date, userUid}, null, null, COLUMN_UPDATE_DATE+ " DESC", null);
        try {
            if(cursor.getCount() > 0){
            } else {
                final ContentValues values = new ContentValues();

                values.put(COLUMN_FILE_URL, item.file_url);
                values.put(COLUMN_FILE_LOCATION, item.file_location);
                values.put(COLUMN_FILE_NAME, item.file_name);
                values.put(COLUMN_IS_BEST, item.is_best);
                values.put(COLUMN_CLUSER_ID, item.cluster_id);
                values.put(COLUMN_QUALITY_RANK, item.quality_rank);
                values.put(COLUMN_BLURRING_INDEX, item.blurring_index);
                values.put(COLUMN_PSNR_INDEX, item.psnr_index);
                values.put(COLUMN_FACE_AREA, item.face_area);
                values.put(COLUMN_FACE_DISTANCE, item.face_distance);
                values.put(COLUMN_EXTRA_FEAT, item.extra_feat);
                values.put(COLUMN_WEIGHT_COEFF, item.weight_coeff);
                values.put(COLUMN_FINAL_SCORE, item.final_score);
                values.put(COLUMN_DELETE_CHECK, item.delete_check);
                values.put(COLUMN_UPDATE_DATE, item.update_date);
                values.put(COLUMN_PHOTO_SIZE, item.photo_size);
                values.put(COLUMN_FLAG, item.flag);
                values.put(COLUMN_SORTSEQ, item.sortseq);
                values.put(COLUMN_USER_ID, item.user_id);


                database.insert(TABLE_PHOTOS, null, values);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCreateTime(String userUid, String name) {


        String poidates =null;
        try {
            Cursor cursor = database.rawQuery(
                    "select a.create_date, "
                            + "(select create_date from LOCATIONS as poisdates"
                            + " from LOCATIONS a"
                            + " WHERE a.name = '" + name + "' and a.user_id = '" + userUid + "'"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    poidates = cursor.getString(cursor.getColumnIndex("create_date"));

                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return poidates;
    }


    public int getClusteredImageSize(String userUid, String cluster_id) {

        int size =0;
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT a.*," +
                            "       (SELECT name FROM LOCATIONS where a.update_date between ifnull(create_date, date('now','localtime')) and ifnull(update_date, datetime('now', 'localtime'))) as name" +
                            "  FROM PHOTOS a" +
                            " WHERE a.cluster_id = '" + cluster_id + "' and a.user_id = '" + userUid + "'"
                    , null);
            if (cursor.moveToFirst()) {
                do {

                    // junyong - define the data if you need
                    size++;

                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    public PhotoInfoModel getPhotoInfo(String userUid, String photo_index) {
        PhotoInfoModel result =  new PhotoInfoModel();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT a.*," +
                            "       (SELECT name FROM LOCATIONS where a.update_date between ifnull(create_date, date('now','localtime')) and ifnull(update_date, datetime('now', 'localtime'))) as name" +
                            "  FROM PHOTOS a" +
                            " WHERE a.file_index = '" + photo_index + "' and a.user_id = '" + userUid + "'"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    result.name = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_LOCATION));
                    result.photo_size = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_SIZE));
                    result.update_date = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_DATE));
                    result.cluster_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CLUSER_ID)));
                    result.weight_coeff = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT_COEFF));  // This is currenlty used as smile probability
                    // junyong - define the data if you need

                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
