package com.ptithcm.moviebooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.BookingDetailResponse;
import com.ptithcm.moviebooking.schema.BookingResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvMovieTitle, tvShowDate, tvShowTime, tvSeats, tvTotalAmount;
    private TextView tvOrderCode, tvBookingId, tvExpiresAt;
    private Button btnCancel, btnShowQr;
    private ImageView ivQrCode;
    private androidx.cardview.widget.CardView cardQrCode;

    private String qrCodeUrl;
    private String bookingId;
    private boolean isQrCodeVisible = false;

    private Handler paymentCheckHandler;
    private Runnable paymentCheckRunnable;
    private static final long PAYMENT_CHECK_INTERVAL = 5000; // 5 seconds
    private boolean isCheckingPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentCheckHandler = new Handler(Looper.getMainLooper());

        initViews();
        loadBookingData();
        setupClickListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPaymentStatusCheck();
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
        btnCancel = findViewById(R.id.btnCancel);
        btnShowQr = findViewById(R.id.btnShowQr);
        ivQrCode = findViewById(R.id.ivQrCode);
        cardQrCode = findViewById(R.id.cardQrCode);
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
        this.bookingId = intent.getStringExtra("bookingId");
        qrCodeUrl = intent.getStringExtra("qrCode");
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
        btnShowQr.setOnClickListener(v -> toggleQrCodeDisplay());
        btnCancel.setOnClickListener(v -> showCancelDialog());
    }

    private void toggleQrCodeDisplay() {
        if (isQrCodeVisible) {
            // Ẩn QR code và dừng kiểm tra thanh toán
            cardQrCode.setVisibility(android.view.View.GONE);
            btnShowQr.setText("Hiển thị mã QR để thanh toán");
            isQrCodeVisible = false;
            stopPaymentStatusCheck();
        } else {
            // Hiển thị QR code
            if (qrCodeUrl != null && !qrCodeUrl.isEmpty()) {
                loadQrCode();
                cardQrCode.setVisibility(android.view.View.VISIBLE);
                btnShowQr.setText("Ẩn mã QR");
                isQrCodeVisible = true;
                
                // Bắt đầu kiểm tra trạng thái thanh toán
                startPaymentStatusCheck();

                // Scroll xuống để người dùng thấy QR code
                new Handler(Looper.getMainLooper()).postDelayed(() -> cardQrCode.requestFocus(), 100);
            } else {
                Toast.makeText(this, "Không có mã QR để hiển thị", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadQrCode() {
        if (qrCodeUrl != null && !qrCodeUrl.isEmpty()) {
            try {
                // Tạo QR code từ chuỗi text
                Bitmap qrBitmap = generateQRCode(qrCodeUrl, 800, 800);
                if (qrBitmap != null) {
                    ivQrCode.setImageBitmap(qrBitmap);
                } else {
                    Toast.makeText(this, "Không thể tạo mã QR", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi tạo mã QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Tạo QR code bitmap từ chuỗi text
     */
    private Bitmap generateQRCode(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hủy đặt vé")
                .setMessage("Bạn có chắc chắn muốn hủy đặt vé này?\n\nVé chưa thanh toán sẽ tự động hủy sau 5 phút.")
                .setPositiveButton("Hủy vé", (dialog, which) -> {
                    Toast.makeText(this, "Đã hủy đặt vé", Toast.LENGTH_SHORT).show();
                    // Quay lại màn hình chính
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    /**
     * Bắt đầu kiểm tra trạng thái thanh toán định kỳ (mỗi 5 giây)
     */
    private void startPaymentStatusCheck() {
        if (bookingId == null || bookingId.isEmpty()) {
            return;
        }

        stopPaymentStatusCheck(); // Dừng bất kỳ check nào đang chạy

        paymentCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkPaymentStatus();
                // Lên lịch kiểm tra tiếp theo
                paymentCheckHandler.postDelayed(this, PAYMENT_CHECK_INTERVAL);
            }
        };

        // Bắt đầu kiểm tra ngay lập tức
        paymentCheckHandler.post(paymentCheckRunnable);
    }

    /**
     * Dừng kiểm tra trạng thái thanh toán
     */
    private void stopPaymentStatusCheck() {
        if (paymentCheckHandler != null && paymentCheckRunnable != null) {
            paymentCheckHandler.removeCallbacks(paymentCheckRunnable);
        }
    }

    /**
     * Gọi API để kiểm tra trạng thái thanh toán
     */
    private void checkPaymentStatus() {
        if (isCheckingPayment || bookingId == null || bookingId.isEmpty()) {
            return;
        }

        isCheckingPayment = true;

        RetrofitClient.getInstance(this).getApiService().getBookingDetail(bookingId)
                .enqueue(new Callback<BookingDetailResponse>() {
                    @Override
                    public void onResponse(Call<BookingDetailResponse> call, Response<BookingDetailResponse> response) {
                        isCheckingPayment = false;

                        if (response.isSuccessful() && response.body() != null) {
                            BookingDetailResponse bookingResponse = response.body();

                            if (bookingResponse.isSuccess() && bookingResponse.getData() != null) {
                                String status = bookingResponse.getData().getStatus();

                                if (status != null) {
                                    handlePaymentStatus(status);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BookingDetailResponse> call, Throwable t) {
                        isCheckingPayment = false;
                        // Không hiển thị lỗi để không làm phiền người dùng
                    }
                });
    }

    /**
     * Xử lý trạng thái thanh toán
     */
    private void handlePaymentStatus(String status) {
        switch (status.toLowerCase()) {
            case "paid":
            case "confirmed":
                // Dừng kiểm tra và hiển thị dialog thành công
                stopPaymentStatusCheck();
                showPaymentSuccessDialog();
                break;
            case "expired":
            case "cancelled":
                // Dừng kiểm tra và hiển thị thông báo
                stopPaymentStatusCheck();
                showPaymentExpiredDialog();
                break;
            case "pending":
                // Tiếp tục chờ
                break;
        }
    }

    /**
     * Hiển thị dialog thanh toán thành công và quay về trang chủ
     */
    private void showPaymentSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thanh toán thành công! 🎉")
                .setMessage("Vé của bạn đã được thanh toán thành công!\n\nVui lòng kiểm tra email để xem chi tiết vé.")
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Hiển thị dialog khi vé hết hạn hoặc bị hủy
     */
    private void showPaymentExpiredDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Vé đã hết hạn")
                .setMessage("Vé của bạn đã hết hạn hoặc đã bị hủy.\n\nVui lòng đặt vé mới.")
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}
