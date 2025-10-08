package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.MovieDetail;

public class MovieDetailResponse {
    private boolean success;
    private MovieDetail data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MovieDetail getData() {
        return data;
    }

    public void setData(MovieDetail data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

