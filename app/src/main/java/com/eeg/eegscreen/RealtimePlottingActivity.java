package com.eeg.eegscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.eeg.bluetoothserial.BluetoothSPP;
import com.eeg.bluetoothserial.BluetoothState;
import com.eeg.bluetoothserial.DeviceList;
import com.eeg.datahandler.ConvertData;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RealtimePlottingActivity extends AppCompatActivity implements OnChartGestureListener,
        OnChartValueSelectedListener {

    BluetoothSPP bt;

    Menu menu;

    TextView textStatus;

    private final static String TAG = RealtimePlottingActivity.class.getSimpleName();

    private LineChart m1Chart;
    private LineChart m2Chart;
    private LineChart m3Chart;
    private LineChart m4Chart;
    private LineChart m5Chart;
    private LineChart m6Chart;
    private LineChart m7Chart;
    private LineChart m8Chart;

    private final float DATAPOINT_TIME = 1.5f;  // needed to adjust while testing
    private final int PLOT_MEMO = 3000;  // max time range in ms (x value) to store on plot

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

    // File Name for each presentations
    private String mFileNamePrefix = "eeg";
    private String mFileExtension = ".csv"; // have to change it as ".edf"
    private String mFileNameSuffix = "";
    private String defaultFileName = "eegTest1.txt";

    private Intent openTextFileIntent = new Intent(Intent.ACTION_VIEW);
    private File directory = new File(
            Environment.getExternalStorageDirectory(), "EEGFolder");

    private float resolution_time;
    private float sampling_freq;
    private int count = 0;

    private List<float[]> main_data;
    private float data_count = 0;
    private long start_data = 0;
    private String start_time;
    private String end_time;
    private long start_watch;
    private String recording_time;

    private final List<List<Float>> accumulated = new ArrayList<>();
    private final List<Float> dp_received = new ArrayList<>();

    private boolean plotStart = false;

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_plotting);

        Log.i("Check", "onCreate");

        bt = new BluetoothSPP(this);

        textStatus = findViewById(R.id.connectionStatus);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }


        bt.setOnDataReceivedListener((data, message) -> {
            data_count++;
            System.out.println(data_count);
            System.out.println(data);
            if(plotStart) {
                menu.clear();
                getMenuInflater().inflate(R.menu.plot_stop, menu);
                List<Float> microV = extractChannelData(data);
                if (accumulated.size() < 11) {
                    if (microV.size() == 8) {
                        accumulated.add(microV);
                    }
                } else {
                    System.out.println(accumulated);
                    addDataEntries(accumulated);
                    accumulated.clear();
                }
//            new SaveData().execute(microV);
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @SuppressLint("SetTextI18n")
            public void onDeviceDisconnected() {
                textStatus.setText("Status : Not connect");
//                button1.setEnabled(false);
            }

            @SuppressLint("SetTextI18n")
            public void onDeviceConnectionFailed() {
                textStatus.setText("Status : Connection failed");
//                button1.setEnabled(false);
            }

            @SuppressLint("SetTextI18n")
            public void onDeviceConnected(String name, String address) {
                textStatus.setText("Status : Connected to " + name);
                menu.clear();
                getMenuInflater().inflate(R.menu.bt_menu_disconnect, menu);
//                button1.setEnabled(true);
            }
        });



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
    }

    private List<Float> extractChannelData(byte[] data) {

        List<Float> channelData = new ArrayList<>();

        if(data.length == 28) {

            // Data from 8-channels, each data 3 bytes (24 bit resolution)
            byte[] signalChannel1 = Arrays.copyOfRange(data, 3, 6);
            byte[] signalChannel2 = Arrays.copyOfRange(data, 6, 9);
            byte[] signalChannel3 = Arrays.copyOfRange(data, 9, 12);
            byte[] signalChannel4 = Arrays.copyOfRange(data, 12, 15);
            byte[] signalChannel5 = Arrays.copyOfRange(data, 15, 18);
            byte[] signalChannel6 = Arrays.copyOfRange(data, 18, 21);
            byte[] signalChannel7 = Arrays.copyOfRange(data, 21, 24);
            byte[] signalChannel8 = Arrays.copyOfRange(data, 24, 27);


            channelData.add(0, ConvertData.convertByteToMicroVolts(signalChannel1));
            channelData.add(1, ConvertData.convertByteToMicroVolts(signalChannel2));
            channelData.add(2, ConvertData.convertByteToMicroVolts(signalChannel3));
            channelData.add(3, ConvertData.convertByteToMicroVolts(signalChannel4));
            channelData.add(4, ConvertData.convertByteToMicroVolts(signalChannel5));
            channelData.add(5, ConvertData.convertByteToMicroVolts(signalChannel6));
            channelData.add(6, ConvertData.convertByteToMicroVolts(signalChannel7));
            channelData.add(7, ConvertData.convertByteToMicroVolts(signalChannel8));

        }
        return channelData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
        System.out.println("service destroyed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get filename for each session
        defaultFileName = getFileNameForSession();

        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.bt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.bt_connect) {
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);

        } else if(id == R.id.bt_disconnect){
            if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bt.disconnect();
                menu.clear();
                getMenuInflater().inflate(R.menu.bt_menu, menu);
            }
        } else if(id == R.id.plot_start){
            bt.send("B", true);
            plotStart = true;
            menu.clear();
            getMenuInflater().inflate(R.menu.plot_stop, menu);
        } else if(id == R.id.plot_stop){
            bt.send("A", true);
            plotStart = false;
            menu.clear();
            getMenuInflater().inflate(R.menu.bt_menu_disconnect, menu);
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * Add entries to chart
     * @param
     * @param
     */

    private void addDataEntries(final List<List<Float>> entryList){

        float x = 0;
        for (List<Float> f : entryList) {
            count++;
            x = count * DATAPOINT_TIME;
            lineEntries1.add(new Entry(x, f.get(0)));
            lineEntries2.add(new Entry(x, f.get(1)));
            lineEntries3.add(new Entry(x, f.get(2)));
            lineEntries4.add(new Entry(x, f.get(3)));
            lineEntries5.add(new Entry(x, f.get(4)));
            lineEntries6.add(new Entry(x, f.get(5)));
            lineEntries7.add(new Entry(x, f.get(6)));
            lineEntries8.add(new Entry(x, f.get(7)));
        }
        final float f_x = x;
        if(thread != null) thread.interrupt();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LineDataSet set1 = createDataSet1(lineEntries1, show_ch1);
                LineData m1LineData = new LineData(set1);
                m1LineData.notifyDataChanged();
                m1Chart.setData(m1LineData);
                m1Chart.notifyDataSetChanged();
                m1Chart.moveViewToX(f_x);

                LineDataSet set2 = createDataSet2(lineEntries2, show_ch2);
                LineData m2LineData = new LineData(set2);
                m2LineData.notifyDataChanged();
                m2Chart.setData(m2LineData);
                m2Chart.notifyDataSetChanged();
                m2Chart.moveViewToX(f_x);

                LineDataSet set3 = createDataSet3(lineEntries3, show_ch3);
                LineData m3LineData = new LineData(set3);
                m3LineData.notifyDataChanged();
                m3Chart.setData(m3LineData);
                m3Chart.notifyDataSetChanged();
                m3Chart.moveViewToX(f_x);

                LineDataSet set4 = createDataSet4(lineEntries4, show_ch4);
                LineData m4LineData = new LineData(set4);
                m4LineData.notifyDataChanged();
                m4Chart.setData(m4LineData);
                m4Chart.notifyDataSetChanged();
                m4Chart.moveViewToX(f_x);

                LineDataSet set5 = createDataSet5(lineEntries5, show_ch5);
                LineData m5LineData = new LineData(set5);
                m5LineData.notifyDataChanged();
                m5Chart.setData(m5LineData);
                m5Chart.notifyDataSetChanged();
                m5Chart.moveViewToX(f_x);

                LineDataSet set6 = createDataSet6(lineEntries6, show_ch6);
                LineData m6LineData = new LineData(set6);
                m6LineData.notifyDataChanged();
                m6Chart.setData(m6LineData);
                m6Chart.notifyDataSetChanged();
                m6Chart.moveViewToX(f_x);

                LineDataSet set7 = createDataSet7(lineEntries7, show_ch7);
                LineData m7LineData = new LineData(set7);
                m7LineData.notifyDataChanged();
                m7Chart.setData(m7LineData);
                m7Chart.notifyDataSetChanged();
                m7Chart.moveViewToX(f_x);

                LineDataSet set8 = createDataSet8(lineEntries8, show_ch8);
                LineData m8LineData = new LineData(set8);
                m8LineData.notifyDataChanged();
                m8Chart.setData(m8LineData);
                m8Chart.notifyDataSetChanged();
                m8Chart.moveViewToX(f_x);



            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(runnable);
            }
        });
        thread.start();
        if (x > PLOT_MEMO) {
            for (int j = 0; j < entryList.size(); j++) {
                // to consider 8 channels
                for (int i = 0; i < m1Chart.getData().getDataSetCount(); i++) {
                    m1Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m2Chart.getData().getDataSetCount(); i++) {
                    m2Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m3Chart.getData().getDataSetCount(); i++) {
                    m3Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m4Chart.getData().getDataSetCount(); i++) {
                    m4Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m5Chart.getData().getDataSetCount(); i++) {
                    m5Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m6Chart.getData().getDataSetCount(); i++) {
                    m6Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m7Chart.getData().getDataSetCount(); i++) {
                    m7Chart.getData().getDataSetByIndex(i).removeFirst();
                }
                for (int i = 0; i < m8Chart.getData().getDataSetCount(); i++) {
                    m8Chart.getData().getDataSetByIndex(i).removeFirst();
                }
            }


        }
    }

    // Get filename for current session
    private String getFileNameForSession() {
        directory.mkdir();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        Date now = new Date();
        mFileNameSuffix = dateFormatter.format(now);
        String fileName = mFileNamePrefix + mFileNameSuffix + mFileExtension;
        return fileName;
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
