package com.toll.tollapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.valdesekamdem.library.mdtoast.MDToast;

import static com.toll.tollapp.R.styleable.MenuItem;

public class TollDetailActivity extends AppCompatActivity {

    GpsDbHelper mDbHelper;
    TextView tname,car,bike,bus,truck;
    private static final String TAG=TollDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper=new GpsDbHelper(this);

       /* tname=(TextView)findViewById(R.id.det_toll_name);
        car=(TextView)findViewById(R.id.car_price);
        bike=(TextView)findViewById(R.id.det_bike);
        bus=(TextView)findViewById(R.id.bus_price);
        truck=(TextView)findViewById(R.id.truck_price);




        Intent i=getIntent();
        String name1=i.getStringExtra("toll_name");
        Log.d(TAG,name1);

        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        String whereargs[]={name1};
        Cursor cursor=db.query(GpsContract.GpsEntry.TABLE_NAME,null, GpsContract.GpsEntry.COLUMN_NAME + " = ? ",whereargs,null,null,null);

        int col=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_NAME);
        Log.d(TAG,String.valueOf(col));
        tname.setText(cursor.getString(col).toString());
        col=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_CAR_COST);
        Log.d(TAG,String.valueOf(col));
        car.setText(String.valueOf(cursor.getInt(col)));
        col=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_BIKE);
        Log.d(TAG,String.valueOf(col));
        bike.setText(String.valueOf(cursor.getInt(col)));
        col=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_BUS);
        Log.d(TAG,String.valueOf(col));
        bus.setText(String.valueOf(cursor.getInt(col)));
        col=cursor.getColumnIndex(GpsContract.GpsEntry.COLUMN_TRUCK);
        Log.d(TAG,String.valueOf(col));
        truck.setText(String.valueOf(cursor.getInt(col)));

*/
        setContentView(R.layout.activity_toll_detail);


        Toolbar mytool=(Toolbar)findViewById(R.id.det_tollbar);
        setSupportActionBar(mytool);
        getSupportActionBar().setTitle("Toll details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });






        TextView amt=(TextView)findViewById(R.id.amt_rs);

        amt.setText("Amount");
        Button map=(Button)findViewById(R.id.show_map);
        final String geoLocation="geo:47.6,-122.3";
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(geoLocation));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

    }
}
