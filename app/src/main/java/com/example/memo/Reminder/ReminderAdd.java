package com.example.memo.Reminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReminderAdd extends AppCompatActivity {

    private Button option1;
    private Button option2;
    private TimePicker timePicker;
    private TimePicker timePicker2;
    private EditText input;

    private String selectedOption = "";
    private String savedTime = "";
    private String savedTime2 = "";
    private String reminderName = "";
    private ArrayList<String> selectedDays = new ArrayList<>();
    private String selectedDate = "";
    private boolean isSecondTimePickerVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_add);

        Button btnAddTime = findViewById(R.id.btn_add_time);
        timePicker2 = findViewById(R.id.timePicker2);
        option2 = findViewById(R.id.option2);
        timePicker = findViewById(R.id.timePicker);
        input = findViewById(R.id.input);

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of the second TimePicker
                if (timePicker2.getVisibility() == View.GONE) {
                    timePicker2.setVisibility(View.VISIBLE);
                    isSecondTimePickerVisible = true;
                } else {
                    timePicker2.setVisibility(View.GONE);
                    isSecondTimePickerVisible = false;
                }
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ReminderOptions.class);
                startActivity(myIntent);
            }
        });

        Button btnSave = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderAdd.this, Reminder.class);
                startActivity(intent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedTime = getTimeFromTimePicker();
                if (isSecondTimePickerVisible) {
                    savedTime2 = getTimeFromTimePicker2();
                }
                reminderName = input.getText().toString();


                saveReminderData();

                Toast.makeText(ReminderAdd.this, "Η υπενθύμιση αποθηκεύτηκε", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(ReminderAdd.this, Reminder.class);
                startActivity(intent);
                finish();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE);
        String selectedDaysString = sharedPreferences.getString("selected_days", "");
        if (!TextUtils.isEmpty(selectedDaysString)) {
            selectedDays.addAll(Arrays.asList(selectedDaysString.split(",")));
        }

        selectedDate = sharedPreferences.getString("selected_date", "");


        if (selectedDate == null) {
            selectedDate = "";
        }
    }

    private void handleOptionSelection(String option) {
        selectedOption = option;
    }

    private String getTimeFromTimePicker() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        return String.format("%02d:%02d", hour, minute);
    }

    private String getTimeFromTimePicker2() {
        int hour = timePicker2.getHour();
        int minute = timePicker2.getMinute();
        return String.format("%02d:%02d", hour, minute);
    }

    private void saveReminderData() {
        SharedPreferences sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        Set<String> remindersSet = sharedPreferences.getStringSet("reminders_list", new HashSet<>());


        String selectedOption = this.selectedOption;
        String savedTime = getTimeFromTimePicker();
        String savedTime2 = isSecondTimePickerVisible ? getTimeFromTimePicker2() : "";
        String reminderName = this.reminderName;
        ArrayList<String> selectedDays = this.selectedDays;
        String selectedDate = this.selectedDate;


        String selectedDaysString = TextUtils.join(",", selectedDays);


        String reminderId = UUID.randomUUID().toString();


        editor.putString(reminderId + "_selected_option", selectedOption);
        editor.putString(reminderId + "_selected_time", savedTime);
        if (isSecondTimePickerVisible) {
            editor.putString(reminderId + "_selected_time2", savedTime2);
        }else {
            editor.putString(reminderId + "_selected_time2", null);
        }
        editor.putString(reminderId + "_reminder_name", reminderName);
        editor.putString(reminderId + "_selected_days", selectedDaysString);
        editor.putString(reminderId + "_selected_date", selectedDate);


        remindersSet.add(reminderId);


        editor.putStringSet("reminders_list", remindersSet);

        editor.apply();
    }
}

