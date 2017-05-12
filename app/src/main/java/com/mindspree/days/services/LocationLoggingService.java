package com.mindspree.days.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.WeatherModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.HttpAdapter;
import com.mindspree.days.network.RequestCode;
import com.mindspree.days.ui.LandingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vision51 on 2016. 12. 14..
 */

public class LocationLoggingService extends Service {
    // logging criteria distance
    //   private int DISTANCE_THRETHOLD = 100;
//    private int MINUTE_THRETHOLD = 10;

    private static final int MILLISINFUTURE = 6000 * 1000 * 1000;
    private static final int COUNT_DOWN_INTERVAL = 6000;

    private CountDownTimer countDownTimer;
    private GoogleApiClient mGapiClient;
    private LocationRequest mLocationRequest = new LocationRequest();

    private DBWrapper mDBWrapper;
    private AppPreference mPreference;
    private Location mOldLocation = new Location("old");
    private int mLocationCount = 0;

    private Runnable mRunnable;
    private Handler mHandler = new Handler();

    private Location mHomeLocation;
    private Location mOtherLocation;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPreference = AppPreference.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        unregisterRestartAlarm();

        if (mGapiClient == null || !mGapiClient.isConnected()) {
            mGapiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(mConnectionCallbacks)
                    .addOnConnectionFailedListener(mOnConnectionFailedListener)
                    .build();

            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(100);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mGapiClient.connect();
        }
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
        initData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * notificationManager에 항상 표시됨
         */
        //startForeground(1, new Notification());

        return Service.START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("PersistentService", "onDestroy");
        countDownTimer.cancel();

