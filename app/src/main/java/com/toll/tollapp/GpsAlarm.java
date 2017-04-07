package com.toll.tollapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.SystemClock;

import com.valdesekamdem.library.mdtoast.MDToast;

/**
 * Created by Rohit33 on 24-03-2017.
 */

public class GpsAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("toll",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int val=sharedPreferences.getInt("Alarm",0);
        val++;
        editor.putInt("Alarm",val);
        editor.commit();
        
        
        MDToast.makeText(context, String.valueOf(val),1,MDToast.TYPE_INFO);
    }

     public void setAlarm(Context context){
         SharedPreferences sharedPreferences=context.getSharedPreferences("toll",Context.MODE_PRIVATE);
         SharedPreferences.Editor editor=sharedPreferences.edit();
         editor.putInt("Alarm",1);
         editor.commit();
         AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         Intent i = new Intent(context, GpsAlarm.class);
         PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
         am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 * 101, pi);
    }

}
