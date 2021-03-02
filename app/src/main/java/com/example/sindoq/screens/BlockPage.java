package com.example.sindoq.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.BgService;
import com.example.sindoq.Database.DatabaseHelper;
import com.example.sindoq.R;
import com.example.sindoq.StopServiceBroadcastReceiver;
import com.example.sindoq.TimerActivity;

import java.util.Timer;
import java.util.TimerTask;

public class BlockPage extends AppCompatActivity {
    int seconds;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_page);
        databaseHelper=new DatabaseHelper(this);
        Cursor c=databaseHelper.getSeconds();
        if(c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                do
                {
                    seconds=c.getInt(1);
                }while(c.moveToNext());
            }
        }
        System.out.println("SECONDS FROM DB "+seconds);
        //unblock
        new CountDownTimer(seconds, 1000){
            public void onTick(long millisUntilFinished){
                Log.e("TIME: ",String.valueOf(millisUntilFinished));
            }
            public  void onFinish(){
                Log.e("TIMER MESSAGE ","UNBLOCK");
                //clear db
                Intent intent = new Intent();
                intent.setAction("com.example.sindoq.intent.action.stopservice");
                sendBroadcast(intent);
            }
        }.start();
    }
}