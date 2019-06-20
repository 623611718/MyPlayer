package com.example.lz.myplayer.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lz on 2019/6/20.
 */

public class ScreenBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.i("play", "screen on");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.i("play", "screen off");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            Log.i("play", "screen unlock");
        } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
            Log.i("play", " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
        }
    }
    }

