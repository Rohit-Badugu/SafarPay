package com.toll.tollapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText loginid,loginpwd;
    Button loginbtn,skipbtn;
    private static final String TAG=LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar mytool=(Toolbar)findViewById(R.id.login_tollbar);
        //setSupportActionBar(mytool);
        //getSupportActionBar().setTitle("         Login");

        loginid=(EditText)findViewById(R.id.login_id);
        loginpwd=(EditText)findViewById(R.id.login_pwd);
        loginbtn=(Button)findViewById(R.id.login_btn);
        skipbtn=(Button)findViewById(R.id.skip_btn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=loginid.getText().toString();
                String pwd=loginpwd.getText().toString();
                if(id.equals("")||pwd.equals("")){
                    MDToast.makeText(getBaseContext(),"Please Enter all Fields",1,MDToast.TYPE_ERROR);
                }else{
                    authenticate_user(id,pwd);
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("logged",false);
                editor.commit();
                Intent i=new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);

            }
        });
    }

    public void authenticate_user(String id,String pwd){
        if(!isConnected()){
            MDToast.makeText(getBaseContext(),"No Internet Connection",1,MDToast.TYPE_ERROR);
        }else{
            final MaterialDialog md= new MaterialDialog.Builder(this)
                    .title("Authenticating..")
                    .content("Please wait")
                    .progress(true, 0)
                    .show();


            String url ="http://192.168.43.107:8000/toll/authenticate";


            Map<String,String> params = new HashMap<String, String>();
            params.put("user_id", id);
            params.put("password",pwd);




// Request a string response
            JsonObjectRequest postRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url,new JSONObject(params),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject rsp) {

                            // Result handling

                            try {

                                String status = rsp.getString("status");
                                if(status.equals("valid")){
                                    String name=rsp.getString("name");
                                    String mobile=rsp.getString("mobile");
                                    String email=rsp.getString("email");
                                    String user_id=rsp.getString("user_id");
                                    int balance=rsp.getInt("balance");
                                    JSONArray vehicles=rsp.getJSONArray("vehicles");
                                    int count=rsp.getInt("count");
                                    SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("name",name);
                                    editor.putString("mobile",mobile);
                                    editor.putString("email",email);
                                    editor.putString("user_id",user_id);
                                    editor.putInt("balance",balance);
                                    for(int i=0;i<count;i++){
                                        editor.putString("vehicle"+String.valueOf(i),vehicles.getString(i));
                                    }
                                    editor.putBoolean("logged",true);
                                    editor.putInt("count",count);
                                    editor.commit();

                                    md.dismiss();
                                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                                    startActivity(i);
                                    MDToast.makeText(getBaseContext(),"Success",1,MDToast.TYPE_SUCCESS);
                                }else{
                                    md.dismiss();
                                    MDToast.makeText(getBaseContext(),"Invalid Credentials",1,MDToast.TYPE_ERROR);
                                }

                            } catch (JSONException e) {

                                Log.d(TAG, e.toString());
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // Error handling
                    MDToast.makeText(getBaseContext(),"Something went wrong!",1,MDToast.TYPE_ERROR);
                    System.out.println("Something went wrong!");
                    error.printStackTrace();

                }
            });

// Add the request to the queue
            Volley.newRequestQueue(this).add(postRequest);

        }

    }

    public boolean isConnected() {
        /*try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        }catch (Exception e){
            return false;
        }*/
        return true;
    }
}
