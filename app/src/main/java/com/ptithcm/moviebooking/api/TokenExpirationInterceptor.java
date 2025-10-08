package com.ptithcm.moviebooking.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ptithcm.moviebooking.LoginActivity;
import com.ptithcm.moviebooking.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Interceptor to handle token expiration
 * If server returns 401, clear token and redirect to login
 */
public class TokenExpirationInterceptor implements Interceptor {

    private final Context context;

    public TokenExpirationInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        // Check if response code is 401 (Unauthorized - token expired or invalid)
        if (response.code() == 401) {
            // Clear the expired token
            TokenManager.getInstance(context).clearToken();

            // Redirect to login activity
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("session_expired", true);
            context.startActivity(intent);
        }

        return response;
    }
}

