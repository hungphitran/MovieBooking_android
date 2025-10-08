package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.User;

public class UpdateProfileResponse {
    private boolean success;
    private String message;
    private User data;

    public UpdateProfileResponse() {

    }
    public UpdateProfileResponse(boolean success, String message, User data) {
        this.success = success;
        this.message = message;
        this.data = data;
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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}

