package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.ptithcm.moviebooking.utils.TokenManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500; // 1.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check token after splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkAuthenticationAndNavigate();
        }, SPLASH_DELAY);
    }

    private void checkAuthenticationAndNavigate() {
        TokenManager tokenManager = TokenManager.getInstance(this);

        Intent intent;
        if (tokenManager.isLoggedIn()) {
            // User has valid token, go to MainActivity
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            // No token, go to LoginActivity
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}

