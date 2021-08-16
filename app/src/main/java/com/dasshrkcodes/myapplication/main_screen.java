package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    Variables
    ImageView menuIcon;
    static final float END_SCALE = 0.7f;

    //Drawer Menu

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;


    private RecyclerView bigR_recyclerview, smallR_recyclerview_1,smallR_recyclerview_2,
            smallR_recyclerview_3,smallR_recyclerview_4,smallR_recyclerview_5,smallR_recyclerview_6;
    private List<Recipes> viewItems = new ArrayList<>();
    private List<Recipes> smallR_recyclerviewItems_1 = new ArrayList<>();
    private List<Recipes> smallR_recyclerviewItems_2 = new ArrayList<>();
    private List<Recipes> smallR_recyclerviewItems_3 = new ArrayList<>();
    private List<Recipes> smallR_recyclerviewItems_4 = new ArrayList<>();
    private List<Recipes> smallR_recyclerviewItems_5 = new ArrayList<>();
    private List<Recipes> smallR_recyclerviewItems_6 = new ArrayList<>();
    String[] card1 = {"rice"};
    String[] card2 = {"sugar", "baking"};
    String[] card3 = {"chicken"};
    String[] card4 = {"fish"};
    String[] card5 = {"bread"};
    String[] card6 = {"sugar"};

    private List<Recipes> viewItemsCopy = new ArrayList<>();
    private List<Recipes> viewItemsEmpty = new ArrayList<>();

    private RecyclerView.Adapter recyclerAdapter, horizontalRecyclerAdapter_1,horizontalRecyclerAdapter_2,
            horizontalRecyclerAdapter_3,horizontalRecyclerAdapter_4,horizontalRecyclerAdapter_5,horizontalRecyclerAdapter_6;
    private RecyclerView.LayoutManager layoutManager;
    ProgressBar mainscreen_progressbar;



    private DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Hooks
        menuIcon = findViewById(R.id.menu_icon);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.content);
        mainscreen_progressbar = findViewById(R.id.mainscreen_progressbar);

        navigationDrawer();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (signInAccount != null) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);

        } else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        }

        //big_Recycler View
        bigR_recyclerview = (RecyclerView) findViewById(R.id.bigR_recyclerview);
        bigR_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        bigR_recyclerview.setLayoutManager(layoutManager);


        horizontalRecyclerAdapter_1 = new HorizontalRecyclerAdapter(this, smallR_recyclerviewItems_1);
        horizontalRecyclerAdapter_2 = new HorizontalRecyclerAdapter(this, smallR_recyclerviewItems_2);
        horizontalRecyclerAdapter_3 = new HorizontalRecyclerAdapter(this, smallR_recyclerviewItems_3);
        horizontalRecyclerAdapter_4 = new HorizontalRecyclerAdapter(this, smallR_recyclerviewItems_4);
        horizontalRecyclerAdapter_5 = new HorizontalRecyclerAdapter(this, smallR_recyclerviewItems_5);
        horizontalRecyclerAdapter_6 = new HorizontalRecyclerAdapter(this, smallR_recyclerviewItems_6);
        //Small recyclerviews
        smallR_recyclerview_1 = (RecyclerView) findViewById(R.id.smallR_recyclerview_1);
        smallR_recyclerview_1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview_1.setLayoutManager(linearLayoutManager);
        smallR_recyclerview_1.setAdapter(horizontalRecyclerAdapter_1);

        smallR_recyclerview_2 = (RecyclerView) findViewById(R.id.smallR_recyclerview_2);
        smallR_recyclerview_2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview_2.setLayoutManager(linearLayoutManager_2);
        smallR_recyclerview_2.setAdapter(horizontalRecyclerAdapter_2);

        smallR_recyclerview_3 = (RecyclerView) findViewById(R.id.smallR_recyclerview_3);
        smallR_recyclerview_3.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview_3.setLayoutManager(linearLayoutManager_3);
        smallR_recyclerview_3.setAdapter(horizontalRecyclerAdapter_3);

        smallR_recyclerview_4 = (RecyclerView) findViewById(R.id.smallR_recyclerview_4);
        smallR_recyclerview_4.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_4 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview_4.setLayoutManager(linearLayoutManager_4);
        smallR_recyclerview_4.setAdapter(horizontalRecyclerAdapter_4);

        smallR_recyclerview_5 = (RecyclerView) findViewById(R.id.smallR_recyclerview_5);
        smallR_recyclerview_5.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_5 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview_5.setLayoutManager(linearLayoutManager_5);
        smallR_recyclerview_5.setAdapter(horizontalRecyclerAdapter_5);

        smallR_recyclerview_6 = (RecyclerView) findViewById(R.id.smallR_recyclerview_6);
        smallR_recyclerview_6.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_6 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview_6.setLayoutManager(linearLayoutManager_6);
        smallR_recyclerview_6.setAdapter(horizontalRecyclerAdapter_6);

        recyclerAdapter = new RecyclerAdapter(this, viewItems);
        bigR_recyclerview.setAdapter(recyclerAdapter);

        mainscreen_progressbar.setVisibility(View.VISIBLE);
        addItemsFromDB();
        checkItems();

        mPrefs = getPreferences(MODE_PRIVATE);
