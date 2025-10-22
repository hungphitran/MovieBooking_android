package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.ForgotPasswordRequest;
import com.ptithcm.moviebooking.schema.ForgotPasswordResponse;
import com.ptithcm.moviebooking.schema.ResetPasswordRequest;
import com.ptithcm.moviebooking.schema.ResetPasswordResponse;
import com.ptithcm.moviebooking.schema.VerifyOtpRequest;
import com.ptithcm.moviebooking.schema.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    // Views
    private TextView tvTitle, tvSubtitle;
    private View btnBack;

    // Step 1: Email
    private LinearLayout layoutEmailStep;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnSendOtp;

    // Step 2: OTP
    private LinearLayout layoutOtpStep;
    private TextView tvOtpSent, tvResendOtp;
    private TextInputLayout tilOtp;
    private TextInputEditText etOtp;
    private MaterialButton btnVerifyOtp;

    // Step 3: New Password
    private LinearLayout layoutPasswordStep;
    private TextInputLayout tilNewPassword, tilConfirmPassword;
    private TextInputEditText etNewPassword, etConfirmPassword;
    private MaterialButton btnResetPassword;

    // Data
    private String email;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        setClickListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        btnBack = findViewById(R.id.btnBack);

        // Step 1
        layoutEmailStep = findViewById(R.id.layoutEmailStep);
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        // Step 2
        layoutOtpStep = findViewById(R.id.layoutOtpStep);
        tvOtpSent = findViewById(R.id.tvOtpSent);
        tvResendOtp = findViewById(R.id.tvResendOtp);
        tilOtp = findViewById(R.id.tilOtp);
        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        // Step 3
        layoutPasswordStep = findViewById(R.id.layoutPasswordStep);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
    }

    private void setClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSendOtp.setOnClickListener(v -> handleSendOtp());

        btnVerifyOtp.setOnClickListener(v -> handleVerifyOtp());

        tvResendOtp.setOnClickListener(v -> handleResendOtp());

        btnResetPassword.setOnClickListener(v -> handleResetPassword());
    }

    private void handleSendOtp() {
        // Clear previous errors
        tilEmail.setError(null);

        // Get email
        email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.email_required));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            return;
        }

        // Disable button
        btnSendOtp.setEnabled(false);
        btnSendOtp.setText("Đang gửi...");

        // Call API
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        RetrofitClient.getInstance(this).getApiService().forgotPassword(request)
                .enqueue(new Callback<ForgotPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                        btnSendOtp.setEnabled(true);
                        btnSendOtp.setText(R.string.send_otp);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        response.body().getMessage() != null ? response.body().getMessage() : "OTP đã được gửi đến email của bạn",
                                        Toast.LENGTH_SHORT).show();
                                showOtpStep();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        response.body().getMessage() != null ? response.body().getMessage() : "Gửi OTP thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                        btnSendOtp.setEnabled(true);
                        btnSendOtp.setText(R.string.send_otp);
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleResendOtp() {
        handleSendOtp();
    }

    private void handleVerifyOtp() {
        // Clear previous errors
        tilOtp.setError(null);

        // Get OTP
        otp = etOtp.getText() != null ? etOtp.getText().toString().trim() : "";

        // Validate OTP
        if (TextUtils.isEmpty(otp)) {
            tilOtp.setError("Vui lòng nhập mã OTP");
            return;
        }

        if (otp.length() < 4) {
            tilOtp.setError("Mã OTP không hợp lệ");
            return;
        }

        // Disable button
        btnVerifyOtp.setEnabled(false);
        btnVerifyOtp.setText("Đang xác thực...");

        // Call API
        VerifyOtpRequest request = new VerifyOtpRequest(email, otp);
        RetrofitClient.getInstance(this).getApiService().verifyOtp(request)
                .enqueue(new Callback<VerifyOtpResponse>() {
                    @Override
                    public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                        btnVerifyOtp.setEnabled(true);
                        btnVerifyOtp.setText(R.string.verify_otp);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Xác thực thành công",
                                        Toast.LENGTH_SHORT).show();
                                showPasswordStep();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        response.body().getMessage() != null ? response.body().getMessage() : "Mã OTP không chính xác",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Mã OTP không chính xác hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                        btnVerifyOtp.setEnabled(true);
                        btnVerifyOtp.setText(R.string.verify_otp);
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleResetPassword() {
        // Clear previous errors
        tilNewPassword.setError(null);
        tilConfirmPassword.setError(null);

        // Get passwords
        String newPassword = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        // Validate
        if (TextUtils.isEmpty(newPassword)) {
            tilNewPassword.setError(getString(R.string.password_required));
            return;
        }

        if (newPassword.length() < 4) {
            tilNewPassword.setError(getString(R.string.password_too_short));
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.password_not_match));
            return;
        }

        // Disable button
        btnResetPassword.setEnabled(false);
        btnResetPassword.setText("Đang đặt lại...");

        // Call API
        ResetPasswordRequest request = new ResetPasswordRequest(otp, newPassword, email);
        RetrofitClient.getInstance(this).getApiService().resetPassword(request)
                .enqueue(new Callback<ResetPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                        btnResetPassword.setEnabled(true);
                        btnResetPassword.setText(R.string.reset_password);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Đặt lại mật khẩu thành công!",
                                        Toast.LENGTH_SHORT).show();
                                // Navigate to login
                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        response.body().getMessage() != null ? response.body().getMessage() : "Đặt lại mật khẩu thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Đặt lại mật khẩu thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                        btnResetPassword.setEnabled(true);
                        btnResetPassword.setText(R.string.reset_password);
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showOtpStep() {
        // Hide email step
        layoutEmailStep.setVisibility(View.GONE);
        // Show OTP step
        layoutOtpStep.setVisibility(View.VISIBLE);
        // Update title
        tvTitle.setText(R.string.verify_otp_title);
        tvSubtitle.setText(R.string.verify_otp_subtitle);
        // Update OTP sent message
        tvOtpSent.setText("Mã OTP đã được gửi đến " + email);
    }

    private void showPasswordStep() {
        // Hide OTP step
        layoutOtpStep.setVisibility(View.GONE);
        // Show password step
        layoutPasswordStep.setVisibility(View.VISIBLE);
        // Update title
        tvTitle.setText(R.string.reset_password_title);
        tvSubtitle.setText(R.string.reset_password_subtitle);
    }
}

