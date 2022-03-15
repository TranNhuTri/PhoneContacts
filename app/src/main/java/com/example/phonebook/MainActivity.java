package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.phonebook.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contacts;
    private ContactsAdapter contactsAdapter;

    private AppDatabase appDatabase;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        appDatabase = AppDatabase.getDb(this);
        contactDao = appDatabase.contactDao();

//        contacts = new ArrayList<>();
//        contacts.add(new Contact("Tran Nhu Tri", "0397405424", "trannhutri0703@gmail.com"));
//        contacts.add(new Contact("Huynh Thi Ai Linh", "0397405425", "huynhthiailinh2105@gmail.com"));
//        contacts.add(new Contact("Le Huynh Nhat Long", "0397405426", "lehuynhnhatlong@gmail.com"));

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                contacts = new ArrayList<>();

                if(contactDao.getAll().size() == 0) {
                    Contact contact = new Contact("Tran Nhu Tri", "0397405424", "trannhutri0703@gmail.com");
                    contactDao.insertAll(contact);
                }
                contacts.addAll(contactDao.getAll());
            }
        });

        contactsAdapter = new ContactsAdapter(contacts);
        binding.rvContacts.setAdapter(contactsAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });
    }
}