package com.example.fotnewsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;



@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null);
    }
}