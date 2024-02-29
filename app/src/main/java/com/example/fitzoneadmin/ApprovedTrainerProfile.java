package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ApprovedTrainerProfile extends AppCompatActivity {
    AppCompatTextView approve_name,approve_specialization,approve_email,approve_number,approve_gender,approve_boi,approve_address,approve_date,approve_experience;
    ImageView approve_img;
    CardView approve_document;
    Button approve_approve,app_change;
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
        approve_approve = findViewById(R.id.approve_approve);
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
    }
}
