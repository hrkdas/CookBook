package com.dasshrkcodes.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    Variables
    ImageView menuIcon;
    static final float END_SCALE = 0.7f;

    //Drawer Menu

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    TextView test;


    private RecyclerView bigR_recyclerview, smallR_recyclerview;
    private List<Recipes> viewItems = new ArrayList<>();

    private RecyclerView.Adapter recyclerAdapter, horizontalRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


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
        test = findViewById(R.id.test);

        navigationDrawer();


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        updateUI(currentUser);

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

        smallR_recyclerview = (RecyclerView) findViewById(R.id.smallR_recyclerview);
        smallR_recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        smallR_recyclerview.setLayoutManager(linearLayoutManager);


        recyclerAdapter = new RecyclerAdapter(this, viewItems);
        bigR_recyclerview.setAdapter(recyclerAdapter);
        horizontalRecyclerAdapter = new HorizontalRecyclerAdapter(this, viewItems);
        smallR_recyclerview.setAdapter(horizontalRecyclerAdapter);

        addItemsFromDB();


    }


    private void addItemsFromDB() {
        db.collection("Recipes").whereEqualTo("cuisine","North Indian Recipes").limit(50)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot itemObj : task.getResult()) {
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
                                        ingredientCount, rating, ratingCount);
                                viewItems.add(recipes);

                            }
                            recyclerAdapter.notifyDataSetChanged();
                            horizontalRecyclerAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
    }


    public void go_to_wishlist(View view) {
        Intent intent = new Intent(main_screen.this, profiles_n_wishlist_page.class);
        intent.putExtra("profile_selected", true);
        startActivity(intent);
    }

    public void show_recipe(View view) {
        Intent intent = new Intent(main_screen.this, recipe_overview.class);
        startActivity(intent);
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