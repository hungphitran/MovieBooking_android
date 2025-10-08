package com.ptithcm.moviebooking;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ptithcm.moviebooking.api.ApiService;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.ChangePasswordRequest;
import com.ptithcm.moviebooking.schema.ChangePasswordResponse;
import com.ptithcm.moviebooking.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmPassword;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        tokenManager = TokenManager.getInstance(this);
        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        setupToolbar();
        setupClickListeners();
    }

    private void initViews() {
        tilCurrentPassword = findViewById(R.id.tilCurrentPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đổi mật khẩu");
        }
    }

    private void setupClickListeners() {
        btnChangePassword.setOnClickListener(v -> {
            if (validateInputs()) {
                changePassword();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        String currentPassword = etCurrentPassword.getText() != null ? etCurrentPassword.getText().toString().trim() : "";
        String newPassword = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        // Validate current password
        if (currentPassword.isEmpty()) {
            tilCurrentPassword.setError("Mật khẩu hiện tại không được để trống");
            isValid = false;
        } else {
            tilCurrentPassword.setError(null);
        }

        // Validate new password
        if (newPassword.isEmpty()) {
            tilNewPassword.setError("Mật khẩu mới không được để trống");
            isValid = false;
        } else if (newPassword.length() < 4) {
            tilNewPassword.setError(getString(R.string.password_too_short));
            isValid = false;
        } else if (newPassword.equals(currentPassword)) {
            tilNewPassword.setError("Mật khẩu mới phải khác mật khẩu hiện tại");
            isValid = false;
        } else {
            tilNewPassword.setError(null);
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.setError("Xác nhận mật khẩu không được để trống");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError(getString(R.string.password_not_match));
            isValid = false;
        } else {
            tilConfirmPassword.setError(null);
        }

        return isValid;
    }

    private void changePassword() {
        progressBar.setVisibility(View.VISIBLE);
        btnChangePassword.setEnabled(false);

        String currentPassword = etCurrentPassword.getText() != null ? etCurrentPassword.getText().toString().trim() : "";
        String newPassword = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";


        ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword);
        Log.d("ChangePasswordActivity", "Request: " + request);
        apiService.changePassword(request).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordResponse> call, @NonNull Response<ChangePasswordResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnChangePassword.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    ChangePasswordResponse changeResponse = response.body();
                    if (changeResponse.isSuccess()) {
                        Toast.makeText(ChangePasswordActivity.this,
                                "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                changeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this,
                            "Đổi mật khẩu thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnChangePassword.setEnabled(true);
                Toast.makeText(ChangePasswordActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}