package com.toll.tollapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Toolbar mytool=(Toolbar)findViewById(R.id.receipt_toolbar);
        setSupportActionBar(mytool);
        getSupportActionBar().setTitle(" E- Receipt ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
