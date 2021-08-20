package com.dasshrkcodes.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dasshrkcodes.myapplication.HorizontalRecyclerAdapter;
import com.dasshrkcodes.myapplication.R;
import com.dasshrkcodes.myapplication.Recipes;
import com.dasshrkcodes.myapplication.RecyclerAdapter;
import com.dasshrkcodes.myapplication.recipe_overview;
import com.dasshrkcodes.myapplication.searchby_cardtype;
import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link wishlist_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wishlist_screen extends Fragment {

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

    private RecyclerView smallR_recyclerview_wishlistScreen;
    private RecyclerView.Adapter recyclerAdapter;
    private List<Recipes> viewItems = new ArrayList<>();
    MaterialCardView small_card;

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

        smallR_recyclerview_wishlistScreen = (RecyclerView) myInflatedView.findViewById(R.id.smallR_recyclerview_wishlistScreen);
        smallR_recyclerview_wishlistScreen.setHasFixedSize(true);
        smallR_recyclerview_wishlistScreen.setLayoutManager(new GridLayoutManager(getContext(), 2));


        recyclerAdapter = new HorizontalRecyclerAdapter(getContext(), viewItems);
        smallR_recyclerview_wishlistScreen.setAdapter(recyclerAdapter);
//        small_card =  myInflatedView.findViewById(R.id.small_card);
//        small_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String id = v.getTag().toString();
//                Intent intent = new Intent(getContext(), recipe_overview.class);
//                intent.putExtra("id_recipe_overview", id);
//                startActivity(intent);
//            }
//        });



        return myInflatedView;
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
}