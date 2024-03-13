package com.example.fitzoneadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FragmentDietList extends Fragment {
    RecyclerView diet_recyc;
    private DietAdapter adapter;
    private List<DietList> dietLists;
    LinearLayout add_diet;
    MaterialSearchBar diet_searchbar;
    List<DietList> filteredList;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet_list, container, false);

        diet_searchbar = view.findViewById(R.id.diet_searchbar);

        // Setup MaterialSearchBar
        diet_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        diet_recyc = view.findViewById(R.id.diet_recyc);
        add_diet = view.findViewById(R.id.add_diet);
        diet_recyc.setHasFixedSize(true);
        diet_recyc.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredList = new ArrayList<>();

        dietLists = new ArrayList<>();
        adapter = new DietAdapter(getContext(), dietLists);
        diet_recyc.setAdapter(adapter);
        add_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddDiet.class));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDietData();
    }

    private void loadDietData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading diets...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dietLists.clear(); // Clear the previous list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("diets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String image = documentSnapshot.getString("imageUrl");
                DietList diet = new DietList(name, description, image);
                dietLists.add(diet);
            }
            filteredList.addAll(dietLists);
            adapter.notifyDataSetChanged(); // Notify adapter about data changes
            progressDialog.dismiss(); // Dismiss the progress dialog when data is loaded
        }).addOnFailureListener(e -> {
            // Handle failure
            progressDialog.dismiss(); // Dismiss the progress dialog on failure as well
        });
    }

    private void filter(String query) {
        List<DietList> filteredList = new ArrayList<>();
        for (DietList member : dietLists) {
            if (member.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }
}