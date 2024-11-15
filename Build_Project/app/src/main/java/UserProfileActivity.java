package com.example.bmi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usernameInput = findViewById(R.id.usernameInput);
        Button saveProfileButton = findViewById(R.id.saveProfileButton);

        saveProfileButton.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String username = usernameInput.getText().toString();
        // Implement logic to save user profile data to SQLite
    }
}
