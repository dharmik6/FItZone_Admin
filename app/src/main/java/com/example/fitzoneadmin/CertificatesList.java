package com.example.fitzoneadmin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CertificatesList extends AppCompatActivity {
    private RecyclerView certificatesRecyclerView;
    private CertificatesAdapter certificatesAdapter;
    private List<CertificatesItemList> certificatesList;
    private TextView dataNotFoundTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates_list);

        // Initialize views
        certificatesRecyclerView = findViewById(R.id.certificates_show);
        dataNotFoundTextView = findViewById(R.id.data_not_show);
        ImageView backImageView = findViewById(R.id.back);

        // Set up RecyclerView
        certificatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        certificatesList = new ArrayList<>();
        certificatesAdapter = new CertificatesAdapter(this, certificatesList);
        certificatesRecyclerView.setAdapter(certificatesAdapter);

        // Get trainer ID from intent
        Intent intent = getIntent();
        String trainerId = intent.getStringExtra("treid");
        Log.d(TAG, "onCreate: trainer id in certificates"+trainerId);

        // Load certificates from Firestore
        if (trainerId != null) {
            loadCertificatesFromFirestore(trainerId);
            listenForCertificateChanges(trainerId);
        }

        // Handle back button press
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadCertificatesFromFirestore(String trainerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(trainerId).collection("certificates")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        certificatesList.clear(); // Clear the list before adding items
                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                String name = documentChange.getDocument().getString("name");
                                String imageUrl = documentChange.getDocument().getString("imageUrl");
                                String description = documentChange.getDocument().getString("description");
                                String certificateId = documentChange.getDocument().getId();
                                CertificatesItemList certificateItem = new CertificatesItemList(name, imageUrl, description, certificateId);
                                certificatesList.add(certificateItem);
                            }
                        }
                        certificatesAdapter.notifyDataSetChanged();
                        updateDataNotFoundVisibility();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failures
                    }
                });
    }

    private void listenForCertificateChanges(String trainerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(trainerId).collection("certificates")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle errors
                        return;
                    }
                    certificatesList.clear(); // Clear the list before adding items
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            String name = documentChange.getDocument().getString("name");
                            String imageUrl = documentChange.getDocument().getString("imageUrl");
                            String description = documentChange.getDocument().getString("description");
                            String certificateId = documentChange.getDocument().getId();
                            CertificatesItemList certificateItem = new CertificatesItemList(name, imageUrl, description, certificateId);
                            certificatesList.add(certificateItem);
                        }
                    }
                    certificatesAdapter.notifyDataSetChanged();
                    updateDataNotFoundVisibility();
                });
    }

    private void updateDataNotFoundVisibility() {
        if (certificatesList != null && certificatesList.isEmpty()) {
            dataNotFoundTextView.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundTextView.setVisibility(View.GONE);
        }
    }
}
