package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class searchby_cardtype extends AppCompatActivity implements Liked_click_RecyclerView {

    private RecyclerView bigR_recyclerview_searchbytype;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private List<Recipes> viewItems = new ArrayList<>();
    private List<Recipes> viewItems_10 = new ArrayList<>();
    MaterialCardView view_more_btn_search;


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

        view_more_btn_search = findViewById(R.id.view_more_btn_search);

        Intent intent = getIntent();
        viewItems = (List<Recipes>) intent.getSerializableExtra("selected_recipes");

        for (int j = 0; j < 10; j++) {
            if (viewItems.size() > j)
                viewItems_10.add(viewItems.get(j));
            else {
                view_more_btn_search.setVisibility(View.INVISIBLE);
            }
        }

        recyclerAdapter = new RecyclerAdapter(this, viewItems_10, this);
        bigR_recyclerview_searchbytype.setAdapter(recyclerAdapter);


    }


    public void go_to_recipe_overview(View view) {
        String id = view.getTag().toString();
        Intent intent = new Intent(searchby_cardtype.this, recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);
    }

    @Override
    public void onClick(Recipes value) {

    }

    Integer n = 10;

    public void view_more_recipes(View view) {
        for (int i = n; i < n + 10; i++) {
            if (viewItems.size() > i)
                viewItems_10.add(viewItems.get(i));
            else {
                view_more_btn_search.setVisibility(View.INVISIBLE);
            }
        }
        n = n + 10;
        recyclerAdapter.notifyDataSetChanged();

    }
}