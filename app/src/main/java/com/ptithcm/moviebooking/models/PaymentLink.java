package com.ptithcm.moviebooking.models;

import com.google.gson.annotations.SerializedName;

public class PaymentLink {
    @SerializedName("checkoutUrl")
    private String checkoutUrl;

    @SerializedName("orderCode")
    private long orderCode;

    @SerializedName("paymentLinkId")
    private String paymentLinkId;

    @SerializedName("qrCode")
    private String qrCode;

    @SerializedName("bookingId")
    private String bookingId;

    public PaymentLink() {
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public String getPaymentLinkId() {
        return paymentLinkId;
    }

    public void setPaymentLinkId(String paymentLinkId) {
        this.paymentLinkId = paymentLinkId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}

