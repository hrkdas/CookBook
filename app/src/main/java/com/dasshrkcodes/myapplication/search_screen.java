package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class search_screen extends AppCompatActivity implements ingredients_click_RecyclerView {

    Animation searchscreen_anim;
    LinearLayout searchscreen_layout;
    TextView selected_ingList_TextView;
    EditText searchBox;

    ProgressBar searchscreen_progressbar;
    List<String> Ing_nameList = new ArrayList<>();
    List<String> Ing_imageList = new ArrayList<>();
    List<String> Selected_Ing_List = new ArrayList<>();
    List<Recipes> found_recipesList = new ArrayList<Recipes>();
    Boolean search_by_ingInBtn_click = false;

    RecyclerView ingList_recyclerview,search_recyclerview;
    RecyclerView.Adapter recyclerAdapter,Search_Adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void makeList() {
        Collections.addAll(Ing_nameList, "Bread", "Broccoli", "Carrot", "Cheese", "Chicken", "Chilli", "Corn",
                "Dal", "Egg", "Fish", "Ladyfinger", "Mushroom", "Noodles", "Onion", "Paneer", "Potato", "Rice", "Shrimp",
                "Tomato");
        Collections.addAll(Ing_imageList, "ing_bread", "ing_broccoli", "ing_carrot", "ing_cheese", "ing_chicken",
                "ing_chilli", "ing_corn", "ing_dal", "ing_eggs", "ing_fish", "ing_ladyfinger", "ing_mushroom", "ing_noodles",
                "ing_onion", "ing_paneer", "ing_potato", "ing_rice", "ing_shrimp", "ing_tomato");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        searchscreen_anim = AnimationUtils.loadAnimation(this, R.anim.searchscreen_animation);
        searchscreen_layout = findViewById(R.id.searchscreen_layout);
        searchscreen_layout.setAnimation(searchscreen_anim);

        makeList();
        ingList_recyclerview = findViewById(R.id.ingList_recyclerview);
        ingList_recyclerview.setLayoutManager(new GridLayoutManager(search_screen.this, 5));
        recyclerAdapter = new IngredientsRecyclerAdapter(getApplicationContext(), Ing_nameList, Ing_imageList
                , this);
        ingList_recyclerview.setAdapter(recyclerAdapter);
        searchscreen_progressbar = findViewById(R.id.searchscreen_progressbar);
        selected_ingList_TextView = findViewById(R.id.selected_ingList_TextView);
        search_recyclerview = findViewById(R.id.search_recyclerview);

        searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//Leave blank do not delete
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//Leave blank do not delete
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "Search box has changed to: " + editable.toString());
                Query query = db.collection("Recipes")
                        .whereEqualTo("name", editable.toString())
                        .orderBy("id");
                FirestoreRecyclerOptions<Recipes> recipes = new FirestoreRecyclerOptions.Builder<Recipes>()
                        .setQuery(query, Recipes.class)
                        .build();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(search_screen.this,
                        RecyclerView.VERTICAL, false);
                Search_Adapter = new search_rAdapter(recipes);
                search_recyclerview.setLayoutManager(linearLayoutManager);
                search_recyclerview.setAdapter(Search_Adapter);

            }
        });
    }


    @Override
    public void onClick(String ing) {
        String value = ing.toLowerCase();
        if (Selected_Ing_List.contains(value)) {
            Selected_Ing_List.remove(value);
        } else
            Selected_Ing_List.add(value);
        final ExtendedFloatingActionButton searchBy_Including_Ingre_button =
                (ExtendedFloatingActionButton) findViewById(R.id.searchBy_Including_Ingre_button);
        final ExtendedFloatingActionButton searchBy_Excluding_Ingre_button =
                (ExtendedFloatingActionButton) findViewById(R.id.searchBy_Excluding_Ingre_button);
        selected_ingList_TextView.setText("");
        String s = "";
        for (int i = 0; i < Selected_Ing_List.size(); i++) {
            s = s + Selected_Ing_List.get(i) + "   ";
        }
        if (s.trim().isEmpty()) {
            selected_ingList_TextView.setText("Select Ingredients");
            searchBy_Excluding_Ingre_button.animate().translationYBy(300).alpha(0).setDuration(400);
            searchBy_Including_Ingre_button.animate().translationYBy(300).alpha(0).setDuration(600).withEndAction(new Runnable() {
                public void run() {
                    searchBy_Including_Ingre_button.setVisibility(View.GONE);
                    searchBy_Excluding_Ingre_button.setVisibility(View.GONE);
                    searchBy_Including_Ingre_button.animate().translationYBy(-300);
                    searchBy_Excluding_Ingre_button.animate().translationYBy(-300);
                }
            });
        } else if (Selected_Ing_List.size() == 1) {
            searchBy_Including_Ingre_button.setVisibility(View.VISIBLE);
            searchBy_Including_Ingre_button.setAlpha(0);
            searchBy_Including_Ingre_button.animate().alpha(1).setDuration(400);

            searchBy_Excluding_Ingre_button.setVisibility(View.VISIBLE);
            searchBy_Excluding_Ingre_button.setAlpha(0);
            searchBy_Excluding_Ingre_button.animate().alpha(1).setDuration(700);
            selected_ingList_TextView.setText(capitalizeString(s));
        } else {
            selected_ingList_TextView.setText(capitalizeString(s));
        }

    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


    public List<Recipes> getSavedObjectFromPreference(Context context, String preferenceFileName
            , String preferenceKey, List<Recipes> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Recipes>>() {
            }.getType();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), collectionType);
        }
        return null;
    }

    private boolean checkIngredients(String cleanedIngredients, String[] cardtype, Boolean search_by_ingInBtn_click) {
        boolean result = false;
        int count = 0;
        int n = cardtype.length;
        for (int i = 0; i < n; i++) {
            if (!search_by_ingInBtn_click)
                result = !cleanedIngredients.contains(cardtype[i]);
            else result = cleanedIngredients.contains(cardtype[i]);

            if (result)
                count = count + 1;
        }
        if (count == n)
            return true;
        else
            return false;

    }

    private void checkItems(String[] cardtype, Boolean search_by_ingInBtn_click) {
        db.collection("Recipes").orderBy("id").limit(100).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot itemObj : task.getResult()) {

                                String id = itemObj.getLong("id").toString();
                                String name = itemObj.getString("name");
                                String ingredientsList = itemObj.getString("ingredientsList");
                                String totalTime = itemObj.getLong("totalTime").toString();
                                String cuisine = itemObj.getString("cuisine");
                                String instructions = itemObj.getString("instructions");
                                String cleanedIngredients = itemObj.getString("cleanedIngredients");
                                String imageUrl = itemObj.getString("imageUrl");
                                String ingredientCount = itemObj.getLong("ingredientCount").toString();
                                String rating = itemObj.getLong("rating").toString();
                                String ratingCount = itemObj.getLong("ratingCount").toString();
                                Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                        cuisine, instructions, cleanedIngredients, imageUrl,
                                        ingredientCount, rating, ratingCount, id);
                                if (checkIngredients(cleanedIngredients, cardtype, search_by_ingInBtn_click)) {
                                    found_recipesList.add(recipes);
                                }

                            }
                            if (found_recipesList.size() == 0) {
                                Toast.makeText(search_screen.this, "No Combination Found", Toast.LENGTH_SHORT).show();
                            } else {
                                Collections.shuffle(found_recipesList);
                                Intent intent = new Intent(search_screen.this, searchby_cardtype.class);
                                intent.putExtra("selected_recipes", (Serializable) found_recipesList);
                                startActivity(intent);
                            }
                            searchscreen_progressbar.setVisibility(View.INVISIBLE);

                        } else {
                            Toast.makeText(search_screen.this, "Failed to Load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void search_by_ingInBtn_clicked(View view) {
        search_by_ingInBtn_click = true;
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        String[] stringArray = Selected_Ing_List.toArray(new String[0]);
        found_recipesList.clear();
        checkItems(stringArray, search_by_ingInBtn_click);
    }

    public void search_by_ingExBtn_clicked(View view) {
        search_by_ingInBtn_click = false;
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        String[] stringArray = Selected_Ing_List.toArray(new String[0]);
        found_recipesList.clear();
        checkItems(stringArray, search_by_ingInBtn_click);
    }


}