package com.example.phonebook;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE id == :contactId")
    Contact loadAllById(int contactId);

    @Query("SELECT * FROM contact WHERE  last_name || ' ' || first_name LIKE  :name")
    List<Contact> findByName(String name);

    @Insert
    void insertAll(Contact... contacts);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
