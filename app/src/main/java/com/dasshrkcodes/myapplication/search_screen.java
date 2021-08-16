package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class search_screen extends AppCompatActivity {

    Animation searchscreen_anim;
    LinearLayout searchscreen_layout;

    ProgressBar searchscreen_progressbar;
    List<String> Ing_nameList=new ArrayList<>();
    List<String> Ing_imageList=new ArrayList<>();

     RecyclerView ingList_recyclerview;
     RecyclerView.Adapter recyclerAdapter;


    public void makeList(){
        Collections.addAll(Ing_nameList, "Bread","Broccoli");
        Collections.addAll(Ing_imageList, "ing_bread","ing_broccoli");
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
        ingList_recyclerview.setLayoutManager(new GridLayoutManager(search_screen.this, 5));
        recyclerAdapter = new IngredientsRecyclerAdapter(getApplicationContext(),Ing_nameList,Ing_imageList);
        ingList_recyclerview.setAdapter(recyclerAdapter);
        searchscreen_progressbar = findViewById(R.id.searchscreen_progressbar);

    }


}