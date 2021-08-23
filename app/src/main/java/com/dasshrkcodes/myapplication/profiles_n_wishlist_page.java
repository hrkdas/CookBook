package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.fragment.app.Fragment;


import com.dasshrkcodes.myapplication.fragments.history_screen;
import com.dasshrkcodes.myapplication.fragments.profile_screen;
import com.dasshrkcodes.myapplication.fragments.wishlist_screen;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class profiles_n_wishlist_page extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    Boolean profile_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_n_wishlist);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        profile_selected = getIntent().getBooleanExtra("profile_selected",false);

        if (profile_selected) {
            chipNavigationBar.setItemSelected(R.id.bottom_nav_profile, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile_screen()).commit();

        } else {
            chipNavigationBar.setItemSelected(R.id.bottom_nav_wishlist, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new wishlist_screen()).commit();

        }
        bottomMenu();
    }


    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_wishlist:
                        fragment = new wishlist_screen();
                        break;

                    case R.id.bottom_nav_history:
                        fragment = new history_screen();
                        break;

                    case R.id.bottom_nav_profile:
                        fragment = new profile_screen();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });


    }


}