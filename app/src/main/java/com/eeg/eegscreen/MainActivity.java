package com.eeg.eegscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eeg.bluetoothserial.BluetoothSPP;
import com.eeg.bluetoothserial.BluetoothState;
import com.eeg.bluetoothserial.DeviceList;

public class MainActivity extends AppCompatActivity {

    BluetoothSPP bt;

    Menu menu;

    TextView textStatus;

    private String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        textStatus = findViewById(R.id.connectionStatus);

        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.openPresentation();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.openPostRecordVisualization();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @SuppressLint("SetTextI18n")
            public void onDeviceDisconnected() {
                textStatus.setText("Status : Not connect");
                menu.clear();
                getMenuInflater().inflate(R.menu.bt_menu, menu);
                button1.setEnabled(false);
                deviceName = "None";
            }

            @SuppressLint("SetTextI18n")
            public void onDeviceConnectionFailed() {
                textStatus.setText("Status : Connection failed");
                button1.setEnabled(false);
            }

            @SuppressLint("SetTextI18n")
            public void onDeviceConnected(String name, String address) {
                deviceName = name;
                textStatus.setText("Status : Connected to " + name);
                menu.clear();
                getMenuInflater().inflate(R.menu.bt_menu_disconnect, menu);
                button1.setEnabled(true);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
        System.out.println("service destroyed");
    }

    public void onStart() {
        super.onStart();
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
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPresentation() {
        Intent presentationIntent = new Intent(this, ElectrodeSelection.class);
        presentationIntent.putExtra("DEVICE_NAME", deviceName);
        startActivity(presentationIntent);
    }

    private void openPostRecordVisualization() {
        Intent postRecordIntent = new Intent(this, PostDataVisualization.class);
        startActivity(postRecordIntent);
    }
}
