package com.example.memo.Maps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;
import com.example.memo.Reminder.Reminder;
import com.example.memo.Reminder.ReminderList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsRemove extends AppCompatActivity {

    private LinearLayout buttonContainer;
    private List<String> currentDestinations;
    private List<String> deletedDestinations;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_remove);

        buttonContainer = findViewById(R.id.destination_container);
        currentDestinations = new ArrayList<>();
        deletedDestinations = new ArrayList<>();

        Button btnSave = findViewById(R.id.btn_add);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), Maps.class);
                startActivityForResult(myIntent, 0);
            }
        });

        btnRemove = findViewById(R.id.btn_remove);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!deletedDestinations.isEmpty()) {
                    currentDestinations.addAll(deletedDestinations);
                    saveDestinations();
                    updateButtonContainer();
                    deletedDestinations.clear();
                    btnRemove.setText("Ακύρωση");
                } else {
                    Snackbar.make(buttonContainer, "No destinations to undo", Snackbar.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(MapsRemove.this, Maps.class);
                startActivity(intent);
            }
        });


        populateButtons();
    }

    private void populateButtons() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);


        if (sharedPreferences.contains("destination_list")) {
            Set<String> destinationSet = sharedPreferences.getStringSet("destination_list", new HashSet<String>());
            currentDestinations.clear();
            currentDestinations.addAll(destinationSet);
            updateButtonContainer();
        }
    }

    private void updateButtonContainer() {
        buttonContainer.removeAllViews();
        LinearLayout currentRow = null;

        for (int i = 0; i < currentDestinations.size(); i++) {
            if (i % 2 == 0) {
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowParams.setMargins(0, 20, 0, 20);
                currentRow.setLayoutParams(rowParams);
                buttonContainer.addView(currentRow);
            }

            String destination = currentDestinations.get(i);
            String[] parts = destination.split(",");
            if (parts.length == 2) {
                String mainText = parts[0];
                String additionalText = parts[1];
                addButton(mainText, additionalText, currentRow);
            }
        }
    }

    private void addButton(String text1, String text2, LinearLayout container) {

        Button newButton = new Button(this);
        newButton.setText(text1);
        newButton.setBackgroundResource(R.drawable.rounded_button_all_sides);
        newButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        newButton.setTransformationMethod(null);
        newButton.setTextColor(getResources().getColor(R.color.black));
        newButton.setTextSize(23);
        newButton.setTypeface(null, Typeface.BOLD);


        float scale = getResources().getDisplayMetrics().density;
        int widthInDp = 165;
        int heightInDp = 70;
        int widthInPixels = (int) (widthInDp * scale + 0.5f);
        int heightInPixels = (int) (heightInDp * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                widthInPixels,
                heightInPixels
        );
        layoutParams.setMargins(10, 0, 10, 0);
        newButton.setLayoutParams(layoutParams);


        Drawable cancelIcon = getResources().getDrawable(R.drawable.ic_cancel);
        int paddingRight = (int) (10 * scale + 0.5f);
        cancelIcon.setBounds(0, 0, cancelIcon.getIntrinsicWidth(), cancelIcon.getIntrinsicHeight());
        newButton.setCompoundDrawablesWithIntrinsicBounds(null, null, cancelIcon, null);


        newButton.setPadding(newButton.getPaddingLeft(), newButton.getPaddingTop(), paddingRight, newButton.getPaddingBottom());


        newButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int left = newButton.getWidth() - newButton.getPaddingRight() - cancelIcon.getIntrinsicWidth();
                if (event.getX() >= left) {

                    String destination = text1 + "," + text2;
                    deletedDestinations.add(destination);
                    currentDestinations.remove(destination);
                    saveDestinations();
                    updateButtonContainer();


                    if (!deletedDestinations.isEmpty()) {
                        btnRemove.setText("Ακύρωση");
                    }

                    return true;
                }
            }
            return false;
        });


        container.addView(newButton);
    }

    private void saveDestinations() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> destinationSet = new HashSet<>(currentDestinations);
        editor.putStringSet("destination_list", destinationSet);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            populateButtons();
        }
    }
}
