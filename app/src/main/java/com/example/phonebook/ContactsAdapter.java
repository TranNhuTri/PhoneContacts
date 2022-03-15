package com.example.phonebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private ArrayList<Contact> contacts;

    public ContactsAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView, icon;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            icon = (TextView) view.findViewById(R.id.name_icon);
            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getIcon() { return icon; }
    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(contacts.get(position).getName());
        holder.getIcon().setText(contacts.get(position).getName().charAt(0) + "");
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
