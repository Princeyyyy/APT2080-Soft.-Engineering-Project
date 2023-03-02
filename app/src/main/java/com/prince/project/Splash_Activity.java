package com.prince.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Activity extends AppCompatActivity {

    private static int SPLASH = 3000;
    Animation animation;

    private TextView appName;
    private ImageView appImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        appName = findViewById(R.id.appName);
        appImage = findViewById(R.id.appImage);

        animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        appImage.setAnimation(animation);
        appName.setAnimation(animation);

        new Handler().postDelayed(() -> {
            Intent intent;
            intent = new Intent(Splash_Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH);
    }
}