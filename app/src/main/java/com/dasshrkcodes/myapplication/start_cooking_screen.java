package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class start_cooking_screen extends AppCompatActivity {

    TextView instructions_text_cooking_screen,stepcount_cooking_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_start_cooking_screen);


        instructions_text_cooking_screen = findViewById(R.id.instructions_text_cooking_screen);
        stepcount_cooking_screen = findViewById(R.id.stepcount_cooking_screen);
    }

    public void next_page_btnClick(View view) {
    }
}