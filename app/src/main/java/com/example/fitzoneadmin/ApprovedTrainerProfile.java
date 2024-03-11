package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ApprovedTrainerProfile extends AppCompatActivity {
    AppCompatTextView approve_name,approve_specialization,approve_email,approve_number,approve_gender,approve_boi,approve_address,approve_date,approve_experience;
    ImageView approve_img;
    CardView approve_document;
    Button reject,app_change;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_trainer_profile);

        // Initialize TextViews
        approve_name = findViewById(R.id.approve_name);
        approve_specialization = findViewById(R.id.approve_specialization);
        approve_email = findViewById(R.id.approve_email);
        approve_number = findViewById(R.id.approve_number);
        approve_gender = findViewById(R.id.approve_gender);
        approve_boi = findViewById(R.id.approve_boi);
        approve_address = findViewById(R.id.approve_address);
        approve_date = findViewById(R.id.approve_date);
        approve_experience = findViewById(R.id.approve_experience);

        // Initialize Image
        approve_img = findViewById(R.id.approve_img);

        // Initialize Button
        reject = findViewById(R.id.reject);
        app_change = findViewById(R.id.app_change);
        approve_document = findViewById(R.id.approve_document);

        Intent intent = getIntent();
        String memberid = intent.getStringExtra("name");

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String namee = documentSnapshot.getString("name");
                String boie = documentSnapshot.getString("boi");
                String experiencee = documentSnapshot.getString("experience");
                String addresse = documentSnapshot.getString("address");
                String specializatione = documentSnapshot.getString("specialization");
                String gendere = documentSnapshot.getString("gender");
                String emaile = documentSnapshot.getString("email");
                String numbere = documentSnapshot.getString("number");
                String imagee = documentSnapshot.getString("image");

//                 Check if the userNameFromIntent matches the user
                if (memberid.equals(namee)) {
                    // Display the data only if they match
                    approve_name.setText(namee != null ? namee : "No name");
                    approve_specialization.setText(specializatione != null ? specializatione : "No specialization");
                    approve_email.setText(emaile != null ? emaile : "No email");
                    approve_number.setText(numbere != null ? numbere : "No number");
                    approve_gender.setText(gendere != null ? gendere : "No gender");
                    approve_boi.setText(boie != null ? boie : "No boi");
                    approve_address.setText(addresse != null ? addresse : "No address");
                    approve_experience.setText(experiencee != null ? experiencee : "No experience");
                    if (imagee != null) {
                        Glide.with(ApprovedTrainerProfile.this)
                                .load(imagee)
                                .into(approve_img);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        approve_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApprovedTrainerProfile.this,DocumentHome.class));
            }
        });

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting authentication...");

        // Other initialization code

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress dialog
                progressDialog.show();

                // Query Firestore to find the document ID associated with the trainer's email
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("trainers")
                        .whereEqualTo("email", approve_email.getText().toString())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String documentId = documentSnapshot.getId();
                                String password = documentSnapshot.getString("password");
                                // Delete user document from Firestore
                                db.collection("trainers").document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // After deleting trainer data, also delete authentication
                                                deleteFirebaseAuthentication(approve_email.getText().toString(), password);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Dismiss progress dialog
                                                progressDialog.dismiss();

                                                // Handle errors
                                                Toast.makeText(ApprovedTrainerProfile.this, "Error deleting trainer data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Dismiss progress dialog
                                progressDialog.dismiss();

                                Toast.makeText(ApprovedTrainerProfile.this, "Error fetching document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // Method to delete Firebase authentication
    private void deleteFirebaseAuthentication(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Authentication successful, proceed to delete the user
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // Dismiss progress dialog
                                                progressDialog.dismiss();

                                                if (task.isSuccessful()) {
                                                    // Authentication deleted successfully
                                                    Toast.makeText(ApprovedTrainerProfile.this, "Authentication deleted successfully", Toast.LENGTH_SHORT).show();
                                                    finish(); // Finish the activity after successful deletion
                                                } else {
                                                    // Handle errors
                                                    Toast.makeText(ApprovedTrainerProfile.this, "Error deleting authentication: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Dismiss progress dialog
                                progressDialog.dismiss();

                                // Handle the case where no user is authenticated
                                Toast.makeText(ApprovedTrainerProfile.this, "No user authenticated", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Dismiss progress dialog
                            progressDialog.dismiss();

                            // Handle authentication failure
                            Toast.makeText(ApprovedTrainerProfile.this, "Authentication failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
