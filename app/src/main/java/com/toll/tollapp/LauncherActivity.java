package com.toll.tollapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import static android.R.id.message;

public class LauncherActivity extends AppCompatActivity {

     TextView name,email,mobno,car_no;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toll_launcher);
        setSupportActionBar(toolbar);

        MDToast.makeText(getBaseContext(), "Car Details are optional",1,MDToast.TYPE_INFO);

/*
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }*/



        //CAr no
        car_no=(TextView)findViewById(R.id.carno);


        spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_type, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //Name
        name=(TextView)findViewById(R.id.name);
        String simple = "Name ";
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        name.setHint(builder);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if( name.getText().toString().trim().equals("")){
                      name.setError("Required");

                    }
                }
            }
        });



        //e-mail
        SpannableStringBuilder builder1 = new SpannableStringBuilder();
        email=(TextView)findViewById(R.id.email);
        simple="E-mail ";
        builder1.append(simple);
        start = builder.length();
        builder1.append(colored);
        end = builder1.length();

        builder1.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        email.setHint(builder1);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if( email.getText().toString().trim().equals("")){
                        email.setError("Required");

                    }
                }
            }
        });




        //mobile
        SpannableStringBuilder builder2 = new SpannableStringBuilder();
        mobno=(TextView)findViewById(R.id.mobno);
        simple="Mobile No. ";
        builder2.append(simple);
        start = builder2.length();
        builder2.append(colored);
        end = builder2.length();

        builder2.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mobno.setHint(builder2);
        mobno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if( mobno.getText().toString().trim().equals("")){
                        mobno.setError("Required");

                    }
                }
            }
        });



        Button save=(Button)findViewById(R.id.buttonsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid=email.getText().toString().trim();
                String name1=name.getText().toString().trim();
                String mobile_no=mobno.getText().toString().trim();
                String msg=validate_form(name1,emailid,mobile_no);
                if(msg==null){
                    if(save_details(name1,emailid,mobile_no)){
                    MDToast.makeText(getBaseContext(), "Saved ",1,MDToast.TYPE_SUCCESS);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_right);}
                }else{
                    MDToast.makeText(getBaseContext(), msg,1,MDToast.TYPE_ERROR);

                }
            }
        });
    }

    private String validate_form(String name,String email,String mobno){
        if(name=="" && email=="" && mobno==""){
            return "Please fill required fields";
        }else{
            int index=email.indexOf('@');
            if(index!=-1){
                int dotindex=email.indexOf('.');
                if(dotindex!=-1){
                    if(dotindex-index < 2){
                        return "Please enter correct E-mail id";
                    }
                }else{
                    return "Please enter correct E-mail id";
                }
            }else{
                return "Please enter correct E-mail id";
            }
            if(mobno.length()!=10){
                return "Please enter correct mobile no.";
            }
        }
        return null;
    }

    private boolean save_details(String name,String email,String mobno){
        SharedPreferences sharedPref = this.getSharedPreferences("toll",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("mobile_no",mobno);
        if(!car_no.getText().toString().trim().equals("")){
            editor.putString("car_number",car_no.getText().toString().trim());
        }
        if(spinner.getSelectedItemPosition()!=1){
            editor.putInt("car_number",spinner.getSelectedItemPosition());
        }

       editor.commit();

        return true;
    }
}
