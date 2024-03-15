package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PurchaseDetails extends AppCompatActivity {
    FirebaseFirestore db;

    AppCompatTextView paymentid,membername,packagename,packageduration,packagestatus,price,paidamount,paymentmethod,purchasedate,experience;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);

        ImageView backPress = findViewById(R.id.back);
        paymentid = findViewById(R.id.pyment_id);
        membername = findViewById(R.id.member_name);
        packagename = findViewById(R.id.package_name);
        packageduration = findViewById(R.id.package_duration);
        packagestatus = findViewById(R.id.package_status);
        price = findViewById(R.id.price);
        paidamount = findViewById(R.id.paid_amount);
        purchasedate = findViewById(R.id.purchase_date);



        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String packagE = intent.getStringExtra("package");

        membername.setText(name);
        packagename.setText(packagE);


        db = FirebaseFirestore.getInstance();


        db.collection("purchases").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Dismiss ProgressDialog when data fetching is complete


                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve additional data and set them to respective TextViews
                                String duration = document.getString("duration");
                                String paymentId = document.getString("paymentId");
                                String pricE = document.getString("price");
                                String purchasesDate = document.getString("purchasesDate");
                                String status = document.getString("status");



                                paymentid.setText(paymentId != null ? paymentId : "No date");
                                packageduration.setText(duration != null ? duration : "No date");
                                price.setText(pricE != null ? pricE : "No date");
                                packagestatus.setText(status != null ? status : "No date");
                                paidamount.setText(pricE != null ? pricE : "No date");
                                purchasedate.setText(purchasesDate != null ? purchasesDate : "No payment id");

                            } else {
                                // Handle the case where the document does not exist
                            }

                        } else {
                            // Handle exceptions while fetching data from Firestore
                            FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                            // Log the exception or handle it as needed
                        }
                    }
                });




        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}