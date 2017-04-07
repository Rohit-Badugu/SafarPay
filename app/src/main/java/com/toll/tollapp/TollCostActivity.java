package com.toll.tollapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;


public class TollCostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_cost);

        Toolbar mytool=(Toolbar)findViewById(R.id.toll_cost_toolbar);
        setSupportActionBar(mytool);
        getSupportActionBar().setTitle(" Calculate Cost");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mytool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





      final MaterialDialog md= new MaterialDialog.Builder(this)
                .title("Loading Maps")
                .content("please_wait")
                .progress(true, 0)
                .show();

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Title")
                .content("Unable to load maps")
                .positiveText("CLOSE");


        final  WebView webview=(WebView)findViewById(R.id.webview_layout);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById(\"signin\").style.display=\"none\"; " +
                        "document.getElementsByClassName(\"divmainheader\")[0].style.display=\"none\"; " +
                        "document.getElementsByTagName(\"TABLE\")[3].style.display=\"none\"; "+
                        "})()");

                md.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){

                webview.destroy();
               builder.build();
               builder.show();


            }

        });



        webview.loadUrl("http://www.transportguru.in/TollCost.aspx");
    }
}
