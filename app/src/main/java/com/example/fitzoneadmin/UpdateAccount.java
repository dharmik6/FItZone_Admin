package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateAccount extends AppCompatActivity {
    Button update_acc;
    LinearLayout admin_camera;
    EditText admin_up_number,admin_up_add,admin_up_name;
    Spinner admin_up_gender;
    CircleImageView admin_up_image;
    ProgressDialog progressDialog; // Progress dialog for showing upload progress
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        admin_up_gender=findViewById(R.id.admin_up_gender);
        admin_camera=findViewById(R.id.admin_camera);
        admin_up_number=findViewById(R.id.admin_up_number);
        admin_up_add=findViewById(R.id.admin_up_add);
        admin_up_name=findViewById(R.id.admin_up_name);
        update_acc=findViewById(R.id.update_acc);
        admin_up_image=findViewById(R.id.admin_up_image);

        // Initialize FirebaseFirestore and StorageReference
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        String eid = intent.getStringExtra("email");
        Toast.makeText(this, eid, Toast.LENGTH_SHORT).show();
        // goal spinner
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admin_up_gender.setAdapter(goalAdapter);

        update_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = admin_up_name.getText().toString().trim();
                String address = admin_up_add.getText().toString().trim();
                String gender = admin_up_gender.getSelectedItem().toString().trim();
                String number = admin_up_number.getText().toString().trim();

                if (!name.isEmpty()  && !address.isEmpty() && !gender.isEmpty() && !number.isEmpty()) {
                    // Show progress dialog
                    progressDialog = new ProgressDialog(UpdateAccount.this);
                    progressDialog.setMessage("Updating admin data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Create a map to store the data
                    Map<String, Object> adminData = new HashMap<>();
                    adminData.put("name", name);
                    adminData.put("address", address);
                    adminData.put("gender", gender);
                    adminData.put("number", number);

                    // Upload image to Firebase Storage if an image is selected
                    if (selectedImageUri != null) {
                        uploadImageAndSaveData(adminData, eid);
                    } else {
                        // Add the data to Firestore without an image
                        saveDataToFirestore(adminData, eid, null);
                    }
                } else {
                    Toast.makeText(UpdateAccount.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        admin_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iuser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iuser, PICK_IMAGE_REQUEST);
            }
        });
    }
    // Method to handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            admin_up_image.setImageURI(selectedImageUri);
        }
    }
    // Method to upload image to Firebase Storage and save data to Firestore
    private void uploadImageAndSaveData(final Map<String, Object> adminData, final String eid) {
        // Get reference to Firebase Storage and set the path for the image
        StorageReference imageRef = storageRef.child("admin_images/" + eid);

        // Upload the image to Firebase Storage
        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Image download URL obtained, save data to Firestore
                                String imageUrl = uri.toString();
                                saveDataToFirestore(adminData, eid, imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error uploading image
                        progressDialog.dismiss();
                        Toast.makeText(UpdateAccount.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to save data to Firestore
    private void saveDataToFirestore(Map<String, Object> adminData, String eid, String imageUrl) {
        if (imageUrl != null) {
            adminData.put("image", imageUrl); // Add the image URL to the document if it exists
        }

        // Add the admin data to Firestore
        db.collection("admins").document(eid)
                .update(adminData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateAccount.this, "Admin data updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateAccount.this, "Failed to update admin data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}