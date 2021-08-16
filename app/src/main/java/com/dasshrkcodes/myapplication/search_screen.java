package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class search_screen extends AppCompatActivity {

    Animation searchscreen_anim;
    LinearLayout searchscreen_layout;

    String[] card1 = {"rice"};
    String[] card2 = {"sugar", "baking"};
    String[] card3 = {"chicken"};
    String[] card4 = {"fish"};
    String[] card5 = {"pizza"};
    String[] card6 = {"sugar"};

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Recipes> selected_recipes = new ArrayList<>();
    ProgressBar searchscreen_progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        searchscreen_anim = AnimationUtils.loadAnimation(this, R.anim.searchscreen_animation);
        searchscreen_layout = findViewById(R.id.searchscreen_layout);
        searchscreen_layout.setAnimation(searchscreen_anim);

        searchscreen_progressbar = findViewById(R.id.searchscreen_progressbar);

    }

    private boolean checkIngredients(String cleanedIngredients, String[] cardtype) {
        boolean result = false;
        int count = 0;
        int n = cardtype.length;
        for (int i = 0; i < n; i++) {
            result = cleanedIngredients.contains(cardtype[i]);
            if (result)
                count = count + 1;
        }
        if (count == n)
            return true;
        else
            return false;

    }


    private void checkItems(String[] cardtype) {
        db.collection("Recipes").orderBy("id").limit(10).get()
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

                                if (checkIngredients(cleanedIngredients, cardtype)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    selected_recipes.add(recipes);

                                }
                            }
//                            Toast.makeText(search_screen.this, selected_recipes.size()+"", Toast.LENGTH_SHORT).show();

                            searchscreen_progressbar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(search_screen.this, searchby_cardtype.class);
                            intent.putExtra("selected_recipes", (Serializable) selected_recipes);
                            startActivity(intent);


                        } else {
                            Toast.makeText(search_screen.this, "Failed to Load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void card1_clicked(View view) {
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        selected_recipes.clear();
        checkItems(card1);
    }

    public void card2_clicked(View view) {
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        selected_recipes.clear();
        checkItems(card2);
    }

    public void card3_clicked(View view) {
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        selected_recipes.clear();
        checkItems(card3);
    }

    public void card4_clicked(View view) {
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        selected_recipes.clear();
        checkItems(card4);
    }

    public void card5_clicked(View view) {
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        selected_recipes.clear();
        checkItems(card5);
    }

    public void card6_clicked(View view) {
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        selected_recipes.clear();
        checkItems(card6);
    }
}