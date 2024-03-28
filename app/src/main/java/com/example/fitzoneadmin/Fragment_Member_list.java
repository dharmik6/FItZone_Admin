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
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Member_list extends Fragment {

    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private List<MemberList> memberList;
    private TextView dataNotFoundText;
    private MaterialSearchBar user_searchbar;
    private ProgressDialog progressDialog;


    private List<MemberList> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__member_list, container, false);

        // Initialize dataNotFoundText
        dataNotFoundText = view.findViewById(R.id.data_not_show);

        user_searchbar = view.findViewById(R.id.user_searchbar);

        // Setup MaterialSearchBar
        user_searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                filter(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        recyclerView = view.findViewById(R.id.recyc_members);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredList = new ArrayList<>();

        memberList = new ArrayList<>();
        adapter = new MemberAdapter(getContext(), memberList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMemberData();
    }

    private void loadMemberData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            memberList.clear(); // Clear existing list before adding new data
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String image = documentSnapshot.getString("image");
                String uid = documentSnapshot.getString("uid");
                MemberList member = new MemberList(name, email, image, uid);
                memberList.add(member);
            }
            filteredList.addAll(memberList);
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

    private void updateDataNotFoundVisibility() {
        if (memberList != null && memberList.isEmpty()) {
            dataNotFoundText.setVisibility(View.VISIBLE);
        } else {
            dataNotFoundText.setVisibility(View.GONE);
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
