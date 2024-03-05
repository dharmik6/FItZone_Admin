package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Account extends AppCompatActivity {
    ImageView admin_image;
    TextView admin_name;
    TextView admin_email,admin_number,admin_address,admin_gender;
    String aname="Dhruv Pandav";
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

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admins").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String number = documentSnapshot.getString("number");
                String address = documentSnapshot.getString("address");
                String gender = documentSnapshot.getString("gender");
//                String image = documentSnapshot.getString("image");

//                 Check if the userNameFromIntent matches the user
                if (aname.equals(name)) {
                    // Display the data only if they match
                admin_name.setText(name != null ? name : "No name");
                admin_address.setText(address != null ? address : "No address");
                admin_gender.setText(gender != null ? gender : "No gender");
                admin_email.setText(email != null ? email : "No email");
                admin_number.setText(number != null ? number : "No number");
//                    if (image != null) {
//                        Glide.with(Account.this)
//                                .load(image)
//                                .into(admin_image);
//                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        ImageView backPress = findViewById(R.id.back_press);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}