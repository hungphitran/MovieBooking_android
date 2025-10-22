package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.BookingDetail;

public class BookingDetailResponse {
    private boolean success;
    private String message;
    private BookingDetail data;

    public BookingDetailResponse() {
    }

    public BookingDetailResponse(boolean success, String message, BookingDetail data) {
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

    public BookingDetail getData() {
        return data;
    }

    public void setData(BookingDetail data) {
        this.data = data;
    }
}

