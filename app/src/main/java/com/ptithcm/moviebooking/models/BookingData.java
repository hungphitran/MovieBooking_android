package com.ptithcm.moviebooking.models;

import com.google.gson.annotations.SerializedName;

public class BookingData {
    @SerializedName("booking")
    private BookingDetail booking;

    @SerializedName("paymentLink")
    private PaymentLink paymentLink;

    public BookingData() {
    }

    public BookingDetail getBooking() {
        return booking;
    }

    public void setBooking(BookingDetail booking) {
        this.booking = booking;
    }

    public PaymentLink getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(PaymentLink paymentLink) {
        this.paymentLink = paymentLink;
    }
}

