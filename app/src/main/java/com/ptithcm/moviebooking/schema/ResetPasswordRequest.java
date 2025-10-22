package com.ptithcm.moviebooking.schema;

public class ResetPasswordRequest {
    private String otp;
    private String newPassword;
    private String email;

    public ResetPasswordRequest(String otp, String newPassword, String email) {
        this.otp = otp;
        this.newPassword = newPassword;
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

