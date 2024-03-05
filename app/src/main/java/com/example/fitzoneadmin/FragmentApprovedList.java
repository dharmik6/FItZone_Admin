package com.example.fitzoneadmin;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentApprovedList#} factory method to
 * create an instance of this fragment.
 */
public class FragmentApprovedList extends Fragment {

    // Inside FragmentMember class
    private RecyclerView recyclerView;
    private ApprovedAdapter adapter;
    private List<TrainersList> trainersLists;
    ProgressDialog progressDialog;
    SearchView searchbar;
//    private List<MemberList> originalMemberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approved_list, container, false);

        // Initialize search bar
//        searchbar = view.findViewById(R.id.searchbar);
// Set a listener on the search bar

        recyclerView = view.findViewById(R.id.approved_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        trainersLists = new ArrayList<>();
//        originalMemberList = new ArrayList<>();
        adapter = new ApprovedAdapter(getContext(),trainersLists);
        recyclerView.setAdapter(adapter);

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
}