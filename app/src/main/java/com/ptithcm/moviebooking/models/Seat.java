package com.ptithcm.moviebooking.models;

import com.google.gson.annotations.SerializedName;

public class Seat {
    @SerializedName("seatId")
    private String id;

    @SerializedName("row")
    private String row;

    @SerializedName("number")
    private int column;

    @SerializedName("status")
    private String status;  // "available" or "booked"

    // Price is not from API, it will be calculated based on movie budget
    private double price;

    private boolean isSelected;

    public Seat() {
    }

    public Seat(String id, String row, int column, boolean isBooked, double price) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.status = isBooked ? "booked" : "available";
        this.price = price;
        this.isSelected = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isBooked() {
        return "booked".equals(status);
    }

    public boolean isReserved() {
        return "reserved".equals(status);
    }

    public void setBooked(boolean booked) {
        this.status = booked ? "booked" : "available";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Helper method to get seat display name (row + column)
    public String getSeatName() {
        return row + column;
    }
}
