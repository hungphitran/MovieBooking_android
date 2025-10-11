package com.ptithcm.moviebooking.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // For Android Emulator: Use 10.0.2.2 (special alias to host machine's localhost)
    // For Physical Device: Use your computer's actual IP (e.g., "http://192.168.1.100:8000/")
    // To find your IP on Windows: Open CMD and run "ipconfig" - look for IPv4 Address
    private static final String BASE_URL = "http://10.0.2.2:8000/api/";
//    private static final String BASE_URL = "https://fastapiproject.fly.dev/api/";
//    private static final String BASE_URL = "https://fastapiproject.fly.dev/api/";

    private static RetrofitClient instance;
    private ApiService apiService;

    private RetrofitClient(Context context) {
        // Create logging interceptor để xem request/response trong Logcat
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create Auth interceptor để tự động thêm token vào header
        AuthInterceptor authInterceptor = new AuthInterceptor(context);

        // Create Token Expiration interceptor để xử lý token hết hạn
        TokenExpirationInterceptor tokenExpirationInterceptor = new TokenExpirationInterceptor(context);

        // Create OkHttpClient với timeout và logging
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor) // Add auth interceptor first
                .addInterceptor(tokenExpirationInterceptor) // Add token expiration interceptor
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
        return instance;
    }

    // Keep this for backward compatibility
    public static synchronized RetrofitClient getInstance() {
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
