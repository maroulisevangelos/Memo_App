package com.example.memo.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.util.TypedValue;

import com.example.memo.R;

public class ReminderList extends AppCompatActivity {

    private LinearLayout reminderContainer;
    private Set<String> pendingDeletes = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_list);

        reminderContainer = findViewById(R.id.reminder_container);

        Button btnSave = findViewById(R.id.btn_add);
        Button btnCancel = findViewById(R.id.btn_remove);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                applyDeletes();

                Intent intent = new Intent(ReminderList.this, Reminder.class);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pendingDeletes.clear();

                Intent intent = new Intent(ReminderList.this, Reminder.class);
                startActivity(intent);
            }
        });


        populateReminders();
    }

    private void populateReminders() {
        SharedPreferences sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE);


        if (sharedPreferences.contains("reminders_list")) {
            Set<String> remindersSet = sharedPreferences.getStringSet("reminders_list", new HashSet<String>());

            int marginBottom = 0;
            int marginLeft = 0;
            int marginRight = 0;
            int marginTop = 0;


            for (String reminderId : remindersSet) {

                if (pendingDeletes.contains(reminderId)) continue;

                String selectedOption = sharedPreferences.getString(reminderId + "_selected_option", "");
                String savedTime = sharedPreferences.getString(reminderId + "_selected_time", "");
                String savedTime2 = sharedPreferences.getString(reminderId + "_selected_time2", "");
                String selectedDate = sharedPreferences.getString(reminderId + "_selected_date", "");
                String reminderName = sharedPreferences.getString(reminderId + "_reminder_name", "");
                String selectedDaysString = sharedPreferences.getString(reminderId + "_selected_days", "");


                List<String> selectedDays = Arrays.asList(selectedDaysString.split(","));

                addButton(selectedOption, savedTime,savedTime2, selectedDays, selectedDate, reminderName, reminderId, marginBottom, marginLeft, marginRight, marginTop);


                marginTop += 16;
            }
        }
    }

    private void addButton(String selectedOption, String selectedTime,String selectedTime2 , List<String> selectedDays, String selectedDate, String reminderName, String reminderId,
                           int marginBottom, int marginLeft, int marginRight, int marginTop) {

        RelativeLayout reminderLayout = new RelativeLayout(this);
        reminderLayout.setBackgroundResource(R.drawable.rounded_button_all_sides);


        LinearLayout infoLayout = new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams infoLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        infoLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        infoLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        infoLayoutParams.setMargins(marginLeft + 32, marginTop, marginRight, marginBottom);
        infoLayout.setLayoutParams(infoLayoutParams);


        TextView reminderInfoTextView = new TextView(this);
        reminderInfoTextView.setTextColor(getResources().getColor(R.color.white));
        reminderInfoTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        reminderInfoTextView.setTypeface(null, Typeface.BOLD);
        reminderInfoTextView.setPadding(0, 0, 0, 0);


        StringBuilder reminderInfo = new StringBuilder();


        if (!TextUtils.isEmpty(selectedDate)) {
            reminderInfo.append(formatDate(Long.parseLong(selectedDate))).append("  ");
        }


        if (!selectedDays.isEmpty()) {
            StringBuilder selectedDaysText = new StringBuilder();
            for (String day : selectedDays) {
                selectedDaysText.append(day).append(", ");
            }
            selectedDaysText.delete(selectedDaysText.length() - 2, selectedDaysText.length());
            reminderInfo.append(selectedDaysText).append("  ");
        }


        if (!selectedTime.isEmpty()) {
            reminderInfo.append(selectedTime).append("\n");
        }

        if (!selectedTime.isEmpty()) {
            reminderInfo.append(selectedTime2).append("\n");
        }


        reminderInfo.append(reminderName);


        reminderInfoTextView.setText(reminderInfo.toString());

        infoLayout.addView(reminderInfoTextView);

        reminderLayout.addView(infoLayout);

        Button deleteButton = new Button(this);
        deleteButton.setBackgroundResource(R.drawable.ic_cancel);


        int buttonSize = getResources().getDimensionPixelSize(R.dimen.button_size);
        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        deleteParams.addRule(RelativeLayout.CENTER_VERTICAL);
        deleteButton.setLayoutParams(deleteParams);

        int padding = getResources().getDimensionPixelSize(R.dimen.button_padding);
        deleteButton.setPadding(padding, padding, padding, padding);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markReminderForDeletion(reminderId);
            }
        });


        reminderLayout.addView(deleteButton);


        LinearLayout.LayoutParams reminderLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        reminderLayoutParams.setMargins(0, 0, 0, 16);
        reminderLayout.setLayoutParams(reminderLayoutParams);


        reminderContainer.addView(reminderLayout);
    }

    private void applyDeletes() {
        SharedPreferences sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        Set<String> remindersSet = sharedPreferences.getStringSet("reminders_list", new HashSet<String>());

        for (String reminderId : pendingDeletes) {

            String selectedOption = sharedPreferences.getString(reminderId + "_selected_option", "");
            String savedTime = sharedPreferences.getString(reminderId + "_selected_time", "");
            String savedTime2 = sharedPreferences.getString(reminderId + "_selected_time2", "");
            String selectedDate = sharedPreferences.getString(reminderId + "_selected_date", "");
            String reminderName = sharedPreferences.getString(reminderId + "_reminder_name", "");
            String selectedDaysString = sharedPreferences.getString(reminderId + "_selected_days", "");

            List<String> selectedDays = Arrays.asList(selectedDaysString.split(","));


            cancelReminder(savedTime,savedTime2, selectedDays, selectedDate, reminderName, reminderId);


            remindersSet.remove(reminderId);


            editor.remove(reminderId + "_selected_option");
            editor.remove(reminderId + "_selected_time");
            editor.remove(reminderId + "_selected_time2");
            editor.remove(reminderId + "_selected_date");
            editor.remove(reminderId + "_reminder_name");
            editor.remove(reminderId + "_selected_days");
        }


        editor.putStringSet("reminders_list", remindersSet);
        editor.apply();


        pendingDeletes.clear();


        reminderContainer.removeAllViews();
        populateReminders();


        Toast.makeText(this, "Reminders deleted", Toast.LENGTH_SHORT).show();
    }

    private void markReminderForDeletion(String reminderId) {

        pendingDeletes.add(reminderId);


        reminderContainer.removeAllViews();


        populateReminders();


        Toast.makeText(this, "Reminder marked for deletion", Toast.LENGTH_SHORT).show();
    }

    private void cancelReminder(String selectedTime,String selectedTime2 , List<String> selectedDays, String selectedDate, String reminderName, String reminderId) {

        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        try{
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date time1 = timeFormat.parse(selectedTime);
            Date time2;
            if (selectedTime2 != "") {
                time2 = timeFormat.parse(selectedTime2);
            }else{
                time2 = null;
            }
            if (!selectedDate.isEmpty()) {
                if (time1 != null) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (reminderId + "_1").hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingIntent);
                        Log.d("Reminder", "Alarm cancelled for reminderId: " + reminderId);
                    } else {
                        Log.e("Reminder", "AlarmManager is null");
                    }
                }
                if (time2 != null) {
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, (reminderId + "_2").hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingIntent2);
                        Log.d("Reminder", "Alarm cancelled for reminderId: " + reminderId);
                    } else {
                        Log.e("Reminder", "AlarmManager is null");
                    }
                }
            }else if (!selectedDays.isEmpty()) {
                for (String day : selectedDays) {
                    int dayOfWeek = getDayOfWeek(day);
                    if (time1 != null) {
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, (reminderId + "_1_" + dayOfWeek).hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        if (alarmManager != null) {
                            alarmManager.cancel(pendingIntent1);
                            Log.d("Reminder", "Alarm cancelled for reminderId: " + reminderId);
                        } else {
                            Log.e("Reminder", "AlarmManager is null");
                        }

                    }

                    if (time2 != null) {
                        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, (reminderId + "_1_" + dayOfWeek).hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        if (alarmManager != null) {
                            alarmManager.cancel(pendingIntent2);
                            Log.d("Reminder", "Alarm cancelled for reminderId: " + reminderId);
                        } else {
                            Log.e("Reminder", "AlarmManager is null");
                        }
                    }
                }

            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String formatDate(long timestamp) {
        if (timestamp == 0) {
            return "Not Selected";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);


        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return sdf.format(calendar.getTime());
    }

    private int getDayOfWeek(String day) {
        switch (day) {
            case "Δευτέρα":
                return Calendar.MONDAY;
            case "Τρίτη":
                return Calendar.TUESDAY;
            case "Τετάρτη":
                return Calendar.WEDNESDAY;
            case "Πέμπτη":
                return Calendar.THURSDAY;
            case "Παρασκευή":
                return Calendar.FRIDAY;
            case "Σάββατο":
                return Calendar.SATURDAY;
            case "Κυριακή":
                return Calendar.SUNDAY;
            default:
                return -1;
        }
    }
}


