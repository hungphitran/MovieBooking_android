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

        // Get token from TokenManager
        String token = TokenManager.getInstance(context).getToken();

        // If token exists, add it to the request header
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        // If no token, proceed with original request
        return chain.proceed(originalRequest);
    }
}