//        viewItemsEmpty.clear();
//        saveObjectToSharedPreference(getApplicationContext(),"LikedRecipeList",
//                "LikedRecipeList",viewItemsEmpty);
//        viewItemsCopy=getSavedObjectFromPreference(getApplicationContext(),"LikedRecipeList",
//                "LikedRecipeList",viewItems);

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

    private void checkItems() {
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

                                if (checkIngredients(cleanedIngredients, card1)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    smallR_recyclerviewItems_1.add(recipes);
                                }
                                if (checkIngredients(cleanedIngredients, card2)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    smallR_recyclerviewItems_2.add(recipes);
                                }
                                if (checkIngredients(cleanedIngredients, card3)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    smallR_recyclerviewItems_3.add(recipes);
                                }
                                if (checkIngredients(cleanedIngredients, card4)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    smallR_recyclerviewItems_4.add(recipes);
                                }
                                if (checkIngredients(cleanedIngredients, card5)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    smallR_recyclerviewItems_5.add(recipes);
                                }
                                if (checkIngredients(cleanedIngredients, card6)) {
                                    Recipes recipes = new Recipes(name, ingredientsList, totalTime,
                                            cuisine, instructions, cleanedIngredients, imageUrl,
                                            ingredientCount, rating, ratingCount, id);
                                    smallR_recyclerviewItems_6.add(recipes);
                                }

                            }
                            Collections.shuffle(smallR_recyclerviewItems_1);
                            Collections.shuffle(smallR_recyclerviewItems_2);
                            Collections.shuffle(smallR_recyclerviewItems_3);
                            Collections.shuffle(smallR_recyclerviewItems_4);
                            Collections.shuffle(smallR_recyclerviewItems_5);
                            Collections.shuffle(smallR_recyclerviewItems_6);
                            horizontalRecyclerAdapter_1.notifyDataSetChanged();
                            horizontalRecyclerAdapter_2.notifyDataSetChanged();
                            horizontalRecyclerAdapter_3.notifyDataSetChanged();
                            horizontalRecyclerAdapter_4.notifyDataSetChanged();
                            horizontalRecyclerAdapter_5.notifyDataSetChanged();
                            horizontalRecyclerAdapter_6.notifyDataSetChanged();
//                            Toast.makeText(search_screen.this, selected_recipes.size()+"", Toast.LENGTH_SHORT).show();
                            mainscreen_progressbar.setVisibility(View.INVISIBLE);


                        } else {
                            Toast.makeText(main_screen.this, "Failed to Load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void card_1_clicked(View view) {
        Intent intent = new Intent(main_screen.this, searchby_cardtype.class);
        intent.putExtra("selected_recipes", (Serializable) smallR_recyclerviewItems_1);
        startActivity(intent);
    }
    public void card_2_clicked(View view) {
        Intent intent = new Intent(main_screen.this, searchby_cardtype.class);
        intent.putExtra("selected_recipes", (Serializable) smallR_recyclerviewItems_2);
        startActivity(intent);
    }
    public void card_3_clicked(View view) {
        Intent intent = new Intent(main_screen.this, searchby_cardtype.class);
        intent.putExtra("selected_recipes", (Serializable) smallR_recyclerviewItems_3);
        startActivity(intent);
    }
    public void card_4_clicked(View view) {
        Intent intent = new Intent(main_screen.this, searchby_cardtype.class);
        intent.putExtra("selected_recipes", (Serializable) smallR_recyclerviewItems_4);
        startActivity(intent);
    }
    public void card_5_clicked(View view) {
        Intent intent = new Intent(main_screen.this, searchby_cardtype.class);
        intent.putExtra("selected_recipes", (Serializable) smallR_recyclerviewItems_5);
        startActivity(intent);
    }
    public void card_6_clicked(View view) {
        Intent intent = new Intent(main_screen.this, searchby_cardtype.class);
        intent.putExtra("selected_recipes", (Serializable) smallR_recyclerviewItems_6);
        startActivity(intent);
    }




    public static void saveObjectToSharedPreference(Context context, String preferenceFileName,
                                                    String serializedObjectKey, List<Recipes> object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public List<Recipes> getSavedObjectFromPreference(Context context, String preferenceFileName
            , String preferenceKey, List<Recipes> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Recipes>>(){}.getType();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""),collectionType);
        }
        return null;
    }





    private void addItemsFromDB() {
        db.collection("Recipes").orderBy("id").limit(5).get()
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
                                        ingredientCount, rating, ratingCount,id);
                                viewItems.add(recipes);
                            }
                            Collections.shuffle(viewItems);
                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                           Toast.makeText(main_screen.this, "Failed to Load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void go_to_wishlist(View view) {
        Intent intent = new Intent(main_screen.this, profiles_n_wishlist_page.class);
        intent.putExtra("profile_selected", true);
        startActivity(intent);
    }

    public void go_to_recipe_overview(View view) {
        String id= view.getTag().toString();
        Intent intent = new Intent(getApplicationContext(), recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);
//        Toast.makeText(this, getSavedObjectFromPreference(getApplicationContext(),"LikedRecipeList",
//                "LikedRecipeList",viewItems).size()+"", Toast.LENGTH_SHORT).show();

    }



    public void go_to_searchscreen(View view) {
        Intent intent = new Intent(main_screen.this, search_screen.class);
        startActivity(intent);
    }


    //Navigation Drawer Functions
    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.wishlist:
                Intent intent = new Intent(main_screen.this, profiles_n_wishlist_page.class);
                startActivity(intent);
                break;

            case R.id.nav_profile:
                Intent intent_profile = new Intent(main_screen.this, profiles_n_wishlist_page.class);
                intent_profile.putExtra("profile_selected", true);
                startActivity(intent_profile);
                break;

            case R.id.nav_login:
                Intent intent_login = new Intent(getApplicationContext(), login_screen.class);
                startActivity(intent_login);
                finish();
                break;
            case R.id.nav_logout: {

                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                googleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();

                Intent intent_logout = new Intent(getApplicationContext(), login_screen.class);
                startActivity(intent_logout);
                finish();
                break;
            }

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_rate:
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}