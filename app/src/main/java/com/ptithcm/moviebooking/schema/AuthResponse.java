package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.User;

public class AuthResponse {
    private boolean success;
    private String message;
    private User data;
    private String token;

    public AuthResponse() {

    }
    public AuthResponse(boolean success, String message, User data, String token) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public  User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

