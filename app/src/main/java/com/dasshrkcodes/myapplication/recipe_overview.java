package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
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
    Spinner recipe_overview_lang_spinner;
    Translator selectedTranslator;
    DownloadConditions conditions;


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

        recipe_overview_lang_spinner = findViewById(R.id.recipe_overview_lang_spinner);
        String[] items = new String[]{"English","Hindi", "Marathi", "Gujarati","Bengali", "Telugu", "Malayalam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        recipe_overview_lang_spinner.setAdapter(adapter);
//        recipe_overview_lang_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                switch (position) {
//                    case 0://English
////                        recipe_overview_instructions.setText(instructions);
//                        break;
//                    case 1://Hindi
//                        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
//                                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                                .setTargetLanguage(TranslateLanguage.HINDI)
//                                .build());
//                        break;
//                    case 2://Marathi
//                        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
//                                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                                .setTargetLanguage(TranslateLanguage.MARATHI)
//                                .build());
//                        break;
//                    case 3://Gujarati
//                        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
//                                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                                .setTargetLanguage(TranslateLanguage.GUJARATI)
//                                .build());
//                        break;
//                    case 4://Bengali
//                        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
//                                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                                .setTargetLanguage(TranslateLanguage.BENGALI)
//                                .build());
//                        break;
//                    case 5://Telugu
//                        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
//                                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                                .setTargetLanguage(TranslateLanguage.TELUGU)
//                                .build());
//                        break;
//                    case 6://Malayalam
//                        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
//                                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                                .setTargetLanguage(TranslateLanguage.MALAY)
//                                .build());
//                        break;
//
//                }
//
//
//                 conditions = new DownloadConditions.Builder()
//                        .requireWifi()
//                        .build();
//                selectedTranslator.downloadModelIfNeeded(conditions)
//                        .addOnSuccessListener(
//                                new OnSuccessListener() {
//
//                                    @Override
//                                    public void onSuccess(Object o) {
//                                Toast.makeText(recipe_overview.this, "Language Downloading...", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                        .addOnFailureListener(
//                                new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Model couldn’t be downloaded or other internal error.
//                                        // ...
//                                    }
//                                });
//
//
//            }
//        });


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
    public static String doublenextLine(String s){
        String s1 = s.replace('\n', '\n');
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

    public void go_to_imageOverview(View view) {

        Intent intent = new Intent(getApplicationContext(), image_overview.class);
        intent.putExtra("image_url_overview", imageUrl);
        startActivity(intent);
    }

    public void translate_buttonClick(View view) {

        selectedTranslator= Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.HINDI)
                                .build());
        final Translator Translator=selectedTranslator;


        Translator.translate(instructions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                String translated_instructions=o.toString();
                                recipe_overview_instructions.setText(translated_instructions);
                                Translator.close();
                            }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                // ...
                            }
                        });

    }
}