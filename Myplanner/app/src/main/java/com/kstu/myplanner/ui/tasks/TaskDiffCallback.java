package com.kstu.myplanner.ui.tasks;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.kstu.myplanner.model.Task;
import java.util.Objects;

public class TaskDiffCallback extends DiffUtil.ItemCallback<Task> {

    @Override
    public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                Objects.equals(oldItem.getDescription(), newItem.getDescription()) &&
                oldItem.getDueDate() == newItem.getDueDate() &&
                Objects.equals(oldItem.getPriority(), newItem.getPriority()) &&
                oldItem.isCompleted() == newItem.isCompleted();
    }
}
