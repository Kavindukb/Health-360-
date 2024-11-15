package com.example.bmi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {
    private EditText feedbackInput;
    private Button submitFeedbackButton;
    private DatabaseHelper databaseHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackInput = findViewById(R.id.feedbackInput);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
        databaseHelper = new DatabaseHelper(this);

        userId = SessionManager.getUserId(this); // Retrieve user_id from SessionManager

        submitFeedbackButton.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        String feedback = feedbackInput.getText().toString().trim();
        if (!feedback.isEmpty()) {
            boolean isSaved = databaseHelper.saveFeedback(userId, feedback);
            if (isSaved) {
                Toast.makeText(this, "Feedback submitted. Thank you!", Toast.LENGTH_SHORT).show();
                feedbackInput.setText(""); // Clear the input field
            } else {
                Toast.makeText(this, "Failed to submit feedback. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter feedback.", Toast.LENGTH_SHORT).show();
        }
    }
}
