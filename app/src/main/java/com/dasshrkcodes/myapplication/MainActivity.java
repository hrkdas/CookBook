package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;
    Animation bottomAnim,topAnim;
    ImageView logoImage;
    TextView logoText,main_appInfo_curren_verion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        logoImage = (ImageView) findViewById(R.id.logoImage);
        logoText = (TextView) findViewById(R.id.logoText);
        logoText.setAnimation(topAnim);
        logoImage.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this, login_screen.class);
                startActivity(intent);
                finish();
            }
        }, (long) SPLASH_SCREEN);

        this.main_appInfo_curren_verion = (TextView) findViewById(R.id.main_appInfo_curren_verion);
        String str = null;
        try {
            str = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.main_appInfo_curren_verion.setText("Version " + str);
    }
}