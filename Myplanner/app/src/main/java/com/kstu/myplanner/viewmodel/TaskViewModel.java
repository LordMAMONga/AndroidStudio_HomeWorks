package com.kstu.myplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> activeTasks;
    private LiveData<List<Task>> completedTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        activeTasks = repository.getActiveTasks();
        completedTasks = repository.getCompletedTasks();
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
        return repository.getTasksByPriority(priority);
    }

    // Получить задачи по категории
    public LiveData<List<Task>> getTasksByCategory(String category) {
        return repository.getTasksByCategory(category);
    }

    // Добавить задачу
    public void insert(Task task) {
        repository.insert(task);
    }

    // Обновить задачу
    public void update(Task task) {
        repository.update(task);
    }

    // Удалить задачу
    public void delete(Task task) {
        repository.delete(task);
    }

    // Удалить все задачи
    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }
}