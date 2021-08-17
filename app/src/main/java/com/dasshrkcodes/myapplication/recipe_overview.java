package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class recipe_overview extends AppCompatActivity {

    ImageView recipe_overview_recipe_image;
    ExtendedFloatingActionButton startcooking_button;
    TextView recipe_overview_name, recipe_overview_cuisine, recipe_overview_totalTime, recipe_overview_ingredientCount,
             recipe_overview_ingredientsList, recipe_overview_instructions,recipe_overview_rating_count;
    RatingBar recipe_overview_rating_display, recipe_overview_rating;
    String id_recipe_overview;
    TextView recipe_overview_difficulty;
    ImageView recipe_ov_veg_icon,recipe_ov_nonveg_icon;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id, name, ingredientsList, totalTime, cuisine, instructions, cleanedIngredients,
            imageUrl, ingredientCount, rating, ratingCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recipe_overview_recipe_image = findViewById(R.id.recipe_overview_recipe_image);
        startcooking_button = findViewById(R.id.startcooking_button);

        recipe_overview_name = findViewById(R.id.recipe_overview_name);
        recipe_overview_cuisine = findViewById(R.id.recipe_overview_cuisine);
        recipe_overview_totalTime = findViewById(R.id.recipe_overview_totalTime);
        recipe_overview_ingredientCount = findViewById(R.id.recipe_overview_ingredientCount);
        recipe_overview_ingredientsList = findViewById(R.id.recipe_overview_ingredientsList);
        recipe_overview_instructions = findViewById(R.id.recipe_overview_instructions);
        recipe_overview_rating_count = findViewById(R.id.recipe_overview_rating_count);
        recipe_overview_rating_display = findViewById(R.id.recipe_overview_rating_display);
        recipe_overview_rating = findViewById(R.id.recipe_overview_rating);
        recipe_overview_difficulty = findViewById(R.id.recipe_overview_difficulty);
        recipe_ov_veg_icon = findViewById(R.id.recipe_ov_veg_icon);
        recipe_ov_nonveg_icon = findViewById(R.id.recipe_ov_nonveg_icon);

        id_recipe_overview = getIntent().getStringExtra("id_recipe_overview");
        displayRecipeFromDB();
    }

    private void displayRecipeFromDB() {
        db.collection("Recipes").whereEqualTo("id", Integer.parseInt(id_recipe_overview))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot itemObj : task.getResult()) {

                                id = itemObj.getLong("id").toString();
                                name = itemObj.getString("name");
                                ingredientsList = itemObj.getString("ingredientsList");
                                totalTime = itemObj.getLong("totalTime").toString();
                                cuisine = itemObj.getString("cuisine");
                                instructions = itemObj.getString("instructions");
                                cleanedIngredients = itemObj.getString("cleanedIngredients");
                                imageUrl = itemObj.getString("imageUrl");
                                ingredientCount = itemObj.getLong("ingredientCount").toString();
                                rating = itemObj.getLong("rating").toString();
                                ratingCount = itemObj.getLong("ratingCount").toString();
//
//                                Recipes recipes = new Recipes(name, ingredientsList, totalTime,
//                                        cuisine, instructions, cleanedIngredients, imageUrl,
//                                        ingredientCount, rating, ratingCount,id);

                            }
                            setValues();
                        } else {

                        }
                    }
                });
    }

    public static String split(String s){
        String s1 = s.replace(',', '\n');
        return s1;
    }

    public static String[] toArray(String string){
        String lines[] = string.split("\\r?\\n");
        return lines;
    }

    private void setValues() {

        recipe_overview_name.setText(name);
        recipe_overview_cuisine.setText(cuisine);
        recipe_overview_totalTime.setText(totalTime+"min");
        recipe_overview_ingredientCount.setText(ingredientCount);
        recipe_overview_ingredientsList.setText(split(ingredientsList));
        recipe_overview_instructions.setText(instructions);
        recipe_overview_rating_count.setText(ratingCount);
        if(instructions.length()<750){//easy
            recipe_overview_difficulty.setText("Easy");

        }else if(instructions.length()>1400){//hard
            recipe_overview_difficulty.setText("Hard");
        }else{
            recipe_overview_difficulty.setText("Medium");
        }

        String s = ingredientsList + cleanedIngredients ;
        String[] card1 = {"chicken", "egg", "mutton", "fish", "shrimp", "prawns", "pomphret",
                "surmai", "beef", "goat"};

        if (IsNonveg(s.toLowerCase(), card1)) {
            recipe_ov_nonveg_icon.setVisibility(View.VISIBLE);
            recipe_ov_veg_icon.setVisibility(View.GONE);
        } else {
            recipe_ov_veg_icon.setVisibility(View.VISIBLE);
            recipe_ov_nonveg_icon.setVisibility(View.GONE);
        }


        recipe_overview_rating.setRating(Float.parseFloat(rating));
        recipe_overview_rating_display.setRating(Float.parseFloat(rating));
        Picasso.get().load(imageUrl).into(this.recipe_overview_recipe_image);
    }

    private boolean IsNonveg(String cleanedIngredients, String[] cardtype) {
        boolean result = false;
        int n = cardtype.length;
        for (int i = 0; i < n; i++) {
            result = cleanedIngredients.contains(cardtype[i]);
            if (result) {
                return true;
            }
        }
        return false;
    }

    public void start_cooking_click(View view) {
        Intent intent = new Intent(recipe_overview.this, start_cooking_screen.class);
        String[] myStrings = toArray(instructions);
        intent.putExtra("instructions_StringArray", myStrings);
        startActivity(intent);
    }

    public void play_yt_buttonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/dlw-PG5EFIE"));
        startActivity(intent);
    }
}