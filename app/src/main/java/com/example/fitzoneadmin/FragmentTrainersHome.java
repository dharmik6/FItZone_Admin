package com.example.fitzoneadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentTrainersHome extends Fragment {
    CardView pendingTrainer ;
    CardView approvedTrainer ;
    CardView rejectedTrainer ;
    CardView trainerBooking ;
    CardView ratingRivew ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainers_home, container, false) ;
        // Inflate the layout for this fragment
        pendingTrainer = view.findViewById(R.id.pendingTrainer);
        approvedTrainer = view.findViewById(R.id.approvTrainer);
        rejectedTrainer = view.findViewById(R.id.rejectedTrainer);
        trainerBooking = view.findViewById(R.id.trainerBooking);
        ratingRivew = view.findViewById(R.id.ratingReview);

        pendingTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             loadFragment(new Fragment_Pending_list(),true);
            }
        });
        approvedTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             loadFragment(new FragmentApprovedList(),true);
            }
        });
        rejectedTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             loadFragment(new FragmentRejectedList(),true);
            }
        });
        trainerBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookingList.class);
                startActivity(intent);
            }
        });
        ratingRivew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Review_List.class);
                startActivity(intent);
            }
        });
        return view;
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