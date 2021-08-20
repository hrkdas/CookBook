package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.nl.translate.Translator;

public class start_cooking_screen extends AppCompatActivity {

    TextView instructions_text_cooking_screen, stepcount_cooking_screen, Totalstepcount_cooking_screen;
    String[] instructions_StringArray;
    Integer step = 0;
    FloatingActionButton next_page_btn, previous_page_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_start_cooking_screen);


        instructions_text_cooking_screen = findViewById(R.id.instructions_text_cooking_screen);
        stepcount_cooking_screen = findViewById(R.id.stepcount_cooking_screen);
        next_page_btn = findViewById(R.id.next_page_btn);
        previous_page_btn = findViewById(R.id.previous_page_btn);
        Totalstepcount_cooking_screen = findViewById(R.id.Totalstepcount_cooking_screen);

        Intent intent = getIntent();
        instructions_StringArray = intent.getStringArrayExtra("instructions_StringArray");

        previous_page_btn.setVisibility(View.INVISIBLE);
        stepcount_cooking_screen.setText("Steps " + (step + 1) + "");
        instructions_text_cooking_screen.setText(instructions_StringArray[step]);
        Totalstepcount_cooking_screen.setText("/" + instructions_StringArray.length + "");

    }

//    public void translate(String s){
//        Translator.translate(s)
//                .addOnSuccessListener(
//                        new OnSuccessListener() {
//                            @Override
//                            public void onSuccess(Object o) {
//                                String translated_s = o.toString();
////                                recipe_overview_instructions.setText(translated_instructions);
//                            }
//
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Error.
//                                // ...
//                            }
//                        });
//    }

    public void next_page_btnClick(View view) {
        step++;
        if (step == instructions_StringArray.length) {
            next_page_btn.setVisibility(View.INVISIBLE);
        } else {
            next_page_btn.setVisibility(View.VISIBLE);
            instructions_text_cooking_screen.setText(instructions_StringArray[step]);
            stepcount_cooking_screen.setText("Steps " + (step + 1));
        }
        if (step != 0) {
            previous_page_btn.setVisibility(View.VISIBLE);
        } else {
            previous_page_btn.setVisibility(View.INVISIBLE);
        }

        if (step+1 == instructions_StringArray.length) {
            next_page_btn.setVisibility(View.INVISIBLE);
        }

    }

    public void previous_page_btnClick(View view) {
        step--;
        if (step == 0) {
            next_page_btn.setVisibility(View.VISIBLE);
            previous_page_btn.setVisibility(View.INVISIBLE);
            instructions_text_cooking_screen.setText(instructions_StringArray[step]);
            stepcount_cooking_screen.setText("Steps " + (step+1));
        }else {
            instructions_text_cooking_screen.setText(instructions_StringArray[step]);
            stepcount_cooking_screen.setText("Steps " + (step+1));
        }
    }
}