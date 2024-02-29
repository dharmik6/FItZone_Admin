package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Review extends AppCompatActivity {
    AppCompatTextView review_name,review_specialization,review_text;
    ImageView review_img;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        review_name = findViewById(R.id.review_name);
        review_specialization = findViewById(R.id.review_specialization);
        review_text = findViewById(R.id.review_text);

        review_img = findViewById(R.id.review_img);


        Intent intent = getIntent();
        String memberid = intent.getStringExtra("name");

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String specialization = documentSnapshot.getString("specialization");
                String review = documentSnapshot.getString("review");
                String image = documentSnapshot.getString("image");

//                 Check if the userNameFromIntent matches the user
                if (memberid.equals(name)) {
                    // Display the data only if they match
                    review_name.setText(name != null ? name : "No name");
                    review_specialization.setText(specialization != null ? specialization : "No specialization");
                    review_text.setText(review != null ? review : "No email");
                    if (image != null) {
                        Glide.with(Review.this)
                                .load(image)
                                .into(review_img);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });
        ImageView backPress = findViewById(R.id.bback);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}