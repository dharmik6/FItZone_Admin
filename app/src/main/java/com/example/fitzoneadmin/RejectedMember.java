package com.example.fitzoneadmin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class RejectedMember extends AppCompatActivity {

    CircleImageView rej_mem_image;
    AppCompatTextView rej_mem_email,rej_mem_number,rej_mem_gender,rej_mem_age,rej_mem_address,rej_mem_activity,rej_mem_joi;
    MaterialTextView rej_mem_name,rej_mem_active;
    ImageView back;
    Button rej_mem_approve;
    // Declare Firestore instance and references
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_member);

        rej_mem_approve = findViewById(R.id.rej_mem_approve);

        // Initialize your TextView elements
        rej_mem_name = findViewById(R.id.rej_mem_name);
        rej_mem_active = findViewById(R.id.rej_mem_active);
        rej_mem_email = findViewById(R.id.rej_mem_email);
        rej_mem_number = findViewById(R.id.rej_mem_number);
        rej_mem_gender = findViewById(R.id.rej_mem_gender);
        rej_mem_age = findViewById(R.id.rej_mem_age);
        rej_mem_address = findViewById(R.id.rej_mem_address);
        rej_mem_activity = findViewById(R.id.rej_mem_activity);
        rej_mem_joi = findViewById(R.id.rej_mem_joi);
        rej_mem_image = findViewById(R.id.rej_mem_image);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String memberid = intent.getStringExtra("name");

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
//                String memberusername = documentSnapshot.getString("username");
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
                    rej_mem_name.setText(membername != null ? membername : "No name");
//                    member_username.setText(memberusername != null ? memberusername : "No username");
                    rej_mem_activity.setText(memberactivity != null ? memberactivity : "No activity");
                    rej_mem_address.setText(memberaddress != null ? memberaddress : "No address");
                    rej_mem_age.setText(memberage != null ? memberage : "No age");
                    rej_mem_gender.setText(membergender != null ? membergender : "No gender");
                    rej_mem_email.setText(memberemail != null ? memberemail : "No email");
                    rej_mem_number.setText(membernumber != null ? membernumber : "No number");
                    if (memberimage != null) {
                        Glide.with(RejectedMember.this)
                                .load(memberimage)
                                .into(rej_mem_image);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

    }
}
