package com.example.fitzoneadmin;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout ;
    NavigationView navigationView;
    TextView text_title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationview);
        text_title = findViewById(R.id.text_title);
        ImageView menu = findViewById(R.id.show_menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_dashbord) {
                    //   loadFragment(new HomeFragment(), false);
                    showToast("Dashbord");
                } else if (itemId == R.id.nav_members) {
                    loadFragment(new FragmentMember(), false);
                } else if (itemId == R.id.nav_Trainers) {
                    //  loadFragment(new SettingsFragment(), false);
                    showToast("Traines");
                } else if (itemId == R.id.nav_workout_exercises) {
                    //  loadFragment(new SettingsFragment(), false);
                    showToast("workout & exercises");
                } else if (itemId == R.id.nav_diet_plans) {
                    //   loadFragment(new SettingsFragment(), false);
                    showToast("diet palns");
                } else if (itemId == R.id.nav_packages_payments) {
                    //  loadFragment(new SettingsFragment(), false);
                    showToast("payments & pakage");
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
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    public void loadFragment(Fragment fragment, boolean flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag)
            ft.add(R.id.fragment_container,fragment);
        else
            ft.replace(R.id.fragment_container,fragment);
        ft.commit();

        // Set title based on the loaded fragment
        if (fragment instanceof FragmentMember) {
            text_title.setText("Members List");
        }
//        else if (fragment instanceof ) {
//            text_title.setText("Some Other Title");
//        }
        else {
            // Handle other fragments accordingly
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}