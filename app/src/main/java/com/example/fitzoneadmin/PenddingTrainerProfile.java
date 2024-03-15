    package com.example.fitzoneadmin;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatTextView;
    import androidx.cardview.widget.CardView;

    import android.annotation.SuppressLint;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Toast;

    import com.bumptech.glide.Glide;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;

    public class PenddingTrainerProfile extends AppCompatActivity {
        AppCompatTextView trainer_name, trainer_specialization, trainer_email, trainer_number, trainer_gender, trainer_bio, trainer_address, trainer_date, trainer_experience;
        ImageView trainer_img;
        Button approve, reject, mouny_change;

        ProgressDialog progressDialog;
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pendding_trainer_profile);

            // Initialize TextViews
            trainer_name = findViewById(R.id.trainer_name);
            trainer_specialization = findViewById(R.id.trainer_specialization);
            trainer_email = findViewById(R.id.trainer_email);
            trainer_number = findViewById(R.id.trainer_number);
            trainer_gender = findViewById(R.id.trainer_gender);
            trainer_bio = findViewById(R.id.trainer_boi);
            trainer_address = findViewById(R.id.trainer_address);
            trainer_date = findViewById(R.id.trainer_date);
            trainer_experience = findViewById(R.id.trainer_experience);

            // Initialize Image
            trainer_img = findViewById(R.id.trainer_img);

            // Initialize Button
            approve = findViewById(R.id.btn_approve);
            reject = findViewById(R.id.btn_reject);
            mouny_change = findViewById(R.id.mouny_change);

            Intent intent = getIntent();
            String memberid = intent.getStringExtra("name");

            // Query Firestore for data
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("name");

                    String bio = documentSnapshot.getString("bio");
                    String experience = documentSnapshot.getString("experience");
                    String address = documentSnapshot.getString("address");
                    String specialization = documentSnapshot.getString("specialization");
                    String gender = documentSnapshot.getString("gender");
                    String email = documentSnapshot.getString("email");
                    String number = documentSnapshot.getString("number");
                    String image = documentSnapshot.getString("image");
                    String treid=documentSnapshot.getId();

                    //                 Check if the userNameFromIntent matches the user
                    if (memberid.equals(name)) {
                        // Display the data only if they match
                        trainer_name.setText(name != null ? name : "No name");
                        trainer_specialization.setText(specialization != null ? specialization : "No specialization");
                        trainer_email.setText(email != null ? email : "No email");
                        trainer_number.setText(number != null ? number : "No number");
                        trainer_gender.setText(gender != null ? gender : "No gender");
                        trainer_bio.setText(bio != null ? bio : "No bio");
                        trainer_address.setText(address != null ? address : "No address");
                        trainer_experience.setText(experience != null ? experience : "No experience");
                        if (image != null) {
                            Glide.with(PenddingTrainerProfile.this)
                                    .load(image)
                                    .into(trainer_img);
                        }
                    } else {
                        // userNameFromIntent and user don't match, handle accordingly
                        //                    showToast("User data does not match the intent.");
                    }
                }
            });


            // Initialize TextViews
            ImageView backPress = findViewById(R.id.back);
            backPress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Query Firestore to find the document ID associated with the trainer's email
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("trainers")
                            .whereEqualTo("email", trainer_email.getText().toString())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String documentId = documentSnapshot.getId();

                                    String price = "0.00";

                                    // Update the document only if it exists
                                    db.collection("trainers").document(documentId)
                                            .update("is_active", true, "charge", price) // Add charge field with default value
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(PenddingTrainerProfile.this, "Trainer Approved Successfully", Toast.LENGTH_SHORT).show();
                                                    // Optionally, you can finish the activity or perform any other actions
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PenddingTrainerProfile.this, "Error approving trainer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PenddingTrainerProfile.this, "Error fetching document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });



            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Deleting authentication...");

            // Other initialization code

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show progress dialog
                    progressDialog.show();

                    // Query Firestore to find the document ID associated with the trainer's email
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("trainers")
                            .whereEqualTo("email", trainer_email.getText().toString())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String documentId = documentSnapshot.getId();
                                    String password = documentSnapshot.getString("password");
                                    // Delete user document from Firestore
                                    db.collection("trainers").document(documentId)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // After deleting trainer data, also delete authentication
                                                    deleteFirebaseAuthentication(trainer_email.getText().toString(), password);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Dismiss progress dialog
                                                    progressDialog.dismiss();

                                                    // Handle errors
                                                    Toast.makeText(PenddingTrainerProfile.this, "Error deleting trainer data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Dismiss progress dialog
                                    progressDialog.dismiss();

                                    Toast.makeText(PenddingTrainerProfile.this, "Error fetching document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

        // Method to delete Firebase authentication
        private void deleteFirebaseAuthentication(String email, String password) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Authentication successful, proceed to delete the user
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    // Dismiss progress dialog
                                                    progressDialog.dismiss();

                                                    if (task.isSuccessful()) {
                                                        // Authentication deleted successfully
                                                        Toast.makeText(PenddingTrainerProfile.this, "Authentication deleted successfully", Toast.LENGTH_SHORT).show();
                                                        finish(); // Finish the activity after successful deletion
                                                    } else {
                                                        // Handle errors
                                                        Toast.makeText(PenddingTrainerProfile.this, "Error deleting authentication: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Dismiss progress dialog
                                    progressDialog.dismiss();

                                    // Handle the case where no user is authenticated
                                    Toast.makeText(PenddingTrainerProfile.this, "No user authenticated", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Dismiss progress dialog
                                progressDialog.dismiss();

                                // Handle authentication failure
                                Toast.makeText(PenddingTrainerProfile.this, "Authentication failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }