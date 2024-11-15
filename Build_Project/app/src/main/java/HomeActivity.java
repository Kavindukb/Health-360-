package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button bmiButton = findViewById(R.id.bmiButton);
        Button dietButton = findViewById(R.id.dietButton);
        Button exerciseButton = findViewById(R.id.exerciseButton);
        Button mentalHealthButton = findViewById(R.id.mentalHealthButton);
        Button feedbackButton = findViewById(R.id.feedbackButton);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        bmiButton.setOnClickListener(v -> startActivity(new Intent(this, BMICalculatorActivity.class))); // Navigate to BMI activity
        dietButton.setOnClickListener(v -> startActivity(new Intent(this, DietPlansActivity.class))); // Navigate to Diet Plans
        exerciseButton.setOnClickListener(v -> startActivity(new Intent(this, ExerciseTutorialsActivity.class))); // Navigate to Exercise Tutorials
        mentalHealthButton.setOnClickListener(v -> startActivity(new Intent(this, MentalHealthAdviceActivity.class))); // Navigate to Mental Health Advice
        feedbackButton.setOnClickListener(v -> startActivity(new Intent(this, FeedbackActivity.class))); // Navigate to Feedback
    }

    private void logout() {
        SessionManager.clearSession(this);
        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Prevent returning to admin home
    }
}
