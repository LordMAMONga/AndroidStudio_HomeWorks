package com.kstu.myplanner.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaskWithSubtasks {
    @Embedded
    public Task task;

    @Relation(
            parentColumn = "id",
            entityColumn = "parentId"
    )
    public List<Task> subtasks;
}
