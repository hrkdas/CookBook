package com.dasshrkcodes.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;


public class main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    Variables
    ImageView menuIcon;
    static final float END_SCALE = 0.7f;

    //Drawer Menu

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Hooks
        menuIcon = findViewById(R.id.menu_icon);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.content);

        navigationDrawer();


    }

    public void go_to_wishlist(View view) {
        Intent intent = new Intent(main_screen.this, wishlist_screen.class);
        startActivity(intent);
    }

    public void show_recipe(View view) {
        Intent intent = new Intent(main_screen.this, recipe_overview.class);
        startActivity(intent);
    }


    //Navigation Drawer Functions
    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


}