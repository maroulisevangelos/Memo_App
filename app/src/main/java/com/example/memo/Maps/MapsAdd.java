package com.example.memo.Maps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

import java.util.HashSet;
import java.util.Set;

public class MapsAdd extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_add_layout);

        editText1 = findViewById(R.id.input);
        editText2 = findViewById(R.id.input2);

        Button btnSave = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String data1 = editText1.getText().toString();
                String data2 = editText2.getText().toString();
                String combinedData = data1 + ", " + data2;

                SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                Set<String> destinationSet = sharedPreferences.getStringSet("destination_list", new HashSet<String>());


                destinationSet.add(combinedData);


                editor.putStringSet("destination_list", destinationSet);
                editor.apply();


                Intent myIntent = new Intent(v.getContext(), Maps.class);
                startActivityForResult(myIntent, 0);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), Maps.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }
}


