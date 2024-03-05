package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RejectTrainerProfile extends AppCompatActivity {
    ImageView reject_img;
    AppCompatTextView reject_name,reject_specialization,reject_email,reject_number,reject_gender,reject_boi,reject_address,reject_date,reject_experience;
    CardView reject_document;
    Button reject_button,reject_change;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_trainer_profile);

        // Initialize TextViews
        reject_name = findViewById(R.id.reject_name);
        reject_specialization = findViewById(R.id.reject_specialization);
        reject_email = findViewById(R.id.reject_email);
        reject_number = findViewById(R.id.reject_number);
        reject_gender = findViewById(R.id.reject_gender);
        reject_boi = findViewById(R.id.reject_boi);
        reject_address = findViewById(R.id.reject_address);
        reject_date = findViewById(R.id.reject_date);
        reject_experience = findViewById(R.id.reject_experience);

        // Initialize Image
        reject_img = findViewById(R.id.reject_img);

        // Initialize Button
//        approve_approve = findViewById(R.id.approve_approve);
        reject_change = findViewById(R.id.reject_change);
        reject_document = findViewById(R.id.reject_document);

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
                    reject_name.setText(namee != null ? namee : "No name");
                    reject_specialization.setText(specializatione != null ? specializatione : "No specialization");
                    reject_email.setText(emaile != null ? emaile : "No email");
                    reject_number.setText(numbere != null ? numbere : "No number");
                    reject_gender.setText(gendere != null ? gendere : "No gender");
                    reject_boi.setText(boie != null ? boie : "No boi");
                    reject_address.setText(addresse != null ? addresse : "No address");
                    reject_experience.setText(experiencee != null ? experiencee : "No experience");
                    if (imagee != null) {
                        Glide.with(RejectTrainerProfile.this)
                                .load(imagee)
                                .into(reject_img);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        reject_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent doc=new Intent(RejectTrainerProfile.this,DocumentHome.class);
                 startActivity(doc);
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