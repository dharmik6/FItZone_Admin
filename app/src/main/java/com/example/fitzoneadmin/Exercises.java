package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.ProgressDialog;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Exercises extends AppCompatActivity {
    ImageView show_image;
    EditText show_name,show_body,show_equipment,show_description;
    Button exe_delete,exe_update;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        exe_update = findViewById(R.id.exe_update);
        exe_delete = findViewById(R.id.exe_delete);
        show_image = findViewById(R.id.show_image);
        show_name = findViewById(R.id.show_name);
        show_body = findViewById(R.id.show_body);
        show_equipment = findViewById(R.id.show_equipment);
        show_description = findViewById(R.id.show_description);


        Intent intent = getIntent();
        String exeid = intent.getStringExtra("name");

        progressDialog = new ProgressDialog(Exercises.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exercises").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String body = documentSnapshot.getString("body");
                String equipment = documentSnapshot.getString("equipment");
                String description = documentSnapshot.getString("description");
                String image = documentSnapshot.getString("imageUrl");
//                 Check if the userNameFromIntent matches the user
                if (exeid.equals(name)) {
                    // Display the data only if they match
                    show_name.setText(name != null ? name : "No name");
                    show_equipment.setText(equipment != null ? equipment : "No name");
                    show_description.setText(description != null ? description : "No name");
                    show_body.setText(body != null ? body : "No username");
                    if (image != null) {
                        Glide.with(Exercises.this)
                                .load(image)
                                .into(show_image);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        exe_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Exercises.this, UpdateExercises.class));
            }
        });
        exe_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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