package com.example.fitzoneadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMembersHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMembersHome extends Fragment {


    TextView text_title;
    public FragmentMembersHome() {
        // Required empty public constructor
    }

    public static FragmentMembersHome newInstance(String param1, String param2) {
        FragmentMembersHome fragment = new FragmentMembersHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members_home, container, false);

        // Assuming you have a button with id "button" in your fragment_members_home layout
        view.findViewById(R.id.active_card_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Fragment_Member_list(), true);
            }
        });
        view.findViewById(R.id.active_card_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Fragment_Member_Data_List(), true);
            }
        });
//        view.findViewById(R.id.active_card_3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new RejectedMemberList(), true);
////                startActivity(new Intent(FragmentMembersHome.this,RejectedMemberList.class));
//            }
//        });

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

        // Set title based on the loaded fragment
//        if (fragment instanceof Fragment_Member_list) {
//            text_title.setText("Members List");
//        }
//        else {
//            // Handle other fragments accordingly
//        }
    }
}