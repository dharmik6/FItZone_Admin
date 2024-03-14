package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    RecyclerView feed_recyc;
    ProgressDialog progressDialog;
    private NotificationAdapter adapter;
    private List<NotificationItemList> dietLists;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        feed_recyc = findViewById(R.id.feed_recyc);
        feed_recyc.setHasFixedSize(true);
        feed_recyc.setLayoutManager(new LinearLayoutManager(this));

        dietLists = new ArrayList<>();
        adapter = new NotificationAdapter(this, dietLists);
        feed_recyc.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading diets...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dietLists.clear(); // Clear the previous list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("feedback").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String feedback_name = documentSnapshot.getString("feedback_name");
                String feedback = documentSnapshot.getString("feedback");
                NotificationItemList diet = new NotificationItemList(feedback_name,feedback);
                dietLists.add(diet);
            }
            adapter.notifyDataSetChanged(); // Notify adapter about data changes
            progressDialog.dismiss(); // Dismiss the progress dialog when data is loaded
        }).addOnFailureListener(e -> {
            // Handle failure
            progressDialog.dismiss(); // Dismiss the progress dialog on failure as well
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