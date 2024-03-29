package com.example.fitzoneadmin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout ;
    NavigationView navigationView;
    TextView text_title;
    ImageView settings , notification ;

    ImageView navImg;
    TextView navName,navEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationview);
        text_title = findViewById(R.id.text_title);
        ImageView menu = findViewById(R.id.show_menu);
        notification = findViewById(R.id.notification);
        settings = findViewById(R.id.settings);


        View headerView = navigationView.getHeaderView(0);

        navImg = headerView.findViewById(R.id.nav_imag);
        navName = headerView.findViewById(R.id.nav_name);
        navEmail = headerView.findViewById(R.id.nav_email);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Now you can use the userId as needed
            Log.d(TAG, "Current user ID: " + userId);
        } else {
            // Handle the case where the user is not signed in
            Log.d(TAG, "No user is currently signed in");
        }


        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d(TAG, "onCreate: userId "+userId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("admins").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String aname = documentSnapshot.getString("name");
                    String aemail = documentSnapshot.getString("email");
                    String aimg = documentSnapshot.getString("image");

                    Log.d(TAG, "onCreate: aname "+aname+"aemail"+aemail);
                    navName.setText(aname != null ? aname : "No name");
                    navEmail.setText(aemail != null ? aemail : "No name");
                    if (aimg != null) {
                        Glide.with(MainActivity.this)
                                .load(aimg)
                                .into(navImg);
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Notification.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this , Settings.class);
                finish();
            }
        });

        // Initialize Firebase
//        FirebaseApp.initializeApp(this);

// for notification page
        ImageView notification = findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Notification.class);
            }
        });
        // for settings page
        ImageView setting = findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               redirectActivity(MainActivity.this, Settings.class);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        // for defulte fragment show
        loadFragment(new FragmentDashbord(), true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_dashbord) {
                       loadFragment(new FragmentDashbord(), false);

                } else if (itemId == R.id.nav_members) {
                    loadFragment(new FragmentMembersHome(), false);
                } else if (itemId == R.id.nav_Trainers) {
                      loadFragment(new FragmentTrainersHome(), false);

                } else if (itemId == R.id.nav_workout_exercises) {
                      loadFragment(new FragmentWorkoutExercisesHome(), false);

                } else if (itemId == R.id.nav_diet_plans) {
                       loadFragment(new FragmentDietList(), false);

                } else if (itemId == R.id.nav_packages_payments) {
                      loadFragment(new FragmentPackagePaymentsHome(), false);

                }
                closeDrawer(drawerLayout);

                return true;
            }
        });




    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If drawer is not open, handle back press to switch to the last fragment
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag)
            ft.add(R.id.fragment_container,fragment);
        else
            ft.replace(R.id.fragment_container,fragment);

        // Add the transaction to the back stack
        ft.addToBackStack(null);

        ft.commit();
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}