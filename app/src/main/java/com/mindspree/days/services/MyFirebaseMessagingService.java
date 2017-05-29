package com.mindspree.days.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.WeatherModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.HttpAdapter;
import com.mindspree.days.network.RequestCode;

import cz.msebera.android.httpclient.Header;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
 private static final String TAG = "FCM Service";
 @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}