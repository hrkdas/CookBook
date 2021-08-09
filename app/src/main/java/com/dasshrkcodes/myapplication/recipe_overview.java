package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

public class recipe_overview extends AppCompatActivity {

    ImageView recipe_image;
    ExtendedFloatingActionButton startcooking_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recipe_image=findViewById(R.id.recipe_image);
        startcooking_button=findViewById(R.id.startcooking_button);

        Picasso.get().load("https://images.immediate.co.uk/production/volatile/sites/30/2020/08/butter-chicken-cf6f9e2.jpg?quality=90&webp=true&resize=440,400").into(this.recipe_image);
    }

    public void start_cooking_click(View view) {
        Intent intent = new Intent(recipe_overview.this, start_cooking_screen.class);
        startActivity(intent);
    }

    public void play_yt_buttonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/dlw-PG5EFIE"));
        startActivity(intent);
    }
}