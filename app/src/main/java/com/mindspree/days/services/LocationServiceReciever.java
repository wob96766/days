package com.mindspree.days.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by vision51 on 2016. 12. 14..
 */

public class LocationServiceReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /**
         * service regist when service is destroyed
         */
        if(intent.getAction().equals("ACTION.RESTART.PersistentService")){

            Log.i("RestartService" ,"ACTION.RESTART.PersistentService " );

            Intent i = new Intent(context, LocationLoggingService.class);
            context.startService(i);
        }

        /**
         * service regist when phone is booted
         */
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            Log.i("RestartService" , "ACTION_BOOT_COMPLETED" );
            Intent i = new Intent(context, LocationLoggingService.class);
            context.startService(i);

        }


    }
}
