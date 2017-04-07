package com.toll.tollapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CarsAdapter mAdapter;


    TextView mobno,email,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mobno=(TextView)findViewById(R.id.tvNumber1);
        email=(TextView)findViewById(R.id.tvNumber2);
        name=(TextView)findViewById(R.id.tvNumber4);

        SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
        mobno.setText(sp.getString("mobile","(123) 456- 7890"));
        email.setText(sp.getString("email","rohit69@vj.com"));
        name.setText(sp.getString("name","Rohit Badugu"));


        recyclerView = (RecyclerView) findViewById(R.id.car_list);

        mAdapter = new CarsAdapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        prepareTransactionData();

        recyclerView.addOnItemTouchListener(
                new RecycleTouchListener(getApplicationContext(),recyclerView, new RecycleTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );



    }

    private void prepareTransactionData() {

        SharedPreferences sp=getSharedPreferences("toll",MODE_PRIVATE);
        int count=sp.getInt("count",0);
        News transaction=new News("MH 01 AN 014","Car","21/07");
        newsList.add(transaction);
        transaction=new News("MH 01 AN 014","Bike","21/07");
        newsList.add(transaction);
        transaction=new News("MH 01 AN 014","Car","15/07");
        newsList.add(transaction);
       /* for(int i=0;i<count ;i++){
            News transaction = new News(sp.getString("vehicle"+String.valueOf(i),"MH010100"), "MH 01 AZ 0123", "21/ 07 ");
            newsList.add(transaction);
        }*/


        mAdapter.notifyDataSetChanged();

    }
}
