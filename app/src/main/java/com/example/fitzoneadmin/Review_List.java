package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class Review_List extends AppCompatActivity {

    private RecyclerView review_rec;
    private ReviewAdapter adapter;
    private List<TrainersList> trainersLists;
    private TextView dataNotFoundText;
    private MaterialSearchBar review_searchbar;
    private ProgressDialog progressDialog;
    private List<TrainersList> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        dataNotFoundText = findViewById(R.id.data_not_show);
        review_searchbar = findViewById(R.id.review_searchbar);
        review_rec = findViewById(R.id.review_rec);
        review_rec.setHasFixedSize(true);
        review_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        filteredList = new ArrayList<>();
        trainersLists = new ArrayList<>();
        adapter = new ReviewAdapter(getApplicationContext(), trainersLists);
        review_rec.setAdapter(adapter);

        progressDialog = new ProgressDialog(Review_List.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            trainersLists.clear(); // Clear the list before adding new data
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String tname = documentSnapshot.getString("name");
                String experience = documentSnapshot.getString("experience");
                String timage = documentSnapshot.getString("image");
                String specialization = documentSnapshot.getString("specialization");
                String id = documentSnapshot.getId();
                TrainersList member = new TrainersList(tname, experience, timage, specialization, id);
                trainersLists.add(member);
            }
            filteredList.addAll(trainersLists); // Initialize filteredList with all members
            adapter.notifyDataSetChanged();
            updateDataNotFoundVisibility();
            progressDialog.dismiss();
        }).addOnFailureListener(e -> {
            // Handle failures
            progressDialog.dismiss();
        });

        review_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void filter(String query) {
        filteredList.clear();
        for (TrainersList member : trainersLists) {
            if (member.getTname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }

    private void updateDataNotFoundVisibility() {
        if (trainersLists != null && trainersLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
