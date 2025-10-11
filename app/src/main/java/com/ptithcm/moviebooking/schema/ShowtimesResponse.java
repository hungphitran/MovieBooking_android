package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.Showtime;
import java.util.List;

public class ShowtimesResponse {
    private boolean success;
    private String message;
    private List<Showtime> data;

    public ShowtimesResponse() {
    }

    public ShowtimesResponse(boolean success, String message, List<Showtime> data) {
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

    public List<Showtime> getData() {
        return data;
    }

    public void setData(List<Showtime> data) {
        this.data = data;
    }
}

