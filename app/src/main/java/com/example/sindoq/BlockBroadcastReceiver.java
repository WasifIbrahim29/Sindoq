package com.example.sindoq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sindoq.screens.BlockPage;

public class BlockBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("From Broadcast Receiver", "RECEIVED");

        Intent intent2= new Intent(context, BlockPage.class);
        context.startActivity(intent2);
    }
}