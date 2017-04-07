package com.toll.tollapp;

import android.provider.BaseColumns;

/**
 * Created by Rohit33 on 22-03-2017.
 */

public class GpsContract {
    public static class GpsEntry implements BaseColumns{
        public static final String TABLE_NAME="Toll";
        public static final String COLUMN_LAT="latitude";
        public static final String COLUMN_LONG="longitude";
        public static final String COLUMN_NAME="toll_name";
        public static final String COLUMN_CAR_COST="car_cost";
        public static final String COLUMN_BIKE="bike_cost";
        public static final String COLUMN_TRUCK="truck_cost";
        public static final String COLUMN_BUS="bus_cost";
        public static final String COLUMN_STATE="state";
        public static final String COLUMN_SERVICES="services";






    }
}
