package com.example.phonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.phonebook.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contacts = new ArrayList<>();;
    private ContactsAdapter contactsAdapter;

    private AppDatabase appDatabase;
    private ContactDao contactDao;
    private static final int REQ_CODE = 100;

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
                for (Contact i: contactDao.getAll()) {
                    contactDao.delete(i);
                }

                if(contactDao.getAll() == null || contactDao.getAll().size() == 0) {
                    Contact contact = new Contact(null, "Nhu Tri", "Tran", "0397405424", "trannhutri0703@gmail.com");
                    contactDao.insertAll(contact);
                }

                contacts.addAll(contactDao.getAll());
            }
        });

        contactsAdapter = new ContactsAdapter(contacts, new ContactsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Contact item) {
                Intent intent = new Intent(MainActivity.this, ContactDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contact", item);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQ_CODE);
            }
        });
        binding.rvContacts.setAdapter(contactsAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_contact);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search contact");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        contacts.clear();
                        contacts.addAll(contactDao.findByName("%" + s + "%"));
                    }
                });
                contactsAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int id = intent != null ? intent.getIntExtra("id", 0) : 0;
                    if(id != 0) {
                        contactDao.delete(contactDao.loadAllById(id));
                    }
                    contacts.clear();
                    contacts.addAll(contactDao.getAll());
                }
            });
            contactsAdapter.notifyDataSetChanged();
        }
    }
}