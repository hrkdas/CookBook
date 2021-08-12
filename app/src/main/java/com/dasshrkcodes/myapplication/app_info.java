package com.dasshrkcodes.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class app_info extends AppCompatActivity {

    TextView appInfo_curren_verion;
    TextView help_app_info_logoText;
    Integer logo_doodle_clickCount = 7;
    public Vibrator myVib;
    String version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        this.appInfo_curren_verion = (TextView) findViewById(R.id.appInfo_curren_verion);
        this.help_app_info_logoText = (TextView) findViewById(R.id.help_app_info_logoText);
        this.myVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            this.version = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.appInfo_curren_verion.setText("Version " + this.version + "\n");
    }

    public void logo_doodle(View view) {
        final Toast toast;
        if (this.logo_doodle_clickCount.intValue() == 0) {
            Toast.makeText(this, "Voila! you are now a Superstar.", Toast.LENGTH_SHORT).show();
            colorChange();
            myVib.vibrate(300);
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else if (this.logo_doodle_clickCount.intValue() < 0) {
            toast = Toast.makeText(this, "Already a Superstar!", Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(this, "You are now " +
                    this.logo_doodle_clickCount + " steps away from being a Superstar.", Toast.LENGTH_SHORT);
        }
        this.logo_doodle_clickCount = Integer.valueOf(this.logo_doodle_clickCount.intValue() - 1);
        if (!toast.equals("")) {
            toast.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    toast.cancel();
                }
            }, 400);
        }
    }

    public void colorChange() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(2000);
        final int[] iArr = new int[1];
        final float[] fArr = new float[3];
        fArr[1] = 1.0f;
        fArr[2] = 1.0f;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fArr[0] = valueAnimator.getAnimatedFraction() * 360.0f;
                iArr[0] = Color.HSVToColor(fArr);
                app_info.this.help_app_info_logoText.setTextColor(iArr[0]);
            }
        });
        ofFloat.setRepeatCount(0);
        ofFloat.start();
        ofFloat.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                app_info.this.help_app_info_logoText.setTextColor(app_info.this.getResources().getColor(R.color.theme_text_color));
                app_info.this.myVib.vibrate(50);
                app_info.this.myVib.vibrate(50);
            }
        });
    }
}
