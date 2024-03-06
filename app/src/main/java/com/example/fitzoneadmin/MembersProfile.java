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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersProfile extends AppCompatActivity {
    MaterialTextView member_name,member_username;
    CircleImageView member_image;
    AppCompatButton user_update,user_delete;
    ImageView member_pro_back;
    AppCompatTextView member_joidate,member_activity,member_address,member_age,member_gender,member_email,member_number;

    // Declare Firestore instance and references
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_profile);

//        user_update = findViewById(R.id.user_update);
        user_delete = findViewById(R.id.user_delete);

        // Initialize your TextView elements
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

        member_pro_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String memberid = intent.getStringExtra("uid");
        String memberemailid = intent.getStringExtra("email");


//        String currentUser = memberid;

//        if (currentUser != null) {
//            String userId = currentUser;
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference docRef = db.collection("users").document(userId);
//        }

//            String username = "name"; // Replace "user_id" with the actual user ID

        // Query Firestore for data
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
                if (memberid.equals(membername)) {
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
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });


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
                                showToast("Authentication deleted successfully");
                                finish(); // Finish the activity after successful deletion
                            } else {
                                // Handle errors
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