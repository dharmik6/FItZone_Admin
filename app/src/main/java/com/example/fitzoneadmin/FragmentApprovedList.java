package com.example.fitzoneadmin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FragmentApprovedList extends Fragment {

    private RecyclerView recyclerView;
    private ApprovedAdapter adapter;
    private List<TrainersList> trainersLists;
    private TextView dataNotFoundText;

    private ProgressDialog progressDialog;
    private MaterialSearchBar app_searchbar;
    private List<TrainersList> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_approved_list, container, false);

        dataNotFoundText = view.findViewById(R.id.data_not_show);
        app_searchbar = view.findViewById(R.id.app_searchbar);
        recyclerView = view.findViewById(R.id.approved_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredList = new ArrayList<>();

        trainersLists = new ArrayList<>();
        adapter = new ApprovedAdapter(getContext(), trainersLists);
        recyclerView.setAdapter(adapter);

        app_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers")
                .whereEqualTo("is_active", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Clear the lists before adding new data
                    trainersLists.clear();
                    filteredList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String tname = documentSnapshot.getString("name");
                        String experience = documentSnapshot.getString("experience");
                        String timage = documentSnapshot.getString("image");
                        String specialization = documentSnapshot.getString("specialization");
                        String review = documentSnapshot.getString("review");
                        TrainersList trainer = new TrainersList(tname, experience, timage, specialization, review);
                        trainersLists.add(trainer);
                        if (app_searchbar != null && app_searchbar.getText() != null
                                && trainer.getTname().toLowerCase().contains(app_searchbar.getText().toLowerCase())) {
                            filteredList.add(trainer);
                        }
                    }
                    // Update the adapter after fetching new data
                    adapter.notifyDataSetChanged();
                    updateDataNotFoundVisibility();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                });
        return view;
    }

    private void filter(String query) {
        filteredList.clear();
        for (TrainersList trainer : trainersLists) {
            if (trainer.getTname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(trainer);
            }
        }
        adapter.filterList(filteredList);
    }

    private void updateDataNotFoundVisibility() {
        if (trainersLists != null && trainersLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }
}
