package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.memo.Calls.ContactMain;
import com.example.memo.Calls.Contacts;
import com.example.memo.Gallery.MainPhoto;
import com.example.memo.Maps.Maps;
import com.example.memo.Panic.MainPanic;
import com.example.memo.Reminder.Reminder;

import java.util.ArrayList;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private Button btn_voice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        SharedPreferences sharedPreferences1 = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.clear();
        editor.apply();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPanic = findViewById(R.id.btn_panic);
        Button btnReminders = findViewById(R.id.btn_reminders);
        Button btnMap = findViewById(R.id.btn_map);
        Button btnPhotos = findViewById(R.id.btn_photos);
        Button btnCalls = findViewById(R.id.btn_calls);

        btn_voice = findViewById(R.id.btn_voice);
        btn_voice.setOnClickListener(v -> promptSpeechInput());

        btnPanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panicButtonClick();
            }
        });

        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderButtonClick();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsButtonClick();
            }
        });

        btnPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photosButtonClick();
            }
        });

        btnCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callsButtonClick();
            }
        });
    }
    private void panicButtonClick() {
        Intent intent = new Intent(com.example.memo.MainActivity.this, MainPanic.class);
        startActivity(intent);
    }
    private void reminderButtonClick() {
        Intent intent = new Intent(com.example.memo.MainActivity.this, Reminder.class);
        startActivity(intent);
    }
    private void mapsButtonClick() {
        Intent intent = new Intent(com.example.memo.MainActivity.this, Maps.class);
        startActivity(intent);
    }

    private void callsButtonClick() {
        Intent intent = new Intent(com.example.memo.MainActivity.this, Contacts.class);
        startActivity(intent);
    }

    private void photosButtonClick() {
        Intent intent = new Intent(com.example.memo.MainActivity.this, MainPhoto.class);
        startActivity(intent);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("el")); // Set language to Greek
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Πείτε το όνομα της λειτουργίας"); // Prompt in Greek
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String recognizedText = result.get(0);

            performActionBasedOnCommand(recognizedText);
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void performActionBasedOnCommand(String command) {
        // Define specific commands
        if (command.equalsIgnoreCase("κλήσεις")) {
            callsButtonClick();
        }else if (command.equalsIgnoreCase("φωτογραφίες")) {
            photosButtonClick();
        } else if (command.equalsIgnoreCase("χάρτης")) {
            mapsButtonClick();
        } else if (command.equalsIgnoreCase("υπενθυμίσεις")) {
            reminderButtonClick();
        }else if (command.equalsIgnoreCase("κουμπί πανικού")) {
            panicButtonClick();
        } else {
            // Handle unrecognized commands
            showErrorToast("Πείτε το όνομα της λειτουργίας");
        }
    }
}
