package com.kstu.myplanner.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.kstu.myplanner.database.AppDatabase;
import com.kstu.myplanner.database.TaskDao;
import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.model.TaskWithSubtasks;
import com.kstu.myplanner.utils.NotificationHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskRepository {
    private static final String TAG = "TaskRepository";
    private final TaskDao taskDao;
    private final NotificationHelper notificationHelper;
    private final LiveData<List<TaskWithSubtasks>> allTasksWithSubtasks;
    private final LiveData<List<TaskWithSubtasks>> activeTasksWithSubtasks;
    private final LiveData<List<TaskWithSubtasks>> completedTasksWithSubtasks;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        taskDao = database.taskDao();
        notificationHelper = new NotificationHelper(application);
        allTasksWithSubtasks = taskDao.getAllTasksWithSubtasks();
        activeTasksWithSubtasks = taskDao.getActiveTasksWithSubtasks();
        completedTasksWithSubtasks = taskDao.getCompletedTasksWithSubtasks();
    }

    public LiveData<List<TaskWithSubtasks>> getAllTasksWithSubtasks() {
        return allTasksWithSubtasks;
    }

    public LiveData<List<TaskWithSubtasks>> getActiveTasksWithSubtasks() {
        return activeTasksWithSubtasks;
    }

    public LiveData<List<TaskWithSubtasks>> getCompletedTasksWithSubtasks() {
        return completedTasksWithSubtasks;
    }

    public LiveData<List<Task>> getTasksByPriority(String priority) {
        return taskDao.getTasksByPriority(priority);
    }

    public LiveData<List<Task>> getTasksByCategory(String category) {
        return taskDao.getTasksByCategory(category);
    }

    public void insert(Task task) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> taskDao.insert(task));
        try {
            long id = future.get();
            task.setId((int) id);
            Log.d(TAG, "Scheduling notification for new task. ID: " + task.getId() + ", Due: " + task.getDueDate() + ", ReminderMins: " + task.getReminderMinutes());
            notificationHelper.scheduleNotification(task);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Error inserting task", e);
        }
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "Scheduling notification for updated task. ID: " + task.getId() + ", Due: " + task.getDueDate() + ", ReminderMins: " + task.getReminderMinutes());
            taskDao.update(task);
            notificationHelper.scheduleNotification(task);
        });
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete(task);
            notificationHelper.cancelNotification(task.getId());
        });
    }

    public void deleteAllTasks() {
        AppDatabase.databaseWriteExecutor.execute(taskDao::deleteAllTasks);
    }
}
