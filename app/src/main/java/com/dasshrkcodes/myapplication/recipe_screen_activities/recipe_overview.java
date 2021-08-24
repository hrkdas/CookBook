package com.dasshrkcodes.myapplication.recipe_screen_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dasshrkcodes.myapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class recipe_overview extends AppCompatActivity {

    ImageView recipe_overview_recipe_image;
    ExtendedFloatingActionButton startcooking_button;
    TextView recipe_overview_name, recipe_overview_cuisine, recipe_overview_totalTime, recipe_overview_ingredientCount,
            recipe_overview_ingredientsList, recipe_overview_instructions, recipe_overview_rating_count;
    RatingBar recipe_overview_rating_display, recipe_overview_rating;
    String id_recipe_overview;
    TextView recipe_overview_difficulty;
    ImageView recipe_ov_veg_icon, recipe_ov_nonveg_icon;
    LikeButton recipe_overview_likebutton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id, name, ingredientsList, totalTime, cuisine, instructions, cleanedIngredients,
            imageUrl, ingredientCount, rating, ratingCount;
    Spinner recipe_overview_lang_spinner;
    Translator selectedTranslator;
    FloatingActionButton google_translate_btn;
    String translated_ingredientsList, translated_instructions,selectedLang;
    List<String> likedRecipe_test=new ArrayList<>();

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
        google_translate_btn = findViewById(R.id.google_translate_btn);
        recipe_overview_likebutton = findViewById(R.id.recipe_overview_likebutton);

        id_recipe_overview = getIntent().getStringExtra("id_recipe_overview");
        displayRecipeFromDB();

        selectedLang= PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("selecteLang","English" );
        recipe_overview_lang_spinner = findViewById(R.id.recipe_overview_lang_spinner);
        String[] items = new String[]{"English", "Hindi", "Marathi", "Gujarati", "Bengali", "Telugu", "Malayalam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        recipe_overview_lang_spinner.setAdapter(adapter);
        for(int i=0;i<items.length;i++){
            if(items[i].toUpperCase().equals(selectedLang.toUpperCase())){
                recipe_overview_lang_spinner.setSelection(i);
            }
        }
        recipe_overview_lang_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {

                    case 1://Hindi
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","Hindi" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.HINDI)
                                .build());
                        break;
                    case 2://Marathi
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","Marathi" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.MARATHI)
                                .build());
                        break;
                    case 3://Gujarati
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","Gujarati" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.GUJARATI)
                                .build());
                        break;
                    case 4://Bengali
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","Bengali" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.BENGALI)
                                .build());
                        break;
                    case 5://Telugu
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","Telugu" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.TELUGU)
                                .build());
                        break;
                    case 6://Malayalam
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","Malayalam" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.MALAY)
                                .build());
                        break;
                    default://English
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                .putString("selecteLang","English" ).apply();
                        google_translate_btn.setVisibility(View.VISIBLE);
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.ENGLISH)
                                .build());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        likedRecipe_test = getLikedRecipeList("likedRecipeListIds");

        if(likedRecipe_test.contains(id_recipe_overview)){
            recipe_overview_likebutton.setLiked(true);
        }else{
            recipe_overview_likebutton.setLiked(false);
        }

        recipe_overview_likebutton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

                docRef.update("likedRecipe", FieldValue.arrayUnion(id_recipe_overview));
                saveLikedRecipeFromDB();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

                docRef.update("likedRecipe", FieldValue.arrayRemove(id_recipe_overview));
                saveLikedRecipeFromDB();
            }
        });


    }

    public List<String> getLikedRecipeList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(json, type);
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


    private void displayRecipeFromDB() {
        db.collection("Recipes").whereEqualTo("id", Integer.parseInt(id_recipe_overview))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    public static String split(String s) {
        String s1 = s.replace(',', '\n');
        return s1;
    }

    public static String[] toArray(String string) {
        String lines[] = string.split("\\r?\\n");
        return lines;
    }

    private void setValues() {

        recipe_overview_name.setText(name);
        recipe_overview_cuisine.setText(cuisine);
        recipe_overview_totalTime.setText(totalTime + "min");
        recipe_overview_ingredientCount.setText(ingredientCount);
        recipe_overview_ingredientsList.setText(split(ingredientsList));
        recipe_overview_instructions.setText(instructions);
        recipe_overview_rating_count.setText(ratingCount);
        if (instructions.length() < 750) {//easy
            recipe_overview_difficulty.setText("Easy");

        } else if (instructions.length() > 1400) {//hard
            recipe_overview_difficulty.setText("Hard");
        } else {
            recipe_overview_difficulty.setText("Medium");
        }

        String s = ingredientsList + cleanedIngredients;
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

        intent.putExtra("selectedLanguage",  PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("selecteLang","English" ));
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
        translate();
    }

    public void translate() {
        final Translator Translator = selectedTranslator;
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        Translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {

                            @Override
                            public void onSuccess(Object o) {

//                                Toast.makeText(recipe_overview.this, "Language will be Downloaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                            }
                        });

        Translator.translate(instructions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                translated_instructions = o.toString();
                                recipe_overview_instructions.setText(translated_instructions);
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

        Translator.translate(ingredientsList)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                translated_ingredientsList = o.toString();
                                recipe_overview_ingredientsList.setText(split(translated_ingredientsList));
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