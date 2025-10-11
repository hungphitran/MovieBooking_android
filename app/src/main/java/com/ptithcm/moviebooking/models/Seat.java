package com.ptithcm.moviebooking.models;

import com.google.gson.annotations.SerializedName;

public class Seat {
    @SerializedName("id")
    private String id;

    @SerializedName("seatNumber")
    private String seatNumber;

    @SerializedName("row")
    private String row;

    @SerializedName("column")
    private int column;

    @SerializedName("isBooked")
    private boolean isBooked;

    @SerializedName("price")
    private double price;

    private boolean isSelected;

    public Seat() {
    }

    public Seat(String id, String seatNumber, String row, int column, boolean isBooked, double price) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.row = row;
        this.column = column;
        this.isBooked = isBooked;
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

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
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
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
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
}

