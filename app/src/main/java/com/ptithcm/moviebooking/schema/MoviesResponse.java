package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.Movie;

import java.util.List;

public class MoviesResponse {
    private boolean success;
    private String message;
    private List<Movie> data;

    public MoviesResponse() {
    }

    public MoviesResponse(boolean success, String message, List<Movie> data) {
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

    public List<Movie> getData() {
        return data;
    }

    public void setData(List<Movie> data) {
        this.data = data;
    }
}

