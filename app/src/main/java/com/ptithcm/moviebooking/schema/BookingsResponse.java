package com.ptithcm.moviebooking.schema;

import com.ptithcm.moviebooking.models.BookingDetail;
import java.util.List;

public class BookingsResponse {
    private boolean success;
    private String message;
    private List<BookingDetail> data;
    private int total;

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

    public List<BookingDetail> getData() {
        return data;
    }

    public void setData(List<BookingDetail> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
