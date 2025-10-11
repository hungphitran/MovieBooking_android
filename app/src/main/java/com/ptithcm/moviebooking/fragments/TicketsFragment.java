package com.ptithcm.moviebooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.moviebooking.LoginActivity;
import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.adapters.BookingAdapter;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.BookingsResponse;
import com.ptithcm.moviebooking.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketsFragment extends Fragment {

    private LinearLayout layoutNotLoggedIn, layoutNoTickets;
    private MaterialButton btnLoginTickets;
    private RecyclerView rvBookings;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private BookingAdapter bookingAdapter;
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
        setupRecyclerView();
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
        rvBookings = view.findViewById(R.id.rvBookings);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        btnLoginTickets.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        if (swipeRefresh != null) {
            swipeRefresh.setOnRefreshListener(() -> {
                loadBookings();
            });
        }
    }

    private void setupRecyclerView() {
        if (rvBookings != null) {
            bookingAdapter = new BookingAdapter(requireContext());
            rvBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvBookings.setAdapter(bookingAdapter);
        }
    }

    private void checkLoginStatus() {
        String token = tokenManager.getToken();

        if (token == null || token.isEmpty()) {
            // Chưa đăng nhập - hiển thị nút đăng nhập
            layoutNotLoggedIn.setVisibility(View.VISIBLE);
            layoutNoTickets.setVisibility(View.GONE);
            if (rvBookings != null) {
                rvBookings.setVisibility(View.GONE);
            }
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            // Đã đăng nhập - load danh sách vé
            layoutNotLoggedIn.setVisibility(View.GONE);
            loadBookings();
        }
    }

    private void loadBookings() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (layoutNoTickets != null) {
            layoutNoTickets.setVisibility(View.GONE);
        }
        if (rvBookings != null) {
            rvBookings.setVisibility(View.GONE);
        }

        RetrofitClient.getInstance(requireContext()).getApiService().getUserBookings()
                .enqueue(new Callback<BookingsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BookingsResponse> call, @NonNull Response<BookingsResponse> response) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        if (swipeRefresh != null) {
                            swipeRefresh.setRefreshing(false);
                        }

                        if (response.isSuccessful() && response.body() != null) {
                            BookingsResponse bookingsResponse = response.body();
                            if (bookingsResponse.isSuccess() && bookingsResponse.getData() != null && !bookingsResponse.getData().isEmpty()) {
                                // Có dữ liệu - hiển thị danh sách
                                bookingAdapter.setBookingList(bookingsResponse.getData());
                                if (rvBookings != null) {
                                    rvBookings.setVisibility(View.VISIBLE);
                                }
                                if (layoutNoTickets != null) {
                                    layoutNoTickets.setVisibility(View.GONE);
                                }
                            } else {
                                // Không có vé nào
                                if (layoutNoTickets != null) {
                                    layoutNoTickets.setVisibility(View.VISIBLE);
                                }
                                if (rvBookings != null) {
                                    rvBookings.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Không thể tải danh sách vé", Toast.LENGTH_SHORT).show();
                            if (layoutNoTickets != null) {
                                layoutNoTickets.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BookingsResponse> call, @NonNull Throwable t) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        if (swipeRefresh != null) {
                            swipeRefresh.setRefreshing(false);
                        }
                        Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        if (layoutNoTickets != null) {
                            layoutNoTickets.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
