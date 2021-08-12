package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class about_screen extends AppCompatActivity {
    Integer help_about_ButtonClickcount = 0;
    Integer help_contact_ButtonClickcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ((TextView) findViewById(R.id.help_page_contactText)).setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void app_info_about_ButtonClick(View view) {
        final ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) findViewById(R.id.help_about_Button);
        final TextView textView = (TextView) findViewById(R.id.help_page_aboutText);
        if (this.help_about_ButtonClickcount.intValue() % 2 == 0) {
            extendedFloatingActionButton.setVisibility(View.VISIBLE);
            extendedFloatingActionButton.animate().translationYBy(200.0f).alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                public void run() {
                    extendedFloatingActionButton.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setTranslationY(-200.0f);
                    textView.setAlpha(0.0f);
                    textView.animate().translationYBy(200.0f).alpha(1.0f).setDuration(500);
                }
            });
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setTranslationY(200.0f);
            textView.setAlpha(1.0f);
            textView.animate().translationYBy(-200.0f).alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                public void run() {
                    textView.setVisibility(View.GONE);
                }
            });
            extendedFloatingActionButton.setVisibility(View.VISIBLE);
            extendedFloatingActionButton.setTranslationY(200.0f);
            extendedFloatingActionButton.setAlpha(0.0f);
            extendedFloatingActionButton.animate().translationYBy(-200.0f).alpha(1.0f).setDuration(500);
        }
        this.help_about_ButtonClickcount = Integer.valueOf(this.help_about_ButtonClickcount.intValue() + 1);
    }

    public void go_to_appInfo(View view) {
        startActivity(new Intent(getApplicationContext(), app_info.class));

    }

    public void help_contact_ButtonClick(View view) {
        final TextView textView = (TextView) findViewById(R.id.help_page_contactText);
        final TextView textView2 = (TextView) findViewById(R.id.help_page_contactText3);
        if (this.help_contact_ButtonClickcount.intValue() % 2 == 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setAlpha(0.0f);
            textView.setTranslationY(-200.0f);
            textView.animate().translationYBy(200.0f).alpha(1.0f).setDuration(500);
            textView2.setVisibility(View.VISIBLE);
            textView2.setAlpha(0.0f);
            textView2.setTranslationY(-200.0f);
            textView2.animate().translationYBy(200.0f).alpha(1.0f).setDuration(500);
        } else {textView.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
        this.help_contact_ButtonClickcount = Integer.valueOf(this.help_contact_ButtonClickcount.intValue() + 1);
    }


    public void go_to_webView(View view) {
//        startActivity(new Intent(getApplicationContext(), help_page_webView.class));
    }

}