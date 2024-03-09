package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class UpdatePackages extends AppCompatActivity {

    Button up_but;
    TextView up_pac_name;
    EditText up_pac_price,up_pac_dur,up_pac_des;
    ProgressDialog progressDialog; // Progress dialog for showing upload progress
    private FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_packages);

        up_but = findViewById(R.id.up_but);
        up_pac_name = findViewById(R.id.up_pac_name);
        up_pac_price = findViewById(R.id.up_pac_price);
        up_pac_dur = findViewById(R.id.up_pac_dur);
        up_pac_des = findViewById(R.id.up_pac_des);

        Intent intent = getIntent();
        String did = intent.getStringExtra("name");
        String ddd = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");
        String duration = intent.getStringExtra("duration");

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Set the received data to the EditText fields
        up_pac_name.setText(did);
        up_pac_price.setText(price);
        up_pac_dur.setText(duration);
        up_pac_des.setText(ddd);

        up_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input values
                String newName = up_pac_name.getText().toString();
                String newprice = up_pac_price.getText().toString();
                String newdur = up_pac_dur.getText().toString();
                String newDescription = up_pac_des.getText().toString();

                // Check if name, description, and image URI are not empty
                if (!newName.isEmpty() && !newDescription.isEmpty() && !newdur.isEmpty() && !newprice.isEmpty()) {
                    // Upload image to Firebase Storage
                    updateFirestoreDocument(newName,newprice,newdur, newDescription);
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
    private void updateFirestoreDocument(String newName, String newprice,String newdur, String newDescription) {
        Intent intent = getIntent();
        String pid = intent.getStringExtra("name");

        // Create a map with the updated fields
        Map<String, Object> dietData = new HashMap<>();
//        dietData.put("name", newName);
        dietData.put("price", newprice);
        dietData.put("duration", newdur);
        dietData.put("description", newDescription);
        // Show progress dialog
        progressDialog = new ProgressDialog(UpdatePackages.this);
        progressDialog.setMessage("Uploading packages...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Update the document in Firestore
        db.collection("packages")
                .document(pid)  // Replace "your_document_id" with the actual document ID
                .update(dietData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Document updated successfully
                            progressDialog.dismiss();

                            Toast.makeText(UpdatePackages.this, "packages information updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Finish the activity after successful update
                        } else {
                            // Error updating document
                            progressDialog.dismiss();

                            Toast.makeText(UpdatePackages.this, "Failed to update packages information: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}