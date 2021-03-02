package com.example.sindoq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.sindoq.Database.DatabaseHelper;

public class TimerActivity extends Activity {

    LinearLayout timerLayout;
    Button blockApps;
    DatabaseHelper databaseHelper;
    EditText hour;
    EditText seconds;
    EditText day;
    EditText minutes;
    int days,mins,secs,hrs;
    int res_day,res_hr,res_min,res_sec;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        timerLayout= findViewById(R.id.timerLayout);
        blockApps= findViewById(R.id.blockApps);

        databaseHelper=new DatabaseHelper(this);

        day=findViewById(R.id.days);
        hour=findViewById(R.id.hour);
        minutes=findViewById(R.id.minute);
        seconds=findViewById(R.id.second);

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.PACKAGE_USAGE_STATS)!=PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new
                    Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);

        }


        //Register Broadcast
        //IntentFilter filter = new IntentFilter("com.example.sindoq.intent.action.ACTION_SHOW_TOAST");
        //BlockBroadcastReceiver receiver = new BlockBroadcastReceiver();
        //registerReceiver(receiver, filter);


        IntentFilter filter1 = new IntentFilter("com.example.sindoq.intent.action.stopservice");
        StopServiceBroadcastReceiver sendBroadcastReceiver1 = new StopServiceBroadcastReceiver();
        registerReceiver(sendBroadcastReceiver1, filter1);

        blockApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                days=Integer.parseInt(day.getText().toString());
                mins=Integer.parseInt(minutes.getText().toString());
                hrs=Integer.parseInt(hour.getText().toString());
                secs=Integer.parseInt(seconds.getText().toString());
                System.out.println("MIN!!!"+mins);
                System.out.println("DAY!!!"+days);
                System.out.println("HRS!!!"+hrs);
                System.out.println("SEC!!!"+secs);
                res_day=days*86400000;
                res_hr=hrs*3600000;
                res_min=mins*60000;
                int res_secs=secs*1000;
                res_sec=res_day+res_hr+res_min+res_secs;

                if(databaseHelper.insert_sec(res_sec))
                {
                    System.out.println("Successfully inserted!!");
                }
                else
                {
                    System.out.println("Insertion Unsuccessful!!");
                }
                Intent intent= new Intent(TimerActivity.this,BlockAppsActivity.class);
                startActivity(intent);
            }
        });

    }
}