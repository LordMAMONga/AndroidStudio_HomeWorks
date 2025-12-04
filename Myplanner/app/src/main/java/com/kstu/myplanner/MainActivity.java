package com.kstu.myplanner;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.model.TaskWithSubtasks;
import com.kstu.myplanner.ui.tasks.AddTaskDialog;
import com.kstu.myplanner.ui.tasks.TaskAdapter;
import com.kstu.myplanner.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textViewEmptyState;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Разрешение на уведомления отклонено. Напоминания не будут работать.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        textViewEmptyState = findViewById(R.id.textViewEmptyState);
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getTasks().observe(this, tasks -> {
            if (tasks != null) {
                List<TaskWithSubtasks> taskWithSubtasks = tasks.stream().map(task -> {
                    TaskWithSubtasks item = new TaskWithSubtasks();
                    item.task = task;
                    item.subtasks = new ArrayList<>(); // Assuming subtasks are loaded separately or not needed here
                    return item;
                }).collect(Collectors.toList());
                adapter.submitList(taskWithSubtasks);
            }

            if (tasks == null || tasks.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                textViewEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                textViewEmptyState.setVisibility(View.GONE);
            }
        });

        fabAddTask.setOnClickListener(v -> showAddTaskDialog(null));

        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                showAddTaskDialog(task);
            }

            @Override
            public void onCheckBoxClick(Task task, boolean isChecked) {
                task.setCompleted(isChecked);
                taskViewModel.update(task);
                Toast.makeText(MainActivity.this,
                        task.isCompleted() ? getString(R.string.task_completed_toast) : getString(R.string.task_active_toast),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Task task) {
                deleteTaskWithUndo(task);
            }

            @Override
            public void onSubtaskCheckBoxClick(Task subtask, boolean isChecked) {
                subtask.setCompleted(isChecked);
                taskViewModel.update(subtask);
            }

            @Override
            public void onSubtaskDeleteClick(Task subtask) {
                taskViewModel.delete(subtask);
            }
        });

        setupSwipeToDelete();
        requestNotificationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkExactAlarmPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                new AlertDialog.Builder(this)
                        .setTitle("Требуется разрешение")
                        .setMessage("Для точной работы напоминаний приложению требуется специальный доступ. Открыть настройки?")
                        .setPositiveButton("Настройки", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent);
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
            }
        }
    }

    private void showAddTaskDialog(Task task) {
        AddTaskDialog dialog = AddTaskDialog.newInstance(task);
        dialog.show(getSupportFragmentManager(), "AddTaskDialog");
    }

    private void deleteTaskWithUndo(Task task) {
        taskViewModel.delete(task);
        Snackbar.make(recyclerView, R.string.task_deleted_toast, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_button_text, v -> taskViewModel.insert(task))
                .show();
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = adapter.getTaskAt(position);
                    deleteTaskWithUndo(task);
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            showDeleteAllConfirmationDialog();
            return true;
        } else if (id == R.id.action_filter_active) {
            taskViewModel.setFilter(TaskViewModel.FILTER_ACTIVE);
            Toast.makeText(this, R.string.filter_active_tasks_toast, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_filter_completed) {
            taskViewModel.setFilter(TaskViewModel.FILTER_COMPLETED);
            Toast.makeText(this, R.string.filter_completed_tasks_toast, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_filter_all) {
            taskViewModel.setFilter(TaskViewModel.FILTER_ALL);
            Toast.makeText(this, R.string.filter_all_tasks_toast, Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_all_tasks_dialog_title)
                .setMessage(R.string.delete_all_tasks_dialog_message)
                .setPositiveButton(R.string.delete_all_button_text, (dialog, which) -> {
                    taskViewModel.deleteAllTasks();
                    Toast.makeText(this, R.string.all_tasks_deleted_toast, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }
}
