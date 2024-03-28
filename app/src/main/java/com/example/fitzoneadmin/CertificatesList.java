package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class CertificatesList extends AppCompatActivity {
    RecyclerView certificates_show;
    TextView trainer_id;
    private CertificatesAdapter adapter;
    private List<CertificatesItemList> trainersLists;
    private TextView dataNotFoundText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates_list);

        dataNotFoundText = findViewById(R.id.data_not_show);

        certificates_show = findViewById(R.id.certificates_show);
        trainer_id = findViewById(R.id.trainer_id);
        Intent intent1 = getIntent();
        String treid = intent1.getStringExtra("treid");
        trainer_id.setText(treid);
        certificates_show.setHasFixedSize(true);
        certificates_show.setLayoutManager(new LinearLayoutManager(this));

        trainersLists = new ArrayList<>();
        adapter = new CertificatesAdapter(this, trainersLists);
        certificates_show.setAdapter(adapter);

        // Query Firestore for initial data if treid is not null
        if (treid != null) {
            loadCertificatesFromFirestore(treid);
        }

        // Listen for real-time updates if treid is not null
        if (treid != null) {
            listenForCertificateChanges(treid);
        }

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadCertificatesFromFirestore(String treid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).collection("certificates").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            String name = documentChange.getDocument().getString("name");
                            String imageUrl = documentChange.getDocument().getString("imageUrl");
                            String description = documentChange.getDocument().getString("description");
                            String cerid = documentChange.getDocument().getId();
                            CertificatesItemList member = new CertificatesItemList(name, imageUrl, description, cerid);
                            trainersLists.add(member);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    updateDataNotFoundVisibility();
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                });
    }

    private void listenForCertificateChanges(String treid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).collection("certificates")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle errors
                        return;
                    }

                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            String name = documentChange.getDocument().getString("name");
                            String imageUrl = documentChange.getDocument().getString("imageUrl");
                            String description = documentChange.getDocument().getString("description");
                            String cerid = documentChange.getDocument().getId();
                            CertificatesItemList member = new CertificatesItemList(name, imageUrl, description, cerid);
                            trainersLists.add(member);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    updateDataNotFoundVisibility();
                });
    }

    private void updateDataNotFoundVisibility() {
        if (trainersLists != null && trainersLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
