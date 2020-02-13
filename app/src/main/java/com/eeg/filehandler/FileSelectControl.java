package com.eeg.filehandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eeg.eegscreen.R;

import java.io.File;

public class FileSelectControl extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PICK_FILE = 1;

    private TextView mFilePathTextView;
    private Button mStartActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select_control);

        // Set the views
        mFilePathTextView = findViewById(R.id.file_path_text_view);
        mStartActivityButton = findViewById(R.id.start_file_picker_button);

        mStartActivityButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.start_file_picker_button:
                // Create a new Intent for the file picker activity
                Intent intent = new Intent(this, FileSelectorActivity.class);

                 //Set the initial directory to be the sdcard
                intent.putExtra(FileSelectorActivity.EXTRA_FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath()+"/EEGFolder");

                 //Show hidden files
//                intent.putExtra(FileSelectorActivity.EXTRA_SHOW_HIDDEN_FILES, true);

                // Only make .png files visible
                //ArrayList<String> extensions = new ArrayList<String>();
                //extensions.add(".png");
                //intent.putExtra(FilePickerActivity.EXTRA_ACCEPTED_FILE_EXTENSIONS, extensions);

                // Start the activity
                startActivityForResult(intent, REQUEST_PICK_FILE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_FILE:
                    if (data.hasExtra(FileSelectorActivity.EXTRA_FILE_PATH)) {
                        // Get the file path
                        File f = new File(data.getStringExtra(FileSelectorActivity.EXTRA_FILE_PATH));

                        // Set the file path text view
                        mFilePathTextView.setText(f.getPath());
                    }
            }
        }
    }
}
