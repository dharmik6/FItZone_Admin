package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExercisesList extends AppCompatActivity {
    EditText exe_searchbar;
    LinearLayout add_exe;
    RecyclerView exe_recyc;

    private ExercisesAdapter adapter;
    private List<ExercisesItemList> exercisesItemLists;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);

        exe_recyc = findViewById(R.id.exe_recyc);
        add_exe = findViewById(R.id.add_exe);
        exe_searchbar = findViewById(R.id.exe_searchbar);

        add_exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExercisesList.this,AddExercises.class));
            }
        });

        exe_recyc.setHasFixedSize(true);
        exe_recyc.setLayoutManager(new LinearLayoutManager(this));

        exercisesItemLists = new ArrayList<>(); // Initialize exercisesItemLists

        adapter = new ExercisesAdapter(this, exercisesItemLists); // Use correct adapter
        exe_recyc.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exercises").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String body = documentSnapshot.getString("body");
                String image = documentSnapshot.getString("imageUrl");
                ExercisesItemList exe = new ExercisesItemList(name, body, image);
                exercisesItemLists.add(exe);
            }

            adapter.notifyDataSetChanged();
            // Dismiss ProgressDialog when data is loaded
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            // Handle failures
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
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
