package com.ptithcm.moviebooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ptithcm.moviebooking.ChangePasswordActivity;
import com.ptithcm.moviebooking.EditProfileActivity;
import com.ptithcm.moviebooking.LoginActivity;
import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.api.ApiService;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.UserResponse;
import com.ptithcm.moviebooking.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private CardView cvEditProfile, cvChangePassword, cvLogout;
    private TokenManager tokenManager;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenManager = TokenManager.getInstance(requireContext());
        apiService = RetrofitClient.getInstance(requireContext()).getApiService();

        initViews(view);
        loadUserData();
        setupClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload user data mỗi khi Fragment được hiển thị lại
        // Điều này đảm bảo dữ liệu được cập nhật sau khi edit profile
        loadUserData();
    }

    private void initViews(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        cvEditProfile = view.findViewById(R.id.cvEditProfile);
        cvChangePassword = view.findViewById(R.id.cvChangePassword);
        cvLogout = view.findViewById(R.id.cvLogout);
    }

    private void loadUserData() {
        apiService.getUserProfile().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.isSuccess() && userResponse.getData() != null) {
                        // Update UI with user data
                        tvUserName.setText(userResponse.getData().getName());
                        tvUserEmail.setText(userResponse.getData().getEmail());
                    } else {
                        Toast.makeText(getActivity(),
                                "Failed to load profile: " + userResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Error loading profile: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(),
                        "Connection error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        cvEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            // Pass user data to edit activity
            intent.putExtra("email", tvUserEmail.getText().toString());
            startActivity(intent);
        });

        cvChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        cvLogout.setOnClickListener(v -> handleLogout());
    }

    private void handleLogout() {
        // Clear saved data
        tokenManager.clearToken();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
