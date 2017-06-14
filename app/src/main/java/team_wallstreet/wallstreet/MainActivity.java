package team_wallstreet.wallstreet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

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

import team_wallstreet.wallstreet.HttpReq.CookieHelper;
import team_wallstreet.wallstreet.HttpReq.RequestListener;
import team_wallstreet.wallstreet.HttpReq.RequestManager;
import team_wallstreet.wallstreet.Utilities.StockParser;

public class MainActivity extends AppCompatActivity {

    RequestManager requestManager;
    BroadcastReceiver receiver;
    Button search_button;
    EditText et_search;

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

                if(et_search.getText().toString().length() != 0) {
                    if (ch.getCookie() != null) {
                        requestManager = new RequestManager();
                        String url = "https://query1.finance.yahoo.com/v7/finance/download/"+et_search.getText().toString()+
                                "?period1=1337478873&period2=" + timestamp + "&interval=1d&events=history&crumb=" + ch.getCrumb();
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

                                sp = new StockParser(response, et_search.getText().toString());
                                createGraph(sp.getDateList(), sp.getCloseAdjList());
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

    private void createGraph(final ArrayList<Date> xVals, double[] yVals) {
        // Initialize plot reference.
        plot = (XYPlot) findViewById(R.id.plot);

        List<Number> list = null;

        // Convert double[] to List<java.lang.Double>;
        for (int i = 0; i < yVals.length; i++) {
            list.add(Double.valueOf(yVals[i]));
        }

        XYSeries series = new SimpleXYSeries(list,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Stock trend");

        LineAndPointFormatter seriesFormat = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);

        // Use a dashed line effect.
        seriesFormat.getLinePaint().setPathEffect(new DashPathEffect(new float[] {
                PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));

        // Use line smoothing
        seriesFormat.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // Add series to XYPlot.
        plot.addSeries(series, seriesFormat);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(xVals.get(i));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }
}
