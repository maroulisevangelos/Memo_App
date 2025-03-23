package com.example.memo.Maps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

import com.example.memo.MainActivity;
import com.example.memo.R;

public class Maps extends AppCompatActivity {

    private LinearLayout buttonContainer;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                populateButtons();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_layout);

        buttonContainer = findViewById(R.id.destination_container);

        LinearLayout destinationContainer = findViewById(R.id.destination_container);

        Button btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MapsAdd.class);
                startActivityForResult(myIntent, 0);
            }
        });

        Button btnRemove = findViewById(R.id.btn_remove);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MapsRemove.class);
                startActivityForResult(myIntent, 0);
            }
        });

        Button btnBack = findViewById(R.id.btn_menu);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        populateButtons();
    }

    private void populateButtons() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);


        if (sharedPreferences.contains("destination_list")) {
            Set<String> destinationSet = sharedPreferences.getStringSet("destination_list", new HashSet<String>());

            int marginLeft = 0;
            int marginRight = 0;
            int buttonCount = 4;
            int marginBottom = 0;
            int marginTop = -200;

            for (String destination : destinationSet) {

                String[] parts = destination.split(",");


                if (parts.length == 2) {

                    String mainText = parts[0];


                    String additionalText = parts[1];

                    if (buttonCount % 2 == 0) {
                        marginTop =  marginTop + 250 ;
                        marginRight = 5;
                        marginLeft = 0;
                    } else {
                        marginLeft = 535;
                        marginRight = 0;
                        marginTop =  marginTop - marginTop - 190  ;
                    }

                    addButton(mainText, additionalText, buttonContainer, marginBottom, marginLeft, marginRight, marginTop);
                    buttonCount++;
                }
            }
        }
    }

    private void addButton(String text1, String text2, LinearLayout container, int marginBottom, int marginLeft, int marginRight, int marginTop ) {
        Button newButton = new Button(this);
        newButton.setText(text1);

        newButton.setBackgroundResource(R.drawable.rounded_button_all_sides);

        float scale = getResources().getDisplayMetrics().density;
        int widthInDp = 165;
        int heightInDp = 70;
        int widthInPixels = (int) (widthInDp * scale + 0.5f);
        int heightInPixels = (int) (heightInDp * scale + 0.5f);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                widthInPixels,
                heightInPixels
        );




        newButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        newButton.setTransformationMethod(null);


        newButton.setTextColor(getResources().getColor(R.color.black));


        newButton.setTextSize(24);


        newButton.setTypeface(null, Typeface.BOLD);


        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);


        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Destination.class);

                intent.putExtra("destination", text2);
                startActivity(intent);
            }
        });

        newButton.setLayoutParams(layoutParams);

        container.addView(newButton);
    }
}


