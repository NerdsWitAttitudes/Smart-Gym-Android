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
import android.util.Log;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.SportSchedule;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class NotificationService extends BroadcastReceiver {

    private static final String NOTIFICATION_ID = "notification_id";
    private static final String NOTIFICATION = "notification";
    private static final int REQUEST_CODE = 0;

    private Context context;
    private Intent notificationIntent;
    private AlarmManager alarmManager;

    public NotificationService() {
    }

    public NotificationService(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        notificationIntent = new Intent(context, NotificationService.class);
    }

    private Notification getNotification(String title, String contentText) {
        return new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_smartgym)
                .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
    }

    public void scheduleNotifications(SportSchedule sportSchedule) {
        Notification notification = getNotification(sportSchedule.getName(), context.getResources().getString(R.string.app_name));

        for (LocalDate.Property weekday : sportSchedule.getWeekdays()) {
            notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, weekday.getLocalDate().getDayOfWeek());
            notificationIntent.putExtra(NotificationService.NOTIFICATION, notification);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationService.REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            DateTime scheduleDateTime = new DateTime()
                    .withDate(weekday.getLocalDate())
                    .withTime(sportSchedule.getTime())
                    .minusMinutes(sportSchedule.getReminderMinutes());

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    scheduleDateTime.getMillis(),
                    AlarmManager.INTERVAL_DAY * DateTimeConstants.DAYS_PER_WEEK,
                    pendingIntent
            );
        }
    }

    public void cancelScheduledNotifications() {
        PendingIntent sender = PendingIntent.getBroadcast(context, NotificationService.REQUEST_CODE, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(sender);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notificationManager.notify(intent.getIntExtra(NOTIFICATION_ID, NotificationService.REQUEST_CODE), notification);
    }
}
