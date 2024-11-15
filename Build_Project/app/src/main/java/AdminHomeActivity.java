package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {
    private Button viewUsersButton, manageExercisesButton, viewFeedbacksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        viewUsersButton = findViewById(R.id.viewUsersButton);
        manageExercisesButton = findViewById(R.id.manageExercisesButton);
        viewFeedbacksButton = findViewById(R.id.viewFeedbacksButton);

        viewUsersButton.setOnClickListener(v -> startActivity(new Intent(this, ViewUsersActivity.class)));
        manageExercisesButton.setOnClickListener(v -> startActivity(new Intent(this, ManageExercisesActivity.class)));
        viewFeedbacksButton.setOnClickListener(v -> startActivity(new Intent(this, ViewFeedbacksActivity.class)));
    }
}
