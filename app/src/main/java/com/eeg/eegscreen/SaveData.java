package com.eeg.eegscreen;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.eeg.datahandler.ConvertData;

import java.io.DataOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class SaveData extends AsyncTask<List<Float>, Void, Void> {

    @Override
    protected Void doInBackground(List<Float>... lists) {

        //Get filename from timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileName = String.valueOf(timestamp.getTime());

        // Create the file directory for current presentation
        File directory = new File(Environment.getExternalStorageDirectory(),"EEGFolder");
        File file = new File(directory, fileName);

        DataOutputStream dos;
        try {
            dos = new DataOutputStream(new FileOutputStream(file.getPath(),true));
            dos.writeChars(ConvertData.convertFloatArrayToString(lists));
            dos.write(System.getProperty("line.separator").getBytes());
            dos.close();
        } catch (FileNotFoundException e) {
            Log.e("Save to file AsyncTask", "Error while finding File");
        } catch (IOException e) {
//            e.printStackTrace();
            Log.e("Save to File AsyncTask", "Error in saving data");
        }
        return null;
    }
}
