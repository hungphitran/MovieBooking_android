package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.BookingData;

public class BookingResponse {
    private boolean success;
    private String message;
    private BookingData data;

    public BookingResponse() {
    }

    public BookingResponse(boolean success, String message, BookingData data) {
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

    public BookingData getData() {
        return data;
    }

    public void setData(BookingData data) {
        this.data = data;
    }
}
