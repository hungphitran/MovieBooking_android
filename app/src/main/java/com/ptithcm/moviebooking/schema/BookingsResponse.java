package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.Booking;
import java.util.List;

public class BookingsResponse {
    private boolean success;
    private String message;
    private List<Booking> data;

    public BookingsResponse() {
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

    public List<Booking> getData() {
        return data;
    }

    public void setData(List<Booking> data) {
        this.data = data;
    }
}

