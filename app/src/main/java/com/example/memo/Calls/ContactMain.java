package com.example.memo.Calls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

public class ContactMain extends AppCompatActivity {

    private static final int REQUEST_EDIT_CONTACT = 2;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_main);

        TextView nameTextView = findViewById(R.id.textViewName);
        TextView relationTextView = findViewById(R.id.textViewRelation);
        TextView num1TextView = findViewById(R.id.textViewNum1);
        TextView num2TextView = findViewById(R.id.textViewNum2);
        ImageView imageView = findViewById(R.id.imageViewContact);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String relation = intent.getStringExtra("relation");
        String num1 = intent.getStringExtra("num1");
        String num2 = intent.getStringExtra("num2");
        String photo = intent.getStringExtra("image");
        position = intent.getIntExtra("position", -1);

        nameTextView.setText(name);
        relationTextView.setText(relation);
        num1TextView.setText(num1);
        num2TextView.setText(num2);
        if (photo != null) {
            int imageResource = getResources().getIdentifier(photo, "drawable", getPackageName());
            imageView.setImageResource(imageResource);
        }

        Button editButton = findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(ContactMain.this, ContactsEdit.class);
            editIntent.putExtra("name", name);
            editIntent.putExtra("relation", relation);
            editIntent.putExtra("num1", num1);
            editIntent.putExtra("num2", num2);
            editIntent.putExtra("photoUri", photo);
            editIntent.putExtra("position", position);
            startActivityForResult(editIntent, REQUEST_EDIT_CONTACT);
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position); // position of the contact to delete
                setResult(RESULT_OK, resultIntent);
                finish(); // Finish ContactMain activity and return to ContactsSearch
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_CONTACT && data != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtras(data.getExtras());
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
