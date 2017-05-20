package team_wallstreet.wallstreet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import team_wallstreet.wallstreet.HttpReq.CookieHelper;
import team_wallstreet.wallstreet.HttpReq.RequestListener;
import team_wallstreet.wallstreet.HttpReq.RequestManager;

public class MainActivity extends AppCompatActivity {

    RequestManager requestManager;
    BroadcastReceiver receiver;
    Button search_button;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){

        // attempt to get cookie
        final CookieHelper ch = new CookieHelper();
        ch.getCookie(getApplicationContext());
        final String timestamp = "" + new Date().getTime();

        //get code
        et_search = (EditText) findViewById(R.id.et_search);

        // set up on click listener for search button
        search_button = (Button) findViewById(R.id.b_search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_search.getText().toString().length() != 0) {
                    if (ch.getCookie() != null) {
                        requestManager = new RequestManager();
                        String url = "https://query1.finance.yahoo.com/v7/finance/download/"+et_search.getText().toString()+"?period1=1337478873&period2=" + timestamp + "&interval=1d&events=history&crumb=" + ch.getCrumb();
                        requestManager.makeRequest(getApplicationContext(), url, new RequestListener() {

                            @Override
                            public void onRequestComplete(final String response) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        // response is the actual csv file as string, needs parsing
                                        TextView tv = (TextView) findViewById(R.id.tv_search_result);
                                        tv.setText(response);

                                    }
                                });
                            }
                        }, ch.getCookie());
                    } else {
                        TextView tv = (TextView) findViewById(R.id.tv_search_result);
                        tv.setText("Invalid Cookie Loaded");
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter code to search.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
