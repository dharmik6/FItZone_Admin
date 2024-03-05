package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private TextView forgetPasswordTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailEditText = findViewById(R.id.id_email);
        passwordEditText = findViewById(R.id.id_pass);
        signInButton = findViewById(R.id.btn_singIn);
        forgetPasswordTextView = findViewById(R.id.id_forgot_pass);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        signInButton.setOnClickListener(view -> validateCredentials());

        forgetPasswordTextView.setOnClickListener(view -> resetPassword());
    }

    private void validateCredentials() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Start validation
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is compulsory");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is compulsory");
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(AdminLogin.this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        // Check if the email exists in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("admins");

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Email exists in the database, proceed with signing in the user
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                pd.dismiss(); // Dismiss the progress dialog
                                                if (task.isSuccessful()) {
                                                    // Credentials match, perform intent operation
                                                    setLoggedInFlag();
                                                    // Sign-in successful, navigate to the next page
                                                    Intent intent = new Intent(AdminLogin.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish(); // Close the login activity
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Toast.makeText(AdminLogin.this, "Sign in Successful!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(AdminLogin.this, "Please Enter the Correct Email id and Password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Email doesn't exist in the database
                                Toast.makeText(AdminLogin.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                                pd.dismiss(); // Dismiss the progress dialog
                            }
                        } else {
                            // Error fetching data
                            Toast.makeText(AdminLogin.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss(); // Dismiss the progress dialog
                        }
                    }
                });
    }

    private void resetPassword() {
        // Implementation of password reset functionality
        // ...
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setLoggedInFlag() {
        // Save the logged-in flag to SharedPreferences
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("flag", true);
        editor.apply();
    }
}
