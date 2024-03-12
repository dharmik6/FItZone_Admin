package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Pending_list extends Fragment {

    // Inside FragmentMember class
    private RecyclerView recyclerView;
    private TrainersAdapter adapter;
    private List<TrainersList> trainersLists;
    ProgressDialog progressDialog;
    MaterialSearchBar panding_searchbar;
    List<TrainersList> filteredList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__pending_list, container, false);

        // Initialize search bar
        panding_searchbar = view.findViewById(R.id.panding_searchbar);

        // Setup MaterialSearchBar
        panding_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // Handle search state changes
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // Perform search
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                // Handle button clicks
            }
        });

        recyclerView = view.findViewById(R.id.recyc_trainer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredList = new ArrayList<>();

        trainersLists = new ArrayList<>();
        adapter = new TrainersAdapter(getContext(),trainersLists);
        recyclerView.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadTrainers(); // Load trainers initially

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload trainers when the fragment is resumed
        loadTrainers();
    }

    private void loadTrainers() {
        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers")
                .whereEqualTo("is_active", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    trainersLists.clear(); // Clear the existing list
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String tname = documentSnapshot.getString("name");
                        String experience = documentSnapshot.getString("experience");
                        String timage = documentSnapshot.getString("image");
                        String specialization = documentSnapshot.getString("specialization");
                        String review = documentSnapshot.getString("review");
                        TrainersList member = new TrainersList(tname, experience, timage, specialization, review);
                        trainersLists.add(member);
                    }
                    filteredList.addAll(trainersLists); // Initialize filteredList with all members
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
    }

    private void filter(String query) {
        List<TrainersList> filteredList = new ArrayList<>();
        for (TrainersList member : trainersLists) {
            if (member.getTname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }
}
