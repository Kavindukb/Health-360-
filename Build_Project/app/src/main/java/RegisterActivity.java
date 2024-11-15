package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput, emailInput, mobileInput, passwordInput;
    private DatabaseHelper databaseHelper;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        mobileInput = findViewById(R.id.mobileInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        databaseHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String mobile = mobileInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isRegistered = databaseHelper.registerUser(name, email, mobile, password);
        if (isRegistered) {
            // Retrieve the user ID
            int userId = databaseHelper.loginUserAndGetId(email, password);
            if (userId != -1) {
                // Store user ID using SessionManager
                SessionManager.saveUserId(this, userId);

                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class)); // Navigate to home
                finish(); // Prevent returning to register
            } else {
                Toast.makeText(this, "Registration failed. Please try logging in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class)); // Navigate to login
                finish();
            }
        } else {
            Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
