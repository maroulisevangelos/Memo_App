package com.example.memo.Panic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.memo.R;



public class MainPanic extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final String PHONE_NUMBER = "tel:6984794819";

    private static final int REQUEST_SMS_PERMISSION = 1;
    private static final String MESSAGE = "ΕΙΜΑΙ ΣΕ ΑΝΑΓΚΗ ΣΕ ΧΡΕΙΑΖΟΜΑΙ!";
    private static final String[] PHONE_NUMBERS = {"6984794819,6980715963"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panic_main);

        ImageView panicButton = findViewById(R.id.panic_btn);
        panicButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(MainPanic.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainPanic.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
            } else {
                sendSms();
            }
            if (ContextCompat.checkSelfPermission(MainPanic.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainPanic.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                makePhoneCall();
            }
        });

    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(PHONE_NUMBER));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Δεν δόθηκε άδεια!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        for (String phoneNumber : PHONE_NUMBERS) {
            smsManager.sendTextMessage(phoneNumber, null, MESSAGE, null, null);
        }
        Toast.makeText(this, "Στάλθηκε SMS στις επαφές ανάγκης!.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Δεν δόθηκε άδεια!", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            } else {
                Toast.makeText(this, "Δεν δόθηκε άδεια!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
