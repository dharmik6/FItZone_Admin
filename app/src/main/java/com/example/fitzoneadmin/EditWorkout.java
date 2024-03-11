package com.example.fitzoneadmin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditWorkout extends AppCompatActivity {

    CircleImageView img_wor_plan_image;
    AppCompatTextView img_wor_plan_name, total_exe;
    LinearLayout add_wor_pan;
    Button add_wor_plan_but;
    RecyclerView wor_plan_recyc;
    ProgressDialog progressDialog;
    private EditWorkoutAdapter adapter;
    private List<ExercisesItemList> exercisesItemLists;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        img_wor_plan_image = findViewById(R.id.img_wor_plan_image);
        img_wor_plan_name = findViewById(R.id.img_wor_plan_name);
        total_exe = findViewById(R.id.total_exe);
        wor_plan_recyc = findViewById(R.id.wor_plan_recyc);

        add_wor_pan = findViewById(R.id.add_wor_pan);
        add_wor_plan_but = findViewById(R.id.add_wor_plan_but);

        wor_plan_recyc.setHasFixedSize(true);
        wor_plan_recyc.setLayoutManager(new LinearLayoutManager(this));

        exercisesItemLists = new ArrayList<>();
        adapter = new EditWorkoutAdapter(this, exercisesItemLists);
        wor_plan_recyc.setAdapter(adapter);

        Intent intent = getIntent();
        String eid = intent.getStringExtra("id");
        String did = intent.getStringExtra("name");
//        String edd = intent.getStringExtra("image");


//        Log.d("id" , id);

        // Set the received data to the EditText fields
        img_wor_plan_name.setText(did);
//        // Set the received data to the ImageView
//        if (!TextUtils.isEmpty(edd)) {
//            try {
//                // Decode the base64 string to a Bitmap
//                byte[] decodedString = Base64.decode(edd, Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//                // Set the decoded Bitmap to the ImageView
//                img_wor_plan_image.setImageBitmap(decodedByte);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                // Handle the case where the base64 string is invalid
//            }
//        }
        // Retrieve the clicked exercise item from the intent
//        Intent intent = getIntent();
//        ExercisesItemList clickedExercise = intent.getParcelableExtra("name1");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (eid != null) {
            db.collection("workout_plans").document(did).set(new HashMap<String, Object>(), SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            // Now you can fetch the document and retrieve its data
                            db.collection("exercises").document(eid).get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String name = documentSnapshot.getString("name");
                                    String body = documentSnapshot.getString("body");
                                    String image = documentSnapshot.getString("imageUrl");
                                    String sid = documentSnapshot.getId();
                                    ExercisesItemList exe = new ExercisesItemList(name, body, image, sid);
                                    exercisesItemLists.add(exe);

                                    adapter.notifyDataSetChanged();
                                    updateTotalExercises(); // Update total exercises count
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    // Handle the case where the document does not exist
                                }
                            }).addOnFailureListener(e -> {
                                // Handle the failure
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        } else {
            progressDialog.dismiss();
            // Display a toast message indicating that Firestore is not available
            Toast.makeText(this, "Firestore is not available", Toast.LENGTH_SHORT).show();
        }




        add_wor_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = img_wor_plan_name.getText().toString(); // Retrieve the text from plan_name TextView
                Intent intent = new Intent(EditWorkout.this, WorkoutExercisesList.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        add_wor_plan_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    // Method to update total exercises count
    private void updateTotalExercises() {
        total_exe.setText("Total Exercises: " + exercisesItemLists.size());
    }
}
