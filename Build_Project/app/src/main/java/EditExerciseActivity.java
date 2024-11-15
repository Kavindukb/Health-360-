package com.example.bmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditExerciseActivity extends AppCompatActivity {
    private EditText exerciseNameInput, exerciseDescriptionInput, exerciseVideoUriInput;
    private Button updateExerciseButton;
    private DatabaseHelper databaseHelper;
    private int exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);

        exerciseNameInput = findViewById(R.id.editExerciseNameInput);
        exerciseDescriptionInput = findViewById(R.id.editExerciseDescriptionInput);
        exerciseVideoUriInput = findViewById(R.id.editExerciseVideoUriInput);
        updateExerciseButton = findViewById(R.id.updateExerciseButton);
        databaseHelper = new DatabaseHelper(this);

        // Get exercise_id from intent
        exerciseId = getIntent().getIntExtra("exercise_id", -1);
        if (exerciseId == -1) {
            Toast.makeText(this, "Invalid exercise ID.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadExerciseDetails();

        updateExerciseButton.setOnClickListener(v -> updateExercise());
    }

    private void loadExerciseDetails() {
        Cursor cursor = databaseHelper.getExerciseById(exerciseId);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String videoUri = cursor.getString(cursor.getColumnIndexOrThrow("video_uri"));

            exerciseNameInput.setText(name);
            exerciseDescriptionInput.setText(description);
            exerciseVideoUriInput.setText(videoUri);
        } else {
            Toast.makeText(this, "Exercise not found.", Toast.LENGTH_SHORT).show();
            cursor.close();
            finish();
        }
        cursor.close();
    }

    private void updateExercise() {
        String name = exerciseNameInput.getText().toString().trim();
        String description = exerciseDescriptionInput.getText().toString().trim();
        String videoUri = exerciseVideoUriInput.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter exercise name and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = databaseHelper.updateExercise(exerciseId, name, description, videoUri);
        if (isUpdated) {
            Toast.makeText(this, "Exercise updated successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Return to ManageExercisesActivity
        } else {
            Toast.makeText(this, "Failed to update exercise.", Toast.LENGTH_SHORT).show();
        }
    }
}
