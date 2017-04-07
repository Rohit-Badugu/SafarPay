package com.toll.tollapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit33 on 25-03-2017.
 */

public class GeofenceReceiver extends BroadcastReceiver {

    Context mcontext;
    String geofenceTransitionDetails;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext=context;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("geo intent", "eror");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();


            geofenceTransitionDetails = getGeofenceTrasitionDetails(geofenceTransition, triggeringGeofences);


            // Send notification and log the transition details.
            Log.i("success", geofenceTransitionDetails);
            sendNotification();
        } else {
            // Log the error.
            Log.e("eroro", "error");

        }
    }

    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        return TextUtils.join( ", ", triggeringGeofencesList);

    }

    private void sendNotification() {

        // Create an explicit content Intent that starts the main Activity
        Intent notificationIntent = new Intent(mcontext, MainActivity.class);
        notificationIntent.putExtra("approaching_toll",geofenceTransitionDetails);

        // Construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mcontext);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions
        // >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                mcontext);

        // Set the notification contents
        builder.setSmallIcon(R.drawable.ic_visa_card)
                .setContentTitle("Toll")
                .setContentText(
                       geofenceTransitionDetails)
                .setContentIntent(notificationPendingIntent);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) mcontext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());

    }
}


