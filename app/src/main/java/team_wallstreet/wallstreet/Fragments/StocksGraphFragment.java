package team_wallstreet.wallstreet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import team_wallstreet.wallstreet.R;

/**
 * Created by anegrete on 6/17/2017.
 */

public class StocksGraphFragment extends Fragment {

    public static final String ARG_STOCK_NAME = "arg_stock_name";

    public interface passStockDataCallBack {
        List<Date> getDates();
        double[] getAdjClosed();
    }

    private LineChart mLineChart;
    private String mStockName;
    private passStockDataCallBack mPassData;

    public StocksGraphFragment(){
        //required empty
    }

    public static StocksGraphFragment newInstance(String stock){
        StocksGraphFragment fragment = new StocksGraphFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_STOCK_NAME, stock);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mStockName = getArguments().getString(ARG_STOCK_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initializeGraph();
    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        mPassData = (passStockDataCallBack) getActivity();
    }

    private void initializeGraph() {
        mLineChart = (LineChart) getView().findViewById(R.id.lineChart);

        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < mPassData.getDates().size() && i < mPassData.getAdjClosed().length; i++){
            entries.add(new Entry(
                    mPassData.getDates().get(i).getTime(), (float) mPassData.getAdjClosed()[i]
                    ));
        }


        LineDataSet dataSet = new LineDataSet(entries, mStockName);
        dataSet.setDrawCircles(false);
        dataSet.setColor(R.color.colorAccent);

        Legend legend = mLineChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setMaxSizePercent(.95f);


        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);

        final SimpleDateFormat formatter = new SimpleDateFormat("MMM");
        mLineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date((long) value);
                return formatter.format(date);
            }
        });


        mLineChart.setData(lineData);
        mLineChart.invalidate();
        Log.d("Finished", "Chart Created ");
    }
}
