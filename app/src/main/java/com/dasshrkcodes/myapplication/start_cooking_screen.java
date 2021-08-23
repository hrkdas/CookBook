package com.dasshrkcodes.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Locale;

public class start_cooking_screen extends AppCompatActivity {

    TextView instructions_text_cooking_screen, stepcount_cooking_screen, Totalstepcount_cooking_screen;
    String[] instructions_StringArray;
    Integer step = 0, position = 0;
    FloatingActionButton next_page_btn, previous_page_btn;
    Translator selectedTranslator;
    String selectedLanguage;
    SwitchMaterial speaker_toggle_switch;
    ImageView speaker_on_icon, speaker_off_icon;
    TextToSpeech tts;
    Boolean SpeakerisChecked = false;
    LinearLayout Speaker_layout;


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
        speaker_on_icon = findViewById(R.id.speaker_on_icon);
        speaker_off_icon = findViewById(R.id.speaker_off_icon);
        Speaker_layout = findViewById(R.id.Speaker_layout);

        Intent intent = getIntent();
        instructions_StringArray = intent.getStringArrayExtra("instructions_StringArray");
        selectedLanguage = intent.getStringExtra("selectedLanguage").toUpperCase();


        switch (selectedLanguage) {
            case "HINDI"://Hindi
                Speaker_layout.setVisibility(View.GONE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.HINDI)
                        .build());
                break;
            case "MARATHI"://Marathi
                Speaker_layout.setVisibility(View.GONE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.MARATHI)
                        .build());
                break;
            case "GUJARATI"://Gujarati
                Speaker_layout.setVisibility(View.GONE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.GUJARATI)
                        .build());
                break;
            case "BENGALI"://Bengali
                Speaker_layout.setVisibility(View.GONE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.BENGALI)
                        .build());
                break;
            case "TELUGU"://Telugu
                Speaker_layout.setVisibility(View.GONE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.TELUGU)
                        .build());
                break;
            case "MALAYALAM"://Malayalam
                Speaker_layout.setVisibility(View.GONE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.MALAY)
                        .build());
                break;
            default://English
                Speaker_layout.setVisibility(View.VISIBLE);
                selectedTranslator = Translation.getClient(new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build());
                break;
        }


        previous_page_btn.setVisibility(View.INVISIBLE);
        stepcount_cooking_screen.setText("Steps " + (step + 1) + "");
        Totalstepcount_cooking_screen.setText("/" + instructions_StringArray.length + "");
        translateAndSetText();


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });
        tts.setLanguage(Locale.ENGLISH);
        speaker_toggle_switch = (SwitchMaterial) findViewById(R.id.speaker_toggle_switch);
        speaker_toggle_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpeakerisChecked = true;
                    speaker_off_icon.setVisibility(View.GONE);
                    speaker_on_icon.setVisibility(View.VISIBLE);
                    tts.speak(instructions_StringArray[step], TextToSpeech.QUEUE_FLUSH, null);

                    Toast.makeText(start_cooking_screen.this, "Speaker On", Toast.LENGTH_SHORT).show();
                } else {
                    tts.stop();
                    SpeakerisChecked = false;
                    speaker_off_icon.setVisibility(View.VISIBLE);
                    speaker_on_icon.setVisibility(View.GONE);

                }
            }
        });

    }

    public void translateAndSetText() {

        final Translator Translator = selectedTranslator;
        Translator.translate(instructions_StringArray[step])
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                instructions_text_cooking_screen.setText(o.toString());
                                if (SpeakerisChecked)
                                    tts.speak(o.toString(), TextToSpeech.QUEUE_FLUSH, null);
                            }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                            }
                        });


    }

    public void next_page_btnClick(View view) {
        step++;
        if (step == instructions_StringArray.length) {
            next_page_btn.setVisibility(View.INVISIBLE);
        } else {
            next_page_btn.setVisibility(View.VISIBLE);
            translateAndSetText();
            stepcount_cooking_screen.setText("Steps " + (step + 1));
        }
        if (step != 0) {
            previous_page_btn.setVisibility(View.VISIBLE);
        } else {
            previous_page_btn.setVisibility(View.INVISIBLE);
        }

        if (step + 1 == instructions_StringArray.length) {
            next_page_btn.setVisibility(View.INVISIBLE);
        }

    }

    public void previous_page_btnClick(View view) {
        step--;
        if (step == 0) {
            next_page_btn.setVisibility(View.VISIBLE);
            previous_page_btn.setVisibility(View.INVISIBLE);
            translateAndSetText();
            stepcount_cooking_screen.setText("Steps " + (step + 1));
        } else {
            translateAndSetText();
            stepcount_cooking_screen.setText("Steps " + (step + 1));
        }
    }
}