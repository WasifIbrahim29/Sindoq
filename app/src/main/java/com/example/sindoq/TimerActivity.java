package com.example.sindoq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

public class TimerActivity extends Activity {

    LinearLayout timerLayout;
    Button blockApps;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        timerLayout= findViewById(R.id.timerLayout);
        blockApps= findViewById(R.id.blockApps);
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.PACKAGE_USAGE_STATS)!=PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new
                    Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);

        }



        //Register Broadcast
        IntentFilter filter = new IntentFilter("com.example.sindoq.intent.action.ACTION_SHOW_TOAST");

        BlockBroadcastReceiver receiver = new BlockBroadcastReceiver();
        registerReceiver(receiver, filter);


        blockApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TimerActivity.this,BlockAppsActivity.class);
                startActivity(intent);
            }
        });

    }
}
