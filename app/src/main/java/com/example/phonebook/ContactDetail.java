package com.example.phonebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.phonebook.databinding.ActivityContactDetailBinding;

public class ContactDetail extends AppCompatActivity {
    private ActivityContactDetailBinding binding;
    private Contact data;
    private static final int REQ_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            return;
        }
        data = (Contact) bundle.get("contact");
        loadData(data);
    }

    public void loadData(Contact data) {
        binding.detailName.setText(data.getName());
        if(!data.getEmail().isEmpty() && data.getEmail() != null)
            binding.detailEmail.setText(data.getEmail());
        else
            binding.detailEmail.setText("Email");
        if(!data.getMobile().isEmpty() && data.getMobile() != null)
            binding.detailPhone.setText(data.getMobile());
        else
            binding.detailPhone.setText("Phone number");
        if(data.getAvatar() != null) {
            binding.detailAvatar.setImageBitmap(Utils.convertByteArray2Image(data.getAvatar()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.mark:
                return true;
            case R.id.edit:
                Intent intent = new Intent(ContactDetail.this, AddEditContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contact", data);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQ_CODE);
                return true;
            case R.id.delete:
                intent = new Intent();
                intent.putExtra("id", data.getId());
                setResult(REQ_CODE, intent);
                finish();
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE) {
            if(intent != null) {
                Bundle bundle = intent.getExtras();
                data = (Contact) bundle.get("contact");
                loadData(data);
            }
        }
    }
}