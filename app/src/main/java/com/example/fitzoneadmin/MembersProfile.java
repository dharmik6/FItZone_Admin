package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersProfile extends AppCompatActivity {
    MaterialTextView member_name, member_username;
    CircleImageView member_image;
    AppCompatButton user_delete;
    ImageView member_pro_back;
    AppCompatTextView  member_activity, member_address, member_age, member_gender, member_email, member_number;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_profile);

        // Initialize TextView elements
        member_name = findViewById(R.id.member_name);
        member_username = findViewById(R.id.member_username);
        member_activity = findViewById(R.id.member_activity);
        member_address = findViewById(R.id.member_address);
        member_age = findViewById(R.id.member_age);
        member_gender = findViewById(R.id.member_gender);
        member_email = findViewById(R.id.member_email);
        member_number = findViewById(R.id.member_number);
        member_image = findViewById(R.id.member_image);
        member_pro_back = findViewById(R.id.member_pro_back);

        // Initialize delete button
        user_delete = findViewById(R.id.user_delete);

        // Set onClickListener for back button
        member_pro_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get member ID and email from intent
        Intent intent = getIntent();
        String memberName = intent.getStringExtra("name");
        String memberEmail = intent.getStringExtra("email");

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String membername = documentSnapshot.getString("name");
                String memberusername = documentSnapshot.getString("username");
                String memberactivity = documentSnapshot.getString("activity");
                String memberaddress = documentSnapshot.getString("address");
                String memberage = documentSnapshot.getString("age");
                String membergender = documentSnapshot.getString("gender");
                String memberemail = documentSnapshot.getString("email");
                String membernumber = documentSnapshot.getString("number");
                String memberimage = documentSnapshot.getString("image");


                //                 Check if the userNameFromIntent matches the user
                if (memberName.equals(membername)) {
                    // Display the data only if they match
                    member_name.setText(membername != null ? membername : "No name");
                    member_username.setText(memberusername != null ? memberusername : "No username");
                    member_activity.setText(memberactivity != null ? memberactivity : "No activity");
                    member_address.setText(memberaddress != null ? memberaddress : "No address");
                    member_age.setText(memberage != null ? memberage : "No age");
                    member_gender.setText(membergender != null ? membergender : "No gender");
                    member_email.setText(memberemail != null ? memberemail : "No email");
                    member_number.setText(membernumber != null ? membernumber : "No number");
                    if (memberimage != null) {
                        Glide.with(MembersProfile.this)
                                .load(memberimage)
                                .into(member_image);
                    }
                    progressDialog.dismiss();
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
                    // showToast("User data does not match the intent.");
                }
            }
        });

//
        // Set onClickListener for delete button
        // Set onClickListener for delete button
        user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(MembersProfile.this);
                builder.setTitle("Delete Confirmation");
                builder.setMessage("Are you sure you want to delete this member?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Show progress dialog
                        progressDialog.show();

                        // Query Firestore to find the document ID associated with the member's email
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users")
                                .whereEqualTo("email", member_email.getText().toString())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String documentId = documentSnapshot.getId();
                                        String password = documentSnapshot.getString("password");
                                        // Delete user document from Firestore
                                        db.collection("users").document(documentId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // After deleting member data, also delete authentication
                                                        deleteFirebaseAuthentication(member_email.getText().toString(), password);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Dismiss progress dialog
                                                        progressDialog.dismiss();

                                                        // Handle errors
                                                        Toast.makeText(MembersProfile.this, "Error deleting member data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Dismiss progress dialog
                                        progressDialog.dismiss();

                                        Toast.makeText(MembersProfile.this, "Error fetching document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if "No" is clicked
                        dialog.dismiss();
                    }
                });
                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
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
                                                    Toast.makeText(MembersProfile.this, "Authentication deleted successfully", Toast.LENGTH_SHORT).show();
                                                    finish(); // Finish the activity after successful deletion
                                                } else {
                                                    // Handle errors
                                                    Toast.makeText(MembersProfile.this, "Error deleting authentication: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Dismiss progress dialog
                                progressDialog.dismiss();

                                // Handle the case where no user is authenticated
                                Toast.makeText(MembersProfile.this, "No user authenticated", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Dismiss progress dialog
                            progressDialog.dismiss();

                            // Handle authentication failure
                            Toast.makeText(MembersProfile.this, "Authentication failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
