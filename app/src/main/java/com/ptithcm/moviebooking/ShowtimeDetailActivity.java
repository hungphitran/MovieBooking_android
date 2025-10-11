package com.ptithcm.moviebooking;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ptithcm.moviebooking.adapters.SeatAdapter;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.models.Seat;
import com.ptithcm.moviebooking.models.Showtime;
import com.ptithcm.moviebooking.schema.BookingRequest;
import com.ptithcm.moviebooking.schema.BookingResponse;
import com.ptithcm.moviebooking.schema.ShowtimeDetailResponse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowtimeDetailActivity extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {
    private static final String PREFS_NAME = "MovieBookingPrefs";
    private static final String KEY_USER_EMAIL = "user_email";

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView tvMovieTitle;
    private TextView tvShowDateTime;
    private TextView tvSelectedSeats;
    private TextView tvTotalPrice;
    private RecyclerView rvSeats;
    private MaterialButton btnBookNow;

    private SeatAdapter seatAdapter;
    private String showtimeId;
    private String movieTitle;
    private Showtime showtime;
    private List<Seat> allSeats;
    private List<Seat> selectedSeats;
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_detail);

        // Get data from intent
        showtimeId = getIntent().getStringExtra("showtimeId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        if (showtimeId == null || showtimeId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin lịch chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        selectedSeats = new ArrayList<>();
        initViews();
        setupRecyclerView();
        loadShowtimeDetail();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvShowDateTime = findViewById(R.id.tvShowDateTime);
        tvSelectedSeats = findViewById(R.id.tvSelectedSeats);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rvSeats = findViewById(R.id.rvSeats);
        btnBookNow = findViewById(R.id.btnBookNow);

        btnBookNow.setOnClickListener(v -> showBookingConfirmation());
    }

    private void setupRecyclerView() {
        seatAdapter = new SeatAdapter(this, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 8);
        rvSeats.setLayoutManager(gridLayoutManager);
        rvSeats.setAdapter(seatAdapter);
    }

    private void loadShowtimeDetail() {
        progressBar.setVisibility(View.VISIBLE);
        rvSeats.setVisibility(View.GONE);

        RetrofitClient.getInstance(this).getApiService().getShowtimeDetail(showtimeId)
                .enqueue(new Callback<ShowtimeDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ShowtimeDetailResponse> call,
                                           @NonNull Response<ShowtimeDetailResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            ShowtimeDetailResponse showtimeResponse = response.body();
                            if (showtimeResponse.isSuccess() && showtimeResponse.getData() != null) {
                                showtime = showtimeResponse.getData().getShowtime();
                                allSeats = showtimeResponse.getData().getSeats();
                                displayShowtimeInfo();
                                displaySeats();
                            } else {
                                showError("Không thể tải thông tin lịch chiếu");
                            }
                        } else {
                            showError("Lỗi kết nối: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ShowtimeDetailResponse> call,
                                          @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        showError("Lỗi: " + t.getMessage());
                    }
                });
    }

    private void displayShowtimeInfo() {
        if (showtime != null) {
            if (showtime.getMovie() != null) {
                tvMovieTitle.setText(showtime.getMovie().getTitle());
            } else if (movieTitle != null && !movieTitle.isEmpty()) {
                tvMovieTitle.setText(movieTitle);
            } else {
                tvMovieTitle.setText("Movie ID: " + showtime.getMovieId());
            }

            String dateTime = String.format("📅 %s | 🕐 %s - %s",
                    showtime.getShowDate(),
                    showtime.getStartTime(),
                    showtime.getEndTime());
            tvShowDateTime.setText(dateTime);
        }
    }

    private void displaySeats() {
        if (allSeats != null && !allSeats.isEmpty()) {
            seatAdapter.setSeatList(allSeats);
            rvSeats.setVisibility(View.VISIBLE);
        } else {
            showError("Không có ghế nào");
        }
    }

    @Override
    public void onSeatClick(Seat seat) {
        if (seat.isSelected()) {
            // Thêm ghế vào danh sách đã chọn
            if (!selectedSeats.contains(seat)) {
                selectedSeats.add(seat);
            }
        } else {
            // Xóa ghế khỏi danh sách đã chọn
            selectedSeats.remove(seat);
        }
        updateSelectedSeatsInfo();
    }

    private void updateSelectedSeatsInfo() {
        if (selectedSeats.isEmpty()) {
            tvSelectedSeats.setText("Ghế đã chọn: Chưa chọn");
            tvTotalPrice.setText("Tổng tiền: 0 VNĐ");
            btnBookNow.setEnabled(false);
            totalPrice = 0;
        } else {
            // Tạo chuỗi danh sách ghế
            StringBuilder seatsText = new StringBuilder("Ghế đã chọn: ");
            totalPrice = 0;
            for (int i = 0; i < selectedSeats.size(); i++) {
                Seat seat = selectedSeats.get(i);
                seatsText.append(seat.getSeatNumber());
                totalPrice += seat.getPrice();
                if (i < selectedSeats.size() - 1) {
                    seatsText.append(", ");
                }
            }
            tvSelectedSeats.setText(seatsText.toString());

            // Format giá tiền
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvTotalPrice.setText("Tổng tiền: " + formatter.format(totalPrice) + " VNĐ");

            btnBookNow.setEnabled(true);
        }
    }

    private void showBookingConfirmation() {
        if (selectedSeats.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Bạn đã chọn ").append(selectedSeats.size()).append(" ghế:\n");
        for (Seat seat : selectedSeats) {
            message.append("• ").append(seat.getSeatNumber()).append("\n");
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        message.append("\nTổng tiền: ").append(formatter.format(totalPrice)).append(" VNĐ");

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đặt vé")
                .setMessage(message.toString())
                .setPositiveButton("Đặt vé", (dialog, which) -> createBooking())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void createBooking() {
        // Lấy email từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userEmail = prefs.getString(KEY_USER_EMAIL, "");

        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo danh sách seat IDs
        List<String> seatIds = new ArrayList<>();
        for (Seat seat : selectedSeats) {
            seatIds.add(seat.getId());
        }

        // Tạo booking request
        BookingRequest bookingRequest = new BookingRequest(userEmail, seatIds, showtimeId);

        progressBar.setVisibility(View.VISIBLE);
        btnBookNow.setEnabled(false);

        RetrofitClient.getInstance(this).getApiService().createBooking(bookingRequest)
                .enqueue(new Callback<BookingResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BookingResponse> call,
                                           @NonNull Response<BookingResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        btnBookNow.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            BookingResponse bookingResponse = response.body();
                            if (bookingResponse.isSuccess()) {
                                showSuccessDialog();
                            } else {
                                Toast.makeText(ShowtimeDetailActivity.this,
                                        "Đặt vé thất bại: " + bookingResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ShowtimeDetailActivity.this,
                                    "Lỗi: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BookingResponse> call,
                                          @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btnBookNow.setEnabled(true);
                        Toast.makeText(ShowtimeDetailActivity.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đặt vé thành công")
                .setMessage("Vé của bạn đã được đặt thành công!\nVui lòng kiểm tra email để xem chi tiết.")
                .setPositiveButton("OK", (dialog, which) -> {
                    finish(); // Quay lại màn hình trước
                })
                .setCancelable(false)
                .show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
