package com.kstu.myplanner.ui.tasks;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kstu.myplanner.R;
import com.kstu.myplanner.model.Priority;
import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.model.TaskWithSubtasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private final AsyncListDiffer<TaskWithSubtasks> differ = new AsyncListDiffer<>(this, new TaskWithSubtasksDiffCallback());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
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
        TaskWithSubtasks currentItem = differ.getCurrentList().get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void submitList(List<TaskWithSubtasks> tasks) {
        differ.submitList(tasks);
    }

    public Task getTaskAt(int position) {
        return differ.getCurrentList().get(position).task;
    }

    private int getPriorityColor(Context context, Priority priority) {
        if (priority == null) {
            return ContextCompat.getColor(context, R.color.priority_default);
        }
        switch (priority) {
            case HIGH:
                return ContextCompat.getColor(context, R.color.priority_high);
            case MEDIUM:
                return ContextCompat.getColor(context, R.color.priority_medium);
            case LOW:
                return ContextCompat.getColor(context, R.color.priority_low);
            default:
                return ContextCompat.getColor(context, R.color.priority_default);
        }
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle, textViewDescription, textViewDueDate, textViewPriority;
        private final View viewPriorityIndicator;
        private final CheckBox checkBoxCompleted;
        private final ImageButton buttonDelete, expandIndicator;
        private final RecyclerView subtaskRecyclerView;
        private final Context context;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
            viewPriorityIndicator = itemView.findViewById(R.id.viewPriorityIndicator);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            expandIndicator = itemView.findViewById(R.id.expandIndicator);
            subtaskRecyclerView = itemView.findViewById(R.id.subtaskRecyclerView);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getTaskAt(position));
                }
            });

            checkBoxCompleted.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCheckBoxClick(getTaskAt(position), checkBoxCompleted.isChecked());
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(getTaskAt(position));
                }
            });
        }

        public void bind(TaskWithSubtasks item) {
            Task task = item.task;
            textViewTitle.setText(task.getTitle());
            textViewDescription.setText(task.getDescription());

            if (task.getDueDate() > 0) {
                textViewDueDate.setText(dateFormat.format(new Date(task.getDueDate())));
                textViewDueDate.setVisibility(View.VISIBLE);
            } else {
                textViewDueDate.setVisibility(View.GONE);
            }

            Priority priority = task.getPriority();
            textViewPriority.setText(priority != null ? priority.name() : "");
            viewPriorityIndicator.setBackgroundColor(getPriorityColor(context, priority));

            checkBoxCompleted.setChecked(task.isCompleted());
            updateStrikeThrough(textViewTitle, task.isCompleted());

            List<Task> subtasks = item.subtasks;
            if (subtasks != null && !subtasks.isEmpty()) {
                expandIndicator.setVisibility(View.VISIBLE);
                subtaskRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                SubtaskAdapter subtaskAdapter = new SubtaskAdapter(subtasks);
                subtaskRecyclerView.setAdapter(subtaskAdapter);

                expandIndicator.setOnClickListener(v -> {
                    boolean isVisible = subtaskRecyclerView.getVisibility() == View.VISIBLE;
                    subtaskRecyclerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                    expandIndicator.animate().rotation(isVisible ? 0f : 180f).start();
                });
                // Restore state
                boolean isExpanded = subtaskRecyclerView.getVisibility() == View.VISIBLE;
                expandIndicator.setRotation(isExpanded ? 180f : 0f);
            } else {
                expandIndicator.setVisibility(View.GONE);
                subtaskRecyclerView.setVisibility(View.GONE);
            }
        }

        private void updateStrikeThrough(TextView textView, boolean isCompleted) {
            if (isCompleted) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    private class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.SubtaskHolder> {
        private final List<Task> subtasks;

        private SubtaskAdapter(List<Task> subtasks) {
            this.subtasks = subtasks;
        }

        @NonNull
        @Override
        public SubtaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtask, parent, false);
            return new SubtaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SubtaskHolder holder, int position) {
            holder.bind(subtasks.get(position));
        }

        @Override
        public int getItemCount() {
            return subtasks.size();
        }

        class SubtaskHolder extends RecyclerView.ViewHolder {
            private final CheckBox checkBox;
            private final TextView title;
            private final ImageButton deleteButton;

            SubtaskHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.subtaskCheckBoxCompleted);
                title = itemView.findViewById(R.id.subtaskTextViewTitle);
                deleteButton = itemView.findViewById(R.id.subtaskButtonDelete);

                checkBox.setOnClickListener(v -> {
                    int pos = getBindingAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onSubtaskCheckBoxClick(subtasks.get(pos), checkBox.isChecked());
                    }
                });

                deleteButton.setOnClickListener(v -> {
                    int pos = getBindingAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onSubtaskDeleteClick(subtasks.get(pos));
                    }
                });
            }

            void bind(Task subtask) {
                title.setText(subtask.getTitle());
                checkBox.setChecked(subtask.isCompleted());
                if (subtask.isCompleted()) {
                    title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
        void onCheckBoxClick(Task task, boolean isChecked);
        void onDeleteClick(Task task);
        void onSubtaskCheckBoxClick(Task subtask, boolean isChecked);
        void onSubtaskDeleteClick(Task subtask);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static class TaskWithSubtasksDiffCallback extends DiffUtil.ItemCallback<TaskWithSubtasks> {
        @Override
        public boolean areItemsTheSame(@NonNull TaskWithSubtasks oldItem, @NonNull TaskWithSubtasks newItem) {
            return oldItem.task.getId() == newItem.task.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskWithSubtasks oldItem, @NonNull TaskWithSubtasks newItem) {
            return oldItem.task.equals(newItem.task) && oldItem.subtasks.equals(newItem.subtasks);
        }
    }
}
