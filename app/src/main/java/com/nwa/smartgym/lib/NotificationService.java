package com.nwa.smartgym.lib;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.util.Log;

import com.nwa.smartgym.R;

import org.joda.time.DateTimeConstants;

public class NotificationService extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";

    private Context context;

    public NotificationService() {
    }

    public NotificationService(Context context) {
        this.context = context;
    }

    private Notification getNotification(String title, String contentText) {
        return new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
    }

    public void scheduleNotifications() {
        Notification notification = getNotification("Ga sporte", "Bij Smart Gym");

        Intent notificationIntent = new Intent(context, NotificationService.class);

        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationService.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + 10000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                futureInMillis,
                AlarmManager.INTERVAL_DAY * DateTimeConstants.DAYS_PER_WEEK,
                pendingIntent
        );
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NWA", "FSDFWEFWFSFFE RECEIVED");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}
