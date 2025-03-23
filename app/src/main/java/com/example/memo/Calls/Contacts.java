package com.example.memo.Calls;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memo.R;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {

    public static final int REQUEST_ADD_CONTACT = 1;
    public static final int REQUEST_DELETE_CONTACT = 3;
    private List<ContactsModel> contacts;
    int a = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        if(a == 0){
            contacts = new ArrayList<>();
            setupExampleContacts();
            a = 1;
        }

        ImageButton leftButton = findViewById(R.id.leftButton);
        ImageButton rightButton = findViewById(R.id.rightButton);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contacts.this, ContactsSearch.class);
                intent.putParcelableArrayListExtra("contacts", (ArrayList<ContactsModel>) contacts);
                startActivity(intent);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contacts.this, ContactsAdd.class);
                startActivityForResult(intent, REQUEST_ADD_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_CONTACT && data != null) {
            String name = data.getStringExtra("name");
            String relation = data.getStringExtra("relation");
            String num1 = data.getStringExtra("num1");
            String num2 = data.getStringExtra("num2");
            String photoUri = data.getStringExtra("photoUri");

            ContactsModel newContact = new ContactsModel(name, relation, num1, num2, photoUri, contacts.size());
            contacts.add(newContact);

        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_DELETE_CONTACT) {
            if (data != null) {
                int positionToDelete = data.getIntExtra("position", -1);
                if (positionToDelete != -1) {
                    // Delete contact from Contacts list
                    if (positionToDelete < contacts.size()) {
                        contacts.remove(positionToDelete);
                    }
                }
            }
        }
    }

    private void setupExampleContacts() {
        contacts.add(new ContactsModel("Guy_1", "doctor", "69XXXXXXXX", "69XXXXXXXX", "g1", 44));
        contacts.add(new ContactsModel("Guy 2", "friend", "69XXXXXXXX", "69XXXXXXXX", "g2", 45));
        contacts.add(new ContactsModel("Woman 1", "daughter", "69XXXXXXXX", "69XXXXXXXX", "w1", 3));
        contacts.add(new ContactsModel("Woman 2", "wife", "69XXXXXXXX", "69XXXXXXXX", "w2", 23));
        contacts.add(new ContactsModel("Woman 5", "friend", "69XXXXXXXX", "69XXXXXXXX", "w3", 78));
    }
}


