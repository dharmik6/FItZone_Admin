package com.example.fitzoneadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentPackagePaymentsHome extends Fragment {

    CardView packages ;
    CardView payments ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_package_payments_home, container, false);

        packages = view.findViewById(R.id.packages);
        payments = view.findViewById(R.id.payments);

        packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PackagesList.class);
                startActivity(intent);
            }
        });
        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchasesList.class);
                startActivity(intent);
            }
        });
        return view;
    }
}