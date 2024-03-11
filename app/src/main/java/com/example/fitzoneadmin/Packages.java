package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;

public class Packages extends AppCompatActivity {
    AppCompatTextView pac_show_descri,pac_show_duration,pac_show_price,pac_show_name;
    Button pac_del,pac_up;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        pac_show_name = findViewById(R.id.pac_show_name);
        pac_show_price = findViewById(R.id.pac_show_price);
        pac_show_duration = findViewById(R.id.pac_show_duration);
        pac_show_descri = findViewById(R.id.pac_show_descri);
        pac_up = findViewById(R.id.pac_up);
        pac_del = findViewById(R.id.pac_del);

        Intent intent = getIntent();
        String pid = intent.getStringExtra("name");

        pac_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the name and description of the diet
                String name = pac_show_name.getText().toString();
                String price = pac_show_price.getText().toString();
                String duration = pac_show_duration.getText().toString();
                String ddescription = pac_show_descri.getText().toString();


                // Start the UpdateDiet activity and pass data as extras
                Intent intent = new Intent(Packages.this, UpdatePackages.class);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("duration", duration);
                intent.putExtra("description", ddescription);
               startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(Packages.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String price = documentSnapshot.getString("price");
                String duration = documentSnapshot.getString("duration");
//                 Check if the userNameFromIntent matches the user
                if (pid.equals(name)) {
                    // Display the data only if they match
                    pac_show_name.setText(name != null ? name : "No name");
                    pac_show_price.setText(price != null ? price : "No name");
                    pac_show_duration.setText(duration != null ? duration : "No name");
                    pac_show_descri.setText(description != null ? description : "No username");

                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        pac_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String pid = intent.getStringExtra("name");


                progressDialog.show();
                // Get the name of the diet from the TextView
                String dietName = pac_show_name.getText().toString();

                // Query Firestore for the document ID of the diet entry based on its name
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("packages")
                        .whereEqualTo("name", dietName)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Get the document ID
                                String documentId = documentSnapshot.getId();

                                // Delete the document from Firestore
                                db.collection("packages")
                                        .document(pid)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Document successfully deleted
                                            // Optionally, you can show a toast or perform any other action
                                            progressDialog.dismiss();
                                            showToast("packages deleted successfully");
                                            finish();

                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle any errors that may occur
                                            progressDialog.dismiss();
                                            showToast("Failed to delete packages");
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors that may occur
                            progressDialog.dismiss();
                            showToast("Failed to delete packages");
                        });
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
    private void showToast(String message) {
        Toast.makeText(Packages.this, message, Toast.LENGTH_SHORT).show();
    }
}