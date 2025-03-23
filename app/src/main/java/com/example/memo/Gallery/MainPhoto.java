package com.example.memo.Gallery;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.R;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import android.content.Intent;

import android.speech.RecognizerIntent;

import android.widget.TextView;
import androidx.annotation.Nullable;

import java.util.Locale;


public class MainPhoto extends AppCompatActivity {
    private ArrayList<photo> photolistDate;
    private ArrayList<StringDatePair> dateList = new ArrayList<>();
    private ArrayList<photo> photolistLike;
    private ArrayList<StringCounterPair> counterList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String formattedDay;
    private String formattedMonth;
    private String formattedYear;
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yy");

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private Button btnSpeak;

    private Button buttonLike;
    private Button buttonTime;

    private int pressedColor;
    private int normalColor;

    private boolean LikeisPressed = false;
    private boolean TimeisPressed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_main);
        recyclerView = findViewById(R.id.recyclerView);

        photolistLike = new ArrayList<>();


        setPhotoInfo();
        sortDate(dateList);
        setAdapter(photolistDate);

        setPhotoInfoLike();
        loadCountersFromPreferences();
        sortCounter(counterList);

        buttonTime = findViewById(R.id.btn_time);
        buttonLike = findViewById(R.id.btn_like);
        btnSpeak = findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(v -> promptSpeechInput());

        // Define colors
        normalColor = ContextCompat.getColor(this, R.color.button_normal);
        pressedColor = ContextCompat.getColor(this, R.color.button_pressed);

        // Create a StateListDrawable
        /*StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        drawable.addState(new int[]{}, new ColorDrawable(normalColor)); // default state

        // Set the StateListDrawable as the background of the button
        buttonTime.setBackground(drawable);
        buttonLike.setBackground(drawable);*/

        // Set the initial color of the button
        buttonLike.setBackground(new ColorDrawable(normalColor));
        buttonTime.setBackground(new ColorDrawable(pressedColor));

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLikeButtonClick();
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButtonClick();
            }
        });
    }

    private void handleTimeButtonClick() {
        if (!TimeisPressed) {
            buttonTime.setBackground(new ColorDrawable(pressedColor));
            TimeisPressed = true;
            buttonLike.setBackground(new ColorDrawable(normalColor));
            LikeisPressed = false;
            sortDate(dateList);
            setAdapter(photolistDate);
        }
    }

    private void handleLikeButtonClick() {
        if (!LikeisPressed) {
            buttonLike.setBackground(new ColorDrawable(pressedColor));
            LikeisPressed = true;
            buttonTime.setBackground(new ColorDrawable(normalColor));
            TimeisPressed = false;
            sortCounter(counterList);
            setAdapter(photolistLike);
            //StringCounterPair athensElement = findElementWithValue("athens");
            //athensElement.addVisit();
        }
    }

    private void setAdapter(ArrayList<photo> phList) {
        recyclerAdapter adapter = new recyclerAdapter(phList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setPhotoInfo(){
        dateList.add(new StringDatePair("naxos", new Date(2024, 6, 4)));
        dateList.add(new StringDatePair("athens", new Date(2024, 6, 6)));
        dateList.add(new StringDatePair("crete", new Date(2024, 6, 5)));
    }

    private void setPhotoInfoLike(){
        counterList.add(new StringCounterPair("naxos", 0));
        counterList.add(new StringCounterPair("athens", 0));
        counterList.add(new StringCounterPair("crete", 0));
    }

    private void sortDate(ArrayList<StringDatePair> dateL) {
        // Sort by date
        Collections.sort(dateL, new Comparator<StringDatePair>() {
            @Override
            public int compare(StringDatePair o1, StringDatePair o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        // save sorted list
        photolistDate = new ArrayList<>();
        int fm;
        for (StringDatePair pair : dateL) {
            formattedDay = dayFormat.format(pair.getDate());
            fm = Integer.valueOf(monthFormat.format(pair.getDate()));
            fm -= 1;
            formattedMonth = String.valueOf(fm);
            formattedYear = yearFormat.format(pair.getDate());
            photolistDate.add(new photo(pair.getValue(),formattedDay +" / " + formattedMonth + " / " + formattedYear));
        }
    }

    private void sortCounter(ArrayList<StringCounterPair> counterL) {
        // Sort by counter
        Collections.sort(counterL, new Comparator<StringCounterPair>() {
            @Override
            public int compare(StringCounterPair o1, StringCounterPair o2) {
                return Integer.compare(o2.getCounter(), o1.getCounter());
            }
        });

        // save sorted list
        photolistLike = new ArrayList<>();
        for (StringCounterPair pair : counterL) {
            photolistLike.add(new photo(pair.getValue(),pair.getSCounter()+" ♥"));
        }
    }

    public StringCounterPair findElementWithValue(String value) {
        // Iterate through the list and find the element with the specified value
        for (StringCounterPair pair : counterList) {
            if (pair.getValue().equals(value)) {
                pair.addVisit();
                return pair;
            }
        }
        // Return null if no element is found
        return null;
    }

    private void saveCountersToPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        for (StringCounterPair pair : counterList) {
            editor.putInt(pair.getValue(), pair.getCounter());
        }

        editor.apply();
    }

    private void loadCountersFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        for (StringCounterPair pair : counterList) {
            pair.setCounter(prefs.getInt(pair.getValue(), 0)); // 0 is the default value
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCountersToPreferences();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("el")); // Set language to Greek
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Πείτε ΗΜΕΡΟΜΗΝΙΑ ή ΑΓΑΠΗΜΕΝΑ"); // Prompt in Greek
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
        if (command.equalsIgnoreCase("ημερομηνία")) {
            handleTimeButtonClick();
        }else if (command.equalsIgnoreCase("αγαπημένα")) {
            handleLikeButtonClick();
        } else {
            // Handle unrecognized commands
            showErrorToast("Πείτε ΗΜΕΡΟΜΗΝΙΑ \n ή ΑΓΑΠΗΜΕΝΑ");
        }
    }
}