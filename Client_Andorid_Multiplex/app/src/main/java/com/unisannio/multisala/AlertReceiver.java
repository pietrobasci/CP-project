package com.unisannio.multisala;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

/**
 * Created by pietrobasci on 18/01/17.
 */
public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Richiesta_notifica", true).apply();


        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, i, 0);


        NotificationCompat.Builder noti = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(intent.getStringExtra("titolo_notifica"))
                .setContentText(intent.getStringExtra("text_notifica"))
                .setTicker("nuova notifica")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        noti.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti.build());
    }
}
