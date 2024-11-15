package com.example.bmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DietPlansActivity extends AppCompatActivity {
    private EditText goalInput;
    private TextView dietPlanResult;
    private DatabaseHelper dbHelper;
    private int userId; // Will be set using SessionManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plans);

        goalInput = findViewById(R.id.goalInput);
        dietPlanResult = findViewById(R.id.dietPlanResult);
        Button generatePlanButton = findViewById(R.id.generatePlanButton);

        dbHelper = new DatabaseHelper(this);
        userId = getUserId(); // Retrieve the logged-in user's ID using SessionManager

        if (userId == -1) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to LoginActivity
            finish();
            return;
        }

        generatePlanButton.setOnClickListener(v -> generateDietPlan());
    }

    private void generateDietPlan() {
        // Retrieve the latest BMI from the database
        Cursor cursor = dbHelper.getBMHistory(userId); // Retrieves BMI records ordered DESC
        String dietPlan;

        if (cursor != null && cursor.moveToFirst()) {
            try {
                double bmi = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BMI));
                String goal = goalInput.getText().toString().trim();
                dietPlan = createDietPlan(bmi, goal);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                dietPlan = "Error retrieving BMI records.";
            }
        } else {
            dietPlan = "No BMI records found. Please calculate your BMI first.";
        }

        if (cursor != null) {
            cursor.close();
        }

        dietPlanResult.setText(dietPlan);
    }

    private String createDietPlan(double bmi, String goal) {
        // Simple logic to create diet plans based on BMI
        StringBuilder dietPlanBuilder = new StringBuilder();
        dietPlanBuilder.append("Diet Plan based on BMI: ").append(String.format("%.2f", bmi)).append("\n\n");

        if (bmi < 18.5) {
            dietPlanBuilder.append("⚖️ **Underweight**\n");
            dietPlanBuilder.append("• Increase calorie intake with healthy foods.\n");
            dietPlanBuilder.append("• Incorporate nuts, dried fruits, and whole grains.\n");
            dietPlanBuilder.append("• Consume protein-rich meals like lean meats and legumes.\n\n");
        } else if (bmi >= 18.5 && bmi < 24.9) {
            dietPlanBuilder.append("✅ **Healthy Weight**\n");
            dietPlanBuilder.append("• Maintain a balanced diet with a variety of nutrients.\n");
            dietPlanBuilder.append("• Include plenty of fruits and vegetables.\n");
            dietPlanBuilder.append("• Stay hydrated and monitor portion sizes.\n\n");
        } else if (bmi >= 25 && bmi < 29.9) {
            dietPlanBuilder.append("⚠️ **Overweight**\n");
            dietPlanBuilder.append("• Reduce intake of refined carbohydrates and sugars.\n");
            dietPlanBuilder.append("• Increase consumption of lean proteins and fiber.\n");
            dietPlanBuilder.append("• Incorporate healthy fats like avocados and olive oil.\n\n");
        } else {
            dietPlanBuilder.append("🚫 **Obese**\n");
            dietPlanBuilder.append("• Consult a nutritionist for a personalized diet plan.\n");
            dietPlanBuilder.append("• Focus on reducing calorie-dense foods.\n");
            dietPlanBuilder.append("• Incorporate regular physical activity.\n\n");
        }

        // Consider the user's dietary goal
        if (!goal.isEmpty()) {
            dietPlanBuilder.append("🎯 **Your Goal:** ").append(goal).append("\n");
            dietPlanBuilder.append("• Adjust your diet plan accordingly to achieve your goal.\n");
        } else {
            dietPlanBuilder.append("🎯 **Your Goal:** Not specified.\n");
            dietPlanBuilder.append("• Define your dietary goal to receive a more tailored plan.\n");
        }

        return dietPlanBuilder.toString();
    }

    private int getUserId() {
        // Retrieve user ID from SessionManager
        return SessionManager.getUserId(this);
    }
}
