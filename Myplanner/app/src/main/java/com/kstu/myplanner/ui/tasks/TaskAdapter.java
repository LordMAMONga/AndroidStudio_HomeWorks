package com.kstu.myplanner.ui.tasks;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kstu.myplanner.R;
import com.kstu.myplanner.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDescription.setText(currentTask.getDescription());

        // Форматируем дату
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        holder.textViewDueDate.setText(dateFormat.format(new Date(currentTask.getDueDate())));

        // Устанавливаем приоритет
        holder.textViewPriority.setText(currentTask.getPriority());
        int priorityColor = getPriorityColor(currentTask.getPriority());
        holder.viewPriorityIndicator.setBackgroundColor(priorityColor);

        // Устанавливаем статус выполнения
        holder.checkBoxCompleted.setChecked(currentTask.isCompleted());

        // Зачеркиваем текст если задача выполнена
        if (currentTask.isCompleted()) {
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    private int getPriorityColor(String priority) {
        switch (priority) {
            case "HIGH":
                return Color.parseColor("#F44336");
            case "MEDIUM":
                return Color.parseColor("#FF9800");
            case "LOW":
                return Color.parseColor("#4CAF50");
            default:
                return Color.GRAY;
        }
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDueDate;
        private TextView textViewPriority;
        private View viewPriorityIndicator;
        private CheckBox checkBoxCompleted;
        private ImageButton buttonDelete;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
            viewPriorityIndicator = itemView.findViewById(R.id.viewPriorityIndicator);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position));
                }
            });

            checkBoxCompleted.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCheckBoxClick(tasks.get(position));
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(tasks.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
        void onCheckBoxClick(Task task);
        void onDeleteClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}