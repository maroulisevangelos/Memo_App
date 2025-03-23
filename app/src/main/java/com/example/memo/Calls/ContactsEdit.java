package com.example.memo.Calls;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

public class ContactsEdit extends AppCompatActivity {

    private EditText editTextName, editTextRelation, editTextNum1, editTextNum2;
    private ImageView imageViewContactEdit;
    private Button buttonSaveChanges;
    private Uri photoUri;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        editTextName = findViewById(R.id.editTextNameEdit);
        editTextRelation = findViewById(R.id.editTextRelationEdit);
        editTextNum1 = findViewById(R.id.editTextNum1Edit);
        editTextNum2 = findViewById(R.id.editTextNum2Edit);
        imageViewContactEdit = findViewById(R.id.imageViewContactEdit);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String relation = intent.getStringExtra("relation");
        String num1 = intent.getStringExtra("num1");
        String num2 = intent.getStringExtra("num2");
        String photoUriString = intent.getStringExtra("photoUri");
        position = intent.getIntExtra("position", -1);

        editTextName.setText(name);
        editTextRelation.setText(relation);
        editTextNum1.setText(num1);
        editTextNum2.setText(num2);

        if (photoUriString != null) {
            photoUri = Uri.parse(photoUriString);
            imageViewContactEdit.setImageURI(photoUri);
        }

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = editTextName.getText().toString().trim();
                String updatedRelation = editTextRelation.getText().toString().trim();
                String updatedNum1 = editTextNum1.getText().toString().trim();
                String updatedNum2 = editTextNum2.getText().toString().trim();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", updatedName);
                resultIntent.putExtra("relation", updatedRelation);
                resultIntent.putExtra("num1", updatedNum1);
                resultIntent.putExtra("num2", updatedNum2);
                resultIntent.putExtra("position", position);
                if (photoUri != null) {
                    resultIntent.putExtra("photoUri", photoUri.toString());
                }

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
