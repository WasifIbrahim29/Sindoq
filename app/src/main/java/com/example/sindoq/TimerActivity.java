package com.example.sindoq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.sindoq.Database.DatabaseHelper;

import java.util.Calendar;

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
    //public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 2323;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_timer);
        timerLayout= findViewById(R.id.timerLayout);
        blockApps= findViewById(R.id.blockApps);

        databaseHelper=new DatabaseHelper(this);

        day=findViewById(R.id.days);
        hour=findViewById(R.id.hour);
        minutes=findViewById(R.id.minute);
        seconds=findViewById(R.id.second);

        //////////////////////////////////////////////



        databaseHelper=new DatabaseHelper(this);
        Cursor c=databaseHelper.getSeconds();

        if(c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                do
                {

                    days=c.getInt(1);
                    mins=c.getInt(2);
                    hrs=c.getInt(3);
                    secs=c.getInt(4);
                }while(c.moveToNext());
            }
        }
        day.setText(String.valueOf(days));
        minutes.setText(String.valueOf(mins));
        hour.setText(String.valueOf(hrs));
        seconds.setText(String.valueOf(secs));

        /////////////////////////////////////////////




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

                int endtime=(int)System.currentTimeMillis() + res_sec;
                if(databaseHelper.insert_sec(days,mins,hrs,secs,res_sec,endtime,0))
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