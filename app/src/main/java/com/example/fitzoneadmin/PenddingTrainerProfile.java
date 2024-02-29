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

public class PenddingTrainerProfile extends AppCompatActivity {
    AppCompatTextView trainer_name,trainer_specialization,trainer_email,trainer_number,trainer_gender,trainer_boi,trainer_address,trainer_date,trainer_experience;
    ImageView trainer_img;
    CardView pendding_document;
    Button approve,reject,mouny_change;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendding_trainer_profile);

        // Initialize TextViews
        trainer_name = findViewById(R.id.trainer_name);
        trainer_specialization = findViewById(R.id.trainer_specialization);
        trainer_email = findViewById(R.id.trainer_email);
        trainer_number = findViewById(R.id.trainer_number);
        trainer_gender = findViewById(R.id.trainer_gender);
        trainer_boi = findViewById(R.id.trainer_boi);
        trainer_address = findViewById(R.id.trainer_address);
        trainer_date = findViewById(R.id.trainer_date);
        trainer_experience = findViewById(R.id.trainer_experience);

        // Initialize Image
        trainer_img = findViewById(R.id.trainer_img);

        // Initialize Button
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        mouny_change = findViewById(R.id.mouny_change);
        pendding_document = findViewById(R.id.pendding_document);

        Intent intent = getIntent();
        String memberid = intent.getStringExtra("name");

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String boi = documentSnapshot.getString("boi");
                String experience = documentSnapshot.getString("experience");
                String address = documentSnapshot.getString("address");
                String specialization = documentSnapshot.getString("specialization");
                String gender = documentSnapshot.getString("gender");
                String email = documentSnapshot.getString("email");
                String number = documentSnapshot.getString("number");
                String image = documentSnapshot.getString("image");

//                 Check if the userNameFromIntent matches the user
                if (memberid.equals(name)) {
                    // Display the data only if they match
                    trainer_name.setText(name != null ? name : "No name");
                    trainer_specialization.setText(specialization != null ? specialization : "No specialization");
                    trainer_email.setText(email != null ? email : "No email");
                    trainer_number.setText(number != null ? number : "No number");
                    trainer_gender.setText(gender != null ? gender : "No gender");
                    trainer_boi.setText(boi != null ? boi : "No boi");
                    trainer_address.setText(address != null ? address : "No address");
                    trainer_experience.setText(experience != null ? experience : "No experience");
                    if (image != null) {
                        Glide.with(PenddingTrainerProfile.this)
                                .load(image)
                                .into(trainer_img);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        pendding_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PenddingTrainerProfile.this,DocumentHome.class));
            }
        });

        // Initialize TextViews
        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}