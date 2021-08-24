package com.dasshrkcodes.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dasshrkcodes.myapplication.HorizontalRecyclerAdapter;
import com.dasshrkcodes.myapplication.Liked_click_RecyclerView;
import com.dasshrkcodes.myapplication.R;
import com.dasshrkcodes.myapplication.Recipes;
import com.dasshrkcodes.myapplication.RecyclerAdapter;
import com.dasshrkcodes.myapplication.UnLiked_click_RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link wishlist_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wishlist_screen extends Fragment implements Liked_click_RecyclerView, UnLiked_click_RecyclerView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public wishlist_screen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment wishlist_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static wishlist_screen newInstance(String param1, String param2) {
        wishlist_screen fragment = new wishlist_screen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerview_wishlistScreen;
    private RecyclerView.Adapter recyclerAdapter;
    private List<Recipes> viewItems = new ArrayList<>();
    List<String> likedRecipeListIds;
    GoogleSignInAccount signInAccount;
    FirebaseFirestore db;

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
        View myInflatedView = inflater.inflate(R.layout.fragment_wishlist_screen, container,false);


        recyclerview_wishlistScreen=myInflatedView.findViewById(R.id.recyclerview_wishlistScreen);

        recyclerAdapter = new RecyclerAdapter(getContext(), viewItems, this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false);
        recyclerview_wishlistScreen.setLayoutManager(linearLayoutManager);
        recyclerview_wishlistScreen.setAdapter(recyclerAdapter);

        db = FirebaseFirestore.getInstance();
        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
//        printItems();

        return myInflatedView;
    }

    public void printItems(){

        db.collection("users").document(signInAccount.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        final List<String> likedRecipe= (List<String>) documentSnapshot.get("likedRecipe");
                        likedRecipeListIds=likedRecipe;
                        Toast.makeText(getContext(), likedRecipeListIds.size()+"", Toast.LENGTH_SHORT).show();

                        db.collection("Recipes").whereEqualTo("id", likedRecipeListIds.get(0)).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                            viewItems.add(recipes);
                                        }
                                        recyclerAdapter.notifyDataSetChanged();

                                    }
                                });

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

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

        docRef.update("likedRecipe", FieldValue.arrayUnion(value.getId()));

    }

    @Override
    public void UnLikeonClick(Recipes value) {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(signInAccount.getEmail());

        docRef.update("likedRecipe", FieldValue.arrayRemove(value.getId()));

    }
}