    package com.example.fitzoneadmin;

    import static android.content.ContentValues.TAG;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatButton;
    import androidx.appcompat.widget.AppCompatTextView;
    import androidx.cardview.widget.CardView;

    import android.annotation.SuppressLint;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
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

        EditText chargeEt ;
        AppCompatButton btnChange ;
        String trainerId ;

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
            chargeEt = findViewById(R.id.charge);
            btnChange = findViewById(R.id.btn_change);

            // Initialize Image
            trainer_img = findViewById(R.id.trainer_img);

            // Initialize Button
            approve = findViewById(R.id.btn_approve);
            reject = findViewById(R.id.btn_reject);

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
                    trainerId = documentSnapshot.getId();
                    Log.d(TAG, "trainer user ID: " + trainerId);

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


            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the new charge value from the EditText
                    String newCharge = chargeEt.getText().toString();

                    // Show progress dialog
                    progressDialog.setMessage("Updating charge...");
                    progressDialog.show();

                    // Query Firestore to find the document ID associated with the trainer's email
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("trainers")
                            .whereEqualTo("email", trainer_email.getText().toString())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String trainerId = documentSnapshot.getId();
                                    // Update charge in Firestore using the retrieved document ID
                                    db.collection("trainers").document(trainerId)
                                            .update("charge", newCharge)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Dismiss progress dialog
                                                    progressDialog.dismiss();
                                                    Toast.makeText(PenddingTrainerProfile.this, "Charge updated successfully", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "email: " + trainer_email.getText().toString());
                                                    Log.d(TAG, "New Charge: " + newCharge);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Dismiss progress dialog
                                                    progressDialog.dismiss();
                                                    // Handle failure
                                                    Toast.makeText(PenddingTrainerProfile.this, "Error updating charge: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Dismiss progress dialog
                                    progressDialog.dismiss();
                                    // Handle failure to retrieve document ID
                                    Toast.makeText(PenddingTrainerProfile.this, "Error fetching document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
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

                    approveTrainer();
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
                                                    String trainerName = documentSnapshot.getString("name");
                                                    sendRejectionEmail(trainer_email.getText().toString(), trainerName);
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



        private void sendEmail() {
            String[] TO = {trainer_email.getText().toString()}; // Replace with the recipient's email address
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear ," +trainer_name.getText().toString()+
                    "\n" +
                    "I hope this email finds you well.\n" +
                    "\n" +
                    "I am pleased to inform you that your application to become a trainer at FitZone Gym has been successful, and you have been approved for the position.\n" +
                    "\n" +
                    "Congratulations! Your expertise and dedication to fitness have made you a standout candidate, and we are thrilled to have you join our team. We believe that your skills and passion will greatly contribute to the success of our gym and the achievement of our clients' fitness goals.\n" +
                    "\n" +
                    "As an approved trainer, you will have the opportunity to inspire and motivate our members, helping them on their fitness journey and fostering a positive and supportive environment within our gym.\n" +
                    "\n" +
                    "We will reach out to you shortly to discuss the next steps, including onboarding procedures and scheduling. In the meantime, if you have any questions or require any further information, please do not hesitate to reach out to us.\n" +
                    "\n" +
                    "Once again, congratulations on your approval as a trainer at FitZone Gym. We are excited to have you on board and look forward to working together to make FitZone the premier destination for fitness enthusiasts.\n" +
                    "\n" +
                    "Best regards,");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                // Email sending is initiated successfully, now proceed to approve the trainer

            } catch (android.content.ActivityNotFoundException ex) {
                // Handle error when no email client is installed
            }
        }

        private void approveTrainer() {
            // Query Firestore to find the document ID associated with the trainer's email
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("trainers")
                    .whereEqualTo("email", trainer_email.getText().toString())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String documentId = documentSnapshot.getId();
                            String price = "0.00"; // Default price

                            // Check if the charge field exists in the document
                            if (documentSnapshot.contains("charge")) {
                                price = documentSnapshot.getString("charge");
                            }

                            // Check if the charge is greater than or equal to 100
                            if (Double.parseDouble(price) >= 100.0) {
                                // Update the document only if it exists and charge is greater than or equal to 100
                                db.collection("trainers").document(documentId)
                                        .update("is_active", true, "charge", price)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(PenddingTrainerProfile.this, "Trainer Approved Successfully", Toast.LENGTH_SHORT).show();
                                                // Call sendEmail() after trainer approval
                                                sendEmail();
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
                            } else {
                                // If the charge is less than 100, display an error message
                                Toast.makeText(PenddingTrainerProfile.this, "Cannot approve trainer. Charge must be greater than or equal to 100.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PenddingTrainerProfile.this, "Error fetching document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        private void sendRejectionEmail(String recipientEmail, String trainerName) {
            String[] TO = {recipientEmail}; // Replace with the recipient's email address
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Application Status: FitZone Gym Trainer Position");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear " + trainerName + ",\n\n" +
                    "I hope this email finds you well.\n\n" +
                    "I regret to inform you that after careful consideration of all applications, we have decided not to proceed with your application for the position of trainer at FitZone Gym. While we received numerous qualified applicants, we had to make some difficult decisions based on our current needs and requirements.\n\n" +
                    "Please understand that this decision does not reflect on your skills or qualifications. We appreciate the time and effort you put into your application and the passion you have for fitness. Unfortunately, we were unable to accommodate all candidates, and we had to select individuals whose experience and expertise aligned more closely with our current priorities.\n\n" +
                    "We encourage you to continue pursuing your career in the fitness industry, as your dedication and skills are valuable assets. Although we are unable to offer you a position at this time, we will keep your application on file for future opportunities that may arise.\n\n" +
                    "Thank you once again for your interest in joining the FitZone team. We wish you all the best in your future endeavors, and we hope our paths may cross again.\n\n" +
                    "Sincerely,"
            );

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                // Handle error when no email client is installed
            }
        }

    }