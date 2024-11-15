package com.example.bmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewFeedbacksActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView feedbacksListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> feedbacksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedbacks);

        databaseHelper = new DatabaseHelper(this);
        feedbacksListView = findViewById(R.id.feedbacksListView);
        feedbacksList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedbacksList);
        feedbacksListView.setAdapter(adapter);

        loadFeedbacks();
    }

    private void loadFeedbacks() {
        Cursor cursor = databaseHelper.getAllFeedbacks();
        if (cursor.moveToFirst()) {
            do {
                int feedbackId = cursor.getInt(cursor.getColumnIndexOrThrow("feedback_id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String feedback = cursor.getString(cursor.getColumnIndexOrThrow("feedback"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                String feedbackInfo = String.format("Feedback ID: %d\nUser ID: %d\nFeedback: %s\nDate: %s\n",
                        feedbackId, userId, feedback, date);
                feedbacksList.add(feedbackInfo);
            } while (cursor.moveToNext());
        } else {
            feedbacksList.add("No feedbacks found.");
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
