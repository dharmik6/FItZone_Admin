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
    private RecyclerView bookingRecyclerView;
    private BookingAdapter adapter;
    private List<BookingItemList> bookingLists;
    private ProgressDialog progressDialog; // Declare ProgressDialog

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        bookingRecyclerView = findViewById(R.id.booking_rec);
        bookingRecyclerView.setHasFixedSize(true);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingLists = new ArrayList<>();
        adapter = new BookingAdapter(this, bookingLists);
        bookingRecyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this); // Initialize ProgressDialog
        progressDialog.setMessage("Loading..."); // Set message for ProgressDialog
        progressDialog.setCancelable(false); // Make ProgressDialog non-cancelable
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDietData();
    }

    private void loadDietData() {
        progressDialog.show(); // Show ProgressDialog before fetching data
        bookingLists.clear(); // Clear the previous list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String userId = documentSnapshot.getString("userId");
                        String status = documentSnapshot.getString("paymentStatus");
                        String id = documentSnapshot.getId();
                        // Fetch user details using userId
                        db.collection("users")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(userDocumentSnapshot -> {
                                    // Retrieve user details
                                    String name = userDocumentSnapshot.getString("name");
                                    String email = userDocumentSnapshot.getString("email");
                                    // Create a BookingItemList object with user details
                                    BookingItemList bookingList = new BookingItemList(name, email, status ,id);
                                    bookingLists.add(bookingList);
                                    adapter.notifyDataSetChanged(); // Notify adapter about data changes
                                    progressDialog.dismiss(); // Dismiss ProgressDialog after fetching data
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to fetch user details
                                    progressDialog.dismiss(); // Dismiss ProgressDialog on failure
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    progressDialog.dismiss(); // Dismiss ProgressDialog on failure
                });
    }
}
