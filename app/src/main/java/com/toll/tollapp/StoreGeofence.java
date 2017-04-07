package com.toll.tollapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit33 on 25-03-2017.
 */

public class StoreGeofence {

    static GeofencingRequest request;
    static GoogleApiClient googleApiClient;
    static Geofence g;


    public static List creategeofences(Context context) {

        GpsDbHelper mDbHelper = new GpsDbHelper(context);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GpsContract.GpsEntry.COLUMN_LAT,
                GpsContract.GpsEntry.COLUMN_LONG,
                GpsContract.GpsEntry.COLUMN_NAME
        };

        Cursor cursor = db.query(GpsContract.GpsEntry.TABLE_NAME, projection, null, null, null, null, null, null);
        ArrayList<Geofence> mgeolist=new ArrayList<>();
        String log=null;

        if (cursor.moveToFirst()){
            do{
            int lat=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_LAT);
            int longitude=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_LONG);
            int name=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_NAME);
                log=log+" "+name;
            Log.d("value of cursor",String.valueOf(lat)+" "+String.valueOf(longitude)+" "+String.valueOf(name));
                mgeolist.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(cursor.getString(name))
                        .setCircularRegion(
                               cursor.getFloat(lat) ,cursor.getFloat(longitude),
                                2000
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());

        }while (cursor.moveToNext());
        }

        Log.d("geofences added",log);
        return mgeolist;

    }


}
