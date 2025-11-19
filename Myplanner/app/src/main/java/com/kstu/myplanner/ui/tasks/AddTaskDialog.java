package com.kstu.myplanner.ui.tasks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.kstu.myplanner.R;
import com.kstu.myplanner.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskDialog extends Dialog {

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private Button buttonSelectDate;
    private TextView textViewSelectedDate;
    private RadioGroup radioGroupPriority;
    private Spinner spinnerCategory;
    private Spinner spinnerReminder;
    private Button buttonSave;
    private Button buttonCancel;

    private Calendar selectedDate;
    private OnTaskCreatedListener listener;

    public AddTaskDialog(@NonNull Context context, OnTaskCreatedListener listener) {
        super(context);
        this.listener = listener;
        this.selectedDate = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_task);

        initViews();
        setupCategorySpinner();
        setupListeners();
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
    }

    private void setupCategorySpinner() {
        String[] categories = {
                "Работа",
                "Личное",
                "Учёба",
                "Покупки",
                "Здоровье",
                "Спорт",
                "Дом",
                "Другое"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        buttonSelectDate.setOnClickListener(v -> showDatePicker());
        buttonCancel.setOnClickListener(v -> dismiss());
        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        textViewSelectedDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Введите название задачи", Toast.LENGTH_SHORT).show();
            return;
        }

        String priority = getPriorityFromRadioGroup();
        String category = spinnerCategory.getSelectedItem().toString();

        Task task = new Task(
                title,
                description,
                selectedDate.getTimeInMillis(),
                priority,
                category
        );

        if (listener != null) {
            listener.onTaskCreated(task);
        }

        dismiss();
    }

    private String getPriorityFromRadioGroup() {
        int selectedId = radioGroupPriority.getCheckedRadioButtonId();

        if (selectedId == R.id.radioHigh) {
            return "HIGH";
        } else if (selectedId == R.id.radioLow) {
            return "LOW";
        } else {
            return "MEDIUM";
        }
    }

    public interface OnTaskCreatedListener {
        void onTaskCreated(Task task);
    }
}