package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingList extends AppCompatActivity {

    private RecyclerView bookingRecyclerView;
    private BookingAdapter adapter;
    private List<BookingItemList> bookingLists;
    private TextView dataNotFoundText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        dataNotFoundText = findViewById(R.id.data_not_show);

        bookingRecyclerView = findViewById(R.id.booking_rec);
        bookingRecyclerView.setHasFixedSize(true);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingLists = new ArrayList<>();
        adapter = new BookingAdapter(this, bookingLists);
        bookingRecyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        loadBookingData();
    }

    private void loadBookingData() {
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String userId = documentSnapshot.getString("userId");
                        String status = documentSnapshot.getString("paymentStatus");
                        String id = documentSnapshot.getId();
                        db.collection("users")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(userDocumentSnapshot -> {
                                    String name = userDocumentSnapshot.getString("name");
                                    String email = userDocumentSnapshot.getString("email");
                                    BookingItemList bookingList = new BookingItemList(name, email, status, id);
                                    bookingLists.add(bookingList);
                                    adapter.notifyDataSetChanged();
                                    updateDataNotFoundVisibility();
                                    progressDialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                });
                    }
                    if (bookingLists.isEmpty()) {
                        updateDataNotFoundVisibility();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                });
    }

    private void updateDataNotFoundVisibility() {
        if (bookingLists != null && bookingLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
