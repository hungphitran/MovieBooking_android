package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.AuthResponse;
import com.ptithcm.moviebooking.schema.LoginRequest;
import com.ptithcm.moviebooking.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private View tvForgotPassword, tvSignUp, btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        initViews();
        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }

    private void setClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());

        tvForgotPassword.setOnClickListener(v ->
            Toast.makeText(this, "Chức năng quên mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show()
        );

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Xử lý nút quay lại trang chủ
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void handleLogin() {
        // Clear previous errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        // Get input values
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Disable button to prevent double click
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        // Call API
        LoginRequest loginRequest = new LoginRequest(email, password);
        RetrofitClient.getInstance(LoginActivity.this).getApiService().login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText(R.string.login_button);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    if (authResponse.isSuccess()) {
                        // Save token and email to SharedPreferences
                        String token = authResponse.getToken();
                        String userEmail = authResponse.getData() != null && authResponse.getData().getEmail() != null
                                ? authResponse.getData().getEmail()
                                : email;

                        if (token != null) {
                            TokenManager.getInstance(LoginActivity.this).saveToken(token, userEmail);
                            Log.d(TAG, "Token saved successfully");
                        }

                        Toast.makeText(LoginActivity.this,
                            authResponse.getMessage() != null ? authResponse.getMessage() : getString(R.string.login_success),
                            Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                            authResponse.getMessage() != null ? authResponse.getMessage() : getString(R.string.login_failed),
                            Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                        "Lỗi: " + response.code() + " - " + response.message(),
                        Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Login failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText(R.string.login_button);

                Toast.makeText(LoginActivity.this,
                    "Lỗi kết nối: " + t.getMessage(),
                    Toast.LENGTH_LONG).show();
                Log.e(TAG, "Login error: ", t);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.email_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.password_required));
            isValid = false;
        }

        return isValid;
    }
}
