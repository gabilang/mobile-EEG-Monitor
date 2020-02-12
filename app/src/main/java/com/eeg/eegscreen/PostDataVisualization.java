package com.eeg.eegscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PostDataVisualization extends AppCompatActivity {

    Button time_domain, aeeg, spectogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_data_visualization);

        time_domain = findViewById(R.id.time_domain);
        aeeg = findViewById(R.id.aeeg);
        spectogram = findViewById(R.id.spectrogram);

        time_domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDataVisualization.this.openTimeDomainData();
            }
        });

        aeeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDataVisualization.this.openAEEG();
            }
        });

        spectogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDataVisualization.this.openSpectrogram();
            }
        });
    }

    private void openSpectrogram() {
    }

    private void openAEEG() {
    }

    private void openTimeDomainData() {
        Intent intent = new Intent(this, TimeDomainViewActivity.class);
        startActivity(intent);
    }
}
