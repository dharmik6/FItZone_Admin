package com.example.fitzoneadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlansList extends AppCompatActivity {

    MaterialSearchBar plan_searchbar;
    LinearLayout add_exercises;
    RecyclerView plan_recyc;
    private WorkoutPlansAdapter adapter;
    private List<WorkoutPlansItemList> exercisesItemLists;
    private ProgressDialog progressDialog;
    private TextView dataNotFoundText;
    private List<WorkoutPlansItemList> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plans_list);

        dataNotFoundText = findViewById(R.id.data_not_show);
        plan_recyc = findViewById(R.id.plan_recyc);
        plan_searchbar = findViewById(R.id.plan_searchbar);
        add_exercises = findViewById(R.id.add_exercises);

        // Setup MaterialSearchBar
        plan_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // Perform search
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        plan_recyc.setHasFixedSize(true);
        plan_recyc.setLayoutManager(new LinearLayoutManager(this));

        exercisesItemLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new WorkoutPlansAdapter(this, exercisesItemLists);
        plan_recyc.setAdapter(adapter);



        add_exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlansList.this, CreateWorkoutPlan.class));
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
    @Override
    protected void onResume() {
        super.onResume();
        // Load workout plans only when the activity is resumed
        loadWorkoutPlans();
    }


    private void loadWorkoutPlans() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("workout_plans").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<WorkoutPlansItemList> newItems = new ArrayList<>(); // Create a new list for storing the fetched items
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String body = documentSnapshot.getString("goal");
                String image = documentSnapshot.getString("image");
                String id = documentSnapshot.getId();
                WorkoutPlansItemList exe = new WorkoutPlansItemList(name, body, image, id);
                newItems.add(exe); // Add the fetched item to the new list
            }
            exercisesItemLists.clear(); // Clear the existing list
            exercisesItemLists.addAll(newItems); // Update the existing list with the new items
            adapter.notifyDataSetChanged();
            updateDataNotFoundVisibility();
            progressDialog.dismiss(); // Dismiss ProgressDialog when data is loaded
        }).addOnFailureListener(e -> {
            // Handle failures
            progressDialog.dismiss(); // Dismiss ProgressDialog on failure
        });
    }

    private void filter(String query) {
        filteredList.clear();
        for (WorkoutPlansItemList member : exercisesItemLists) {
            if (member.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }

    private void updateDataNotFoundVisibility() {
        if (exercisesItemLists != null && exercisesItemLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
