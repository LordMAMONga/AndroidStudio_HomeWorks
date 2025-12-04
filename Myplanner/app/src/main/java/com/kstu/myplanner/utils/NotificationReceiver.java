package com.kstu.myplanner.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        int taskId = intent.getIntExtra("task_id", 0);
        String taskTitle = intent.getStringExtra("task_title");
        String taskDescription = intent.getStringExtra("task_description");

        if (taskTitle == null) {
            return;
        }

        NotificationHelper notificationHelper = new NotificationHelper(context);

        String notificationText = taskDescription != null && !taskDescription.isEmpty()
                ? taskDescription
                : "Не забудьте выполнить эту задачу!";

        notificationHelper.showNotification(
                taskId,
                "⏰ Напоминание: " + taskTitle,
                notificationText
        );
    }
}