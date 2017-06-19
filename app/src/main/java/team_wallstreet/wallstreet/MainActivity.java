package team_wallstreet.wallstreet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import team_wallstreet.wallstreet.Fragments.StocksGraphFragment;
import team_wallstreet.wallstreet.HttpReq.RequestListener;
import team_wallstreet.wallstreet.HttpReq.RequestManager;
import team_wallstreet.wallstreet.Utilities.StockParser;

import static team_wallstreet.wallstreet.SplashActivity.COOKIE_KEY;
import static team_wallstreet.wallstreet.SplashActivity.CRUMB_KEY;

public class MainActivity extends AppCompatActivity implements StocksGraphFragment.passStockDataCallBack {

    private static final String LOG_TAG = MainActivity.class.getName();

    private RequestManager mRequestManager;
    private String mCookie;
    private String mCrumb;
    private Button mButtonSearch;
    private EditText mEditTextSearch;
    private TextView mTextViewResult;
    private RelativeLayout mProgressBarLayout;
    private StockParser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get cookie & Crumb from SplashActivity intent
        mCookie = getIntent().getStringExtra(COOKIE_KEY);
        mCrumb = getIntent().getStringExtra(CRUMB_KEY);

        init();
    }

    void init(){

        mProgressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar);

        // get now's timestamp
        final String timestamp = "" + new Date().getTime();

        //get code
        mEditTextSearch = (EditText) findViewById(R.id.et_search);

        // set up on click listener for search button
        mButtonSearch = (Button) findViewById(R.id.b_search);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress bar
                mProgressBarLayout.setVisibility(View.VISIBLE);

                if(mEditTextSearch.getText().toString().length() != 0) {
                    if (mCookie != null) {
                        mRequestManager = new RequestManager();
                        String url = "https://query1.finance.yahoo.com/v7/finance/download/" + mEditTextSearch.getText().toString() +
                                "?period1=1337478873&period2=" + timestamp + "&interval=1d&events=history&crumb=" + mCrumb;
                        mRequestManager.makeRequest(getApplicationContext(), url, new RequestListener() {

                            @Override
                            public void onRequestComplete(final String response) {
                                
                                // Parse the response
                                sp = new StockParser(response, mEditTextSearch.getText().toString());

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


                        }, mCookie);
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

    /**
     * Replace R.id.fragment with StockGraphFragment
     */
    private void loadGraphFragment(){
        StocksGraphFragment fragment = StocksGraphFragment.newInstance(mEditTextSearch.getText().toString());
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
