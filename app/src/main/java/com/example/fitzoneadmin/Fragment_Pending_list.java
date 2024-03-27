package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Pending_list extends Fragment {

    private RecyclerView recyclerView;
    private TrainersAdapter adapter;
    private List<TrainersList> trainersLists;
    private TextView dataNotFoundText;

    private ProgressDialog progressDialog;
    private MaterialSearchBar pending_searchbar;
    private List<TrainersList> filteredList;
    private ListenerRegistration listenerRegistration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__pending_list, container, false);

        dataNotFoundText = view.findViewById(R.id.data_not_show);
        pending_searchbar = view.findViewById(R.id.pending_searchbar);

        pending_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
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
        adapter = new TrainersAdapter(getContext(), trainersLists);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadTrainers();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload trainers when the fragment is resumed
        loadTrainers();
    }

    private void loadTrainers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listenerRegistration = db.collection("trainers")
                .whereEqualTo("is_active", false)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    trainersLists.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String tname = documentSnapshot.getString("name");
                        String experience = documentSnapshot.getString("experience");
                        String timage = documentSnapshot.getString("image");
                        String specialization = documentSnapshot.getString("specialization");
                        String review = documentSnapshot.getString("review");
                        TrainersList member = new TrainersList(tname, experience, timage, specialization, review);
                        trainersLists.add(member);
                    }

                    filteredList.addAll(trainersLists);
                    adapter.notifyDataSetChanged();
                    updateDataNotFoundVisibility();

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

    private void updateDataNotFoundVisibility() {
        if (trainersLists != null && trainersLists.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
    public void onBackPressed() {
        // Get the fragment manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Check if there are fragments in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the last fragment transaction from the back stack
            fragmentManager.popBackStack();
        } else {
            // If no fragments in the back stack, perform default back action (exit the app)
            super.getActivity().onBackPressed();
        }
    }
}
