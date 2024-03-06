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
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class BookingList extends AppCompatActivity {

    // Inside FragmentMember class
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<TrainersList> trainersLists;
    ProgressDialog progressDialog;
    MaterialSearchBar book_searchbar;
    List<TrainersList> filteredList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        // Initialize search bar
        book_searchbar = findViewById(R.id.book_searchbar);

        // Setup MaterialSearchBar
        book_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // Handle search state changes
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // Perform search
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                // Handle button clicks
            }
        });

        recyclerView = findViewById(R.id.booking_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use activity context
        filteredList = new ArrayList<>();

        trainersLists = new ArrayList<>();
        adapter = new BookingAdapter(this, trainersLists); // Use activity context
        recyclerView.setAdapter(adapter);

        // Show ProgressDialog with activity context
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String tname = documentSnapshot.getString("name");
                String experience = documentSnapshot.getString("experience");
                String timage = documentSnapshot.getString("image");
                String specialization = documentSnapshot.getString("specialization");
                String review = documentSnapshot.getString("review");
                TrainersList member = new TrainersList(tname, experience, timage, specialization, review);
                trainersLists.add(member);
            }
            filteredList.addAll(trainersLists); // Initialize filteredList with all members

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
    private void filter(String query) {
        List<TrainersList> filteredList = new ArrayList<>();
        for (TrainersList member : trainersLists) {
            if (member.getTname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }
}