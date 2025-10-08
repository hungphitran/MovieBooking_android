package com.ptithcm.moviebooking.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "MovieBookingPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static TokenManager instance;
    private final SharedPreferences sharedPreferences;

    private TokenManager(Context context) {
        // Use MODE_PRIVATE so token is only accessible by this app
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    /**
     * Save authentication token and email
     * @param token JWT token from server
     * @param email User's email
     */
    public void saveToken(String token, String email) {
        saveToken(token, email, false);
    }

    /**
     * Save authentication token and email with remember option
     * @param token JWT token from server
     * @param email User's email
     * @param rememberMe If true, token persists; if false, cleared on app close
     */
    public void saveToken(String token, String email, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    /**
     * Get saved authentication token
     * @return token or null if not found
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    /**
     * Get saved user email
     * @return email or null if not found
     */
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    /**
     * Check if user is logged in (has valid token)
     * @return true if token exists
     */
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    /**
     * Check if remember me is enabled
     * @return true if user wants to stay logged in
     */
    public boolean isRememberMe() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    /**
     * Clear all saved authentication data
     * Called on logout
     */
    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_REMEMBER_ME);
        editor.apply();
    }

    /**
     * Clear token if not in remember me mode
     * Called when app is closed
     */
    public void clearSessionToken() {
        if (!isRememberMe()) {
            clearToken();
        }
    }

    /**
     * Clear all data including other preferences
     */
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
