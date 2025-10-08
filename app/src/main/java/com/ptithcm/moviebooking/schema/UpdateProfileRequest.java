package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.User;

public class UpdateProfileRequest {
    private User data;

    public UpdateProfileRequest(String name, String email, String phone, int age) {
        this.data = new User(email, name, phone, age);
    }

    public UpdateProfileRequest(User user) {
        this.data = user;
    }

    // Getters and Setters
    public User getUser() {
        return data;
    }

    public void setUser(User user) {
        this.data = user;
    }
}
