package com.dasshrkcodes.myapplication.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.dasshrkcodes.myapplication.Classes.Recipes;
import com.dasshrkcodes.myapplication.R;
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
import com.google.common.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link history_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class history_screen extends Fragment implements Liked_click_RecyclerView, OnItem_click_RecyclerView, UnLiked_click_RecyclerView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public history_screen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static history_screen newInstance(String param1, String param2) {
        history_screen fragment = new history_screen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerview_historyscreen;
    private RecyclerView.Adapter recyclerAdapter;
    private List<Recipes> viewItems = new ArrayList<>();
    List<String> historyRecipeListIds;
    GoogleSignInAccount signInAccount;
    FirebaseFirestore db;
    LottieAnimationView historyscreen_noresult_animationView,historyscreen_loading_animationView;
    TextView HistoryEmptyText;
    MaterialCardView clear_history_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myInflatedView = inflater.inflate(R.layout.fragment_history_screen, container, false);


        recyclerview_historyscreen = myInflatedView.findViewById(R.id.recyclerview_historyscreen);
        HistoryEmptyText = myInflatedView.findViewById(R.id.HistoryEmptyText);
        clear_history_btn = myInflatedView.findViewById(R.id.clear_history_btn);
        historyscreen_loading_animationView = myInflatedView.findViewById(R.id.historyscreen_loading_animationView);
        historyscreen_noresult_animationView = myInflatedView.findViewById(R.id.historyscreen_noresult_animationView);

        recyclerAdapter = new RecyclerAdapter(getContext(), viewItems, this,
                this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false);
        recyclerview_historyscreen.setLayoutManager(linearLayoutManager);
        recyclerview_historyscreen.setAdapter(recyclerAdapter);

        db = FirebaseFirestore.getInstance();
        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                printItems();
            }
        }, (long) 1500);


        clear_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                viewItems.clear();
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection("users").document(signInAccount.getEmail());
                docRef.update("history", viewItems);
                recyclerAdapter.notifyDataSetChanged();

                historyscreen_noresult_animationView.setVisibility(View.VISIBLE);
                HistoryEmptyText.setVisibility(View.VISIBLE);
                clear_history_btn.setVisibility(View.GONE);

                final List<String> RecipeList = new ArrayList<>();
                saveLikedRecipeList(RecipeList,"historyRecipeListIds");

            }
        });

        return myInflatedView;
        
    }

    public void saveLikedRecipeList(List<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }
    public List<String> getLikedRecipeList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveLikedRecipeFromDB() {

        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(getContext());

        db.collection("users").document(signInAccount.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        final List<String> likedRecipe = (List<String>) documentSnapshot.get("likedRecipe");
                        final List<String> historyRecipe = (List<String>) documentSnapshot.get("history");
                        saveLikedRecipeList(likedRecipe,"historyRecipeListIds");
                        saveLikedRecipeList(historyRecipe,"historyRecipeListIds");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void printItems() {
        historyRecipeListIds = getLikedRecipeList("historyRecipeListIds");

        if(historyRecipeListIds.size()==0){
            historyscreen_noresult_animationView.setVisibility(View.VISIBLE);
            HistoryEmptyText.setVisibility(View.VISIBLE);
            clear_history_btn.setVisibility(View.GONE);

        }else {
            Collections.reverse(historyRecipeListIds);
            for (int i = 0; i < historyRecipeListIds.size(); i++)
                addRecipeOfCurrentId(Integer.parseInt(historyRecipeListIds.get(i)) - 1);
            recyclerAdapter.notifyDataSetChanged();

            historyscreen_noresult_animationView.setVisibility(View.GONE);
            HistoryEmptyText.setVisibility(View.GONE);
            clear_history_btn.setVisibility(View.VISIBLE);
        }
        historyscreen_loading_animationView.setVisibility(View.GONE);

    }
    private void addRecipeOfCurrentId(Integer currentId) {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            JSONObject itemObj = jsonArray.getJSONObject(currentId);

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

            viewItems.add(recipes);

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


    @Override
    public void LikeonClick(Recipes value) {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

        docRef.update("likedRecipe", FieldValue.arrayUnion(value.getId()));
        saveLikedRecipeFromDB();

    }

    @Override
    public void UnLikeonClick(Recipes value) {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

        docRef.update("likedRecipe", FieldValue.arrayRemove(value.getId()));
        saveLikedRecipeFromDB();

    }

    @Override
    public void ItemeOnClick(Recipes value) {
        String id = value.getId();
        Intent intent = new Intent(getActivity(), recipe_overview.class);
        intent.putExtra("id_recipe_overview", id);
        startActivity(intent);
    }
    

}