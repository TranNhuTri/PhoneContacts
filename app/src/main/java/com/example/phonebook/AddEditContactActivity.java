package com.example.phonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.phonebook.databinding.ActivityAddEditContactBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddEditContactActivity extends AppCompatActivity {
    private ActivityAddEditContactBinding binding;
    private AppDatabase appDatabase;
    private ContactDao contactDao;
    private boolean isEdit = false;
    private Contact data;
    private Bitmap  avatar = null;
    private static final int MAIN_REQ_CODE = 100, DETAIL_REQ_CODE = 200, GALLERY_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditContactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add new contact");
        actionBar.setDisplayHomeAsUpEnabled(true);

        appDatabase = AppDatabase.getDb(this);
        contactDao = appDatabase.contactDao();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            data = (Contact) bundle.get("contact");
            loadData(data);
            actionBar.setTitle("Edit contact");
            isEdit = true;
        }

        binding.avatarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void loadData(Contact data) {
        binding.firstName.setText(data.getFirst_name());
        binding.lastName.setText(data.getLast_name());
        binding.email.setText(data.getEmail());
        binding.phone.setText(data.getMobile());
        if(data.getAvatar() != null) {
            binding.contactAvatar.setImageBitmap(Utils.convertByteArray2Image(data.getAvatar()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                String firstName = binding.firstName.getText().toString().trim();
                String lastName = binding.lastName.getText().toString().trim();
                String phone = binding.phone.getText().toString();
                String email = binding.email.getText().toString();
                if((lastName + firstName).equals("")) {
                    Toast.makeText(this, "Name should not be empty", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Contact newContact = new Contact(avatar == null ? null : Utils.convertImage2ByteArray(avatar), firstName, lastName, phone, email);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(isEdit){
                            newContact.setId(data.getId());
                            contactDao.update(newContact);
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("contact", newContact);
                            intent.putExtras(bundle);
                            setResult(DETAIL_REQ_CODE, intent);
                            finish();
                            return;
                        }
                        contactDao.insertAll(newContact);
                        Intent intent = new Intent();
                        setResult(MAIN_REQ_CODE, intent);
                        finish();
                    }
                });
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.contactAvatar.setImageBitmap(bitmap);
                    avatar = bitmap;
                }
                catch (IOException e) {};
            }
        } else {
            Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}