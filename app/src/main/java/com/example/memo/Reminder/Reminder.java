package com.example.memo.Reminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.memo.MainActivity;
import com.example.memo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Reminder extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 2;

    private LinearLayout reminderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            }
        }

        checkNotificationSettings();

        reminderContainer = findViewById(R.id.reminder_container);

        Button btnAdd = findViewById(R.id.btn_add);
        Button btnRemove = findViewById(R.id.btn_remove);
        Button btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminder.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminder.this, ReminderAdd.class);
                startActivity(intent);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminder.this, ReminderList.class);
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
        LinearLayout reminderLayout = new LinearLayout(this);
        reminderLayout.setOrientation(LinearLayout.VERTICAL);
        reminderLayout.setBackgroundResource(R.drawable.rounded_button_all_sides);


        TextView reminderInfoTextView = new TextView(this);
        reminderInfoTextView.setTextColor(getResources().getColor(R.color.white));
        reminderInfoTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        reminderInfoTextView.setTypeface(null, Typeface.BOLD);
        reminderInfoTextView.setPadding(25, 0, 0, 0);


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

        if (!selectedTime2.isEmpty()) {
            reminderInfo.append(selectedTime2).append("\n");
        }


        reminderInfo.append(reminderName);


        reminderInfoTextView.setText(reminderInfo.toString());


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(marginLeft + 32, marginTop, marginRight, marginBottom); // Adjust margins as needed
        reminderInfoTextView.setLayoutParams(layoutParams);


        reminderLayout.addView(reminderInfoTextView);


        LinearLayout.LayoutParams reminderLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        reminderLayoutParams.setMargins(0, 0, 0, 16);
        reminderLayout.setLayoutParams(reminderLayoutParams);

                reminderContainer.addView(reminderLayout);


        scheduleReminder(this, reminderName, selectedTime,selectedTime2, selectedDays, selectedDate, reminderId);
    }

    private void scheduleReminder(Context context, String reminderName, String selectedTime, String selectedTime2, List<String> selectedDays, String selectedDate, String reminderId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        try {

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date time1 = timeFormat.parse(selectedTime);
            Date time2;

            if (selectedTime2 != "") {
                time2 = timeFormat.parse(selectedTime2);
            }else{
                time2 = null;
            }


            if (!selectedDate.isEmpty()) {

                long timestamp = Long.parseLong(selectedDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);

                if (time1 != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, time1.getHours());
                    calendar.set(Calendar.MINUTE, time1.getMinutes());
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent1 = new Intent(context, ReminderBroadcastReceiver.class);
                    intent1.putExtra("reminder_name", reminderName);
                    intent1.putExtra("reminder_time", selectedTime);
                    intent1.putExtra("reminder_id", reminderId + "_1");
                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, (reminderId + "_1").hashCode(), intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent1);
                    Log.d("Reminder", "One-time reminder scheduled for first time: " + calendar.getTime());
                }

                if (time2 != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, time2.getHours());
                    calendar.set(Calendar.MINUTE, time2.getMinutes());
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent2 = new Intent(context, ReminderBroadcastReceiver.class);
                    intent2.putExtra("reminder_name", reminderName);
                    intent2.putExtra("reminder_time", selectedTime2);
                    intent2.putExtra("reminder_id", reminderId + "_2");
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, (reminderId + "_2").hashCode(), intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent2);
                    Log.d("Reminder", "One-time reminder scheduled for second time: " + calendar.getTime());
                }

            } else if (!selectedDays.isEmpty()) {
                Calendar calendar1 = null;
                Calendar calendar2 = null;
                Intent intent1 = null;
                Intent intent2 = null;
                for (String day : selectedDays) {
                    int dayOfWeek = getDayOfWeek(day);


                    if (time1 != null) {
                        calendar1 = getNextWeekdayCalendar(dayOfWeek, time1);
                        Log.d("Reminder", "Scheduled first time for " + day + ": " + calendar1.getTime());
                        intent1 = new Intent(context, ReminderBroadcastReceiver.class);
                        intent1.putExtra("reminder_name", reminderName);
                        intent1.putExtra("reminder_time", selectedTime);
                        intent1.putExtra("reminder_id", reminderId + "_1");
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, (reminderId + "_1_" + dayOfWeek).hashCode(), intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
                    }


                    if (time2 != null) {
                        calendar2 = getNextWeekdayCalendar(dayOfWeek, time2);
                        Log.d("Reminder", "Scheduled second time for " + day + ": " + calendar2.getTime());
                        intent2 = new Intent(context, ReminderBroadcastReceiver.class);
                        intent2.putExtra("reminder_name", reminderName);
                        intent2.putExtra("reminder_time", selectedTime2);
                        intent2.putExtra("reminder_id", reminderId + "_2");
                        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, (reminderId + "_2_" + dayOfWeek).hashCode(), intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
                    }


                    for (int i = 1; i <= 3; i++) {
                        if (calendar1 != null) {
                            calendar1.add(Calendar.DAY_OF_YEAR, 7); // Move to the next week
                            Log.d("Reminder", "Scheduled first time for " + day + " (Week " + (i + 1) + "): " + calendar1.getTime());
                            PendingIntent subsequentPendingIntent1 = PendingIntent.getBroadcast(context, (reminderId + "_1_" + dayOfWeek + "_week_" + i).hashCode(), intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), subsequentPendingIntent1);
                        }
                        if (calendar2 != null) {
                            calendar2.add(Calendar.DAY_OF_YEAR, 7); // Move to the next week
                            Log.d("Reminder", "Scheduled second time for " + day + " (Week " + (i + 1) + "): " + calendar2.getTime());
                            PendingIntent subsequentPendingIntent2 = PendingIntent.getBroadcast(context, (reminderId + "_2_" + dayOfWeek + "_week_" + i).hashCode(), intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), subsequentPendingIntent2);
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Calendar getNextWeekdayCalendar(int dayOfWeek, Date time) {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilNextOccurrence = (dayOfWeek - currentDayOfWeek + 7) % 7;

        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextOccurrence);


        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, 0);


        if (daysUntilNextOccurrence == 0 && calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7); // Move to the next week if the time has already passed for today
        }

        return calendar;
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



    private void checkNotificationSettings() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (!notificationManagerCompat.areNotificationsEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable Notifications")
                    .setMessage("Notifications are disabled for this app. Please enable them in the settings.")
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openNotificationSettings();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void openNotificationSettings() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Exact alarm permission granted", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Exact alarm permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}





