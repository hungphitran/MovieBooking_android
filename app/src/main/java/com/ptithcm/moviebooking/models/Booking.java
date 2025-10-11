package com.ptithcm.moviebooking.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Booking {
    @SerializedName("id")
    private String id;

    @SerializedName("customerEmail")
    private String customerEmail;

    @SerializedName("showtimeId")
    private String showtimeId;

    @SerializedName("seatIds")
    private List<String> seatIds;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("bookingDate")
    private String bookingDate;

    @SerializedName("status")
    private String status;

    public Booking() {
    }

    public Booking(String customerEmail, String showtimeId, List<String> seatIds) {
        this.customerEmail = customerEmail;
        this.showtimeId = showtimeId;
        this.seatIds = seatIds;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<String> seatIds) {
        this.seatIds = seatIds;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
