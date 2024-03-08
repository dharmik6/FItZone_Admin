package com.example.fitzoneadmin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class RejectedMemberList extends Fragment {

    // Inside FragmentMember class
    private RecyclerView reject_recyc;
    private RejectedMemberAdapter adapter;
    private List<MemberList> memberList;
    ProgressDialog progressDialog;
    MaterialSearchBar rej_mem_searchbar;
    List<MemberList> filteredList;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_rejected_member_list, container, false);

        // Initialize search bar
        rej_mem_searchbar = view.findViewById(R.id.rej_mem_searchbar);

        // Setup MaterialSearchBar
        rej_mem_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
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


        reject_recyc = view.findViewById(R.id.reject_recyc);
        reject_recyc.setHasFixedSize(true);
        reject_recyc.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredList = new ArrayList<>();

        memberList = new ArrayList<>();
        adapter = new RejectedMemberAdapter(getContext(),memberList);
        reject_recyc.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String image = documentSnapshot.getString("image");
                String uid = documentSnapshot.getString("name");
//                memberList.add(new MemberList(name, email,image));
                MemberList member = new MemberList(name, email,image,uid);
                memberList.add(member);
            }
            filteredList.addAll(memberList); // Initialize filteredList with all members
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


        return view; // Return the inflated view
    }
    private void filter(String query) {
        List<MemberList> filteredList = new ArrayList<>();
        for (MemberList member : memberList) {
            if (member.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }
}
