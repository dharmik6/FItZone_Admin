package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private TextView forgetPasswordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_pass);
        signInButton = findViewById(R.id.btn_singIn);
        forgetPasswordTextView = findViewById(R.id.tv_forgot_pass);

        signInButton.setOnClickListener(view -> validateCredentials());

        forgetPasswordTextView.setOnClickListener(view -> {
            // Handle forget password logic here
            Toast.makeText(AdminLogin.this, "Forget Password Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void validateCredentials() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.contains("@")) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        // You can add additional validation logic if needed

        // If both email and password are valid, you can proceed with login
        performLogin(email, password);
    }

    private void performLogin(String email, String password) {
        // Add your login logic here
        // For example, you can make a network request to authenticate the user

        // For demonstration purposes, let's assume the login is successful
        // and navigate to the HomeActivity
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AdminLogin.this, DashboardScreen.class);
        startActivity(intent);
        finish();  // Optional: Close the current activity to prevent going back

    }
}