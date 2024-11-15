package com.example.bmi;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BMIHistoryActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView bmiHistoryListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> bmiList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_history);

        databaseHelper = new DatabaseHelper(this);
        bmiHistoryListView = findViewById(R.id.bmiHistoryListView);
        bmiList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bmiList);
        bmiHistoryListView.setAdapter(adapter);

        userId = getUserId(); // Retrieve the current user's ID

        if (userId == -1) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        loadBMIHistory();
    }

    private void loadBMIHistory() {
        Cursor cursor = databaseHelper.getBMHistory(userId);
        if (cursor.moveToFirst()) {
            do {
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEIGHT));
                double height = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HEIGHT));
                double bmi = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BMI));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BMI_DATE));
                bmiList.add(String.format("BMI: %.2f (Weight: %.1f kg, Height: %.2f m) on %s", bmi, weight, height, date));
            } while (cursor.moveToNext());
        } else {
            bmiList.add("No BMI records found.");
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private int getUserId() {
        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1); // -1 indicates no user found
    }
}
