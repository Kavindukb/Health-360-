package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BMICalculatorActivity extends AppCompatActivity {
    private EditText weightInput, heightInput;
    private TextView bmiResult;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        bmiResult = findViewById(R.id.bmiResult);
        Button calculateButton = findViewById(R.id.calculateButton);
        Button viewHistoryButton = findViewById(R.id.viewHistoryButton);
        databaseHelper = new DatabaseHelper(this);

        calculateButton.setOnClickListener(v -> calculateBMI());
        viewHistoryButton.setOnClickListener(v -> viewBMIHistory());
    }

    private void calculateBMI() {
        double weight = Double.parseDouble(weightInput.getText().toString());
        double height = Double.parseDouble(heightInput.getText().toString());
        double bmi = weight / (height * height);
        bmiResult.setText(String.format("Your BMI: %.2f", bmi));

        // Get user ID (implement your own method to get the current user's ID)
        int userId = getUserId(); // Replace this with your actual method to get the user ID

        // Save the result to SQLite
        saveBMIDatabase(userId, weight, height, bmi);
    }

    private int getUserId() {
        // Implement your logic to retrieve the current user's ID
        return 1; // Replace this with actual logic
    }


    private void saveBMIDatabase(int userId,double weight, double height, double bmi) {
        databaseHelper.saveBMIDatabase(userId, weight, height, bmi);
    }

    private void viewBMIHistory() {
        Intent intent = new Intent(this, BMIHistoryActivity.class);
        startActivity(intent);
    }
}
