package com.example.phonebook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.phonebook.databinding.ActivityAddContactBinding;
import com.example.phonebook.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    private ActivityAddContactBinding binding;
    private AppDatabase appDatabase;
    private ContactDao contactDao;
    private static final int REQ_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add new contact");
        actionBar.setDisplayHomeAsUpEnabled(true);

        appDatabase = AppDatabase.getDb(this);
        contactDao = appDatabase.contactDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                String firstName = binding.firstName.getText().toString();
                String lastName = binding.lastName.getText().toString();
                String phone = binding.phone.getText().toString();
                String email = binding.email.getText().toString();
                Contact newContact = new Contact(lastName + firstName, phone, email);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        contactDao.insertAll(newContact);
                        Intent intent = new Intent();
                        setResult(REQ_CODE, intent);
                        finish();
                    }
                });
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}