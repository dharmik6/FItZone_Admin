package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Review extends AppCompatActivity {
    AppCompatTextView review_name, review_specialization, all_review, review_text;
    ImageView review_img;
    ProgressDialog progressDialog;
    RatingBar xml2_rating_bar;
    RecyclerView recyc_review;
    private TrainerReviewAdapter adapter;
    private List<TrainerReviewList> trainersLists;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        review_name = findViewById(R.id.review_name);
        review_specialization = findViewById(R.id.review_specialization);
        review_text = findViewById(R.id.review_text);
        xml2_rating_bar = findViewById(R.id.xml2_rating_bar);
        recyc_review = findViewById(R.id.recyc_review);
        review_img = findViewById(R.id.review_img);
        all_review = findViewById(R.id.all_review);

        ImageView backPress = findViewById(R.id.bback);

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String treid = intent.getStringExtra("id");

        // Initialize RecyclerView
        recyc_review.setHasFixedSize(true);
        recyc_review.setLayoutManager(new LinearLayoutManager(this));

        // Initialize List for holding trainer reviews
        trainersLists = new ArrayList<>();
        adapter = new TrainerReviewAdapter(this, trainersLists);
        recyc_review.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for trainer details
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String specialization = documentSnapshot.getString("specialization");
                String image = documentSnapshot.getString("image");

                // Display trainer details
                review_name.setText(name != null ? name : "No name");
                review_specialization.setText(specialization != null ? specialization : "No specialization");
                if (image != null) {
                    Glide.with(Review.this)
                            .load(image)
                            .into(review_img);
                }

                // Query Firestore for trainer reviews
                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                db2.collection("trainers").document(treid).collection("trainers_review").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    float totalRating = 0;
                    int reviewCount = 0;

                    for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots) {
                        String rating = documentSnapshot2.getString("rating");

                        if (rating != null) {
                            totalRating += Float.parseFloat(rating);
                            reviewCount++;
                        } else {
                            // Handle null ratings by incrementing the review count and setting rating to 0
                            reviewCount++;
                        }

                        String rname = documentSnapshot2.getString("review_name");
                        String rimage = documentSnapshot2.getString("review_image");
                        TrainerReviewList member = new TrainerReviewList(rating, rname, rimage);
                        trainersLists.add(member);
                    }

                    if (reviewCount > 0) {
                        float averageRating = totalRating / reviewCount;
                        xml2_rating_bar.setRating(averageRating);
                        review_text.setText(String.valueOf(averageRating));
                        all_review.setText("All Reviews (" + reviewCount + ")");
                    } else {
                        // If there are no reviews, set default rating to 0
                        xml2_rating_bar.setRating(0);
                        review_text.setText("0");
                        all_review.setText("All Reviews (0)");
                    }

                    // Notify adapter about data changes and dismiss ProgressDialog
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }).addOnFailureListener(e -> {
                    // Handle failures
                    progressDialog.dismiss();
                });
            } else {
                // Handle document not found
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            // Handle failures
            progressDialog.dismiss();
        });
    }
}
