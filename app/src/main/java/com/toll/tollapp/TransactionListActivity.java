package com.toll.tollapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

public class TransactionListActivity extends AppCompatActivity {

    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        Toolbar mytool=(Toolbar)findViewById(R.id.trans_list_toolbar);
        setSupportActionBar(mytool);
        getSupportActionBar().setTitle("My Transactions");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.trans_list);

        mAdapter = new NewsAdapter(newsList);
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
                        Intent i=new Intent(getBaseContext(),ReceiptActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );

    }


    private void prepareTransactionData() {
        News transaction = new News("Thane Toll Plaza", "MH 01 AZ 0123", "21/ 07 ");
        newsList.add(transaction);

        transaction = new News("Pune Toll Plaza", "MH 01 AZ 0123", "21/ 07 ");
        newsList.add(transaction);

        transaction = new News("Pune Toll Plaza", "MH 01 AZ 0123", "21/ 07 ");
        newsList.add(transaction);

        transaction = new News("Pune Toll Plaza", "MH 01 AZ 0123", "21/ 07 ");
        newsList.add(transaction);

        transaction = new News("Pune Toll Plaza", "MH 01 AZ 0123", "21/ 07 ");
        newsList.add(transaction);

        transaction = new News("Pune Toll Plaza", "MH 01 AZ 0123", "21/ 07 ");
        newsList.add(transaction);


        mAdapter.notifyDataSetChanged();


    }
}

