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

import java.util.ArrayList;
import java.util.List;

public class PurchasesList extends AppCompatActivity {

    private RecyclerView purchasRecyclerView;
    private PurchasAdapter adapter;
    private List<MemberList> purchasLists;
    private TextView dataNotFoundText;

    private ProgressDialog progressDialog; // Declare ProgressDialog

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases_list);

        dataNotFoundText = findViewById(R.id.data_not_show);

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        purchasRecyclerView = findViewById(R.id.purchas);
        purchasRecyclerView.setHasFixedSize(true);
        purchasRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        purchasLists = new ArrayList<>();
        adapter = new PurchasAdapter(this, purchasLists);
        purchasRecyclerView.setAdapter(adapter);

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
        purchasLists.clear(); // Clear the previous list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("purchases")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String userId = documentSnapshot.getString("userId");
                        String packagename = documentSnapshot.getString("packageName");
                        String id = documentSnapshot.getId();
                        // Fetch user details using userId
                        db.collection("users")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(userDocumentSnapshot -> {
                                    // Retrieve user details
                                    String name = userDocumentSnapshot.getString("name");
                                    // Create a BookingItemList object with user details
                                    MemberList purchasList = new MemberList(name, packagename ,id);
                                    purchasLists.add(purchasList);
                                    adapter.notifyDataSetChanged(); // Notify adapter about data changes
                                    progressDialog.dismiss(); // Dismiss ProgressDialog after fetching data
                                    updateDataNotFoundVisibility(); // Update visibility of "Data Not Found" message
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to fetch user details
                                    progressDialog.dismiss(); // Dismiss ProgressDialog on failure
                                    updateDataNotFoundVisibility(); // Update visibility of "Data Not Found" message
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    progressDialog.dismiss(); // Dismiss ProgressDialog on failure
                    updateDataNotFoundVisibility(); // Update visibility of "Data Not Found" message
                });
    }

    private void updateDataNotFoundVisibility() {
        if (purchasLists != null && purchasLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
