package com.eeg.eegscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ElectrodeSelection extends AppCompatActivity {

    TextView textStatus;

    private boolean fp1_selected = false;
    private boolean isFp2_selected = false;
    private boolean isF7_selected = false;
    private boolean isF3_selected = false;
    private boolean isFz_selected = false;
    private boolean isF4_selected = false;
    private boolean isF8_selected = false;
    private boolean isA1_selected = false;
    private boolean isT3_selected = false;
    private boolean isC3_selected = false;
    private boolean isCz_selected = false;
    private boolean isC4_selected = false;
    private boolean isT4_selected = false;
    private boolean isA2_selected = false;
    private boolean isT5_selected = false;
    private boolean isP3_selected = false;
    private boolean isPz_selected = false;
    private boolean isP4_selected = false;
    private boolean isT6_selected = false;
    private boolean isO1_selected = false;
    private boolean isO2_selected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrode_selection);

        Bundle bundle = getIntent().getExtras();

        textStatus = findViewById(R.id.connectionStatus);
        textStatus.setText("Connected to : " + bundle.getString("DEVICE_NAME"));

        final Button fp1 = findViewById(R.id.fp1);

        fp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fp1_selected){
                    fp1.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    fp1_selected = false;
                } else {
                    fp1.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    fp1_selected = true;
                }
            }
        });

        final Button fp2 = findViewById(R.id.fp2);

        fp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFp2_selected){
                    fp2.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isFp2_selected = false;
                } else {
                    fp2.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isFp2_selected = true;
                }
            }
        });

        final Button f7 = findViewById(R.id.f7);

        f7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isF7_selected) {
                    f7.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isF7_selected = false;
                } else {
                    f7.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isF7_selected = true;
                }
            }
        });

        final Button f3 = findViewById(R.id.f3);

        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isF3_selected) {
                    f3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isF3_selected = false;
                } else {
                    f3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isF3_selected = true;
                }
            }
        });

        final Button fz = findViewById(R.id.fz);

        fz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFz_selected) {
                    fz.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isFz_selected = false;
                } else {
                    fz.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isFz_selected = true;
                }
            }
        });

        final Button f4 = findViewById(R.id.f4);

        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isF4_selected) {
                    f4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isF4_selected = false;
                } else {
                    f4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isF4_selected = true;
                }
            }
        });

        final Button f8 = findViewById(R.id.f8);

        f8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isF8_selected) {
                    f8.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isF8_selected = false;
                } else {
                    f8.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isF8_selected = true;
                }
            }
        });

        final Button A1 = findViewById(R.id.A1);

        A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isA1_selected) {
                    A1.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isA1_selected = false;
                } else {
                    A1.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isA1_selected = true;
                }
            }
        });

        final Button t3 = findViewById(R.id.t3);

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isT3_selected) {
                    t3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isT3_selected = false;
                } else {
                    t3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isT3_selected = true;
                }
            }
        });

        final Button c3 = findViewById(R.id.c3);

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC3_selected) {
                    c3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isC3_selected = false;
                } else {
                    c3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isC3_selected = true;
                }
            }
        });

        final Button cz = findViewById(R.id.cz);

        cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCz_selected) {
                    cz.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isCz_selected = false;
                } else {
                    cz.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isCz_selected = true;
                }
            }
        });

        final Button c4 = findViewById(R.id.c4);

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isC4_selected) {
                    c4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isC4_selected = false;
                } else {
                    c4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isC4_selected = true;
                }
            }
        });

        final Button t4 = findViewById(R.id.t4);

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isT4_selected) {
                    t4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isT4_selected = false;
                } else {
                    t4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isT4_selected = true;
                }
            }
        });

        final Button A2 = findViewById(R.id.A2);

        A2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isA2_selected) {
                    A2.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isA2_selected = false;
                } else {
                    A2.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isA2_selected = true;
                }
            }
        });

        final Button t5 = findViewById(R.id.t5);

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isT5_selected) {
                    t5.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isT5_selected = false;
                } else {
                    t5.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isT5_selected = true;
                }
            }
        });

        final Button p3 = findViewById(R.id.p3);

        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isP3_selected) {
                    p3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isP3_selected = false;
                } else {
                    p3.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isP3_selected = true;
                }
            }
        });

        final Button pz = findViewById(R.id.pz);

        pz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPz_selected) {
                    pz.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isPz_selected = false;
                } else {
                    pz.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isPz_selected = true;
                }
            }
        });

        final Button p4 = findViewById(R.id.p4);

        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isP4_selected) {
                    p4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isP4_selected = false;
                } else {
                    p4.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isP4_selected = true;
                }
            }
        });

        final Button t6 = findViewById(R.id.t6);

        t6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isT6_selected) {
                    t6.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isT6_selected = false;
                } else {
                    t6.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isT6_selected = true;
                }
            }
        });

        final Button O1 = findViewById(R.id.O1);

        O1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isO1_selected) {
                    O1.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isO1_selected = false;
                } else {
                    O1.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isO1_selected = true;
                }
            }
        });

        final Button O2 = findViewById(R.id.O2);

        O2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isO2_selected) {
                    O2.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes));
                    isO2_selected = false;
                } else {
                    O2.setBackground(getResources().getDrawable(R.drawable.virtual_electrodes_selected));
                    isO2_selected = true;
                }
            }
        });

        Button plotStart = findViewById(R.id.submit_electrode);

        plotStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElectrodeSelection.this.startRealtimePlotting();
            }
        });

    }

    private void startRealtimePlotting() {
        Intent realtimeIntent = new Intent(this, RealtimePlottingActivity.class);
        startActivity(realtimeIntent);
    }
}
