package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet_list, container, false);

        diet_recyc = view.findViewById(R.id.diet_recyc);
        diet_recyc.setHasFixedSize(true);
        diet_recyc.setLayoutManager(new LinearLayoutManager(getContext()));

        dietLists = new ArrayList<>();
        adapter = new DietAdapter(getContext(), dietLists);
        diet_recyc.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDietData();
    }

    private void loadDietData() {
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
            adapter.notifyDataSetChanged(); // Notify adapter about data changes
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }
}