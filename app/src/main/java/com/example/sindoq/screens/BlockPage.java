package com.example.sindoq.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.sindoq.adapter.BlockPageAdapter;
import com.example.sindoq.adapter.ConfirmPageAdapter;
import com.example.sindoq.model.AppListMain;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class BlockPage extends AppCompatActivity {
    long seconds;
    DatabaseHelper databaseHelper;
    private  AppListMain appListMain;
    ArrayList<AppListMain> appListMainList;
    TextView textView;
    TextView noapp;
    BlockPageAdapter blockPageAdapter;
    RecyclerView recyclerView;
    TextView finish;
    int flag;
    private long mEndTime;
    private CountDownTimer mCountDownTimer;
    int tempend;
    private static BlockPage lastPausedActivity = null;

    private void Start()
    {
        mEndTime = System.currentTimeMillis() + seconds;
        System.out.println("END TIME IN TIMER "+(int)mEndTime);
        mCountDownTimer=new CountDownTimer(seconds, 1000){

            public void onTick(long millisUntilFinished){
                seconds= millisUntilFinished;
                updateCountDownText();
            }
            public  void onFinish(){
                finish.setText("Apps Unblocked!");
                Log.e("TIMER MESSAGE ","UNBLOCK");
                //clear db
                databaseHelper.delete();
                flag=0;
                Intent intent = new Intent();
                intent.setAction("com.example.sindoq.intent.action.stopservice");
                sendBroadcast(intent);
            }

        }.start();
        flag=1;

        System.out.println("FLAG IN TIMER !!!!! " + flag);
    }
    @Override
    protected void onStop() {
        Log.e("Stop","in on Stop");
        super.onStop();
        lastPausedActivity = this;

        databaseHelper=new DatabaseHelper(this);
        System.out.println("inserting in db "+seconds);
        System.out.println("insertinf in db "+mEndTime);
        if(databaseHelper.insert_sec(0,0,0,0,(int)seconds,(int)mEndTime,flag))
        {
            System.out.println("Insertion successful!!!");
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        lastPausedActivity = this;
    }

    private void updateCountDownText() {

        int days=(int)((seconds/(1000))/86400);
        System.out.println(days);
        int hours = (int)((seconds / (1000*60*60)) % 24);
        int minutes =(int) (seconds / (1000*60)) % 60;
        int second = (int) (seconds / 1000) % 60;
        String timeLeftFormatted;
        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
        if(hours!=0)
        {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, second);
        }
        if(days!=0)
        {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d",days,hours, minutes, second);
        }
        textView.setText(timeLeftFormatted);

        /*int days=(int)((seconds/1000)/86400);
        int hours = (int)((seconds / 1000) / 3600);
        int minutes =(int) (seconds / 1000) / 60;
        int second = (int) (seconds / 1000) % 60;
        String timeLeftFormatted;
        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
        textView.setText(timeLeftFormatted);*/
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
                    seconds=(long)(c.getLong(5));
                    mEndTime=(long)(c.getLong(6));
                    flag=c.getInt(7);
                }while(c.moveToNext());
            }
        }
        System.out.println("END TIME FIRST "+(int)mEndTime);
        tempend=(int)mEndTime;
        //starttime=seconds;

        //System.out.println("SECONDS FROM DB "+seconds);
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
                        byte[] bitmap = c1.getBlob(3);
                        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0 , bitmap.length);
                        Drawable d = new BitmapDrawable(getResources(), image);
                        appListMain.setAppIcon(d);
                        appListMainList.add(appListMain);

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
            noapp.setText("NO BLOCKED APPS!");
        }
        else {
            blockPageAdapter = new BlockPageAdapter(this, appListMainList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(blockPageAdapter);
        }

        if(flag==0) {
            Start();
        }
        else
        {
            System.out.println("END TIME "+(int)mEndTime+" SYSTEM TIME "+(int)System.currentTimeMillis());
            seconds = (int)mEndTime-(int) System.currentTimeMillis();
            System.out.println("updated secs "+seconds);
            //updateCountDownText();
            if (seconds < 0)
            {
                seconds = 0;
                flag = 0;
                updateCountDownText();
                finish.setText("Apps Unblocked!");
                Log.e("TIMER MESSAGE ","UNBLOCK");
                //clear db
                databaseHelper.clearDBApps();
                databaseHelper.delete();
                flag=0;
                Intent intent = new Intent();
                intent.setAction("com.example.sindoq.intent.action.stopservice");
                sendBroadcast(intent);
                //updateButtons();
            }
            else
            {
                Start();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lastPausedActivity = this;
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(startMain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lastPausedActivity = this;


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Resume","in on Resume");


    }

   /* public void StopBlocking(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Unblock All Apps?");
        builder.setMessage("Are you sure you want to unblock all Apps?");
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("com.example.sindoq.intent.action.stopservice");
                        sendBroadcast(intent);

                        Log.e("Stop blocking", "onClick: Stop blocking called");
                        Intent intent1 = new Intent();
                        intent1.setAction("com.example.sindoq.intent.action.TimerActivityIntent");
                        sendBroadcast(intent1);
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }*/
}
