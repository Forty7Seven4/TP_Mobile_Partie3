package com.example.part3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.example.my_application.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MyBroadcastBatteryLow batteryLowReceiver;
    private IntentFilter batteryLowFilter;
    private TextView textView;

    private class MyBroadcastBatteryLow extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText("Evenement Batterie faible reçu");
        }

    }

    private TextView send;
    private MyBroadcastPhoneState phoneStateReceiver;
    private IntentFilter phoneStateFilter;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    private class MyBroadcastPhoneState extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String callState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (callState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Date callStartTime = new Date();
                send.setText("Appel entrant de " + number + " à " + callStartTime.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        batteryLowReceiver = new MyBroadcastBatteryLow();
        batteryLowFilter = new IntentFilter();
        batteryLowFilter.addAction(Intent.ACTION_BATTERY_LOW);


        send = findViewById(R.id.button_send_event);
        phoneStateReceiver = new MyBroadcastPhoneState();
        phoneStateFilter = new IntentFilter();
        phoneStateFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
    }

    @Override
    protected void onResume() {
        super.onResume();


        registerReceiver(batteryLowReceiver, batteryLowFilter);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }

        registerReceiver(phoneStateReceiver, phoneStateFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(batteryLowReceiver);
        unregisterReceiver(phoneStateReceiver);
}





}