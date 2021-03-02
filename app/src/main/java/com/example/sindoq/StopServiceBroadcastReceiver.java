package com.example.sindoq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.BgService;

public class StopServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Stopping Service!", Toast.LENGTH_LONG).show();
        Intent intent1=new Intent(context,BgService.class);
        context.stopService(intent1);
    }
}