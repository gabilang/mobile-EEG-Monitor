package com.eeg.filehandler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.eeg.eegscreen.R;

public class FileList extends AppCompatActivity {

    private ArrayAdapter<String> mFileArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
    }
}
