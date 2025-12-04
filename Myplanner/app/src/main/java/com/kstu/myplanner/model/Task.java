package com.kstu.myplanner.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("parentId")})
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private long dueDate; // Дата в миллисекундах
    private Priority priority; // Enum: HIGH, MEDIUM, LOW
    private boolean isCompleted;
    private String category;
    private Integer reminderMinutes; // В минутах до события. null если нет напоминания
    private long createdDate;
    private Integer parentId; // For subtasks

    @Ignore
    private List<Task> subtasks;

    // Конструктор
    public Task(String title, String description, long dueDate, Priority priority, String category, Integer reminderMinutes, Integer parentId) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.reminderMinutes = reminderMinutes;
        this.isCompleted = false;
        this.createdDate = System.currentTimeMillis();
        this.parentId = parentId;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(Integer reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks;
    }
}