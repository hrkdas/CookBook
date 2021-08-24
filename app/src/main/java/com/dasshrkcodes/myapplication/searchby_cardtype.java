package com.dasshrkcodes.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dasshrkcodes.myapplication.Classes.Recipes;
import com.dasshrkcodes.myapplication.RecyclerCard.Liked_click_RecyclerView;
import com.dasshrkcodes.myapplication.RecyclerCard.OnItem_click_RecyclerView;
import com.dasshrkcodes.myapplication.RecyclerCard.RecyclerAdapter;
import com.dasshrkcodes.myapplication.RecyclerCard.UnLiked_click_RecyclerView;
import com.dasshrkcodes.myapplication.recipe_screen_activities.recipe_overview;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class searchby_cardtype extends AppCompatActivity implements Liked_click_RecyclerView, UnLiked_click_RecyclerView, OnItem_click_RecyclerView {

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

        recyclerAdapter = new RecyclerAdapter(this, viewItems_10, this,
                this,this);
        bigR_recyclerview_searchbytype.setAdapter(recyclerAdapter);


    }


    public void go_to_recipe_overview(View view) {
        String id = view.getTag().toString();
        Intent intent = new Intent(searchby_cardtype.this, recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);
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


    public void saveLikedRecipeList(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }
    public void saveLikedRecipeFromDB() {

        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(signInAccount.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        final List<String> likedRecipe = (List<String>) documentSnapshot.get("likedRecipe");
                        saveLikedRecipeList(likedRecipe,"likedRecipeListIds");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    public void LikeonClick(Recipes value) {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

        docRef.update("likedRecipe", FieldValue.arrayUnion(value.getId()));
        saveLikedRecipeFromDB();

    }

    @Override
    public void UnLikeonClick(Recipes value) {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

        docRef.update("likedRecipe", FieldValue.arrayRemove(value.getId()));
        saveLikedRecipeFromDB();

    }

    @Override
    public void ItemeOnClick(Recipes value) {
        String id = value.getId();
        Intent intent = new Intent(getApplicationContext(), recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);
    }
}