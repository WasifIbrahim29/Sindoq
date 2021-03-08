package com.example.sindoq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sindoq.Database.DatabaseHelper;
import com.example.sindoq.adapter.ConfirmPageAdapter;
import com.example.sindoq.listener.RecyclerItemClickListener;
import com.example.sindoq.model.AppListMain;
import com.facebook.stetho.inspector.protocol.module.Database;

import java.util.ArrayList;
import java.util.List;

public class ConfirmPage extends AppCompatActivity {
    private static final int REQUEST_UNINSTALL = 222;
    RecyclerView recyclerView;
TextView textView;
EditText day;
EditText min;
EditText sec;
EditText hr;
DatabaseHelper databaseHelper;
ArrayList<AppListMain> appListMainList;
ConfirmPageAdapter confirmPageAdapter;
private  AppListMain appListMain;
    int selectedPos = 0;
int days,mins,secs,hrs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);
        recyclerView=findViewById(R.id.apps);
        textView=findViewById(R.id.no_apps);
        day=findViewById(R.id.days);
        min=findViewById(R.id.minute);
        sec=findViewById(R.id.second);
        hr=findViewById(R.id.hour);

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
        min.setText(String.valueOf(mins));
        hr.setText(String.valueOf(hrs));
        sec.setText(String.valueOf(secs));
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
                        System.out.println("temp 1 "+temp1);
                        System.out.println("temp 2 "+temp2);
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
            textView.setText("NO UNBLOCKED APPS!");
        }
        else {
            confirmPageAdapter = new ConfirmPageAdapter(this, appListMainList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(confirmPageAdapter);
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                appListMain = appListMainList.get(position);
                if (appListMain != null) {
                    if(!"Sindoq".equals(appListMain.getAppName().toString())) {
                        if (databaseHelper.CHeckIfAppExists(appListMain.getAppName().toString())) {
                            appListMain.setAppSelected(true);
                            databaseHelper.deleteApp(appListMain.getAppName().toString()); //delete from bloc); //Add in Unblocked Apps
                            databaseHelper.insertUnBlockedApp(appListMain.getAppName().toString(), appListMain.getAppPackage().toString()); //Add in Unblocked Apps
                            confirmPageAdapter.notifyDataSetChanged();
                        } else {
                            databaseHelper.deleteUnBlockedApp(appListMain.getAppName().toString());
                            databaseHelper.insertapp(appListMain.getAppName().toString(), appListMain.getAppPackage().toString());
                            confirmPageAdapter.notifyDataSetChanged();
                        }
                    }

                }

                //Intent intent = packageManager.getLaunchIntentForPackage(appListMainArrayList.get(position).getAppPackage().toString());
                //startActivity(intent);
            }


            @Override
            public void onItemLongClick(View view, int position) {
                appListMain = appListMainList.get(position);
                if (appListMain != null) {
                   /* Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:"+appListMain.getAppPackage()));
                    startActivity(intent);
                    customAppListAdapter.notifyDataSetChanged();*/




                    selectedPos = position;
//                    Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
//                    intent.setData(Uri.parse("package:" + appListMain.getAppPackage()));
//                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//                    startActivityForResult(intent, REQUEST_UNINSTALL);

                }
            }
        }));


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UNINSTALL) {
            if (resultCode == RESULT_OK) {
                Log.d("TAG", "onActivityResult: user accepted the (un)install");
//                customAppListAdapter.updateList(appListMainArrayList,selectedPos);
                confirmPageAdapter.notifyItemRemoved(selectedPos);
                Toast.makeText(this, "Uninstall successfully!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("TAG", "onActivityResult: user canceled the (un)install");
                // Toast.makeText(this, "System can't uninstall!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_FIRST_USER) {
                Log.d("TAG", "onActivityResult: failed to (un)install");
            }
        }
    }
    public void Confirm(View v)
    {
        int day1,min1,hr1,sec1,res1;
        day1=Integer.parseInt(day.getText().toString());
        min1=Integer.parseInt(min.getText().toString());
        hr1=Integer.parseInt(hr.getText().toString());
        sec1=Integer.parseInt(sec.getText().toString());
        System.out.println("MIN!!!"+min1);
        System.out.println("DAY!!!"+day1);
        System.out.println("HRS!!!"+hr1);
        System.out.println("SEC!!!"+sec1);

        res1=day1*86400000+hr1*3600000+min1*60000+sec1*1000;

        if(databaseHelper.insert_sec(day1,min1,hr1,sec1,res1))
        {
            System.out.println("Successfully inserted!!");
        }
        else
        {
            System.out.println("Insertion Unsuccessful!!");
        }
        Intent intent=new Intent(this,Countdown.class);
        startActivity(intent);
        finish();
    }
}