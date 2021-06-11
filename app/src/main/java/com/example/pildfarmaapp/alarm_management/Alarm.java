package com.example.pildfarmaapp.alarm_management;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.activities.LecturaDatosActivity;
import com.example.pildfarmaapp.providers.AuthProvider;
import com.example.pildfarmaapp.providers.PostProvider;
import com.example.pildfarmaapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class Alarm  extends BroadcastReceiver {

    private MediaPlayer mp;
    AlarmManager mgr;
    PendingIntent pi;
    Intent intent;
    int cantidad;
    private AuthProvider mAuthProvider;
    private PostProvider mPostProvider;
    private UsersProvider mUsersProvider;
    int recuest;

    @Override
    public void onReceive(Context context, Intent intent) {
        //mp = MediaPlayer.create(context, R.raw.music);
        //mp.start();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        mUsersProvider = new UsersProvider();

        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("cantidad")){
                        String NumBroadcaster = documentSnapshot.getString("cantidad");
                        cantidad = Integer.parseInt(NumBroadcaster);
                    }
                    if(documentSnapshot.contains("Broadcaster")){
                        String NumBroadcaster = documentSnapshot.getString("Broadcaster");
                        recuest = Integer.parseInt(NumBroadcaster);
                    }
                }
            }
        });

        if(cantidad>0){
            mp = MediaPlayer.create(context, R.raw.music);
            mp.start();
            Toast.makeText(context, "Sonando", Toast.LENGTH_SHORT).show();
        }
        else{
            this.intent = new Intent(context, LecturaDatosActivity.class);
            mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            pi = PendingIntent.getService(context,recuest,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mgr.cancel(pi);
            pi.cancel();
        }
    }
}
