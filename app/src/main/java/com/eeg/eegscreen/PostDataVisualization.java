package com.eeg.eegscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.eeg.filehandler.GetExternalDirectory;

public class PostDataVisualization extends AppCompatActivity {

    public static final int TIME_DOMAIN = 0;
    public static final int AEEG = 1;
    public static final int SPECT = 2;

    Button time_domain, aeeg, spectogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_data_visualization);

        time_domain = findViewById(R.id.time_domain);
        aeeg = findViewById(R.id.aeeg);
        spectogram = findViewById(R.id.spectrogram);

        time_domain.setOnClickListener(v -> PostDataVisualization.this.openTimeDomainData());

        aeeg.setOnClickListener(v -> PostDataVisualization.this.openAEEG());

        spectogram.setOnClickListener(v -> PostDataVisualization.this.openSpectrogram());
    }

    private void openSpectrogram() {
        Intent intent = new Intent(this, GetExternalDirectory.class);
        intent.putExtra("VIEW", SPECT);
        startActivity(intent);
    }

    private void openAEEG() {
        Intent intent = new Intent(this, GetExternalDirectory.class);
        intent.putExtra("VIEW", AEEG);
        startActivity(intent);
    }

    private void openTimeDomainData() {
        Intent intent = new Intent(this, GetExternalDirectory.class);
        intent.putExtra("VIEW", TIME_DOMAIN);
        startActivity(intent);
    }
}
