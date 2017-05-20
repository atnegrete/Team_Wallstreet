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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                String response = null;
//                String fileName = intent.getStringExtra(RequestManager.RESULT_KEY);
//                File file;
//                try {
//                    InputStream inputStream = openFileInput("myfile.txt");
//
//                    file = File.createTempFile(fileName, null, context.getCacheDir());
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    String receiveString = "";
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    while ( (receiveString = bufferedReader.readLine()) != null ) {
//                        stringBuilder.append(receiveString);
//                    }
//
//                    inputStream.close();
//                    response = stringBuilder.toString();
//
//                    TextView tv = (TextView) findViewById(R.id.tv_search_result);
//                    tv.setText(response);
//
//                    System.out.print(response);
//
//                } catch (IOException e) {
//                    Log.e("FILE ERROR", "File Load Error: " + e.toString());
//                }
//
//
//            }
//        };
//
//        IntentFilter filter = new IntentFilter("SEARCH_FILTER");
//        getApplicationContext().registerReceiver(receiver, filter);


        // attempt to get cookie
        final CookieHelper ch = new CookieHelper();
        ch.getCookie(getApplicationContext());
        final String timestamp = "" + new Date().getTime();

        // set up on click listener for search button
        search_button = (Button) findViewById(R.id.b_search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch.getCookie() != null) {
                    requestManager = new RequestManager();
                    String url = "https://query1.finance.yahoo.com/v7/finance/download/SNAP?period1=1337478873&period2=" + timestamp + "&interval=1d&events=history&crumb=" + ch.getCrumb();
                    requestManager.makeRequest(getApplicationContext(), url, new RequestListener() {

                        @Override
                        public void onRequestComplete(final String response) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView tv = (TextView) findViewById(R.id.tv_search_result);
                                    tv.setText(response);
                                    Log.e("FILE ERROR", "File Load Error: " + response);

                                }
                            });
                        }
                    },ch.getCookie());
                }else{
                    TextView tv = (TextView) findViewById(R.id.tv_search_result);
                    tv.setText("Invalid Cookie Loaded");
                }
            }
        });
    }
}
