package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.dasshrkcodes.myapplication.Classes.Recipes;
import com.dasshrkcodes.myapplication.OtherRecyclerViews_adapters.IngredientsRecyclerAdapter;
import com.dasshrkcodes.myapplication.OtherRecyclerViews_adapters.ingredients_click_RecyclerView;
import com.dasshrkcodes.myapplication.RecyclerCard.Liked_click_RecyclerView;
import com.dasshrkcodes.myapplication.RecyclerCard.OnItem_click_RecyclerView;
import com.dasshrkcodes.myapplication.RecyclerCard.RecyclerAdapter;
import com.dasshrkcodes.myapplication.RecyclerCard.UnLiked_click_RecyclerView;
import com.dasshrkcodes.myapplication.recipe_screen_activities.recipe_overview;
import com.dasshrkcodes.myapplication.recipe_screen_activities.start_cooking_screen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class search_screen extends AppCompatActivity implements ingredients_click_RecyclerView, Liked_click_RecyclerView, UnLiked_click_RecyclerView, OnItem_click_RecyclerView {

    Animation searchscreen_anim;
    LinearLayout searchscreen_layout;
    TextView selected_ingList_TextView,location_city_text;
    EditText searchBox;
    LinearLayout searchscreen_selectIng_layout;

    ProgressBar searchscreen_progressbar;
    LottieAnimationView searchscreen_progressbar_animationView,
            searchscreen_noresult_animationView;

    List<String> Ing_nameList = new ArrayList<>();
    List<String> Ing_imageList = new ArrayList<>();
    List<String> Selected_Ing_List = new ArrayList<>();
    List<Recipes> found_recipesList = new ArrayList<Recipes>();
    List<Recipes> search_found_recipesList = new ArrayList<Recipes>();
    Boolean search_by_ingInBtn_click = false;

    RecyclerView ingList_recyclerview, search_recyclerview;
    RecyclerView.Adapter recyclerAdapter, Search_Adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FusedLocationProviderClient fusedLocationProviderClient;
    SwitchMaterial location_toggle_switch;

    private final static int REQUEST_CODE = 100;

    private static final String TAG = "FirestoreSearchActivity";

    public void makeList() {
        Collections.addAll(Ing_nameList, "Bread", "Broccoli", "Carrot", "Cheese", "Chicken", "Chilli", "Corn",
                "Dal", "Egg", "Fish", "Bhindi", "Mushroom", "Noodles", "Onion", "Paneer", "Potato", "Rice", "Prawn",
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
        ingList_recyclerview.setLayoutManager(new GridLayoutManager(search_screen.this, 4));
        recyclerAdapter = new IngredientsRecyclerAdapter(getApplicationContext(), Ing_nameList, Ing_imageList
                , this);
        ingList_recyclerview.setAdapter(recyclerAdapter);
        searchscreen_progressbar = findViewById(R.id.searchscreen_progressbar);
        searchscreen_progressbar_animationView = findViewById(R.id.searchscreen_progressbar_animationView);
        searchscreen_noresult_animationView = findViewById(R.id.searchscreen_noresult_animationView);
        selected_ingList_TextView = findViewById(R.id.selected_ingList_TextView);
        search_recyclerview = findViewById(R.id.search_recyclerview);
        searchscreen_selectIng_layout = findViewById(R.id.searchscreen_selectIng_layout);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(search_screen.this,
                RecyclerView.VERTICAL, false);
        search_recyclerview.setLayoutManager(linearLayoutManager);

        Search_Adapter = new RecyclerAdapter(getApplicationContext(), search_found_recipesList,
                this, this,this);
        search_recyclerview.setAdapter(Search_Adapter);

        searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_found_recipesList.clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_found_recipesList.clear();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search_found_recipesList.clear();

                if (!editable.toString().trim().equals("")) {
                    searchscreen_selectIng_layout.setVisibility(View.GONE);
                    search_found_recipesList.clear();
                    searchscreen_progressbar_animationView.setVisibility(View.VISIBLE);

                    afterTextChangedAddItemsFromJSON(editable);

                } else {
                    searchscreen_progressbar_animationView.setVisibility(View.GONE);
                    View view = search_screen.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        searchscreen_selectIng_layout.setVisibility(View.VISIBLE);
                    }
                    searchBox.clearFocus();

                }

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        location_toggle_switch = findViewById(R.id.location_toggle_switch);
        location_city_text = findViewById(R.id.location_city_text);

        location_toggle_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getLastLocation();

                } else {
                    location_city_text.setText("");
                }
            }
        });

    }




    private void getLastLocation(){

        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED){


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null){



                                try {
                                    Geocoder geocoder = new Geocoder(search_screen.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                                    lattitude.setText("Lattitude: "+addresses.get(0).getLatitude());
//                                    longitude.setText("Longitude: "+addresses.get(0).getLongitude());
//                                    address.setText("Address: "+addresses.get(0).getAddressLine(0));
                                    location_city_text.setText("("+addresses.get(0).getAdminArea()+")");
//                                    country.setText("Country: "+addresses.get(0).getCountryName());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    });


        }else {

            askPermission();


        }


    }

    private void askPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, "android.permission.ACCESS_FINE_LOCATION");
        ActivityCompat.requestPermissions(this,new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                getLastLocation();

            }else {

                Toast.makeText(search_screen.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();

            }



        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }








    private void afterTextChangedAddItemsFromJSON(Editable editable) {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String id = itemObj.getString("id");
                String name = itemObj.getString("name");
                String ingredientsList = itemObj.getString("ingredientsList");
                String totalTime = itemObj.getString("totalTime");
                String cuisine = itemObj.getString("cuisine");
                String instructions = itemObj.getString("instructions");
                String cleanedIngredients = itemObj.getString("cleanedIngredients");
                String imageUrl = itemObj.getString("imageUrl");
                String ingredientCount = itemObj.getString("ingredientCount");
                String rating = itemObj.getString("rating");
                String ratingCount = itemObj.getString("ratingCount");


                Recipes recipes = new Recipes(name, ingredientsList, totalTime, cuisine, instructions,
                        cleanedIngredients, imageUrl, ingredientCount, rating, ratingCount, id);


                if (name.toLowerCase().contains(editable.toString().toLowerCase().trim())) {
                    search_found_recipesList.add(recipes);
                }

                if (editable.toString().trim().equals("")) {
                    search_found_recipesList.clear();
                    break;
                }
            }
            if(search_found_recipesList.size()==0){
                searchscreen_noresult_animationView.setVisibility(View.VISIBLE);
            }else {
                searchscreen_noresult_animationView.setVisibility(View.GONE);
            }
            Search_Adapter.notifyDataSetChanged();
            searchscreen_progressbar_animationView.setVisibility(View.GONE);

        } catch (JSONException | IOException e) {
        }
    }

    private String readJSONDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.recipes);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }

    public void search_recyclerview_refresh(View view) {
    }

    public void clearFocus_search(View view) {
        searchBox.clearFocus();

    }

    public void go_to_recipe_overview(View view) {
        String id = view.getTag().toString();
        Intent intent = new Intent(getApplicationContext(), recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);

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

    private void AddItemsFromJSONcheckItems(String[] cardtype, Boolean search_by_ingInBtn_click) {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject itemObj = jsonArray.getJSONObject(i);

                String id = itemObj.getString("id");
                String name = itemObj.getString("name");
                String ingredientsList = itemObj.getString("ingredientsList");
                String totalTime = itemObj.getString("totalTime");
                String cuisine = itemObj.getString("cuisine");
                String instructions = itemObj.getString("instructions");
                String cleanedIngredients = itemObj.getString("cleanedIngredients");
                String imageUrl = itemObj.getString("imageUrl");
                String ingredientCount = itemObj.getString("ingredientCount");
                String rating = itemObj.getString("rating");
                String ratingCount = itemObj.getString("ratingCount");
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
                List<Recipes> recipesList=new ArrayList<>();
                if(found_recipesList.size()>150) {
                    for (int i = 0; i < 150; i++) {
                        recipesList.add(found_recipesList.get(i));
                    }
                }else {
                    recipesList=found_recipesList;
                }
                intent.putExtra("selected_recipes", (Serializable) recipesList);
                startActivity(intent);
            }
            searchscreen_progressbar.setVisibility(View.INVISIBLE);

        } catch (JSONException | IOException e) {
        }
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


    public void search_by_ingInBtn_clicked(View view) {
        search_by_ingInBtn_click = true;
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        String[] stringArray = Selected_Ing_List.toArray(new String[0]);
        found_recipesList.clear();
        AddItemsFromJSONcheckItems(stringArray, search_by_ingInBtn_click);
    }

    public void search_by_ingExBtn_clicked(View view) {
        search_by_ingInBtn_click = false;
        searchscreen_progressbar.setVisibility(View.VISIBLE);
        String[] stringArray = Selected_Ing_List.toArray(new String[0]);
        found_recipesList.clear();
        AddItemsFromJSONcheckItems(stringArray, search_by_ingInBtn_click);
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