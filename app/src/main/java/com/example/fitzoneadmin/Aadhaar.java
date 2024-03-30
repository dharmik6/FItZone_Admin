package com.example.fitzoneadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class Aadhaar extends AppCompatActivity {

    private static final String TAG = "AadhaarActivity";
    private ImageView imgFront, imgBack;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhaar);

        Intent intent1 = getIntent();
        String treid = intent1.getStringExtra("treid");
        Log.d(TAG, "onCreate: xyz trainer id" + treid);

        imgFront = findViewById(R.id.imgFront);
        imgBack = findViewById(R.id.imgBack);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Aadhar Images...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadAadharFromFirestore(treid);

        ImageView backPress = findViewById(R.id.back_press);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadAadharFromFirestore(String treid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String aadharFrontUrl = documentSnapshot.getString("aadharFront");
                        String aadharBackUrl = documentSnapshot.getString("aadharBack");

                        // Load Aadhar Front Image
                        Glide.with(this)
                                .load(aadharFrontUrl)
                                .into(imgFront);

                        // Load Aadhar Back Image
                        Glide.with(this)
                                .load(aadharBackUrl)
                                .into(imgBack);

                        progressDialog.dismiss(); // Dismiss the progress dialog after loading images
                    } else {
                        Log.d(TAG, "No such document");
                        progressDialog.dismiss(); // Dismiss the progress dialog if document doesn't exist
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting documents.", e);
                    progressDialog.dismiss(); // Dismiss the progress dialog on failure
                });
    }
}
