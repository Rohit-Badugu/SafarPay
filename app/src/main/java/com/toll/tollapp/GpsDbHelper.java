package com.toll.tollapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rohit33 on 23-03-2017.
 */

public class GpsDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GpsContract.GpsEntry.TABLE_NAME + " (" +
                    GpsContract.GpsEntry._ID + " INTEGER AUTO INCREMENT PRIMARY KEY," +
                    GpsContract.GpsEntry.COLUMN_LAT+ " FLOAT, " +
                    GpsContract.GpsEntry.COLUMN_LONG+ " FLOAT, "+
                    GpsContract.GpsEntry.COLUMN_NAME+ " TEXT, "+
                    GpsContract.GpsEntry.COLUMN_CAR_COST+ " INT, " +
                    GpsContract.GpsEntry.COLUMN_BIKE+" INT, "+
                    GpsContract.GpsEntry.COLUMN_BUS+" INT, "+
                    GpsContract.GpsEntry.COLUMN_TRUCK+" INT, "+
                    GpsContract.GpsEntry.COLUMN_STATE +" TEXT, "+
                    GpsContract.GpsEntry.COLUMN_SERVICES+" TEXT "+
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GpsContract.GpsEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "Gps.db";


    public GpsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }



    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //store values in db
    public static void  storeindb(final Context context){


        String url = "http://192.168.43.107:8000/toll/get_toll_details";

        GpsDbHelper mDbHelper=new GpsDbHelper(context);
        // Gets the data repository in write mode
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();


// Create a new map of values, where column names are the keys

// Request a string response
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Result handling


                        try {
                            JSONObject rsp = new JSONObject(response);
                            int count=rsp.getInt("count");
                            JSONArray tolldata=rsp.getJSONArray("toll_data");
                            for(int i=0;i<count;i++){

                                JSONObject data=tolldata.getJSONObject(i);
                                Log.d("data",data.toString());

                                Double lat=data.getDouble("latitude");
                                Double lon=data.getDouble("longitude");
                                int cost=data.getInt("car");
                                Log.d("DBHelper",String.valueOf(lat)+String.valueOf(lon)+" "+String.valueOf(cost));

                                ContentValues values = new ContentValues();
                                values.put(GpsContract.GpsEntry.COLUMN_LAT, data.getDouble("latitude"));
                                values.put(GpsContract.GpsEntry.COLUMN_LONG, data.getDouble("longitude"));
                                values.put(GpsContract.GpsEntry.COLUMN_NAME, data.getString("name"));
                                values.put(GpsContract.GpsEntry.COLUMN_CAR_COST, data.getInt("car"));
                                values.put(GpsContract.GpsEntry.COLUMN_BIKE, data.getInt("bike"));
                                values.put(GpsContract.GpsEntry.COLUMN_BUS, data.getInt("bus"));
                                values.put(GpsContract.GpsEntry.COLUMN_TRUCK, data.getInt("truck"));
                                values.put(GpsContract.GpsEntry.COLUMN_STATE,data.getString("state"));
                                values.put(GpsContract.GpsEntry.COLUMN_SERVICES,data.getString("services"));


                                long rowid=db.insert(GpsContract.GpsEntry.TABLE_NAME,null,values);

                                if(rowid==-1){
                                    Log.d("Gps_dbhelper","Some error");
                                }else{
                                    Log.d("Gps_dbhelper",String.valueOf(rowid)+data.getString("name"));
                                }
                            }


                            MDToast.makeText(context,"sbh" , 1, MDToast.TYPE_INFO);



                        } catch (JSONException e) {

                            Log.d("Gps DB Helper", e.toString());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MDToast.makeText(context, error.toString(), 1, MDToast.TYPE_INFO);
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);



    }


}
