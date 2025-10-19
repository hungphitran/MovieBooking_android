package com.ptithcm.moviebooking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvMovieTitle, tvShowDate, tvShowTime, tvSeats, tvTotalAmount;
    private TextView tvOrderCode, tvBookingId, tvExpiresAt;
    private Button btnPayNow, btnCancel;

    private String checkoutUrl;
    private Handler handler;
    private Runnable checkPaymentRunnable;
    private boolean isPaymentWindowOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        handler = new Handler(Looper.getMainLooper());

        initViews();
        loadBookingData();
        setupClickListeners();
    }

    private void initViews() {
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvShowDate = findViewById(R.id.tvShowDate);
        tvShowTime = findViewById(R.id.tvShowTime);
        tvSeats = findViewById(R.id.tvSeats);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvOrderCode = findViewById(R.id.tvOrderCode);
        tvBookingId = findViewById(R.id.tvBookingId);
        tvExpiresAt = findViewById(R.id.tvExpiresAt);
        btnPayNow = findViewById(R.id.btnPayNow);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadBookingData() {
        Intent intent = getIntent();
        if (intent != null) {
            displayBookingInfo(intent);
        } else {
            Toast.makeText(this, "Không có thông tin đặt vé", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayBookingInfo(Intent intent) {
        // Lấy thông tin booking
        String movieTitle = intent.getStringExtra("movieTitle");
        String showDate = intent.getStringExtra("showDate");
        String showTime = intent.getStringExtra("showTime");
        String seats = intent.getStringExtra("seats");
        double totalAmount = intent.getDoubleExtra("totalAmount", 0);
        long orderCode = intent.getLongExtra("orderCode", 0);
        String bookingId = intent.getStringExtra("bookingId");
        checkoutUrl = intent.getStringExtra("checkoutUrl");
        String expiresAt = intent.getStringExtra("expiresAt");

        // Hiển thị thông tin
        tvMovieTitle.setText(movieTitle != null ? movieTitle : "Unknown Movie");
        tvShowDate.setText(formatDate(showDate));
        tvShowTime.setText(formatTime(showTime));
        tvSeats.setText(seats);

        // Format số tiền
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedAmount = formatter.format(totalAmount) + " VNĐ";
        tvTotalAmount.setText(formattedAmount);

        tvOrderCode.setText(String.valueOf(orderCode));
        tvBookingId.setText(bookingId);

        // Format thời gian hết hạn
        if (expiresAt != null) {
            String expiresMessage = "Vui lòng thanh toán trước: " + formatDateTime(expiresAt);
            tvExpiresAt.setText(expiresMessage);
        }
    }

    private String formatDate(String date) {
        if (date == null) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date d = inputFormat.parse(date);
            if (d != null) {
                return outputFormat.format(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String formatTime(String time) {
        if (time == null) return "";
        // Nếu time có format "HH:mm:ss", chỉ lấy HH:mm
        if (time.length() > 5) {
            return time.substring(0, 5);
        }
        return time;
    }

    private String formatDateTime(String dateTime) {
        if (dateTime == null) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
            Date d = inputFormat.parse(dateTime);
            if (d != null) {
                return outputFormat.format(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private void setupClickListeners() {
        btnPayNow.setOnClickListener(v -> openPaymentUrl());
        btnCancel.setOnClickListener(v -> showCancelDialog());
    }

    private void openPaymentUrl() {
        if (checkoutUrl != null && !checkoutUrl.isEmpty()) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkoutUrl));
                startActivity(browserIntent);

                isPaymentWindowOpened = true;

                // Hiển thị thông báo hướng dẫn
                Toast.makeText(this, "Đang chuyển đến trang thanh toán...", Toast.LENGTH_SHORT).show();

                // Thay đổi text của button thành "Đã thanh toán"
                btnPayNow.setText("Đã thanh toán xong");
                btnPayNow.setOnClickListener(v -> showPaymentCompleteDialog());

            } catch (Exception e) {
                Toast.makeText(this, "Không thể mở trang thanh toán: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không có link thanh toán", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPaymentCompleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Bạn đã hoàn tất thanh toán?\n\nNếu đã thanh toán thành công, vui lòng kiểm tra email để xem chi tiết vé.")
                .setPositiveButton("Đã thanh toán", (dialog, which) -> {
                    Toast.makeText(this, "Cảm ơn bạn đã đặt vé!", Toast.LENGTH_SHORT).show();
                    // Đóng activity và quay về màn hình chính
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Chưa thanh toán", null)
                .show();
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hủy đặt vé")
                .setMessage("Bạn có chắc chắn muốn hủy đặt vé này?\nVé chưa thanh toán sẽ tự động hủy sau 15 phút.")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Quay lại màn hình chính
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Nếu đã mở trang thanh toán, hiển thị dialog hỏi người dùng
        if (isPaymentWindowOpened) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isFinishing()) {
                    showReturnFromPaymentDialog();
                }
            }, 1000); // Đợi 1 giây sau khi quay lại app
        }
    }

    private void showReturnFromPaymentDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Trạng thái thanh toán")
                .setMessage("Bạn đã hoàn tất thanh toán chưa?")
                .setPositiveButton("Đã thanh toán", (dialog, which) -> {
                    Toast.makeText(this, "Cảm ơn bạn! Vui lòng kiểm tra email để xem chi tiết vé.", Toast.LENGTH_LONG).show();
                    // Đóng activity và quay về màn hình chính
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Chưa xong", (dialog, which) -> {
                    // Reset flag để không hiển thị dialog lần nữa khi onResume
                    isPaymentWindowOpened = false;
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        showCancelDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && checkPaymentRunnable != null) {
            handler.removeCallbacks(checkPaymentRunnable);
        }
    }
}
