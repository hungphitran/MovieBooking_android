package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.AuthResponse;
import com.ptithcm.moviebooking.schema.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private TextInputLayout tilName, tilEmail, tilPhone, tilAge, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPhone, etAge, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private android.view.View tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        initViews();

        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilAge = findViewById(R.id.tilAge);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setClickListeners() {
        btnRegister.setOnClickListener(v -> handleRegister());

        tvLogin.setOnClickListener(v -> {
            // Navigate back to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleRegister() {
        // Clear previous errors
        clearErrors();

        // Get input values
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String ageStr = etAge.getText() != null ? etAge.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        // Validate inputs
        if (!validateInputs(name, email, phone, ageStr, password, confirmPassword)) {
            return;
        }

        // Parse age
        int age = Integer.parseInt(ageStr);

        // Disable button to prevent double click
        btnRegister.setEnabled(false);
        btnRegister.setText("Đang đăng ký...");

        // Call API
        RegisterRequest registerRequest = new RegisterRequest(email, password, name, phone, age);
        RetrofitClient.getInstance(RegisterActivity.this).getApiService().register(registerRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText(R.string.register_button);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    if (authResponse.isSuccess()) {
                        Toast.makeText(RegisterActivity.this,
                                authResponse.getMessage() != null ? authResponse.getMessage() : getString(R.string.register_success),
                                Toast.LENGTH_SHORT).show();

                        // Navigate to LoginActivity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                authResponse.getMessage() != null ? authResponse.getMessage() : getString(R.string.register_failed),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Lỗi: " + response.code() + " - " + response.message(),
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Register failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText(R.string.register_button);

                Toast.makeText(RegisterActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "Register error: ", t);
            }
        });
    }

    private void clearErrors() {
        tilName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilAge.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);
    }

    private boolean validateInputs(String name, String email, String phone, String ageStr,
                                   String password, String confirmPassword) {
        boolean isValid = true;

        // Validate name
        if (TextUtils.isEmpty(name)) {
            tilName.setError(getString(R.string.name_required));
            isValid = false;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.email_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            isValid = false;
        }

        // Validate phone
        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError(getString(R.string.phone_required));
            isValid = false;
        } else if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            tilPhone.setError(getString(R.string.invalid_phone));
            isValid = false;
        }

        // Validate age
        if (TextUtils.isEmpty(ageStr)) {
            tilAge.setError(getString(R.string.age_required));
            isValid = false;
        } else {
            try {
                int age = Integer.parseInt(ageStr);
                if (age < 13 || age > 120) {
                    tilAge.setError(getString(R.string.invalid_age));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilAge.setError(getString(R.string.invalid_age));
                isValid = false;
            }
        }


        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.password_required));
            isValid = false;
        } else if (password.length() < 4) {
            tilPassword.setError(getString(R.string.password_too_short));
            isValid = false;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.password_required));
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.password_not_match));
            isValid = false;
        }

        return isValid;
    }
}