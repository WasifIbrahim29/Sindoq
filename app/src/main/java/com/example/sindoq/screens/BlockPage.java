package com.example.sindoq.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.BgService;
import com.example.sindoq.Database.DatabaseHelper;
import com.example.sindoq.R;
import com.example.sindoq.SplashActivity;
import com.example.sindoq.StopServiceBroadcastReceiver;
import com.example.sindoq.TimerActivity;
import com.example.sindoq.adapter.ConfirmPageAdapter;
import com.example.sindoq.model.AppListMain;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class BlockPage extends AppCompatActivity {
    long seconds;
    int counter=1;
    DatabaseHelper databaseHelper;
    private  AppListMain appListMain;
    ArrayList<AppListMain> appListMainList;
    TextView textView;
    TextView noapp;
    ConfirmPageAdapter confirmPageAdapter;
    RecyclerView recyclerView;
    TextView finish;
    boolean flag=false;
    private long mEndTime;
    private CountDownTimer mCountDownTimer;
    long starttime;
    private static BlockPage lastPausedActivity = null;

    public void Start()
    {
        mEndTime = System.currentTimeMillis() + seconds;
        System.out.println(mEndTime);
        mCountDownTimer=new CountDownTimer(seconds, 1000){
            public void onTick(long millisUntilFinished){
                seconds= millisUntilFinished;
                updateCountDownText();
            }
            public  void onFinish(){
                finish.setText("Apps Unblocked!");
                Log.e("TIMER MESSAGE ","UNBLOCK");
                //clear db
                //databaseHelper.delete();
                flag=false;
                Intent intent = new Intent();
                intent.setAction("com.example.sindoq.intent.action.stopservice");
                sendBroadcast(intent);
            }

        }.start();
        flag=true;
    }

    protected void onPause() {
        Log.e("Pause","in on pause");
        super.onPause();
        databaseHelper=new DatabaseHelper(this);
        databaseHelper.insert_sec(0,0,0,0,(int)seconds);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", seconds);
        editor.putBoolean("timerRunning", flag);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        lastPausedActivity = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this == lastPausedActivity) {
            lastPausedActivity = null;
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );
        }
    }

    @Override
    protected void onStart() {
        Log.e("START","in on start");
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        seconds = prefs.getLong("millisLeft", starttime);
        flag = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        //updateButtons();
        if (flag) {
            mEndTime = prefs.getLong("endTime", 0);
            seconds =  mEndTime - System.currentTimeMillis();
            if (seconds < 0) {
                seconds = 0;
                flag = false;
                updateCountDownText();
                //updateButtons();
            }
            else {
                Start();
            }
        }
    }

    private void updateCountDownText() {
        int days=(int)((seconds/1000)/86400);
        int hours = (int)((seconds / 1000) / 3600);
        int minutes =(int) (seconds / 1000) / 60;
        int second = (int) (seconds / 1000) % 60;
        String timeLeftFormatted;
        System.out.println(days+" "+hours+" "+minutes+" "+second);
        /*if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, second);
        }*/

       // if(days>0)
        //{
          //  timeLeftFormatted = String.format(Locale.getDefault(),
            //        "%d:%02d:%02d:%02d",days, hours, minutes, second);
       // }
       // else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                   "%02d:%02d", minutes, second);
       // }
       // String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
        textView.setText(timeLeftFormatted);
        // textView.setText(String.valueOf(counter));
        counter++;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("CREATE","in on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_page);
        databaseHelper=new DatabaseHelper(this);
        //databaseHelper.delete();
        Cursor c=databaseHelper.getSeconds();
        // c2=databaseHelper.getCounter();
        textView=findViewById(R.id.timer);
        recyclerView=findViewById(R.id.recycler);
        noapp=findViewById(R.id.noapp);
        finish=findViewById(R.id.textView);
        if(c.getCount()>0)
        {
            if(c.moveToFirst())
            {
                do
                {
                    seconds=(long)(c.getInt(5));
                }while(c.moveToNext());
            }
        }
        starttime=seconds;
        /*if(c2.getCount()>0)
        {
            if(c2.moveToFirst())
            {
                do
                {
                    counter=c2.getInt(1);
                }while(c2.moveToNext());
            }
        }
        else
        {
            counter=1;
        }
*/
        System.out.println("SECONDS FROM DB "+seconds);
        //unblock
        Cursor c1=databaseHelper.getUnblockedApps();

        appListMainList=new ArrayList<>();
        // ResolveInfo resolveInfo=new ResolveInfo();
        try {
            if (c1.getCount() > 0) {
                if (c1.moveToFirst()) {
                    do {
                        appListMain=new AppListMain();
                        String temp1=c1.getString(1);
                        String temp2=c1.getString(2);
                        appListMain.setAppName(temp1);
                        appListMain.setAppPackage(temp2);
                        appListMainList.add(appListMain);
                        //appListMain.setAppIcon(resolveInfo.icon(c.getString(3)));
                    } while (c1.moveToNext());
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(appListMainList==null||appListMainList.size()==0)
        {
            noapp.setText("NO UNBLOCKED APPS!");
        }
        else {
            confirmPageAdapter = new ConfirmPageAdapter(this, appListMainList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(confirmPageAdapter);
        }

        Start();

    }
    /*public void onStart() {
        Log.e("START","in on start");
        super.onStart();
        Cursor c2=databaseHelper.getCounter();
        if(c2.getCount()>0)
        {
            if(c2.moveToFirst())
            {
                do
                {
                    counter=c2.getInt(0);
                }while(c2.moveToNext());
            }
        }
        Start();
    }
    public void onResume() {
        Log.e("RESUME","in on resume");
        super.onResume();
        /*Cursor c2=databaseHelper.getCounter();
        if(c2.getCount()>0)
        {
            if(c2.moveToFirst())
            {
                do
                {
                    counter=c2.getInt(0);
                }while(c2.moveToNext());
            }
        }
        Start();

    }
    public void onPause() {
        Log.e("PAUSE","in on pause");
        super.onPause();
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        databaseHelper.insert_counter(counter);
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(startMain);
        //android.os.Process.killProcess(android.os.Process.myPid());

    }


    public void StopBlocking(View view) {
        Intent intent = new Intent();
        intent.setAction("com.example.sindoq.intent.action.stopservice");
        sendBroadcast(intent);
        this.finish();
        //System.exit(0);
        finish();
    }
}