package com.example.memo.Reminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReminderOptions extends AppCompatActivity {

    private CheckBox checkBoxMonday, checkBoxTuesday, checkBoxWednesday, checkBoxThursday, checkBoxFriday, checkBoxSaturday, checkBoxSunday;
    private Button btnSelectDate, btnSaveOptions, btnCancel;
    private TextView selectedDateTextView;
    private CalendarView calendarView;

    private ArrayList<String> selectedDays = new ArrayList<>();
    private String selectedDate = "";

    long selectedTimestamp1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_options);

        checkBoxMonday = findViewById(R.id.checkBoxMonday);
        checkBoxTuesday = findViewById(R.id.checkBoxTuesday);
        checkBoxWednesday = findViewById(R.id.checkBoxWednesday);
        checkBoxThursday = findViewById(R.id.checkBoxThursday);
        checkBoxFriday = findViewById(R.id.checkBoxFriday);
        checkBoxSaturday = findViewById(R.id.checkBoxSaturday);
        checkBoxSunday = findViewById(R.id.checkBoxSunday);

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSaveOptions = findViewById(R.id.btnSaveOptions);
        btnCancel = findViewById(R.id.btnCancel);

        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                selectedTimestamp1 = selectedCalendar.getTimeInMillis();


            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long selectedTimestamp = selectedTimestamp1;
                selectedDate = String.valueOf(selectedTimestamp);
                selectedDateTextView.setText("Επιλεγμένη Ημερομηνία: " + formatDate(Long.parseLong(selectedDate)));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderOptions.this, ReminderAdd.class);
                startActivity(intent);
            }
        });

        btnSaveOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedDays();
                saveSelectedDate();


                Toast.makeText(ReminderOptions.this, "Επιλογές Αποθηκεύτηκαν", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(ReminderOptions.this, ReminderAdd.class);
                intent.putStringArrayListExtra("selected_days", selectedDays);
                intent.putExtra("selected_date", selectedDate);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveSelectedDays() {

        List<String> selectedDays = new ArrayList<>();

        if (checkBoxMonday.isChecked()) {
            selectedDays.add("Δευτέρα");
        }
        if (checkBoxTuesday.isChecked()) {
            selectedDays.add("Τρίτη");
        }
        if (checkBoxWednesday.isChecked()) {
            selectedDays.add("Τετάρτη");
        }
        if (checkBoxThursday.isChecked()) {
            selectedDays.add("Πέμπτη");
        }
        if (checkBoxFriday.isChecked()) {
            selectedDays.add("Παρασκευή");
        }
        if (checkBoxSaturday.isChecked()) {
            selectedDays.add("Σάββατο");
        }
        if (checkBoxSunday.isChecked()) {
            selectedDays.add("Κυριακή");
        }


        String selectedDaysString = TextUtils.join(",", selectedDays);


        SharedPreferences sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_days", selectedDaysString);
        editor.apply();


        this.selectedDays.clear();
        this.selectedDays.addAll(selectedDays);
    }

    private void saveSelectedDate() {

        SharedPreferences sharedPreferences = getSharedPreferences("reminder_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_date", selectedDate);
        editor.apply();
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
}
