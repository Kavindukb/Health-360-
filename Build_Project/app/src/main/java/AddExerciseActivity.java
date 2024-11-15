package com.example.bmi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExerciseActivity extends AppCompatActivity {
    private EditText exerciseNameInput, exerciseDescriptionInput, exerciseVideoUriInput;
    private Button saveExerciseButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        exerciseNameInput = findViewById(R.id.exerciseNameInput);
        exerciseDescriptionInput = findViewById(R.id.exerciseDescriptionInput);
        exerciseVideoUriInput = findViewById(R.id.exerciseVideoUriInput);
        saveExerciseButton = findViewById(R.id.saveExerciseButton);
        databaseHelper = new DatabaseHelper(this);

        saveExerciseButton.setOnClickListener(v -> saveExercise());
    }

    private void saveExercise() {
        String name = exerciseNameInput.getText().toString().trim();
        String description = exerciseDescriptionInput.getText().toString().trim();
        String videoUri = exerciseVideoUriInput.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter exercise name and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAdded = databaseHelper.addExercise(name, description, videoUri);
        if (isAdded) {
            Toast.makeText(this, "Exercise added successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Return to ManageExercisesActivity
        } else {
            Toast.makeText(this, "Failed to add exercise.", Toast.LENGTH_SHORT).show();
        }
    }
}
