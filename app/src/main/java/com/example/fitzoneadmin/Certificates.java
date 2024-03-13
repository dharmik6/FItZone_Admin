package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Certificates extends AppCompatActivity {
    ImageView certificates_image;
    TextView certificates_name, certificates_dec;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);
        Intent intent1 = getIntent();
        String name1 = intent1.getStringExtra("name");
        String tid = intent1.getStringExtra("id");
        String image = intent1.getStringExtra("image");
        String description = intent1.getStringExtra("description");

        certificates_image = findViewById(R.id.certificates_image);
        certificates_dec = findViewById(R.id.certificates_dec);
        certificates_name = findViewById(R.id.certificates_name);

        certificates_name.setText(name1);
        certificates_dec.setText(description);
        // Load image using Glide
        Glide.with(this)
                .load(image)
                .into(certificates_image);

        progressDialog = new ProgressDialog(Certificates.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);


        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
