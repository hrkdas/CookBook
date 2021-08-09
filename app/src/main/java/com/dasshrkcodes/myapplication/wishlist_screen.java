package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class wishlist_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    public void go_to_mainscreen(View view) {
        Intent intent = new Intent(wishlist_screen.this, main_screen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}