package com.example.fitzoneadmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPackages extends AppCompatActivity {
    AppCompatEditText pay_name, pay_price, pay_duration, pay_description;
    Button pay_sub;
    ProgressDialog progressDialog; // Progress dialog for showing upload progress
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_packages);

        db = FirebaseFirestore.getInstance();

        pay_name = findViewById(R.id.pay_name);
        pay_price = findViewById(R.id.pay_price);
        pay_duration = findViewById(R.id.pay_duration);
        pay_description = findViewById(R.id.pay_description);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        pay_sub = findViewById(R.id.pay_sub);
        pay_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pay_name.getText().toString().trim();
                String price = pay_price.getText().toString().trim();
                String duration = pay_duration.getText().toString().trim();
                String description = pay_description.getText().toString().trim();

                // Check if name, description, and image URI are not empty
                if (!name.isEmpty() && !description.isEmpty() && !price.isEmpty() && !duration.isEmpty()  ) {
                    // save image to Firebase Storage
                    saveDataToFirestore(name,description,price,duration);
                } else {
                    // Handle empty fields or no selected image
                    // You can show a toast message or provide some feedback to the user
                }

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

    private void saveDataToFirestore(String name, String description, String price, String duration) {
        // Show ProgressDialog
        progressDialog.show();
        // Create a Map object to store the package data
        Map<String, Object> packageData = new HashMap<>();
        packageData.put("name", name);
        packageData.put("description", description);
        packageData.put("price", price);
        packageData.put("duration", duration);

        // Add the package data to Firestore
        db.collection("packages").document(name)
                .set(packageData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();
                        // Handle success
                        // You can show a success message or navigate back to the previous screen
                        onBackPressed(); // Example: Navigate back to the previous screen
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure

                        progressDialog.dismiss();
                        // You can show an error message or handle the failure as per your requirement
                        Toast.makeText(AddPackages.this, "Failed to add package: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
