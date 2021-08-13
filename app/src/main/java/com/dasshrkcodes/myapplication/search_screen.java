package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class search_screen extends AppCompatActivity {

    Animation searchscreen_anim;
    LinearLayout searchscreen_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        searchscreen_anim = AnimationUtils.loadAnimation(this, R.anim.searchscreen_animation);
        searchscreen_layout=findViewById(R.id.searchscreen_layout);
        searchscreen_layout.setAnimation(searchscreen_anim);

    }
}