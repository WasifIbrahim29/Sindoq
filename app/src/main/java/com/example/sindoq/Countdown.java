package com.example.sindoq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.example.BgService;
import com.example.sindoq.adapter.ConfirmPageAdapter;

public class Countdown extends AppCompatActivity {
TextView textView;
    private int counter=1;
    RecyclerView recyclerView;
    ConfirmPageAdapter confirmPageAdapter;
    CountDownTimer Timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        textView=findViewById(R.id.countdown);

        Timer= new CountDownTimer(5000, 1000){
            public void onTick(long millisUntilFinished){
                textView.setText(String.valueOf(counter));
                counter++;
            }
            public  void onFinish(){
                Intent intent = new Intent();
                intent.setAction("com.example.sindoq.intent.action.startservice");
                sendBroadcast(intent);
            }

        }.start();
    }
    public void Stop(View v)
    {
        Intent intent=new Intent(this,ConfirmPage.class);
        startActivity(intent);
        Timer.cancel();
        finish();
    }
}