package com.ptithcm.moviebooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.moviebooking.LoginActivity;
import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.utils.TokenManager;

public class TicketsFragment extends Fragment {

    private LinearLayout layoutNotLoggedIn, layoutNoTickets;
    private MaterialButton btnLoginTickets;
    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenManager = TokenManager.getInstance(requireContext());

        initViews(view);
        checkLoginStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Kiểm tra lại trạng thái đăng nhập khi quay lại fragment
        checkLoginStatus();
    }

    private void initViews(View view) {
        layoutNotLoggedIn = view.findViewById(R.id.layoutNotLoggedIn);
        layoutNoTickets = view.findViewById(R.id.layoutNoTickets);
        btnLoginTickets = view.findViewById(R.id.btnLoginTickets);

        btnLoginTickets.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void checkLoginStatus() {
        String token = tokenManager.getToken();

        if (token == null || token.isEmpty()) {
            // Chưa đăng nhập - hiển thị nút đăng nhập
            layoutNotLoggedIn.setVisibility(View.VISIBLE);
            layoutNoTickets.setVisibility(View.GONE);
        } else {
            // Đã đăng nhập - hiển thị danh sách vé hoặc thông báo chưa có vé
            layoutNotLoggedIn.setVisibility(View.GONE);
            layoutNoTickets.setVisibility(View.VISIBLE);
            // TODO: Load tickets from API
        }
    }
}
