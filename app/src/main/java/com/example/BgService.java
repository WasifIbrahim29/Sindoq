package com.example;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.sindoq.Database.DatabaseHelper;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class BgService extends Service {
    public BgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }













    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        Log.d("IMPORTANT!!!!", "Servive Started");
        Timer timer  =  new Timer();
        Toast.makeText(getApplicationContext(), "IN SERVICE!!!", Toast.LENGTH_LONG).show();
        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(this);

        Stetho.initializeWithDefaults(this);
        timer.scheduleAtFixedRate(new TimerTask() {

            @SuppressLint("NewApi")
            public void run() {




             /*   ActivityManager activityManager =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
                for(int i = 0; i < procInfos.size(); i++)
                {
                    if(procInfos.get(i).processName.equals("Facebook"))
                    {
                        //Toast.makeText(getApplicationContext(), "Notify Message", Toast.LENGTH_LONG).show();
                        Log.d("IMPORTANT!!!!", "Facebook launched");
                    }
                }*/


              /*  ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> tasks = activityManager.getRunningAppProcesses();

                for (ActivityManager.RunningAppProcessInfo task : tasks) {
                    Log.d("IMPORTANT!!!!", String.valueOf(task.processName));

                }

                ActivityManager.RunningAppProcessInfo ans=getForegroundApp();
                if(ans!=null)
                {
                    Log.d("OYEE!!!!",ans.processName);
                }*/

                String currentApp = "NULL";
                UsageStatsManager usm = (UsageStatsManager)getSystemService(Service.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
                if (appList != null && appList.size() > 0) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                    for (UsageStats usageStats : appList) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
                        currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    }
                }

                //Log.e("adapter", "Current App in foreground is: " + currentApp);


                if(!currentApp.equals("NULL"))
                {
                    if(currentApp.contains("facebook")) {

                        Intent intent = new Intent();
                        intent.setAction("com.example.sindoq.intent.action.ACTION_SHOW_TOAST");
                        sendBroadcast(intent);
                    }

                }



            }

        }, 0, 5);  // every 6 seconds


        return START_STICKY;
    }




    private ActivityManager.RunningAppProcessInfo getForegroundApp() {
        ActivityManager.RunningAppProcessInfo result = null, info = null;

        final ActivityManager activityManager  =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()) {
            info = i.next();
            if(info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !isRunningService(info.processName)) {
                result = info;
                break;
            }
        }
        return result;
    }

    private boolean isRunningService(String processName) {
        if(processName == null)
            return false;

        ActivityManager.RunningServiceInfo service;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningServiceInfo> l = activityManager.getRunningServices(9999);
        Iterator <ActivityManager.RunningServiceInfo> i = l.iterator();
        while(i.hasNext()){
            service = i.next();
            if(service.process.equals(processName))
                return true;
        }
        return false;
    }

    private boolean isRunningApp(String processName) {
        if(processName == null)
            return false;

        ActivityManager.RunningAppProcessInfo app;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()){
            app = i.next();
            if(app.processName.equals(processName) && app.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE)
                return true;
        }
        return false;
    }


    private boolean checkifThisIsActive(ActivityManager.RunningAppProcessInfo target){
        boolean result = false;
        ActivityManager.RunningTaskInfo info;

        if(target == null)
            return false;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningTaskInfo> l = activityManager.getRunningTasks(9999);
        Iterator <ActivityManager.RunningTaskInfo> i = l.iterator();

        while(i.hasNext()){
            info=i.next();
            if(info.baseActivity.getPackageName().equals(target.processName)) {
                result = true;
                break;
            }
        }

        return result;
    }


    // what is in b that is not in a ?
    public static Collection subtractSets(Collection a, Collection b)
    {
        Collection result = new ArrayList(b);
        result.removeAll(a);
        return result;
    }







}