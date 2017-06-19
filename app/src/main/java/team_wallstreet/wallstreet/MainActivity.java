package team_wallstreet.wallstreet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import team_wallstreet.wallstreet.Fragments.StocksGraphFragment;
import team_wallstreet.wallstreet.HttpReq.CookieHelper;
import team_wallstreet.wallstreet.HttpReq.RequestListener;
import team_wallstreet.wallstreet.HttpReq.RequestManager;
import team_wallstreet.wallstreet.Utilities.StockParser;

public class MainActivity extends AppCompatActivity implements StocksGraphFragment.passStockDataCallBack {

    RequestManager requestManager;
    BroadcastReceiver receiver;
    Button search_button;
    EditText et_search;
    private LineChart mLineChart;
    private RelativeLayout mProgressBarLayout;

    private XYPlot plot;
    private StockParser sp;

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

                // Initialize the Request if the Input is valid
                if(!et_search.getText().toString().isEmpty()) {

                    // Show loading circle
                    mProgressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar);
                    mProgressBarLayout.setVisibility(View.VISIBLE);

                    if (ch.getCookie() != null) {
                        requestManager = new RequestManager();
                        String url = "https://query1.finance.yahoo.com/v7/finance/download/"+et_search.getText().toString()+
                                "?period1=1465924420&period2=" + timestamp + "&interval=1d&events=history&crumb=" + ch.getCrumb();
                        requestManager.makeRequest(getApplicationContext(), url, new RequestListener() {

                            @Override
                            public void onRequestComplete(final String response) {

                                // Parse the response
                                sp = new StockParser(response, et_search.getText().toString());

                                // Show the Graph
                                loadGraphFragment();

                                // Hide progress bar
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBarLayout.setVisibility(View.GONE);
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

    private void loadGraphFragment(){
        StocksGraphFragment fragment = StocksGraphFragment.newInstance(et_search.getText().toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    public List<Date> getDates() {
        return sp.getDateList();
    }

    @Override
    public double[] getAdjClosed() {
        return sp.getCloseAdjList();
    }
}
