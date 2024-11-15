package com.example.bmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageExercisesActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView exercisesListView;
    private Button addExerciseButton;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> exercisesList;
    private ArrayList<Integer> exerciseIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercises);

        databaseHelper = new DatabaseHelper(this);
        exercisesListView = findViewById(R.id.exercisesListView);
        addExerciseButton = findViewById(R.id.addExerciseButton);
        exercisesList = new ArrayList<>();
        exerciseIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercisesList);
        exercisesListView.setAdapter(adapter);

        loadExercises();

        addExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExerciseActivity.class);
            startActivity(intent);
        });

        exercisesListView.setOnItemClickListener((parent, view, position, id) -> {
            // Show options to edit or delete
            int exerciseId = exerciseIds.get(position);
            showExerciseOptions(exerciseId, position);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExercises();
    }

    private void loadExercises() {
        exercisesList.clear();
        exerciseIds.clear();

        Cursor cursor = databaseHelper.getAllExercises();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("exercise_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                String exerciseInfo = String.format("Name: %s\nDescription: %s", name, description);
                exercisesList.add(exerciseInfo);
                exerciseIds.add(id);
            } while (cursor.moveToNext());
        } else {
            exercisesList.add("No exercises found.");
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void showExerciseOptions(int exerciseId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option")
                .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // Edit
                            Intent intent = new Intent(this, EditExerciseActivity.class);
                            intent.putExtra("exercise_id", exerciseId);
                            startActivity(intent);
                            break;
                        case 1:
                            // Delete
                            confirmDeleteExercise(exerciseId, position);
                            break;
                    }
                })
                .show();
    }

    private void confirmDeleteExercise(int exerciseId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Exercise")
                .setMessage("Are you sure you want to delete this exercise?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean isDeleted = databaseHelper.deleteExercise(exerciseId);
                    if (isDeleted) {
                        Toast.makeText(this, "Exercise deleted.", Toast.LENGTH_SHORT).show();
                        loadExercises();
                    } else {
                        Toast.makeText(this, "Failed to delete exercise.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
