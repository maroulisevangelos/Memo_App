package com.example.memo.Calls;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsSearch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Contacts_RVAdapter adapter;
    private List<ContactsModel> contacts;
    private List<ContactsModel> filteredContacts;

    private static final int REQUEST_ADD_CONTACT = 1;
    private static final int REQUEST_EDIT_CONTACT = 2;
    private static final int REQUEST_DELETE_CONTACT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_search);

        // Initialize contacts list
        contacts = new ArrayList<>();

        // Retrieve contacts ArrayList from intent extras
        if (getIntent().hasExtra("contacts")) {
            contacts = getIntent().getParcelableArrayListExtra("contacts");
        }

        // Initialize filtered contacts list
        filteredContacts = new ArrayList<>(contacts);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.contactsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Contacts_RVAdapter(this, (ArrayList<ContactsModel>) filteredContacts);
        recyclerView.setAdapter(adapter);

        // Initialize SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String query) {
        filteredContacts.clear();

        if (TextUtils.isEmpty(query)) {
            filteredContacts.addAll(contacts);
        } else {
            String filterPattern = query.toLowerCase().trim();

            for (ContactsModel contact : contacts) {
                if (contact.getName().toLowerCase().contains(filterPattern)) {
                    filteredContacts.add(contact);
                }
            }
        }

        adapter.filterList((ArrayList<ContactsModel>) filteredContacts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh RecyclerView when returning to the activity
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_ADD_CONTACT || requestCode == REQUEST_EDIT_CONTACT) {
                // Retrieve updated contact list from the intent
                ArrayList<ContactsModel> updatedContacts = data.getParcelableArrayListExtra("updatedContacts");
                if (updatedContacts != null) {
                    // Update both lists
                    contacts.clear();
                    contacts.addAll(updatedContacts);
                    filteredContacts.clear();
                    filteredContacts.addAll(updatedContacts);
                    // Notify adapter of changes
                    adapter.notifyDataSetChanged();
                }
            } else if (data.getIntExtra("position",-1) != -1) {
                if (data != null) {
                    int positionToDelete = data.getIntExtra("position", -1);
                    if (positionToDelete != -1) {
                        // Pass the result back to Contacts activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("position", positionToDelete);
                        setResult(RESULT_OK, resultIntent);
                        finish(); // Finish ContactsSearch activity and return to Contacts
                    }
                }
            }

        }
    }
}

