package com.example.sindoq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.BgService;
import com.example.sindoq.adapter.ConfirmPageAdapter;

public class Countdown extends AppCompatActivity {
TextView textView, text2view2, textview3;
    private int counter=5;
    RecyclerView recyclerView;
    ConfirmPageAdapter confirmPageAdapter;
    CountDownTimer Timer;
    Button stopbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        textView=findViewById(R.id.countdown);
        stopbtn= findViewById(R.id.btnstop);
        text2view2= findViewById(R.id.textView2);
        textview3= findViewById(R.id.text);

        Timer= new CountDownTimer(5000, 1000){
            public void onTick(long millisUntilFinished){
                textView.setText(String.valueOf(counter));
                counter--;
                if(text2view2.getVisibility()!= View.GONE)
                { text2view2.setVisibility(View.GONE); }
            }
            public  void onFinish(){

                stopbtn.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textview3.setVisibility(View.GONE);
                text2view2.setVisibility(View.VISIBLE);
                text2view2.setText("In Blocking Service");
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