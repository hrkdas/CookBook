package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.dasshrkcodes.myapplication.Classes.user;
import com.dasshrkcodes.myapplication.OtherRecyclerViews_adapters.CuisinesRecyclerAdapter;
import com.dasshrkcodes.myapplication.OtherRecyclerViews_adapters.ingredients_click_RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class login_screen extends AppCompatActivity implements ingredients_click_RecyclerView {

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    ProgressBar login_progressbar,select_cuisine_progressbar;
    RecyclerView cuisineList_recyclerview;
    RecyclerView.Adapter recyclerAdapter;
    List<String> Cuisine_nameList = new ArrayList<>();
    List<String> Cuisine_imageList = new ArrayList<>();
    ExtendedFloatingActionButton selecte_cuisine_done_Btn;
    Spinner login_screen_lang_spinner;
    Translator selectedTranslator;
    String selectedLanguage;
    List<String> cuisines = new ArrayList<>();
    Boolean isSetupComplete = false;
    MaterialCardView signUp_card, selectLang_card, select_cuisine_card;
    FirebaseUser currentUser;

    public void makeList() {
        Collections.addAll(Cuisine_nameList, "Bengali", "Punjabi", "Maharashtrian","Gujarati",
                "South Indian", "Hyderabadi", "Goan", "Chinese", "Himachali", "Bihari", "North Eastern", "Continental");
        Collections.addAll(Cuisine_imageList, "cuisine_bengali", "cuisine_punjabi", "cuisine_maharashtrian",
                "cuisine_gujarati","cuisine_south_indian", "cuisine_hyderabadi", "cuisine_goan", "cuisine_chinese",
                "cuisine_himachali", "cuisine_bihari", "cuisine_north_eastern", "cuisine_continental");
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && isSetupComplete == true) {
            go_to_mainscreen();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        login_progressbar = findViewById(R.id.login_progressbar);
        select_cuisine_progressbar = findViewById(R.id.select_cuisine_progressbar);
        selecte_cuisine_done_Btn = findViewById(R.id.selecte_cuisine_done_Btn);
        signUp_card = findViewById(R.id.signUp_card);
        selectLang_card = findViewById(R.id.selectLang_card);
        select_cuisine_card = findViewById(R.id.select_cuisine_card);
        login_progressbar.setVisibility(View.INVISIBLE);
        select_cuisine_progressbar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        createRequest();
        isSetupComplete = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("isSetupComplete", false);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null && isSetupComplete == false) {
            signUp_card.setVisibility(View.GONE);
            selectLang_card.setVisibility(View.VISIBLE);
            select_cuisine_card.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_progressbar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_mainscreen();

            }
        });

        //cuisines
        makeList();
        cuisineList_recyclerview = findViewById(R.id.cuisineList_recyclerview);
        cuisineList_recyclerview.setLayoutManager(new GridLayoutManager(login_screen.this, 3));
        recyclerAdapter = new CuisinesRecyclerAdapter(getApplicationContext(), Cuisine_nameList,
                Cuisine_imageList, this);
        cuisineList_recyclerview.setAdapter(recyclerAdapter);

        //set languages
        login_screen_lang_spinner = findViewById(R.id.login_screen_lang_spinner);
        String[] items = new String[]{"English", "Hindi", "Marathi", "Gujarati", "Bengali", "Telugu", "Malayalam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        login_screen_lang_spinner.setAdapter(adapter);
        login_screen_lang_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {

                    case 1://Hindi
                        selectedLanguage = parent.getItemAtPosition(position).toString();
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.HINDI)
                                .build());
                        break;
                    case 2://Marathi
                        selectedLanguage = parent.getItemAtPosition(position).toString();
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.MARATHI)
                                .build());
                        break;
                    case 3://Gujarati
                        selectedLanguage = parent.getItemAtPosition(position).toString();
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.GUJARATI)
                                .build());
                        break;
                    case 4://Bengali
                        selectedLanguage = parent.getItemAtPosition(position).toString();
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.BENGALI)
                                .build());
                        break;
                    case 5://Telugu
                        selectedLanguage = parent.getItemAtPosition(position).toString();
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.TELUGU)
                                .build());
                        break;
                    case 6://Malayalam
                        selectedLanguage = parent.getItemAtPosition(position).toString();
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.MALAY)
                                .build());
                        break;
                    default://English
                        selectedLanguage = "English";
                        selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.ENGLISH)
                                .build());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void go_to_mainscreen() {
        Intent intent = new Intent(getApplicationContext(), main_screen.class);
        startActivity(intent);
        finish();
    }


    //GoogleSignIn

    private void createRequest() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
//
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .whereEqualTo("email", (Object) user.getEmail()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        public void onComplete(Task<QuerySnapshot> task) {
                                            if (!task.getResult().isEmpty()) {
                                                go_to_mainscreen();
                                                Toast.makeText(login_screen.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
//                                                Toast.makeText(login_screen.this, "isPresent", Toast.LENGTH_SHORT).show();
                                            } else {

                                                signUp_card.setVisibility(View.GONE);
                                                selectLang_card.setVisibility(View.VISIBLE);
                                                select_cuisine_card.setVisibility(View.VISIBLE);
                                                Toast.makeText(getApplicationContext(), "Signed In!", Toast.LENGTH_SHORT).show();
                                                login_progressbar.setVisibility(View.INVISIBLE);
//                                                Toast.makeText(login_screen.this, "isAbsent", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });


//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            updateUI(null);
                            Toast.makeText(login_screen.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                            login_progressbar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }


    public void downloadLang() {
        final Translator Translator = selectedTranslator;
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        Translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {

                            @Override
                            public void onSuccess(Object o) {

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
    }

    @Override
    public void onClick(String value) {
        if (cuisines.contains(value)) {
            cuisines.remove(value);
        } else {
            cuisines.add(value);
        }
    }

    public void done_BtnClicked(View view) {
        select_cuisine_progressbar.setVisibility(View.VISIBLE);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        String username, email;
        username = signInAccount.getDisplayName();
        email = signInAccount.getEmail();
        downloadLang();

        List<String> c = new ArrayList<>();
        Map<String,String> userRatings=new HashMap<>();
        user u = new user(username, email, cuisines,
                c, c, c, selectedLanguage,userRatings);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(email)
                .set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                select_cuisine_progressbar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getApplicationContext(), main_screen.class);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("isSetupComplete", true).apply();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putString("selecteLang",selectedLanguage ).apply();
                Toast.makeText(login_screen.this, "Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

}