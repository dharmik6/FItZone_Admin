package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMember extends AppCompatActivity {
    AppCompatEditText text_name, text_username, text_email, text_number, text_pass, text_address;
    Spinner spiner_gender, spiner_age, spiner_goal, spiner_activity, spiner_weight, spiner_height;
    Button button;
    ImageView imageView;
    CircleImageView circleImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    // Declare a Firestore instance
    private FirebaseFirestore db;
    private FirebaseAuth auth; // Declare FirebaseAuth instance
    private Uri selectedImageUri; // Declare selectedImageUri variable
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);


        // Initialize EditText fields
        text_name = findViewById(R.id.text_name);
        text_username = findViewById(R.id.text_username);
        text_email = findViewById(R.id.text_email);
        text_number = findViewById(R.id.text_number);
        text_pass = findViewById(R.id.text_pass);
        text_address = findViewById(R.id.text_address);

        // Initialize spinner fields
        spiner_gender = findViewById(R.id.spiner_gender);
        spiner_age = findViewById(R.id.spiner_age);
        spiner_goal = findViewById(R.id.spiner_goal);
        spiner_activity = findViewById(R.id.spiner_activity);
        spiner_weight = findViewById(R.id.spiner_weight);
        spiner_height = findViewById(R.id.spiner_height);

        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();
        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();


        // Initialize Spinners (code remains the same)
        // Specify the layout to use when the list of choices appears
        // Apply the adapter to the spinners
        // gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_gender.setAdapter(genderAdapter);

        // age spinner
        ArrayAdapter<Integer> ageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Populate the age spinner with ages from 18 to 100
        for (int i = 18; i <= 80; i++) {
            ageAdapter.add(i);
        }
        spiner_age.setAdapter(ageAdapter);

        // goal spinner
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.goal_array, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_goal.setAdapter(goalAdapter);

        // activity spinner
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_array, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_activity.setAdapter(activityAdapter);

        // weight spinner
        ArrayAdapter<Integer> weightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Populate the age spinner with ages from 18 to 100
        for (int i = 30; i <= 250; i++) {
            weightAdapter.add(i);
        }
        spiner_weight.setAdapter(weightAdapter);

        // height spinner
        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Populate the height spinner with heights from 90cm to 250cm
        for (int i = 90; i <= 250; i++) {
            heightAdapter.add(i + " cm");
        }
        spiner_height.setAdapter(heightAdapter);


        // Initialize Button and ImageView
        button=findViewById(R.id.submit);
        imageView=findViewById(R.id.insert_image);
        circleImageView=findViewById(R.id.circleImageView);

        // Set click listener for selecting image from gallery
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iuser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iuser,PICK_IMAGE_REQUEST);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set the message for the ProgressDialog
        progressDialog.setCancelable(false); // Set cancelable to false to prevent dismissing on touch outside

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressDialog
                progressDialog.show();

                // Get the user input values
                String name = text_name.getText().toString();
                String username = text_username.getText().toString();
                String email = text_email.getText().toString();
                String number = text_number.getText().toString();
                String pass = text_pass.getText().toString();
                String add = text_address.getText().toString();

                String gender = spiner_gender.getSelectedItem().toString();
                String age = spiner_age.getSelectedItem().toString();
                String goal = spiner_goal.getSelectedItem().toString();
                String activity = spiner_activity.getSelectedItem().toString();
                String weight = spiner_weight.getSelectedItem().toString();
                String height = spiner_height.getSelectedItem().toString();

                // Add more fields as needed...

                // Create a map to store user data
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", name);
                userData.put("username", username);
                userData.put("email", email);
                userData.put("number", number);
                userData.put("address", add);
                userData.put("gender", gender);
                userData.put("age", age);
                userData.put("goal", goal);
                userData.put("activity", activity);
                userData.put("weight", weight);
                userData.put("height", height);

                // Add the image URI if available
//                if (selectedImageUri != null) {
//                    // Upload image to Firestore storage
//                    uploadImageToFirestore(username, userData);
//                } else {
//                    // If no image selected, only save user data
//                    saveUserDataToFirestore(username, userData);
//                }

                // Save user data to Firestore and create account in Firebase Authentication
                saveUserDataAndCreateAccount(username, email, pass, userData);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Initialize selectedImageUri when an image is selected
            selectedImageUri = data.getData();
            circleImageView.setImageURI(selectedImageUri);
        }
    }
    // Method to upload image to Firestore storage
    private void uploadImageToFirestore(final String username, final Map<String, Object> userData) {
        // Check if an image is selected
        if (selectedImageUri != null) {
            // Get reference to the Firestore storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child("profile_images/" + username + "/" + UUID.randomUUID().toString());

            // Upload the image to Firestore storage
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // If image upload successful, get the download URL
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Add the image download URL to user data
                                    userData.put("profileImageUrl", uri.toString());
                                    // Save user data to Firestore
                                    saveUserDataToFirestore(username, userData);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddMember.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // If no image is selected, save user data without an image URL
            saveUserDataToFirestore(username, userData);
        }
    }

    private void saveUserDataToFirestore(String username, Map<String, Object> userData) {
        // Save the user data to Firestore
        db.collection("users").document(username).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss(); // Dismiss the ProgressDialog

                        Toast.makeText(AddMember.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddMember.this,Fragment_Member_list.class));
                        // Clear the input fields after successful data saving
//                        clearInputFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss(); // Dismiss the ProgressDialog

                        Toast.makeText(AddMember.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to save user data to Firestore and create account in Firebase Authentication
    private void saveUserDataAndCreateAccount(String username, String email, String password, Map<String, Object> userData) {
        // Create the user account in Firebase Authentication
        auth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // User account created successfully
                        // Get the UID of the newly created user
                        String uid = authResult.getUser().getUid();

                        // Upload the image to Firestore storage
                        uploadImageToFirestore(username, userData);
//                        // Save the user data to Firestore
//                        saveUserDataToFirestore(uid, username, userData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss(); // Dismiss the ProgressDialog

                        // Failed to create user account
                        Toast.makeText(AddMember.this, "Failed to create user account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to save user data to Firestore
//    private void saveUserDataToFirestore(String uid, String username, Map<String, Object> userData) {
//        // Add the UID to the user data
////        userData.put("uid", uid);
//
//        // Save the user data to Firestore
//        db.collection("users").document(username).set(userData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        progressDialog.dismiss(); // Dismiss the ProgressDialog
//
//                        Toast.makeText(AddMember.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(AddMember.this,Fragment_Member_list.class));
//                        // Clear the input fields after successful data saving
////                        clearInputFields();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss(); // Dismiss the ProgressDialog
//
//                        Toast.makeText(AddMember.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    public void loadFragment(Fragment fragment, boolean flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag)
            ft.add(R.id.fragment_container,fragment);
        else
            ft.replace(R.id.fragment_container,fragment);
        ft.commit();

    }
}
