package com.ptithcm.moviebooking.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ptithcm.moviebooking.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String path = originalRequest.url().encodedPath();

        // Chỉ thêm token cho các endpoint cần xác thực
        // Danh sách endpoints cần token: user profile, bookings, tickets, change password, update profile
        boolean requiresAuth = path.contains("/user/") ||
                              path.contains("/booking") ||
                              path.contains("/ticket") ||
                              path.contains("/auth/change-password");

        // Nếu endpoint cần xác thực và có token, thêm vào header
        if (requiresAuth) {
            String token = TokenManager.getInstance(context).getToken();
            if (token != null && !token.isEmpty()) {
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }

        // Nếu không cần xác thực hoặc không có token, tiếp tục với request gốc
        return chain.proceed(originalRequest);
    }
}
