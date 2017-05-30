package com.mindspree.days.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
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
import com.mindspree.days.ui.MainActivity;

import cz.msebera.android.httpclient.Header;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
// private static final String TAG = "FCM Service";
// @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // TODO: Handle FCM messages here.
//        // If the application is in the foreground handle both data and notification messages here.
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//    }


    private static final String TAG = "MyFirebaseMsgService";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        super.onMessageReceived(remoteMessage);

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();
                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
                manager.notify(123, notification);
            }


            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {

                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();
                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
                manager.notify(123, notification);

                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }


        }





}