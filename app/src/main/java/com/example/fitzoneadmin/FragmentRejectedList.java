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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentApprovedList#} factory method to
 * create an instance of this fragment.
 */
public class FragmentRejectedList extends Fragment {

    // Inside FragmentMember class
    private RecyclerView rec_rejected;
    private RejectedAdapter adapter;
    private List<TrainersList> trainersLists;
    ProgressDialog progressDialog;
    MaterialSearchBar rej_searchbar;
    List<TrainersList> filteredList;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rejected_list, container, false);

        // Initialize search bar
        rej_searchbar = view.findViewById(R.id.rej_searchbar);

        // Setup MaterialSearchBar
        rej_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
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
        rec_rejected = view.findViewById(R.id.rec_rejected);
        rec_rejected.setHasFixedSize(true);
        rec_rejected.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredList = new ArrayList<>();
        trainersLists = new ArrayList<>();
        adapter = new RejectedAdapter(getContext(),trainersLists);
        rec_rejected.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String tname = documentSnapshot.getString("name");
                String experience = documentSnapshot.getString("experience");
                String timage = documentSnapshot.getString("image");
                String specialization = documentSnapshot.getString("specialization");
                String review = documentSnapshot.getString("review");
//                memberList.add(new MemberList(name, email,image));
                TrainersList member = new TrainersList(tname, experience,timage,specialization,review);
                trainersLists.add(member);
//                originalMemberList.add(member); // Add to both lists
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
        return view;
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