package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText adminUsernameInput, adminPasswordInput;
    private Button adminLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminUsernameInput = findViewById(R.id.adminUsernameInput);
        adminPasswordInput = findViewById(R.id.adminPasswordInput);
        adminLoginButton = findViewById(R.id.adminLoginButton);

        adminLoginButton.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {
        String username = adminUsernameInput.getText().toString().trim();
        String password = adminPasswordInput.getText().toString().trim();

        // Hardcoded credentials
        if (username.equals("admin") && password.equals("admin")) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminHomeActivity.class);
            startActivity(intent);
            finish(); // Prevent returning to login
        } else {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
