package com.kstu.myplanner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.model.TaskWithSubtasks;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks")
    void deleteAllTasks();

    @Transaction
    @Query("SELECT * FROM tasks WHERE parentId IS NULL ORDER BY dueDate ASC")
    LiveData<List<TaskWithSubtasks>> getAllTasksWithSubtasks();

    @Transaction
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND parentId IS NULL ORDER BY dueDate ASC")
    LiveData<List<TaskWithSubtasks>> getActiveTasksWithSubtasks();

    @Transaction
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 AND parentId IS NULL ORDER BY dueDate DESC")
    LiveData<List<TaskWithSubtasks>> getCompletedTasksWithSubtasks();

    @Query("SELECT * FROM tasks WHERE parentId = :parentId ORDER BY createdDate ASC")
    LiveData<List<Task>> getSubtasks(int parentId);

    // These methods might still be useful for other purposes, so we keep them.
    @Query("SELECT * FROM tasks WHERE priority = :priority AND parentId IS NULL ORDER BY dueDate ASC")
    LiveData<List<Task>> getTasksByPriority(String priority);

    @Query("SELECT * FROM tasks WHERE category = :category AND parentId IS NULL ORDER BY dueDate ASC")
    LiveData<List<Task>> getTasksByCategory(String category);
}
