package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

        signInButton.setOnClickListener(view -> validateCredentials());

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth


        forgetPasswordTextView.setOnClickListener(view -> {
            // Build an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminLogin.this);
            builder.setTitle("Reset Password");
            builder.setMessage("Enter your email to receive a password reset link");

            // Set an EditText for user input
            final EditText input = new EditText(AdminLogin.this);
            input.setHint("Enter Email");
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            builder.setView(input);

            // Set action buttons for the dialog
            builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String userEmail = input.getText().toString().trim();

                    if (TextUtils.isEmpty(userEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        Toast.makeText(AdminLogin.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Send password reset email
                    FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminLogin.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AdminLogin.this, "Failed to send reset email. Check your email address.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel(); // Dismiss the dialog if Cancel is clicked
                }
            });

            // Create and show the AlertDialog
            builder.create().show();
        });
    }

    private void validateCredentials() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        // start validation
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditText.setError("Email is compulsary");
        }
        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditText.setError("Password is compulsary");
        }else if (!isValidEmail(email)) {
            Toast.makeText(AdminLogin.this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
        }else {

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
                                                    if (task.isSuccessful()) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        Toast.makeText(AdminLogin.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AdminLogin.this, DashboardScreen.class));
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Toast.makeText(AdminLogin.this, "Sign in  failed: " + task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                        pd.dismiss();
                                                    }
                                                }
                                            });
                                } else {
                                    // Email doesn't exist in the database
                                    Toast.makeText(AdminLogin.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            } else {
                                // Error fetching data
                                Toast.makeText(AdminLogin.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });

            // You can add additional validation logic if needed

        }
    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //********************************************
}