package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PackagesList extends AppCompatActivity {
    LinearLayout add_payment;
    RecyclerView payment_recyc;
    private PackagesAdapter adapter;
    private List<PackagesItemList> dietLists;
    private ProgressDialog progressDialog;

    private TextView dataNotFoundText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_list);

        dataNotFoundText = findViewById(R.id.data_not_show);

        add_payment = findViewById(R.id.add_payment);
        payment_recyc = findViewById(R.id.payment_recyc);

        // Set click listener for adding payment
        add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PackagesList.this, AddPackages.class));
            }
        });

        payment_recyc.setHasFixedSize(true);
        payment_recyc.setLayoutManager(new LinearLayoutManager(this));

        dietLists = new ArrayList<>();
        adapter = new PackagesAdapter(this, dietLists);
        payment_recyc.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Load data from Firestore
        loadPackageData();

        // Set back button click listener
        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data every time activity is resumed
        loadPackageData();
    }

    private void loadPackageData() {
        // Clear previous data
        dietLists.clear();

        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String price = documentSnapshot.getString("price");
                String duration = documentSnapshot.getString("duration");
                String description = documentSnapshot.getString("description");
                PackagesItemList diet = new PackagesItemList(name, price, duration, description);
                dietLists.add(diet);
            }

            // Notify adapter about data changes
            adapter.notifyDataSetChanged();

            // Dismiss ProgressDialog when data is loaded
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // Update visibility of "Data Not Found" message
            updateDataNotFoundVisibility();
        }).addOnFailureListener(e -> {
            // Handle failures
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
    }

    private void updateDataNotFoundVisibility() {
        if (dietLists != null && dietLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
