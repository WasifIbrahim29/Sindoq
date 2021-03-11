package com.example.sindoq.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.BgService;
import com.example.sindoq.Database.DatabaseHelper;
import com.example.sindoq.R;
import com.example.sindoq.StopServiceBroadcastReceiver;
import com.example.sindoq.TimerActivity;
import com.example.sindoq.adapter.ConfirmPageAdapter;
import com.example.sindoq.model.AppListMain;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BlockPage extends AppCompatActivity {
    int seconds;
    int counter=1;
    DatabaseHelper databaseHelper;
    private  AppListMain appListMain;
    ArrayList<AppListMain> appListMainList;
    TextView textView;
    TextView noapp;
    ConfirmPageAdapter confirmPageAdapter;
    RecyclerView recyclerView;
    TextView finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_page);
        databaseHelper=new DatabaseHelper(this);
        Cursor c=databaseHelper.getSeconds();
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
                    seconds=c.getInt(5);
                }while(c.moveToNext());
            }
        }
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
        new CountDownTimer(seconds, 1000){
            public void onTick(long millisUntilFinished){
                textView.setText(String.valueOf(counter));
                counter++;

            }
            public  void onFinish(){
                finish.setText("Apps Unblocked!");
                Log.e("TIMER MESSAGE ","UNBLOCK");
                //clear db
                databaseHelper.clearDBApps();
                Intent intent = new Intent();
                intent.setAction("com.example.sindoq.intent.action.stopservice");
                sendBroadcast(intent);
            }
        }.start();
    }

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