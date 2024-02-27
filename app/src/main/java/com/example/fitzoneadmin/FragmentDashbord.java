package com.example.fitzoneadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Member;

public class FragmentDashbord extends Fragment {
    CardView member ;
    CardView trainer ;
    CardView workout ;
    CardView diet ;
    CardView packages ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashbord, container, false) ;
        // Inflate the layout for this fragment
        member = view.findViewById(R.id.members);
        trainer = view.findViewById(R.id.trainer);
        workout = view.findViewById(R.id.workout);
        diet = view.findViewById(R.id.diet);
        packages = view.findViewById(R.id.packages_payments);



        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FragmentMembersHome(), true);
            }
        });
        trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FragmentTrainersHome(), true);
            }
        });
        workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FragmentWorkoutExercisesHome(), true);
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FragmentDietList(), true);
            }
        });
        packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FragmentPackagePaymentsHome(), true);
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
        ft.addToBackStack(null); // Add fragment transaction to back stack
        ft.commit();
    }

    // Override onBackPressed in the parent Activity
   
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