package com.ptithcm.moviebooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ptithcm.moviebooking.LoginActivity;
import com.ptithcm.moviebooking.R;

public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private CardView cvEditProfile, cvChangePassword, cvBookingHistory, cvLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        loadUserData();
        setupClickListeners();
    }

    private void initViews(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        cvEditProfile = view.findViewById(R.id.cvEditProfile);
        cvChangePassword = view.findViewById(R.id.cvChangePassword);
        cvBookingHistory = view.findViewById(R.id.cvBookingHistory);
        cvLogout = view.findViewById(R.id.cvLogout);
    }

    private void loadUserData() {
        // TODO: Load from SharedPreferences or API
        tvUserName.setText("Người dùng");
        tvUserEmail.setText("user@example.com");
    }

    private void setupClickListeners() {
        cvEditProfile.setOnClickListener(v -> {
            // TODO: Navigate to edit profile
        });

        cvChangePassword.setOnClickListener(v -> {
            // TODO: Navigate to change password
        });

        cvBookingHistory.setOnClickListener(v -> {
            // TODO: Navigate to booking history
        });

        cvLogout.setOnClickListener(v -> {
            handleLogout();
        });
    }

    private void handleLogout() {
        // Clear saved data
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}

