package com.kstu.myplanner.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.kstu.myplanner.database.AppDatabase;
import com.kstu.myplanner.database.TaskDao;
import com.kstu.myplanner.model.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> activeTasks;
    private LiveData<List<Task>> completedTasks;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
        activeTasks = taskDao.getActiveTasks();
        completedTasks = taskDao.getCompletedTasks();
    }

    // Получить все задачи
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // Получить активные задачи
    public LiveData<List<Task>> getActiveTasks() {
        return activeTasks;
    }

    // Получить завершенные задачи
    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    // Получить задачи по приоритету
    public LiveData<List<Task>> getTasksByPriority(String priority) {
        return taskDao.getTasksByPriority(priority);
    }

    // Получить задачи по категории
    public LiveData<List<Task>> getTasksByCategory(String category) {
        return taskDao.getTasksByCategory(category);
    }

    // Добавить задачу
    public void insert(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }

    // Обновить задачу
    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.update(task);
        });
    }

    // Удалить задачу
    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete(task);
        });
    }

    // Удалить все задачи
    public void deleteAllTasks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteAllTasks();
        });
    }
}