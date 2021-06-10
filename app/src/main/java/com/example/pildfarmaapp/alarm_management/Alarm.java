package com.example.pildfarmaapp.alarm_management;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.pildfarmaapp.R;

public class Alarm  extends BroadcastReceiver {

    private MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        //mp = MediaPlayer.create(context, R.raw.music);
        //mp.start();
        Toast.makeText(context, "Sonando", Toast.LENGTH_SHORT).show();
    }
}
