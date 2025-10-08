package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.User;

public class UserResponse {

    private boolean success;
    private String message;
    private User data;

    public UserResponse() {

    }

    public UserResponse(boolean success, String message, User data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
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

    public User getData() {
        return data;
    }


    public void setData(User data) {
        this.data = data;
    }


}

