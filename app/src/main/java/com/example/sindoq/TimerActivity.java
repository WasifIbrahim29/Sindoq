package com.example.sindoq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

        blockApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TimerActivity.this,BlockAppsActivity.class);
                startActivity(intent);
            }
        });

    }
}
