package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */

public class Fragment_Member_Data_List extends Fragment {

    // Inside FragmentMember class
    private RecyclerView recyclerView;
    private MemberDataAdapter dataAdapter;
    private List<MemberList> memberList;
    ProgressDialog progressDialog;
    SearchView searchbar;
//    private List<MemberDataList> originalMemberDataList;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__member__data__list, container, false);

        // Initialize search bar
//        searchbar = view.findViewById(R.id.searchbar);
// Set a listener on the search bar

        recyclerView = view.findViewById(R.id.data_recyc_members);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        memberList = new ArrayList<>();
//        originalMemberDataList = new ArrayList<>();
        dataAdapter = new MemberDataAdapter(getContext(),memberList);
        recyclerView.setAdapter(dataAdapter);

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
//                originalMemberDataList.add(member); // Add to both lists
            }

            dataAdapter.notifyDataSetChanged();
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



    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.fragment_container, fragment);
        else
            ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}