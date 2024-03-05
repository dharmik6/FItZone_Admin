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

import java.util.ArrayList;
import java.util.List;

public class FragmentDietList extends Fragment {
    RelativeLayout rl_add_diet;
    RecyclerView diet_recyc;
    EditText diet_search;
    private DietAdapter adapter;
    private List<DietList> dietLists;
    private List<DietList> filteredDietLists; // List to hold filtered diet items
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet_list, container, false);

        rl_add_diet = view.findViewById(R.id.rl_add_diet);
        rl_add_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDiet.class);
                startActivity(intent);
            }
        });

        diet_recyc = view.findViewById(R.id.diet_recyc);
        diet_recyc.setHasFixedSize(true);
        diet_recyc.setLayoutManager(new LinearLayoutManager(getContext()));

        dietLists = new ArrayList<>();
        filteredDietLists = new ArrayList<>(); // Initialize filtered list

        adapter = new DietAdapter(getContext(), dietLists);
        diet_recyc.setAdapter(adapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("diets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String image = documentSnapshot.getString("imageUrl");
                DietList diet = new DietList(name, description, image);
                dietLists.add(diet);
                filteredDietLists.add(diet); // Add to filtered list as well
            }

            adapter.notifyDataSetChanged();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });

        // Search functionality
        diet_search = view.findViewById(R.id.diet_searchbar);
        diet_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return view;
    }

    // Filter method to filter the diet lists based on search query
    private void filter(String text) {
        filteredDietLists.clear();

        for (DietList diet : dietLists) {
            if (diet.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredDietLists.add(diet);
            }
        }

        adapter.filterList(filteredDietLists); // Update the adapter with filtered list
    }

}