        /**
         * 서비스 종료 시 알람 등록을 통해 서비스 재 실행
         */
        registerRestartAlarm();
    }

    /**
     * 데이터 초기화
     */
    private void initData() {


        countDownTimer();
        countDownTimer.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(!FileJobService.isScheduled(LocationLoggingService.this)) {
                FileJobService.cancelJob(LocationLoggingService.this);
                FileJobService.scheduleJob(LocationLoggingService.this);
            }
        }
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                Log.d(AppConfig.LOG_TAG, "Timer Handler");
                if (mGapiClient.isConnected()) {
                    if (ActivityCompat.checkSelfPermission(LocationLoggingService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(LocationLoggingService.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGapiClient, mLocationRequest, mLocationListener);

                    }
                }
            }

            public void onFinish() {
                initData();
            }
        };
    }


    /**
     * 알람 매니져에 서비스 등록
     */
    private void registerRestartAlarm() {

        Intent intent = new Intent(LocationLoggingService.this, LocationServiceReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(LocationLoggingService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 3 * 6000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 3 * 6000, sender);
    }

    /**
     * 알람 매니져에 서비스 해제
     */
    private void unregisterRestartAlarm() {

        Log.i("000 PersistentService", "unregisterRestartAlarm");

        Intent intent = new Intent(LocationLoggingService.this, LocationServiceReciever.class);
        intent.setAction("ACTION.RESTART.PersistentService");
        PendingIntent sender = PendingIntent.getBroadcast(LocationLoggingService.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle arg0) {

            if (mGapiClient.isConnected()) {
                if (ActivityCompat.checkSelfPermission(LocationLoggingService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(LocationLoggingService.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGapiClient);
                    if(location != null) {
                        if (mPreference.getMeasureLatitude() == 0)
                            mPreference.setMeasureLatitude(location.getLatitude());
                        if (mPreference.getMeasureLongitude() == 0)
                            mPreference.setMeasureLongitude(location.getLongitude());
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGapiClient, mLocationRequest, mLocationListener);
                }
            }
        }
        @Override
        public void onConnectionSuspended(int arg0) {
            int i = 0;
            i++;
        }
    };

    GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener(){
        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
        }
    };

    private boolean test = false;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 로그아웃시 동작 방지
            if(!mPreference.IsLogin())
                return;
            if(mPreference.getPoiLatitude1() != 0 && mPreference.getPoiLongitude1() != 0) {
                mHomeLocation = new Location("home");
                mHomeLocation.setLatitude(mPreference.getPoiLatitude1());
                mHomeLocation.setLongitude(mPreference.getPoiLongitude1());
            } else {
                mHomeLocation = null;
            }
            if(mPreference.getPoiLatitude2() != 0 && mPreference.getPoiLongitude2() != 0) {
                mOtherLocation = new Location("other");
                mOtherLocation.setLatitude(mPreference.getPoiLatitude2());
                mOtherLocation.setLongitude(mPreference.getPoiLongitude2());
            } else {
                mOtherLocation = null;
            }

            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            // warning for memory
            if(!mPreference.getDate().equals(dayFormat.format(now))) {
                long totalSize = AppUtils.GetTotalInternalMemorySize();
                long availableSize = AppUtils.GetAvailableInternalMemorySize();
                if((availableSize * 100 / totalSize ) < 10) {
                    sendNotification(AppUtils.getAppText(R.string.app_name), AppUtils.getAppText(R.string.message_memory_warning), 999999);
                }
                mPreference.setDate(dayFormat.format(now));
            }
            // weather update
            if(!mPreference.getWeatherDate().equals(dayFormat.format(now))) {
                Date weatherTime = AppUtils.getTodayDateTime(now, "16:00:00");
                if(now.after(weatherTime)){
                    mPreference.setWeatherDate(dayFormat.format(now));
                    Random random = new Random();
                    int randomSecond = random.nextInt(7200);
                    //int randomSecond = random.nextInt(10);
                    Intent intent = new Intent(LocationLoggingService.this, WeatherServiceReciever.class);
                    intent.setAction("ACTION.REQUEST.WeatherService");
                    PendingIntent sender = PendingIntent.getBroadcast(LocationLoggingService.this, 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, randomSecond * 1000, sender);

                }
            } else {
                Date weatherTime = AppUtils.getTodayDateTime(now, "19:00:00");
                if(!mDBWrapper.getWeatherToday().equals("") && now.after(weatherTime)){
                    mPreference.setWeatherDate(dayFormat.format(now));
                    Intent intent = new Intent(LocationLoggingService.this, WeatherServiceReciever.class);
                    intent.setAction("ACTION.REQUEST.WeatherService");
                    PendingIntent sender = PendingIntent.getBroadcast(LocationLoggingService.this, 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, sender);

                }
            }
            // location logging
            Log.d(AppConfig.LOG_TAG, String.format("lat:%.8f,lng:%.8f", location.getLatitude(), location.getLongitude()));
            mPreference.setLatitude(location.getLatitude());
            mPreference.setLongitude(location.getLongitude());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date loggingStart = AppUtils.getTodayDateTime(now, "00:20:00");
            Date loggingEnd = AppUtils.getTodayDateTime(now, "23:59:00");
            if(now.before(loggingStart) || now.after(loggingEnd)){
                if(mPreference.getLoggingOnOff() == true) {
                    mDBWrapper.setLastTimeline();
                    sendNotification(AppUtils.getAppText(R.string.app_name), AppUtils.getAppText(R.string.message_journal_created), 999988);
                }
                mPreference.setLoggingOnOff(false);
            } else {
                try {
                    TimelineModel model = mDBWrapper.getLastTimeline();
                    if (model.getLatitude() == 0 && model.getLongitude() == 0) {
                        //if (AppUtils.datediffinminutes(now, dateFormat.parse(mPreference.getMeasureTime())) >= mPreference.getDuration()) {
                        sendAnalyticsEvent(mPreference.getUserUid(), "location_init", String.format("%f,%f on %f,%f", location.getLatitude(), location.getLongitude(), model.getLatitude() , model.getLongitude()));
                        mDBWrapper.insertLocation(location.getLatitude(), location.getLongitude());
                        sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                        //}
                    } else {
                        model = mDBWrapper.getLastTimelineToday();

                        Location dbLocation = new Location("dbLocation");
                        dbLocation.setLatitude(model.getMeasureLatitude());
                        dbLocation.setLongitude(model.getMeasureLogitude());
                        if (AppUtils.datediffinminutes(now, dateFormat.parse(model.getMeasureDate())) >= mPreference.getDuration()) {
                            if (dbLocation.distanceTo(location) > mPreference.getDistance()) {
                                if (model.mLock == 1) {
                                    if(mLocationCount < 10){
                                        sendAnalyticsEvent(mPreference.getUserUid(), "dum", String.format("%f,%f on %f,%f", location.getLatitude(), location.getLongitude(), model.getLatitude() , model.getLongitude()));
                                        mLocationCount++;
                                    } else {
                                        sendAnalyticsEvent(mPreference.getUserUid(), "location",  String.format("%f,%f on %f,%f", location.getLatitude(), location.getLongitude(), model.getLatitude() , model.getLongitude()));
                                        mDBWrapper.insertLocation(location.getLatitude(), location.getLongitude());
                                        mLocationCount = 0;
                                        sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                                    }
                                } else {
                                    mDBWrapper.updateLocation(location.getLatitude(), location.getLongitude());
                                    mDBWrapper.updateMeasureLocation(location.getLatitude(), location.getLongitude());
                                }
                            } else {
                                if (model.mLock != 1) {
                                    mDBWrapper.updateMeasureLocation(location.getLatitude(), location.getLongitude());
                                    if (mHomeLocation != null && mHomeLocation.distanceTo(location) < 300) {
                                        mDBWrapper.setLocationLog(model.mLocationId, AppUtils.getAppText(R.string.text_location_home), mHomeLocation.getLatitude(), mHomeLocation.getLongitude());
                                    } else if (mOtherLocation != null && mOtherLocation.distanceTo(location) < 300) {
                                        mDBWrapper.setLocationLog(model.mLocationId, AppUtils.getAppText(R.string.text_location_other), mOtherLocation.getLatitude(), mOtherLocation.getLongitude());
                                    } else {
                                        mDBWrapper.setLocationLog(model.mLocationId, 1);
                                    }
                                    sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                                } else {
                                    mDBWrapper.updateMeasureLocation(location.getLatitude(), location.getLongitude());
                                }
                            }
                        } else {
                            if (dbLocation.distanceTo(location) > 10) {
                                mDBWrapper.updateLocation(location.getLatitude(), location.getLongitude());
                            }
                        }
                        /*                        int newPhotoCount = mDBWrapper.getNewPhotoCount();
                        if(newPhotoCount > 0){
                            mDBWrapper.updateNewPhotoFlag();
                            Location dbLocation = new Location("dbLocation");
                            dbLocation.setLatitude(model.getMeasureLatitude());
                            dbLocation.setLongitude(model.getMeasureLogitude());
                            if (dbLocation.distanceTo(location) > mPreference.getDistance()) {
                                sendAnalyticsEvent(mPreference.getUserUid(), "location", String.format("%f,%f",location.getLatitude(), location.getLongitude()));
                                mDBWrapper.insertLocation(location.getLatitude(), location.getLongitude());
                                sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                            }
                        } else {
                            Location dbLocation = new Location("dbLocation");
                            dbLocation.setLatitude(model.getMeasureLatitude());
                            dbLocation.setLongitude(model.getMeasureLogitude());
                            if (AppUtils.datediffinminutes(now, dateFormat.parse(model.getMeasureDate())) >= mPreference.getDuration()) {
                                if (dbLocation.distanceTo(location) > mPreference.getDistance()) {
                                    if (model.mLock == 1) {
                                        sendAnalyticsEvent(mPreference.getUserUid(), "location", String.format("%f,%f",location.getLatitude(), location.getLongitude()));
                                        mDBWrapper.insertLocation(location.getLatitude(), location.getLongitude());
                                        sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                                    } else {
                                        mDBWrapper.updateLocation(location.getLatitude(), location.getLongitude());
                                        mDBWrapper.updateMeasureLocation(location.getLatitude(), location.getLongitude());
                                    }
                                } else {
                                    if (model.mLock != 1) {
                                        mDBWrapper.updateMeasureLocation(location.getLatitude(), location.getLongitude());
                                        if (mHomeLocation != null && mHomeLocation.distanceTo(location) < 300) {
                                            mDBWrapper.setLocationLog(model.mLocationId, AppUtils.getAppText(R.string.text_location_home), mHomeLocation.getLatitude(), mHomeLocation.getLongitude());
                                        } else if (mOtherLocation != null && mOtherLocation.distanceTo(location) < 300) {
                                            mDBWrapper.setLocationLog(model.mLocationId, AppUtils.getAppText(R.string.text_location_other), mOtherLocation.getLatitude(), mOtherLocation.getLongitude());
                                        } else {
                                            mDBWrapper.setLocationLog(model.mLocationId, 1);
                                        }
                                        sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_DATA));
                                    } else {
                                        mDBWrapper.updateMeasureLocation(location.getLatitude(), location.getLongitude());
                                    }
                                }
                            } else {
                                if (dbLocation.distanceTo(location) > 10) {
                                    mDBWrapper.updateLocation(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }*/

                    }
                    mPreference.setLoggingOnOff(true);
                } catch (Exception e)  {
                    e.printStackTrace();
                }
            }
        }
    };
    //  register a notificaiton to NotificationManager
    private void sendNotification(String title, String message, int messageId) {
        if(mPreference.getPushOnOff()) {
            Intent intent = new Intent(this, LandingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(bm)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

                nm.notify(messageId, notification);
            }
        }
    }

    private void RequestWeather() {
        HttpAdapter httpClient = HttpAdapter.getInstance(this);
        sendAnalyticsEvent(mPreference.getUserUid(), "weather", String.format("%f,%f",mPreference.getLatitude(), mPreference.getLongitude()));
        httpClient.RequestWeather(this, mPreference.getLatitude(), mPreference.getLongitude(), AppUtils.getAppText(R.string.openweather_key), mAsyncHttpResponse);
    }

    private void sendAnalyticsEvent(String id, String name, String content){
        /*Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);*/
        HttpAdapter httpClient = HttpAdapter.getInstance(this);
        httpClient.RequestAnalytics(this, id, name, content, mAsyncHttpResponse);
    }

    // 서버 응답 처리
    AsyncHttpResponse mAsyncHttpResponse = new AsyncHttpResponse()
    {
        @Override
        public void onHttpStart(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_WEATHER:
                    break;
            }
        }

        @Override
        public void onHttpFinish(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_WEATHER:
                    break;
            }
        }

        @Override
        public void onHttpSuccess(int reqCode, int code, Header[] headers, byte[] response, Object tag) {
            String data = new String(response);
            try {
                switch(reqCode) {
                    case RequestCode.GET_WEATHER:
                        try {
                            WeatherModel model = WeatherModel.parseData(data);
                            sendAnalyticsEvent(mPreference.getUserUid(), "weather", model.mWeather);
                            mDBWrapper.setWeather(model.mWeather);

                        } catch (Exception e) {
                            sendAnalyticsEvent(mPreference.getUserUid(), "weather", "exception");
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
                    case RequestCode.GET_WEATHER:
                        sendAnalyticsEvent(mPreference.getUserUid(), "weather", "error");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
}

