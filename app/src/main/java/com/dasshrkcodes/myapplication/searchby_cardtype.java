package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class searchby_cardtype extends AppCompatActivity {

    private RecyclerView bigR_recyclerview_searchbytype;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private List<Recipes> viewItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchby_cardtype);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bigR_recyclerview_searchbytype = (RecyclerView) findViewById(R.id.bigR_recyclerview_searchbytype);
        bigR_recyclerview_searchbytype.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        bigR_recyclerview_searchbytype.setLayoutManager(layoutManager);

        Intent i = getIntent();
        viewItems = (List<Recipes>) i.getSerializableExtra("selected_recipes");


        recyclerAdapter = new RecyclerAdapter(this, viewItems);
        bigR_recyclerview_searchbytype.setAdapter(recyclerAdapter);
    }

    public void go_to_recipe_overview(View view) {
        String id = view.getTag().toString();
        Intent intent = new Intent(searchby_cardtype.this, recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);
    }
}