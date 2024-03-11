package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersProfile extends AppCompatActivity {
    MaterialTextView member_name, member_username;
    CircleImageView member_image;
    AppCompatButton user_delete;
    ImageView member_pro_back;
    AppCompatTextView member_joidate, member_activity, member_address, member_age, member_gender, member_email, member_number;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_profile);

        // Initialize TextView elements
        member_name = findViewById(R.id.member_name);
        member_username = findViewById(R.id.member_username);
        member_joidate = findViewById(R.id.member_joidate);
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
        String memberid = intent.getStringExtra("name");
        String memberemailid = intent.getStringExtra("email");

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(memberid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Document exists, retrieve data
                String membername = documentSnapshot.getString("name");
                String memberusername = documentSnapshot.getString("username");
                String memberactivity = documentSnapshot.getString("activity");
                String memberaddress = documentSnapshot.getString("address");
                String memberage = documentSnapshot.getString("age");
                String membergender = documentSnapshot.getString("gender");
                String memberemail = documentSnapshot.getString("email");
                String membernumber = documentSnapshot.getString("number");
                String memberimage = documentSnapshot.getString("image");

                // Display the data
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
                progressDialog.dismiss(); // Dismiss progress dialog after data retrieval
            } else {
                // Document does not exist
                progressDialog.dismiss(); // Dismiss progress dialog
                showToast("No such member found");
            }
        }).addOnFailureListener(e -> {
            // Handle errors
            progressDialog.dismiss();
            showToast("Error retrieving user data: " + e.getMessage());
        });

        // Set onClickListener for delete button
        user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete user document from Firestore
                db.collection("users").document(memberid)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // DocumentSnapshot successfully deleted
                                showToast("User data deleted successfully");
                                // After deleting user data, also delete authentication
                                deleteFirebaseAuthentication(memberemailid);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors
                                progressDialog.dismiss();
                                showToast("Error deleting user data: " + e.getMessage());
                            }
                        });
            }
        });
    }

    private void deleteFirebaseAuthentication(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Authentication deleted successfully
                                progressDialog.dismiss();
                                showToast("Authentication deleted successfully");
                                finish(); // Finish the activity after successful deletion
                            } else {
                                // Handle errors
                                progressDialog.dismiss();
                                showToast("Error deleting authentication: " + task.getException().getMessage());
                            }
                        }
                    });
        } else {
            showToast("No user authenticated");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
