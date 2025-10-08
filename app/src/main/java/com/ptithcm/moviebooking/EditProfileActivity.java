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
import com.ptithcm.moviebooking.schema.UpdateProfileRequest;
import com.ptithcm.moviebooking.schema.UpdateProfileResponse;
import com.ptithcm.moviebooking.schema.UserResponse;
import com.ptithcm.moviebooking.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPhone, tilAge;
    private TextInputEditText etName, etEmail, etPhone, etAge;
    private Button btnSave;
    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private ApiService apiService;
    private UserResponse userResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tokenManager = TokenManager.getInstance(this);
        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        setupToolbar();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilAge = findViewById(R.id.tilAge);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);

        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chỉnh sửa thông tin");
        }
    }

    private void loadUserData() {
        // For now, loading from intent extras if available
        String email = getIntent().getStringExtra("email");

        apiService.getUserProfile().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.isSuccess() && userResponse.getData() != null) {
                        // Update UI with user data
                        etName.setText(userResponse.getData().getName());
                        etEmail.setText(userResponse.getData().getEmail());
                        etPhone.setText(userResponse.getData().getPhone());
                        etAge.setText(String.valueOf(userResponse.getData().getAge()));
                    } else {
                        Toast.makeText(EditProfileActivity.this,
                                "Failed to load profile: " + userResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Error loading profile: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Connection error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                updateProfile();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String ageStr = etAge.getText() != null ? etAge.getText().toString().trim() : "";

        // Validate name
        if (name.isEmpty()) {
            tilName.setError(getString(R.string.name_required));
            isValid = false;
        } else {
            tilName.setError(null);
        }

        // Validate email
        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.email_required));
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Validate phone
        if (phone.isEmpty()) {
            tilPhone.setError(getString(R.string.phone_required));
            isValid = false;
        } else if (!phone.matches("^[0-9]{10,11}$")) {
            tilPhone.setError(getString(R.string.invalid_phone));
            isValid = false;
        } else {
            tilPhone.setError(null);
        }

        // Validate age
        if (ageStr.isEmpty()) {
            tilAge.setError(getString(R.string.age_required));
            isValid = false;
        } else {
            try {
                int age = Integer.parseInt(ageStr);
                if (age < 13 || age > 120) {
                    tilAge.setError(getString(R.string.invalid_age));
                    isValid = false;
                } else {
                    tilAge.setError(null);
                }
            } catch (NumberFormatException e) {
                tilAge.setError(getString(R.string.invalid_age));
                isValid = false;
            }
        }

        return isValid;
    }

    private void updateProfile() {
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        int age = etAge.getText() != null ? Integer.parseInt(etAge.getText().toString().trim()) : 0;

        UpdateProfileRequest request = new UpdateProfileRequest(name, email, phone, age);
        Log.d("EditProfileActivity", "Updating profile with: " + request);
        apiService.updateProfile(request).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    UpdateProfileResponse updateResponse = response.body();
                    if (updateResponse.isSuccess()) {
                        Toast.makeText(EditProfileActivity.this,
                                "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this,
                                updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Cập nhật thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                Toast.makeText(EditProfileActivity.this,
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