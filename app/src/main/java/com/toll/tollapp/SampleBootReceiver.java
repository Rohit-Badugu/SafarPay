package com.toll.tollapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Rohit33 on 24-03-2017.
 */

public class SampleBootReceiver extends BroadcastReceiver {

    GpsAlarm alarm=new GpsAlarm();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarm.setAlarm(context);
        }
    }

}
