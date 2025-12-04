package com.kstu.myplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.model.TaskWithSubtasks;
import com.kstu.myplanner.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;
    private final MutableLiveData<String> filterLiveData = new MutableLiveData<>();
    private final LiveData<List<Task>> tasks;

    public static final String FILTER_ALL = "ALL";
    public static final String FILTER_ACTIVE = "ACTIVE";
    public static final String FILTER_COMPLETED = "COMPLETED";

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);

        filterLiveData.setValue(FILTER_ALL);

        tasks = Transformations.switchMap(filterLiveData, filter -> {
            LiveData<List<TaskWithSubtasks>> source;
            if (Objects.equals(filter, FILTER_ACTIVE)) {
                source = repository.getActiveTasksWithSubtasks();
            } else if (Objects.equals(filter, FILTER_COMPLETED)) {
                source = repository.getCompletedTasksWithSubtasks();
            } else {
                source = repository.getAllTasksWithSubtasks();
            }
            return Transformations.map(source, this::convertTaskWithSubtasksToTasks);
        });
    }

    private List<Task> convertTaskWithSubtasksToTasks(List<TaskWithSubtasks> tasksWithSubtasks) {
        if (tasksWithSubtasks == null) {
            return new ArrayList<>();
        }
        return tasksWithSubtasks.stream()
                .map(taskWithSubtasks -> {
                    taskWithSubtasks.task.setSubtasks(taskWithSubtasks.subtasks);
                    return taskWithSubtasks.task;
                })
                .collect(Collectors.toList());
    }

    public void setFilter(String filter) {
        filterLiveData.setValue(filter);
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }
}
