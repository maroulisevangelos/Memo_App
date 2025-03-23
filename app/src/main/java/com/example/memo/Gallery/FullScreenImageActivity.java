package com.example.memo.Gallery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

public class FullScreenImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = findViewById(R.id.fullScreenImageView);
        TextView photoDesc = findViewById(R.id.PhotoDesc);

        // Get the image resource ID from the Intent
        int imageResId = getIntent().getIntExtra("imageResId", 0);
        String imageName = getIntent().getStringExtra("imageName");
        String imageInfo = getIntent().getStringExtra("imageInfo");

        photoDesc.setText(imageName +"     "+ imageInfo);

        if (imageResId != 0) {
            fullScreenImageView.setImageResource(imageResId);
        } else {
            // Handle the case where the image resource is not found
            fullScreenImageView.setImageResource(R.drawable.default_image);
        }
    }
}