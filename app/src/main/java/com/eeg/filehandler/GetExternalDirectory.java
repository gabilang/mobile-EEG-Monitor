package com.eeg.filehandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eeg.eegscreen.R;
import com.eeg.eegscreen.TimeDomainViewActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetExternalDirectory extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    ArrayList<String> myList;
    ListView listview;
    File dir;
    String fileName;

    private int raw_count = 0;
    private int count = 0;

    List<List<Float>> eegSession = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_external_directory);

        listview = findViewById(R.id.list);
        myList = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    dir = new File(Environment.getExternalStorageDirectory().
                            getAbsolutePath() + "/EEGFolder");
                    if (dir.exists()) {
                        Log.d("path", dir.toString());
                        File list[] = dir.listFiles();
                        for (int i = 0; i < list.length; i++) {
                            myList.add(list[i].getName());
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(GetExternalDirectory.this,
                                android.R.layout.simple_list_item_1, myList);
                        listview.setAdapter(arrayAdapter);

                    }
                } else {
                    requestPermission(); // Code for permission
                }
            } else {
                dir = new File(Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/");
                if (dir.exists()) {
                    Log.d("path", dir.toString());
                    File list[] = dir.listFiles();
                    for (int i = 0; i < list.length; i++) {
                        myList.add(list[i].getName());
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(GetExternalDirectory.this,
                            android.R.layout.simple_list_item_1, myList);
                    listview.setAdapter(arrayAdapter);
                }
            }
        }

        listview.setOnItemClickListener(mFileClickListener);

    }

    private AdapterView.OnItemClickListener mFileClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // read .csv file line by line here
            fileName = ((TextView) view).getText().toString();
            Log.d("info", fileName);

            openTimeDomain();

//            File inputFile = new File(dir, fileName);
//            Log.d("input", String.valueOf(inputFile));
//
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(inputFile));
//
//                String line;
//
//                while ((line = br.readLine()) != null) {
//                    // use comma as separator columns of CSV
//
//                    if(raw_count == 0) {
//                        String[] cols = line.split(",");
//                        raw_count++;
//                    } else {
//                        String[] cols = line.split(",");
//
//                        List<Float> rawValues = new ArrayList<>();
//                        for(int i=0; i<cols.length;i++){
//                            rawValues.add(i, Float.valueOf(cols[i]));
//                        }
//
//
//                        eegSession.add(rawValues);
//                    }
//
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //Log.d("raw :", String.valueOf(eegSession));
//            Intent intent = new Intent();
//            intent.putExtra("EEG", (Serializable) eegSession);
        }
    };

    private void openTimeDomain(){
        Intent intent = new Intent(this, TimeDomainViewActivity.class);
        intent.putExtra("Dir", dir.toString());
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(GetExternalDirectory.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(GetExternalDirectory.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(GetExternalDirectory.this,
                    "Write External Storage permission allows us to read  files. Please allow this permission in App Settings.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(GetExternalDirectory.this, new String[]
                    {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }
}
