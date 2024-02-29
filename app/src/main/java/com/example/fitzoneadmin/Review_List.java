package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Review_List extends AppCompatActivity {

    // Inside FragmentMember class
    private RecyclerView review_rec;
    private ReviewAdapter adapter;
    private List<TrainersList> trainersLists;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        review_rec = findViewById(R.id.review_rec);
        review_rec.setHasFixedSize(true);
        review_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        trainersLists = new ArrayList<>();
//        originalMemberList = new ArrayList<>();
        adapter = new ReviewAdapter(getApplicationContext(),trainersLists);
        review_rec.setAdapter(adapter);

        // Show ProgressDialog if activity is not finishing
        if (!isFinishing()) {
            progressDialog = new ProgressDialog(Review_List.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String tname = documentSnapshot.getString("name");
                String experience = documentSnapshot.getString("experience");
                String timage = documentSnapshot.getString("image");
                String specialization = documentSnapshot.getString("specialization");
                String review = documentSnapshot.getString("review");
//                memberList.add(new MemberList(name, email,image));
                TrainersList member = new TrainersList(tname, experience,timage,specialization,review);
                trainersLists.add(member);
//                originalMemberList.add(member); // Add to both lists
            }

            adapter.notifyDataSetChanged();
            // Dismiss ProgressDialog when data is loaded
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            // Handle failures

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
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