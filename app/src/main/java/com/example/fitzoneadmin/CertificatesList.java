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
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CertificatesList extends AppCompatActivity {
    RecyclerView certificates_show;
    TextView trainer_id;
    private CertificatesAdapter adapter;
    private List<CertificatesItemList> trainersLists;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates_list);

        certificates_show=findViewById(R.id.certificates_show);
        trainer_id=findViewById(R.id.trainer_id);
        Intent intent1 = getIntent();
        String treid = intent1.getStringExtra("treid");
        trainer_id.setText(treid);
        certificates_show.setHasFixedSize(true);
        certificates_show.setLayoutManager(new LinearLayoutManager(this));

        trainersLists = new ArrayList<>();
        adapter = new CertificatesAdapter(this,trainersLists);
        certificates_show.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).collection("certificates").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String  imageUrl= documentSnapshot.getString("imageUrl");
                String  description= documentSnapshot.getString("description");
                String cerid=documentSnapshot.getId();
                CertificatesItemList member = new CertificatesItemList(name, imageUrl,description,cerid);
                trainersLists.add(member);
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