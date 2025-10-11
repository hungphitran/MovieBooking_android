package com.ptithcm.moviebooking.schema;

import java.util.List;

public class BookingRequest {
    private String customerEmail;
    private List<String> seatIds;
    private String showtimeId;

    public BookingRequest() {
    }

    public BookingRequest(String customerEmail, List<String> seatIds, String showtimeId) {
        this.customerEmail = customerEmail;
        this.seatIds = seatIds;
        this.showtimeId = showtimeId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<String> seatIds) {
        this.seatIds = seatIds;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }
}

