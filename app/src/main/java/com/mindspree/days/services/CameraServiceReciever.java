package com.mindspree.days.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.WeatherModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.HttpAdapter;
import com.mindspree.days.network.RequestCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vision51 on 2016. 12. 14..
 */

public class CameraServiceReciever extends BroadcastReceiver {
    private AppPreference mPreference;
    private Context mContext;
    private DBWrapper mDBWrapper;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mPreference = AppPreference.getInstance();
        mDBWrapper = new DBWrapper(mPreference.getUserUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        TimelineModel model = mDBWrapper.getLastTimelineToday();

        Location dbLocation = new Location("dbLocation");
        dbLocation.setLatitude(model.getMeasureLatitude());
        dbLocation.setLongitude(model.getMeasureLogitude());

        Location location = new Location("deviceLocation");
        location.setLatitude(mPreference.getLatitude());
        location.setLongitude(mPreference.getLongitude());


        try {

            if (AppUtils.datediffinminutes(now, dateFormat.parse(model.getMeasureDate())) >= (mPreference.getDuration()/2)) {
                if (dbLocation.distanceTo(location) > mPreference.getDistance()) {
                    sendAnalyticsEvent(mPreference.getUserUid(), "photo_location", String.format("%f,%f",location.getLatitude(), location.getLongitude()));
                    mDBWrapper.insertLocationAminute(location.getLatitude(), location.getLongitude());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAnalyticsEvent(String id, String name, String content){
        /*Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);*/
        HttpAdapter httpClient = HttpAdapter.getInstance(mContext);
        httpClient.RequestAnalytics(mContext, id, name, content, mAsyncHttpResponse);
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
