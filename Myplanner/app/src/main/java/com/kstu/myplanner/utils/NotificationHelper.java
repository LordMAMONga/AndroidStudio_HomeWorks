package com.kstu.myplanner.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.kstu.myplanner.MainActivity;
import com.kstu.myplanner.R;
import com.kstu.myplanner.model.Task;

import java.util.concurrent.TimeUnit;

public class NotificationHelper {

    private static final String CHANNEL_ID = "task_reminders";
    private static final String CHANNEL_NAME = "Напоминания о задачах";
    private static final String CHANNEL_DESCRIPTION = "Уведомления о предстоящих задачах";
    private static final String TAG = "NotificationHelper";

    private final Context context;
    private final NotificationManager notificationManager;
    private final AlarmManager alarmManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 250, 500});
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleNotification(Task task) {
        if (task.getDueDate() <= 0 || task.getReminderMinutes() == null || task.getReminderMinutes() <= 0) {
            return;
        }

        if (alarmManager == null) {
            Log.e(TAG, "AlarmManager is null. Cannot schedule notification.");
            return;
        }

        long notificationTime = task.getDueDate() - TimeUnit.MINUTES.toMillis(task.getReminderMinutes());

        if (notificationTime <= System.currentTimeMillis()) {
            return;
        }

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("task_id", task.getId());
        intent.putExtra("task_title", task.getTitle());
        intent.putExtra("task_description", task.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        boolean canSchedule = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms();

        if (canSchedule) {
            try {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
            } catch (Exception e) {
                Log.e(TAG, "Error scheduling alarm for task ID: " + task.getId(), e);
            }
        } else {
            Log.w(TAG, "Cannot schedule exact alarm: SCHEDULE_EXACT_ALARM permission not granted.");
        }
    }

    public void cancelNotification(int taskId) {
        if (alarmManager == null) {
            Log.e(TAG, "AlarmManager is null. Cannot cancel notification.");
            return;
        }
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        alarmManager.cancel(pendingIntent);
    }

    public void showNotification(int taskId, String title, String description) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 500, 250, 500});

        notificationManager.notify(taskId, builder.build());
    }
}
