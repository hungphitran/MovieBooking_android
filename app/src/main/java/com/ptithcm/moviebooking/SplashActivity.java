package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500; // 1.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Navigate to MainActivity after splash delay
        // User can browse home screen without login
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            navigateToMainActivity();
        }, SPLASH_DELAY);
    }

    private void navigateToMainActivity() {
        // Luôn chuyển đến MainActivity
        // Người dùng có thể xem trang chủ mà không cần đăng nhập
        // Khi chuyển sang tab Vé hoặc Tài khoản, sẽ yêu cầu đăng nhập nếu chưa có token
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
