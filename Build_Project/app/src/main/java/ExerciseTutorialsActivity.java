package com.example.bmi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExerciseTutorialsActivity extends AppCompatActivity {
    private EditText searchInput;
    private ListView exerciseListView;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exercises;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tutorials);

        searchInput = findViewById(R.id.searchInput);
        exerciseListView = findViewById(R.id.exerciseList);
        dbHelper = new DatabaseHelper(this);
        exercises = new ArrayList<>();

        // Retrieve the logged-in user's ID using SessionManager
        userId = SessionManager.getUserId(this);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load exercises from the database
        loadExercises();

        // Initialize the custom adapter with the exercises list
        exerciseAdapter = new ExerciseAdapter(this, exercises);
        exerciseListView.setAdapter(exerciseAdapter);

        // Implement search functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ExerciseTutorialsActivity.this.exerciseAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });

        // Handle item clicks to navigate to ExerciseDetailActivity
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Exercise selectedExercise = exerciseAdapter.getItem(position);
                if (selectedExercise != null) {
                    Intent intent = new Intent(ExerciseTutorialsActivity.this, ExerciseDetailActivity.class);
                    intent.putExtra("exerciseName", selectedExercise.getName());
                    intent.putExtra("exerciseVideoUrl", selectedExercise.getVideoUrl());
                    startActivity(intent);
                }
            }
        });
    }

    private void loadExercises() {
        // Retrieve the exercises from the database
        Cursor cursor = dbHelper.getAllExercises(); // New method to fetch all exercises
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXERCISE_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXERCISE_DESCRIPTION));
                String videoUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXERCISE_VIDEO_URI));
                exercises.add(new Exercise(name, videoUri));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No exercises found. Please add exercises first.", Toast.LENGTH_SHORT).show();
        }
    }
}
