package com.example.fitzoneadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Account extends AppCompatActivity {
    ImageView admin_image;
    TextView admin_name;
    TextView admin_email,admin_number,admin_address,admin_gender;
    String aname="dhruvpandav04@gmail.com";
    String bname="dharmik.kacha.2526@gmail.com";
    CardView edit_pro_acc;
    ProgressBar progressBar ;
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
        progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this); // Initialize ProgressDialog

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false); // Prevent user from cancelling ProgressDialog
        progressDialog.show(); // Show ProgressDialog

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admins").get().addOnSuccessListener(queryDocumentSnapshots -> {
            progressDialog.dismiss(); // Dismiss ProgressDialog when data is retrieved
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String number = documentSnapshot.getString("number");
                String address = documentSnapshot.getString("address");
                String gender = documentSnapshot.getString("gender");
                String image = documentSnapshot.getString("image");

                // Check if the userNameFromIntent matches the user
                if (aname.equals(email)) {
                    // Display the data only if they match
                    admin_name.setText(name != null ? name : "No name");
                    admin_address.setText(address != null ? address : "No address");
                    admin_gender.setText(gender != null ? gender : "No gender");
                    admin_email.setText(email != null ? email : "No email");
                    admin_number.setText(number != null ? number : "No number");
                    // Inside onCreate method


                    if (image != null) {
                        // Show ProgressBar before starting image loading
                        progressBar.setVisibility(View.VISIBLE);

                        Glide.with(Account.this)
                                .load(image)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressDialog.dismiss(); // Dismiss ProgressDialog on image load failure
                                        progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressDialog.dismiss(); // Dismiss ProgressDialog on successful image load
                                        progressBar.setVisibility(View.GONE); // Hide ProgressBar on successful load
                                        return false;
                                    }
                                })
                                .into(admin_image);
                    }

                }

            }
        });

        // Handle Edit Profile button click
        edit_pro_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= admin_email.getText().toString().trim();
                Intent intent1=new Intent(Account.this,UpdateAccount.class);
                intent1.putExtra("email",email);
                startActivity(intent1);
            }
        });

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