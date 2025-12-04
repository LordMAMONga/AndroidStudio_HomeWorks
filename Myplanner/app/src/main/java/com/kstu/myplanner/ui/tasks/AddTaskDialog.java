package com.kstu.myplanner.ui.tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.kstu.myplanner.R;
import com.kstu.myplanner.model.Priority;
import com.kstu.myplanner.model.Task;
import com.kstu.myplanner.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskDialog extends DialogFragment {

    private static final String ARG_TASK = "task";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private Button buttonSelectDate;
    private Button buttonSelectTime;
    private TextView textViewSelectedDate;
    private RadioGroup radioGroupPriority;
    private AutoCompleteTextView autoCompleteCategory;
    private AutoCompleteTextView autoCompleteReminder;
    private Button buttonSave;
    private Button buttonCancel;

    private Calendar selectedDate;
    private TaskViewModel taskViewModel;
    private Integer selectedReminderMinutes = null;
    private Task existingTask = null;
    private int[] reminderValues;
    private String[] reminderTimes;

    public AddTaskDialog() {}

    public static AddTaskDialog newInstance(@Nullable Task task) {
        AddTaskDialog dialog = new AddTaskDialog();
        Bundle args = new Bundle();
        if (task != null) {
            args.putSerializable(ARG_TASK, task);
        }
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedDate = Calendar.getInstance();

        if (getArguments() != null && getArguments().containsKey(ARG_TASK)) {
            existingTask = (Task) getArguments().getSerializable(ARG_TASK);
        }

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupExposedDropdownMenus();
        setupListeners();

        if (existingTask != null) {
            populateUiWithTaskData();
        } else {
            updateDateDisplay();
            // Set a default reminder for new tasks
            setDefaultReminder();
        }
    }

    private void initViews(View view) {
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonSelectDate = view.findViewById(R.id.buttonSelectDate);
        buttonSelectTime = view.findViewById(R.id.buttonSelectTime);
        textViewSelectedDate = view.findViewById(R.id.textViewSelectedDate);
        radioGroupPriority = view.findViewById(R.id.radioGroupPriority);
        autoCompleteCategory = view.findViewById(R.id.autoCompleteCategory);
        autoCompleteReminder = view.findViewById(R.id.autoCompleteReminder);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonCancel = view.findViewById(R.id.buttonCancel);
    }

    private void setupExposedDropdownMenus() {
        // Category Menu
        String[] categories = getResources().getStringArray(R.array.task_categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                categories
        );
        autoCompleteCategory.setAdapter(categoryAdapter);

        // Reminder Menu
        reminderValues = getResources().getIntArray(R.array.reminder_time_values_minutes);
        reminderTimes = getResources().getStringArray(R.array.reminder_times);
        ArrayAdapter<String> reminderAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                reminderTimes
        );
        autoCompleteReminder.setAdapter(reminderAdapter);
        autoCompleteReminder.setOnItemClickListener((parent, view, position, id) -> {
            int selectedValue = reminderValues[position];
            // Ensure that only positive values are considered valid reminders
            selectedReminderMinutes = selectedValue > 0 ? selectedValue : null;
        });
    }

    private void setupListeners() {
        buttonSelectDate.setOnClickListener(v -> showDatePicker());
        buttonSelectTime.setOnClickListener(v -> showTimePicker());
        buttonCancel.setOnClickListener(v -> dismiss());
        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void populateUiWithTaskData() {
        editTextTitle.setText(existingTask.getTitle());
        editTextDescription.setText(existingTask.getDescription());
        selectedDate.setTimeInMillis(existingTask.getDueDate());
        updateDateDisplay();
        setPriorityInRadioGroup(existingTask.getPriority());
        autoCompleteCategory.setText(existingTask.getCategory(), false);

        selectedReminderMinutes = existingTask.getReminderMinutes();
        // Check if the existing task has a valid reminder.
        if (selectedReminderMinutes != null && selectedReminderMinutes > 0) {
            // A valid reminder exists, find and display it.
            for (int i = 0; i < reminderValues.length; i++) {
                if (reminderValues[i] == selectedReminderMinutes) {
                    autoCompleteReminder.setText(reminderTimes[i], false);
                    break;
                }
            }
        } else {
            // No valid reminder is set, so apply the default reminder logic.
            setDefaultReminder();
        }
    }

    private void setDefaultReminder() {
        // Find the first valid reminder value (greater than 0)
        int defaultIndex = -1;
        for (int i = 0; i < reminderValues.length; i++) {
            if (reminderValues[i] > 0) {
                defaultIndex = i;
                break;
            }
        }

        if (defaultIndex != -1) {
            // A valid default reminder is found (e.g., "10 minutes")
            autoCompleteReminder.setText(reminderTimes[defaultIndex], false);
            selectedReminderMinutes = reminderValues[defaultIndex];
        } else if (reminderTimes.length > 0) {
            // No valid positive reminder found, default to the first item (e.g., "No reminder")
            autoCompleteReminder.setText(reminderTimes[0], false);
            selectedReminderMinutes = null;
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateDisplay();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDate.set(Calendar.MINUTE, minute);
                    updateDateDisplay();
                },
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE),
                true // 24-hour format
        );
        timePickerDialog.show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("ru"));
        textViewSelectedDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), R.string.enter_task_name_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        Priority priority = getPriorityFromRadioGroup();
        String category = autoCompleteCategory.getText().toString();

        if (existingTask != null) {
            existingTask.setTitle(title);
            existingTask.setDescription(description);
            existingTask.setDueDate(selectedDate.getTimeInMillis());
            existingTask.setPriority(priority);
            existingTask.setCategory(category);
            existingTask.setReminderMinutes(selectedReminderMinutes);
            taskViewModel.update(existingTask);
            Toast.makeText(getContext(), R.string.task_updated_toast, Toast.LENGTH_SHORT).show();
        } else {
            Task task = new Task(
                    title,
                    description,
                    selectedDate.getTimeInMillis(),
                    priority,
                    category,
                    selectedReminderMinutes,
                    null
            );
            taskViewModel.insert(task);
            Toast.makeText(getContext(), R.string.task_added_toast, Toast.LENGTH_SHORT).show();
        }

        dismiss();
    }

    private Priority getPriorityFromRadioGroup() {
        int selectedId = radioGroupPriority.getCheckedRadioButtonId();

        if (selectedId == R.id.radioHigh) {
            return Priority.HIGH;
        } else if (selectedId == R.id.radioLow) {
            return Priority.LOW;
        } else {
            return Priority.MEDIUM;
        }
    }

    private void setPriorityInRadioGroup(Priority priority) {
        if (priority == null) {
            radioGroupPriority.check(R.id.radioMedium);
            return;
        }
        switch (priority) {
            case HIGH:
                radioGroupPriority.check(R.id.radioHigh);
                break;
            case MEDIUM:
                radioGroupPriority.check(R.id.radioMedium);
                break;
            case LOW:
                radioGroupPriority.check(R.id.radioLow);
                break;
        }
    }
}
