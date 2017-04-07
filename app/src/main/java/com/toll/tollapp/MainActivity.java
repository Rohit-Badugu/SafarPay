package com.toll.tollapp;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentActivity;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.JusPayRequestCallback;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Card;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.MediaStore.Images.ImageColumns.LATITUDE;
import static android.provider.MediaStore.Images.ImageColumns.LONGITUDE;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;

    GeofencingRequest request;

    private static final String LOG_TAG = "toll app";
    public Order order;
    private ProgressDialog dialog;
    private String name, email, mobile_no;
    private String accessToken = null, transactionID = null;
    TextView balanc;


    ImageView imagev;


    public static GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        final SharedPreferences sp = getSharedPreferences("toll", MODE_PRIVATE);



        final MaterialDialog md = new MaterialDialog.Builder(this)
                .title("Enter Amount")
                .inputRangeRes(2, 5, R.color.accent_color)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Amount", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putInt("tmp_bal", Integer.parseInt(input.toString()));
                        ed.commit();
                        fetchtokens(input.toString());
                    }
                }).build();

        GpsDbHelper.storeindb(this);

        //Retreive details

        // getDetails();
        Button pay_button = (Button) findViewById(R.id.recharge);
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md.show();

            }
        });



        //Alarm
        // GpsAlarm alarm=new GpsAlarm();
        //alarm.setAlarm(this);

        ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        //Geofence
      /* List g=StoreGeofence.creategeofences(this);

        request = new GeofencingRequest.Builder()
                // Notification to trigger when the Geofence is created
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofences( g ) // add a Geofence
                .build();


        googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        //Firebase

        Intent i=new Intent(this,com.toll.tollapp.FirebaseMessagingService.class);
        startService(i);
*/


        recyclerView = (RecyclerView) findViewById(R.id.news_list);

        mAdapter = new CustomAdapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setAdapter(mAdapter);

        prepareTransactionData();

        recyclerView.addOnItemTouchListener(
                new RecycleTouchListener(getApplicationContext(), recyclerView, new RecycleTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );


    }




    private void prepareTransactionData() {


        News transaction=new News("Old notes valid till Feb 3","With effect from December 3, 2016, old Rs. 500 notes cannot be used for purchase of petrol, diesel and gas. ","");
        newsList.add(transaction);
        transaction=new News("Daula toll plaza blocked","The mob blocked the Kherki Daula toll plaza following which the long tailbacks were seen on the highway","");
        newsList.add(transaction);
        transaction=new News("No Toll from Talpady locals","The dawn-to-dusk protest against collection of toll at the Toll Plaza in Talapady on National Highway 66 has been called off ","");
        newsList.add(transaction);
       /* for(int i=0;i<count ;i++){
            News transaction = new News(sp.getString("vehicle"+String.valueOf(i),"MH010100"), "MH 01 AZ 0123", "21/ 07 ");
            newsList.add(transaction);
        }*/


        mAdapter.notifyDataSetChanged();

    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        Intent intent = new Intent("com.toll.tollapp.ACTION_RECEIVE_GEOFENCE");
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

public void addgeo(){
    try {
        LocationServices.GeofencingApi.addGeofences(googleApiClient, request, getGeofencePendingIntent());
    }catch (SecurityException e){
        Log.e("error",e.toString());
    }

//Firebase

    GpsDbHelper mDbHelper = new GpsDbHelper(this);
    // Gets the data repository in write mode
    final SQLiteDatabase db = mDbHelper.getReadableDatabase();





}



    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
//        googleApiClient.connect();

    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        addgeo();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("googleapiclient","suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public  void getDetails() {
        SharedPreferences sp = this.getSharedPreferences("toll", Context.MODE_PRIVATE);
        name = sp.getString("name", null);
        email = sp.getString("email", null);
        mobile_no = sp.getString("mobile_no", null);
    }



    public void fetchtokens(final String amt){
        {
            final MaterialDialog md= new MaterialDialog.Builder(this)
                    .title("Connecting")
                    .content("Please wait")
                    .progress(true, 0)
                    .show();
            String url ="http://192.168.43.107:8000/toll/getaccesstoken";

// Request a string response
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // Result handling

                            try {
                                JSONObject rsp = new JSONObject(response);
                                accessToken = rsp.getString("access_token");
                                transactionID = rsp.getString("transaction_id");

                                MDToast.makeText(getBaseContext(), accessToken + " " + transactionID, 1, MDToast.TYPE_INFO);
                                md.dismiss();

                                createOrder(accessToken, transactionID,amt);
                            } catch (JSONException e) {

                                Log.d(LOG_TAG, e.toString());
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    md.dismiss();
                    MDToast.makeText(getBaseContext(), error.toString(), 1, MDToast.TYPE_INFO);
                    // Error handling
                    System.out.println("Something went wrong!");
                    error.printStackTrace();

                }
            });

// Add the request to the queue
            Volley.newRequestQueue(this).add(stringRequest);
        }
    }




    public void createOrder(String accessToken, String transactionID,String amt) {
        SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
        String name=sp.getString("name","admin");
        String email=sp.getString("email","admin@gmail.com");
        String mobile=sp.getString("mobile","8452078307");

        //Create the Order
        order = new Order(accessToken, transactionID, name, email,"8452078307", amt, "toll payment");

        if (!order.isValid()){
            //oops order validation failed. Pinpoint the issue(s).

            if (!order.isValidName()){
                Log.e("App", "Buyer name is invalid");
            }

            if (!order.isValidEmail()){
                Log.e("App", "Buyer email is invalid");
            }

            if (!order.isValidPhone()){
                Log.e("App", "Buyer phone is invalid");
            }

            if (!order.isValidAmount()){
                Log.e("App", "Amount is invalid");
            }

            if (!order.isValidDescription()){
                Log.e("App", "description is invalid");
            }

            if (!order.isValidTransactionID()){
                Log.e("App", "Transaction ID is invalid");
            }

            if (!order.isValidRedirectURL()){
                Log.e("App", "Redirection URL is invalid");
            }


            return;
        }


        Request request = new Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(Order order, Exception error) {
                //dismiss the dialog if showed

                // Make sure the follwoing code is called on UI thread to show Toasts or to
                //update UI elements
                if (error != null) {
                    if (error instanceof Errors.ConnectionError) {
                        Log.e("App", "No internet connection");
                    } else if (error instanceof Errors.ServerError) {
                        Log.e("App", "Server Error. Try again");
                    } else if (error instanceof Errors.AuthenticationError){
                        Log.e("App", "Access token is invalid or expired");
                    } else if (error instanceof Errors.ValidationError){
                        // Cast object to validation to pinpoint the issue
                        Errors.ValidationError validationError = (Errors.ValidationError) error;
                        if (!validationError.isValidTransactionID()) {
                            Log.e("App", "Transaction ID is not Unique");
                            return;
                        }
                        if (!validationError.isValidRedirectURL()) {
                            Log.e("App", "Redirect url is invalid");
                            return;
                        }


                        if (!validationError.isValidWebhook()) {
                            Log.e("dhs","Webhook url is invalid");
                            return;
                        }

                        if (!validationError.isValidPhone()) {
                            Log.e("App", "Buyer's Phone Number is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidEmail()) {
                            Log.e("App", "Buyer's Email is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidAmount()) {
                            Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                            return;
                        }
                        if (!validationError.isValidName()) {
                            Log.e("App", "Buyer's Name is required");
                            return;
                        }
                    } else {
                        Log.e("App", error.getMessage());
                    }
                    return;
                }

                startPreCreatedUI(order);
            }
        });

        request.execute();

}

    private void startPreCreatedUI(Order order) {

        //Using Pre created UI
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }


    private  void startpayment(Order order){
        Card card=new Card();
        SharedPreferences sp=this.getSharedPreferences("toll",Context.MODE_PRIVATE);
        String mon=sp.getString("cc_exp_mon",null);
        String yr=sp.getString("cc_exp_yr",null);
        String exp_date=mon+"/" +yr;

        card.setCardNumber(sp.getString("cc_cardno",null));
        card.setDate(sp.getString("cc_exp_mon",null));
        card.setCardHolderName(exp_date);
        card.setCvv(sp.getString("cc_cvv",null));

        if (!cardValid(card)) {
            return;
        }

        //Get order details form Juspay
        proceedWithCard(order, card);
    }


    private  void proceedWithCard(Order order, final Card card) {
        Request request = new Request(order, card, new JusPayRequestCallback() {
            @Override
            public void onFinish(final Bundle bundle, final Exception error) {
                if (error != null) {
                    if (error instanceof Errors.ConnectionError) {
                        Log.e("App", "No internet");
                    } else if (error instanceof Errors.ServerError) {
                        Log.e("App", "Server Error. trya again");
                    } else {
                        Log.e("App", error.getMessage());
                    }
                    return;
                }
                startPaymentActivity(bundle);

            }
        });


        request.execute();
    }

    private void startPaymentActivity(Bundle bundle) {
        // Start the payment activity
        //Do not change this unless you know what you are doing
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra(Constants.PAYMENT_BUNDLE, bundle);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }


    private  boolean cardValid(Card card) {
        if (!card.isCardValid()) {

            if (!card.isCardNameValid()) {
                showErrorToast("Card Holders Name is invalid");
            }

            if (!card.isCardNumberValid()) {
                showErrorToast("Card Number is invalid");
            }

            if (!card.isDateValid()) {
                showErrorToast("Expiry date is invalid");
            }

            if (!card.isCVVValid()) {
                showErrorToast("CVV is invalid");
            }

            return false;
        }

        return true;
    }

    private  void showErrorToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(getBaseContext(),SettingsActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.action_refresh){
            update_balance();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent i=new Intent(this,ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.payments) {
            Intent i=new Intent(this,TransactionListActivity.class);
            startActivity(i);

        }
        else if (id == R.id.toll_cost) {
            Intent i=new Intent(this,TollCostActivity.class);
            startActivity(i);

        }

        else if (id == R.id.search_toll) {
            Intent i=new Intent(this,SearchActivty.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (transactionID != null || paymentID != null) {
                SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
                int balance=sp.getInt("tmp_bal",0);
                update_app(balance);
                String user_id=sp.getString("user_id",null);
                int addbal=sp.getInt("balance",0);
                if(user_id!=null){
                    update_db(user_id,addbal);
                }else{
                    MDToast.makeText(getBaseContext(),"Some error as occurred",1,MDToast.TYPE_ERROR);
                }

            } else {
                MDToast.makeText(getBaseContext(),"Payment was cancelled",1,MDToast.TYPE_ERROR);
            }
        }
    }

    public void update_app(int bal){
        SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        int curr_bal=sp.getInt("balance",0);
        ed.putInt("balance",curr_bal+bal);
        balanc.setText(String.valueOf(curr_bal+bal));
    }


    public void update_db(String user_id,int addbal){
        String url = "http://192.168.43.107:8000/toll/update_bal";

        Map<String,String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("addbalance",String.valueOf(addbal));
        Log.d("add balance",String.valueOf(addbal));

// Request a string response
        JsonObjectRequest postRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url,new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject rsp) {
                        // Result handling

                        try {
                            String status = rsp.getString("status");

                            if (status == "ok") {
                                MDToast.makeText(getBaseContext(), "Recharge done successfully", 1, MDToast.TYPE_INFO);

                            }else {
                                MDToast.makeText(getBaseContext(), "Unable to process request", 1, MDToast.TYPE_INFO);
                            }

                        } catch (JSONException e) {

                            Log.d(LOG_TAG, e.toString());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MDToast.makeText(getBaseContext(), error.toString(), 1, MDToast.TYPE_INFO);
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(this).add(postRequest);
    }


    public void update_balance(){

        Map<String,String> params = new HashMap<String, String>();
        SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
        String user_id=sp.getString("user_id","");
        if(!user_id.equals("")){

        params.put("user_id",user_id);


        String url = "http://192.168.43.107:8000/toll/get_new_balance";


// Request a string response
        JsonObjectRequest postRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url,new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject rsp) {
                        // Result handling

                        try {
                            String balance=rsp.getString("balance");
                            balanc.setText(balance);

                        } catch (Exception e) {

                            Log.d(LOG_TAG, e.toString());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MDToast.makeText(getBaseContext(), error.toString(), 1, MDToast.TYPE_INFO);
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(this).add(postRequest);

    }
        else{

        }

    }


}