package com.kstu.myplanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.ui.tasks.AddTaskDialog;
import com.kstu.myplanner.ui.tasks.TaskAdapter;
import com.kstu.myplanner.utils.NotificationHelper;
import com.kstu.myplanner.viewmodel.TaskViewModel;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textViewEmptyState;
    private FloatingActionButton fabAddTask;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        textViewEmptyState = findViewById(R.id.textViewEmptyState);
        fabAddTask = findViewById(R.id.fabAddTask);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        // Инициализация ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Наблюдаем за изменениями в списке задач
        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter.setTasks(tasks);

            // Показываем/скрываем empty state
            if (tasks == null || tasks.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                textViewEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                textViewEmptyState.setVisibility(View.GONE);
            }
        });

        // Обработчик нажатия на кнопку добавления задачи
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        // Обработчики кликов в адаптере
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                // Здесь можно добавить диалог редактирования
                Toast.makeText(MainActivity.this,
                        "Редактирование: " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckBoxClick(Task task) {
                task.setCompleted(!task.isCompleted());
                taskViewModel.update(task);
                Toast.makeText(MainActivity.this,
                        task.isCompleted() ? "✓ Задача выполнена" : "◯ Задача активна",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Task task) {
                showDeleteConfirmationDialog(task);
            }
        });

        // Свайп для удаления
        setupSwipeToDelete();
    }

    private void showAddTaskDialog() {
        AddTaskDialog dialog = new AddTaskDialog(this, task -> {
            taskViewModel.insert(task);
            Toast.makeText(this, "✓ Задача добавлена", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private void showDeleteConfirmationDialog(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Удалить задачу?")
                .setMessage("Вы уверены, что хотите удалить \"" + task.getTitle() + "\"?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    taskViewModel.delete(task);
                    Toast.makeText(this, "✗ Задача удалена", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Task task = adapter.getTaskAt(viewHolder.getAdapterPosition());
                showDeleteConfirmationDialog(task);
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
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
            taskViewModel.getActiveTasks().observe(this, tasks -> adapter.setTasks(tasks));
            Toast.makeText(this, "Показаны активные задачи", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_filter_completed) {
            taskViewModel.getCompletedTasks().observe(this, tasks -> adapter.setTasks(tasks));
            Toast.makeText(this, "Показаны завершенные задачи", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_filter_all) {
            taskViewModel.getAllTasks().observe(this, tasks -> adapter.setTasks(tasks));
            Toast.makeText(this, "Показаны все задачи", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить все задачи?")
                .setMessage("Это действие нельзя отменить!")
                .setPositiveButton("Удалить всё", (dialog, which) -> {
                    taskViewModel.deleteAllTasks();
                    Toast.makeText(this, "Все задачи удалены", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}