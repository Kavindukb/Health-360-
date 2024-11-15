package com.example.bmi;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    public ExerciseAdapter(@NonNull Context context, @NonNull List<Exercise> exercises) {
        super(context, android.R.layout.simple_list_item_1, exercises);
    }

    // You can override getView if you want to customize how each item is displayed
    // For simplicity, we'll use the default implementation
}
