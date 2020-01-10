package com.example.cgmarketplace.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.cgmarketplace.R;

public class SplashActivity extends AppCompatActivity {

    private int waktu_loading=2000;

    Animation app_splash;
    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // load animation
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);

        //load element
        img_splash =findViewById(R.id.img_splash);

        //run animation
        img_splash.startAnimation(app_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(home);
                finish();
                overridePendingTransition(0,0);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        },waktu_loading);
    }
    }
