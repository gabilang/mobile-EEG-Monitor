package com.eeg.eegscreen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TimeDomainViewActivity extends AppCompatActivity implements OnChartGestureListener,
        OnChartValueSelectedListener {

    private LineChart m1Chart;
    private LineChart m2Chart;
    private LineChart m3Chart;
    private LineChart m4Chart;
    private LineChart m5Chart;
    private LineChart m6Chart;
    private LineChart m7Chart;
    private LineChart m8Chart;


    private final ArrayList<Entry> lineEntries1 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries2 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries3 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries4 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries5 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries6 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries7 = new ArrayList<>();
    private final ArrayList<Entry> lineEntries8 = new ArrayList<>();

    private boolean show_ch1 = true;
    private boolean show_ch2 = true;
    private boolean show_ch3 = true;
    private boolean show_ch4 = true;
    private boolean show_ch5 = true;
    private boolean show_ch6 = true;
    private boolean show_ch7 = true;
    private boolean show_ch8 = true;

    private int raw_count = 0;
    private int count = 0;

    private final List<List<Float>> eegSession = new ArrayList<>();

    InputStream inputStream;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_domain_view);

        m1Chart = findViewById(R.id.channel1);
        m2Chart = findViewById(R.id.channel2);
        m3Chart = findViewById(R.id.channel3);
        m4Chart = findViewById(R.id.channel4);
        m5Chart = findViewById(R.id.channel5);
        m6Chart = findViewById(R.id.channel6);
        m7Chart = findViewById(R.id.channel7);
        m8Chart = findViewById(R.id.channel8);

        setM1Chart();
        setM2Chart();
        setM3Chart();
        setM4Chart();
        setM5Chart();
        setM6Chart();
        setM7Chart();
        setM8Chart();

        try {
            readDataByColumn();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addDataEntries(eegSession);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<List<Float>> readDataByColumn() throws FileNotFoundException {

        File dir = Environment.getExternalStorageDirectory();
        File inputFile = new File(dir, "EEGFolder/sample_8channel.csv");

//        inputStream = getResources().openRawResource(R.raw.sample_8channel);
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        // Initialization
        String line = "";

        // Handling exceptions
        try {
            // If buffer is not empty
            while ((line = br.readLine()) != null) {
                // use comma as separator columns of CSV

                if(raw_count == 0) {
                    String[] cols = line.split(",");
                    raw_count++;
                } else {
                    String[] cols = line.split(",");

                    List<Float> rawValues = new ArrayList<>();
                    for(int i=0; i<cols.length;i++){
                        rawValues.add(i, Float.valueOf(cols[i]));
                    }

                    System.out.println(rawValues);
                    eegSession.add(rawValues);
                }

            }
        } catch (IOException e) {
            // Prints throwable details
            e.printStackTrace();
        }
        return eegSession;
    }



    private void setM1Chart(){
        OnChartValueSelectedListener m1OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m1Chart = findViewById(R.id.channel1);
        m1Chart.setOnChartValueSelectedListener(m1OL);
        m1Chart.getDescription().setEnabled(false);
        m1Chart.setScaleEnabled(true);
        m1Chart.setTouchEnabled(true);
        m1Chart.setDragEnabled(true);
        m1Chart.setPinchZoom(true);
        m1Chart.setDrawGridBackground(false);
        m1Chart.setDrawBorders(true);
        m1Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis1 = m1Chart.getAxisLeft();
        leftAxis1.setEnabled(false);
        YAxis rightAxis1 = m1Chart.getAxisRight();
        rightAxis1.setEnabled(false);
        XAxis xAxis1 = m1Chart.getXAxis();
        xAxis1.setEnabled(false);

        //hide legends
        Legend legend1 = m1Chart.getLegend();
        legend1.setEnabled(false);

        //set data of channel 1
        LineData m1Data = new LineData();
        m1Data.setValueTextColor(Color.BLACK);
        //add empty data
        m1Chart.setData(m1Data);

        m1Chart.invalidate();
    }

    private void setM2Chart(){
        OnChartValueSelectedListener m2OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m2Chart = findViewById(R.id.channel2);
        m2Chart.setOnChartValueSelectedListener(m2OL);
        m2Chart.getDescription().setEnabled(false);
        m2Chart.setScaleEnabled(true);
        m2Chart.setTouchEnabled(true);
        m2Chart.setDragEnabled(true);
        m2Chart.setPinchZoom(true);
        m2Chart.setDrawGridBackground(false);
        m2Chart.setDrawBorders(true);
        m2Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis2 = m2Chart.getAxisLeft();
        leftAxis2.setEnabled(false);
        YAxis rightAxis2 = m2Chart.getAxisRight();
        rightAxis2.setEnabled(false);
        XAxis xAxis2 = m2Chart.getXAxis();
        xAxis2.setEnabled(false);

        //hide legends
        Legend legend2 = m2Chart.getLegend();
        legend2.setEnabled(false);

        //set data of channel 1
        LineData m2Data = new LineData();
        m2Data.setValueTextColor(Color.BLACK);
        //add empty data
        m2Chart.setData(m2Data);

        m2Chart.invalidate();
    }

    private void setM3Chart(){
        OnChartValueSelectedListener m3OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m3Chart = findViewById(R.id.channel3);
        m3Chart.setOnChartValueSelectedListener(m3OL);
        m3Chart.getDescription().setEnabled(false);
        m3Chart.setScaleEnabled(true);
        m3Chart.setTouchEnabled(true);
        m3Chart.setDragEnabled(true);
        m3Chart.setPinchZoom(true);
        m3Chart.setDrawGridBackground(false);
        m3Chart.setDrawBorders(true);
        m3Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis3 = m3Chart.getAxisLeft();
        leftAxis3.setEnabled(false);
        YAxis rightAxis3 = m3Chart.getAxisRight();
        rightAxis3.setEnabled(false);
        XAxis xAxis3 = m3Chart.getXAxis();
        xAxis3.setEnabled(false);

        //hide legends
        Legend legend3 = m3Chart.getLegend();
        legend3.setEnabled(false);

        //set data of channel 1
        LineData m3Data = new LineData();
        m3Data.setValueTextColor(Color.BLACK);
        //add empty data
        m3Chart.setData(m3Data);

        m3Chart.invalidate();
    }

    private void setM4Chart(){
        OnChartValueSelectedListener m4OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m4Chart = findViewById(R.id.channel4);
        m4Chart.setOnChartValueSelectedListener(m4OL);
        m4Chart.getDescription().setEnabled(false);
        m4Chart.setScaleEnabled(true);
        m4Chart.setTouchEnabled(true);
        m4Chart.setDragEnabled(true);
        m4Chart.setPinchZoom(true);
        m4Chart.setDrawGridBackground(false);
        m4Chart.setDrawBorders(true);
        m4Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis4 = m4Chart.getAxisLeft();
        leftAxis4.setEnabled(false);
        YAxis rightAxis4 = m4Chart.getAxisRight();
        rightAxis4.setEnabled(false);
        XAxis xAxis4 = m4Chart.getXAxis();
        xAxis4.setEnabled(false);

        //hide legends
        Legend legend4 = m1Chart.getLegend();
        legend4.setEnabled(false);

        //set data of channel 1
        LineData m4Data = new LineData();
        m4Data.setValueTextColor(Color.BLACK);
        //add empty data
        m4Chart.setData(m4Data);

        m4Chart.invalidate();
    }

    private void setM5Chart(){
        OnChartValueSelectedListener m5OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m5Chart = findViewById(R.id.channel5);
        m5Chart.setOnChartValueSelectedListener(m5OL);
        m5Chart.getDescription().setEnabled(false);
        m5Chart.setScaleEnabled(true);
        m5Chart.setTouchEnabled(true);
        m5Chart.setDragEnabled(true);
        m5Chart.setPinchZoom(true);
        m5Chart.setDrawGridBackground(false);
        m5Chart.setDrawBorders(true);
        m5Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis5 = m5Chart.getAxisLeft();
        leftAxis5.setEnabled(false);
        YAxis rightAxis5 = m5Chart.getAxisRight();
        rightAxis5.setEnabled(false);
        XAxis xAxis5 = m5Chart.getXAxis();
        xAxis5.setEnabled(false);

        //hide legends
        Legend legend5 = m5Chart.getLegend();
        legend5.setEnabled(false);

        //set data of channel 1
        LineData m5Data = new LineData();
        m5Data.setValueTextColor(Color.BLACK);
        //add empty data
        m5Chart.setData(m5Data);

        m5Chart.invalidate();
    }

    private void setM6Chart(){
        OnChartValueSelectedListener m6OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m6Chart = findViewById(R.id.channel6);
        m6Chart.setOnChartValueSelectedListener(m6OL);
        m6Chart.getDescription().setEnabled(false);
        m6Chart.setScaleEnabled(true);
        m6Chart.setTouchEnabled(true);
        m6Chart.setDragEnabled(true);
        m6Chart.setPinchZoom(true);
        m6Chart.setDrawGridBackground(false);
        m6Chart.setDrawBorders(true);
        m6Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis6 = m6Chart.getAxisLeft();
        leftAxis6.setEnabled(false);
        YAxis rightAxis6 = m6Chart.getAxisRight();
        rightAxis6.setEnabled(false);
        XAxis xAxis6 = m6Chart.getXAxis();
        xAxis6.setEnabled(false);

        //hide legends
        Legend legend6 = m6Chart.getLegend();
        legend6.setEnabled(false);

        //set data of channel 1
        LineData m6Data = new LineData();
        m6Data.setValueTextColor(Color.BLACK);
        //add empty data
        m6Chart.setData(m6Data);

        m6Chart.invalidate();
    }

    private void setM7Chart(){
        OnChartValueSelectedListener m7OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m7Chart = findViewById(R.id.channel7);
        m7Chart.setOnChartValueSelectedListener(m7OL);
        m7Chart.getDescription().setEnabled(false);
        m7Chart.setScaleEnabled(true);
        m7Chart.setTouchEnabled(true);
        m7Chart.setDragEnabled(true);
        m7Chart.setPinchZoom(true);
        m7Chart.setDrawGridBackground(false);
        m7Chart.setDrawBorders(true);
        m7Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis7 = m7Chart.getAxisLeft();
        leftAxis7.setEnabled(false);
        YAxis rightAxis7 = m7Chart.getAxisRight();
        rightAxis7.setEnabled(false);
        XAxis xAxis7 = m7Chart.getXAxis();
        xAxis7.setEnabled(false);

        //hide legends
        Legend legend7 = m7Chart.getLegend();
        legend7.setEnabled(false);

        //set data of channel 1
        LineData m7Data = new LineData();
        m7Data.setValueTextColor(Color.BLACK);
        //add empty data
        m7Chart.setData(m7Data);

        m7Chart.invalidate();
    }

    private void setM8Chart(){
        OnChartValueSelectedListener m8OL = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //e.getData();
                //returns null
            }

            @Override
            public void onNothingSelected() {
                // add code if needed
            }
        };

        m8Chart = findViewById(R.id.channel8);
        m8Chart.setOnChartValueSelectedListener(m8OL);
        m8Chart.getDescription().setEnabled(false);
        m8Chart.setScaleEnabled(true);
        m8Chart.setTouchEnabled(true);
        m8Chart.setDragEnabled(true);
        m8Chart.setPinchZoom(true);
        m8Chart.setDrawGridBackground(false);
        m8Chart.setDrawBorders(true);
        m8Chart.setAutoScaleMinMaxEnabled(true);

        //remove axis
        YAxis leftAxis8 = m8Chart.getAxisLeft();
        leftAxis8.setEnabled(false);
        YAxis rightAxis8 = m8Chart.getAxisRight();
        rightAxis8.setEnabled(false);
        XAxis xAxis8 = m8Chart.getXAxis();
        xAxis8.setEnabled(false);

        //hide legends
        Legend legend8 = m8Chart.getLegend();
        legend8.setEnabled(false);

        //set data of channel 1
        LineData m8Data = new LineData();
        m8Data.setValueTextColor(Color.BLACK);
        //add empty data
        m8Chart.setData(m8Data);

        m8Chart.invalidate();
    }

    /**
     * Set relevant data for each channel
     * @param entryArrayList
     * @param show
     */

    private LineDataSet createDataSet1(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set1 = new LineDataSet(entryArrayList, "Ch1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setDrawCircles(false);
        set1.setLineWidth(1f);
        set1.setVisible(show);
        return set1;
    }


    private LineDataSet createDataSet2(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set2 = new LineDataSet(entryArrayList, "Ch2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setDrawCircles(false);
        set2.setLineWidth(1f);
        set2.setVisible(show);
        return set2;
    }


    private LineDataSet createDataSet3(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set3 = new LineDataSet(entryArrayList, "Ch3");
        set3.setLineWidth(1f);
        set3.setDrawCircles(false);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setVisible(show);
        return set3;
    }


    private LineDataSet createDataSet4(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set4 = new LineDataSet(entryArrayList, "Ch4");
        set4.setAxisDependency(YAxis.AxisDependency.LEFT);
        set4.setDrawCircles(false);
        set4.setLineWidth(1f);
        set4.setVisible(show);
        return set4;
    }


    private LineDataSet createDataSet5(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set5 = new LineDataSet(entryArrayList, "Ch5");
        set5.setAxisDependency(YAxis.AxisDependency.LEFT);
        set5.setDrawCircles(false);
        set5.setLineWidth(1f);
        set5.setVisible(show);
        return set5;
    }

    private LineDataSet createDataSet6(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set6 = new LineDataSet(entryArrayList, "Ch6");
        set6.setAxisDependency(YAxis.AxisDependency.LEFT);
        set6.setLineWidth(1f);
        set6.setDrawCircles(false);
        set6.setVisible(show);
        return set6;
    }

    private LineDataSet createDataSet7(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set7 = new LineDataSet(entryArrayList, "Ch7");
        set7.setAxisDependency(YAxis.AxisDependency.LEFT);
        set7.setDrawCircles(false);
        set7.setLineWidth(1f);
        set7.setVisible(show);
        return set7;
    }

    private LineDataSet createDataSet8(ArrayList<Entry> entryArrayList, boolean show) {
        LineDataSet set8 = new LineDataSet(entryArrayList, "Ch8");
        set8.setAxisDependency(YAxis.AxisDependency.LEFT);
        set8.setDrawCircles(false);
        set8.setLineWidth(1f);
        set8.setVisible(show);
        return set8;
    }

    private void addDataEntries(final List<List<Float>> entryList) {

        float x = 0;
        for (List<Float> f : entryList) {
            count++;
            x = count;
            lineEntries1.add(new Entry(x, f.get(0)));
            lineEntries2.add(new Entry(x, f.get(1)));
            lineEntries3.add(new Entry(x, f.get(2)));
            lineEntries4.add(new Entry(x, f.get(3)));
            lineEntries5.add(new Entry(x, f.get(4)));
            lineEntries6.add(new Entry(x, f.get(5)));
            lineEntries7.add(new Entry(x, f.get(6)));
            lineEntries8.add(new Entry(x, f.get(7)));
        }
        final double f_x = x;

        LineDataSet set1 = createDataSet1(lineEntries1, show_ch1);
        LineData m1LineData = new LineData(set1);
        m1LineData.notifyDataChanged();
        m1Chart.setData(m1LineData);
        m1Chart.notifyDataSetChanged();
        m1Chart.moveViewToX((float) f_x);

        LineDataSet set2 = createDataSet2(lineEntries2, show_ch2);
        LineData m2LineData = new LineData(set2);
        m2LineData.notifyDataChanged();
        m2Chart.setData(m2LineData);
        m2Chart.notifyDataSetChanged();
        m2Chart.moveViewToX((float) f_x);

        LineDataSet set3 = createDataSet3(lineEntries3, show_ch3);
        LineData m3LineData = new LineData(set3);
        m3LineData.notifyDataChanged();
        m3Chart.setData(m3LineData);
        m3Chart.notifyDataSetChanged();
        m3Chart.moveViewToX((float) f_x);

        LineDataSet set4 = createDataSet4(lineEntries4, show_ch4);
        LineData m4LineData = new LineData(set4);
        m4LineData.notifyDataChanged();
        m4Chart.setData(m4LineData);
        m4Chart.notifyDataSetChanged();
        m4Chart.moveViewToX((float) f_x);

        LineDataSet set5 = createDataSet5(lineEntries5, show_ch5);
        LineData m5LineData = new LineData(set5);
        m5LineData.notifyDataChanged();
        m5Chart.setData(m5LineData);
        m5Chart.notifyDataSetChanged();
        m5Chart.moveViewToX((float) f_x);

        LineDataSet set6 = createDataSet6(lineEntries6, show_ch6);
        LineData m6LineData = new LineData(set6);
        m6LineData.notifyDataChanged();
        m6Chart.setData(m6LineData);
        m6Chart.notifyDataSetChanged();
        m6Chart.moveViewToX((float) f_x);

        LineDataSet set7 = createDataSet7(lineEntries7, show_ch7);
        LineData m7LineData = new LineData(set7);
        m7LineData.notifyDataChanged();
        m7Chart.setData(m7LineData);
        m7Chart.notifyDataSetChanged();
        m7Chart.moveViewToX((float) f_x);

        LineDataSet set8 = createDataSet8(lineEntries8, show_ch8);
        LineData m8LineData = new LineData(set8);
        m8LineData.notifyDataChanged();
        m8Chart.setData(m8LineData);
        m8Chart.notifyDataSetChanged();
        m8Chart.moveViewToX((float) f_x);
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
