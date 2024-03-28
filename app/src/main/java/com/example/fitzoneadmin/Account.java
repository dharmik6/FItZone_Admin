package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Account extends AppCompatActivity {
    ImageView admin_image;
    TextView admin_name;
    TextView admin_email,admin_number,admin_address,admin_gender;
    CardView edit_pro_acc;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        admin_image = findViewById(R.id.admin_img);
        admin_name = findViewById(R.id.admin_name);
        admin_email = findViewById(R.id.admin_email);
        admin_number = findViewById(R.id.admin_number);
        admin_address = findViewById(R.id.admin_address);
        admin_gender = findViewById(R.id.admin_gender);
        edit_pro_acc = findViewById(R.id.edit_pro_acc);

        progressDialog = new ProgressDialog(this); // Initialize ProgressDialog

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false); // Prevent user from cancelling ProgressDialog
        progressDialog.show(); // Show ProgressDialog

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            progressDialog.show(); // Show progress dialog before fetching user data
            String userId = currentUser.getUid();
            // Query Firestore for data
            // Query Firestore for data
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("admins").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                progressDialog.dismiss(); // Dismiss ProgressDialog when data is retrieved
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String number = documentSnapshot.getString("number");
                    String address = documentSnapshot.getString("address");
                    String gender = documentSnapshot.getString("gender");
                    String image = documentSnapshot.getString("image");
                    String aid = documentSnapshot.getId();

                    // Display the data
                    admin_name.setText(name != null ? name : "No name");
                    admin_address.setText(address != null ? address : "No address");
                    admin_gender.setText(gender != null ? gender : "No gender");
                    admin_email.setText(email != null ? email : "No email");
                    admin_number.setText(number != null ? number : "No number");
                    if (image != null) {
                        Glide.with(Account.this)
                                .load(image)
                                .into(admin_image);
                    }
                    // Handle Edit Profile button click
                    edit_pro_acc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1=new Intent(Account.this,UpdateAccount.class);
                            intent1.putExtra("email",aid);
                            startActivity(intent1);
                        }
                    });
                }
            });
        }


        // Handle back button click
        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}