package com.example.bmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewUsersActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView usersListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        databaseHelper = new DatabaseHelper(this);
        usersListView = findViewById(R.id.usersListView);
        usersList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        usersListView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        Cursor cursor = databaseHelper.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));

                String userInfo = String.format("ID: %d\nName: %s\nEmail: %s\nMobile: %s\n", id, name, email, mobile);
                usersList.add(userInfo);
            } while (cursor.moveToNext());
        } else {
            usersList.add("No users found.");
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
