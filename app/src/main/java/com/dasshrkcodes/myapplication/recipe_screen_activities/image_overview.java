package com.dasshrkcodes.myapplication.recipe_screen_activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dasshrkcodes.myapplication.R;
import com.squareup.picasso.Picasso;

public class image_overview extends AppCompatActivity {

    ImageView image_overview_recipe_image;
    String image_url_overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_overview);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        image_overview_recipe_image=findViewById(R.id.image_overview_recipe_image);

        image_url_overview = getIntent().getStringExtra("image_url_overview");
        Picasso.get().load(image_url_overview).into(this.image_overview_recipe_image);

    }

    public void go_back_toscreen(View view) {
        finish();
    }
}