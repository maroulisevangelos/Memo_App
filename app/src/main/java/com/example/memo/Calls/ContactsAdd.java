package com.example.memo.Calls;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

import java.io.IOException;

public class ContactsAdd extends AppCompatActivity {

    private EditText editTextName, editTextRelation, editTextNum1, editTextNum2;
    private Button buttonAddContact, buttonChooseImage;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add);

        editTextName = findViewById(R.id.editTextName);
        editTextRelation = findViewById(R.id.editTextRelation);
        editTextNum1 = findViewById(R.id.editTextNum1);
        editTextNum2 = findViewById(R.id.editTextNum2);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imageView = findViewById(R.id.imageView);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String relation = editTextRelation.getText().toString().trim();
                String num1 = editTextNum1.getText().toString().trim();
                String num2 = editTextNum2.getText().toString().trim();

                if (name.isEmpty() || relation.isEmpty() || (num1.isEmpty() && num2.isEmpty())) {
                    Toast.makeText(ContactsAdd.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String photoUriString = (imageUri != null) ? imageUri.toString() : null;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", name);
                resultIntent.putExtra("relation", relation);
                resultIntent.putExtra("num1", num1);
                resultIntent.putExtra("num2", num2);
                if (photoUriString != null) {
                    resultIntent.putExtra("photoUri", photoUriString);
                }

                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });



    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


