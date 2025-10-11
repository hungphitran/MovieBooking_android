package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.Booking;

public class BookingResponse {
    private boolean success;
    private String message;
    private Booking data;

    public BookingResponse() {
    }

    public BookingResponse(boolean success, String message, Booking data) {
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

    public Booking getData() {
        return data;
    }

    public void setData(Booking data) {
        this.data = data;
    }
}

