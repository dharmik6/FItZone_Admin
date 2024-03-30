package com.example.fitzoneadmin;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DocumentHome extends AppCompatActivity {
    CardView aadhaar_card,certificates;
    TextView trainer_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_home);

        certificates=findViewById(R.id.certificates);
        aadhaar_card=findViewById(R.id.aadhaar_card);
        trainer_id=findViewById(R.id.trainer_id);
        Intent intent1 = getIntent();
        String treid = intent1.getStringExtra("treid");
        trainer_id.setText(treid);
        // Check if aadhaar_card is not null before setting OnClickListener
        // Set OnClickListener for aadhaar_card
        if (aadhaar_card != null) {
            aadhaar_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aadhaarIntent = new Intent(DocumentHome.this, Aadhaar.class);
                    aadhaarIntent.putExtra("treid",treid);
                    startActivity(aadhaarIntent);
                }
            });
        }

        // Set OnClickListener for certificates
        if (certificates != null) {
            certificates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent certificatesIntent = new Intent(DocumentHome.this, CertificatesList.class);
                    certificatesIntent.putExtra("treid",treid);
                    startActivity(certificatesIntent);
                }
            });
        }

        ImageView backPress = findViewById(R.id.doc_back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}