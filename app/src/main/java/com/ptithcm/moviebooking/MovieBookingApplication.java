package com.ptithcm.moviebooking;

import android.app.Application;

import com.ptithcm.moviebooking.api.RetrofitClient;

public class MovieBookingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize RetrofitClient with application context
        RetrofitClient.getInstance(this);
    }
}

