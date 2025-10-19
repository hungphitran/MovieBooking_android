package com.ptithcm.moviebooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.models.BookingDetail;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<BookingDetail> bookingList;

    public BookingAdapter(Context context) {
        this.context = context;
        this.bookingList = new ArrayList<>();
    }

    public void setBookingList(List<BookingDetail> bookingList) {
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingDetail booking = bookingList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieTitle;
        private TextView tvBookingId;
        private TextView tvShowDateTime;
        private TextView tvSeats;
        private TextView tvTotalPrice;
        private TextView tvPaymentMethod;
        private TextView tvBookingDate;
        private TextView tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvShowDateTime = itemView.findViewById(R.id.tvShowDateTime);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(BookingDetail booking) {
            // Movie title
            tvMovieTitle.setText(booking.getMovieTitle() != null ? booking.getMovieTitle() : "N/A");

            // Booking ID
            tvBookingId.setText("Mã đặt: " + (booking.getBookingId() != null ? booking.getBookingId() : "N/A"));

            // Show date and time
            String showDate = booking.getShowDate() != null ? booking.getShowDate() : "";
            String showTime = booking.getShowTime() != null ? booking.getShowTime() : "";
            tvShowDateTime.setText("Ngày chiếu: " + showDate + " | " + showTime);

            // Format danh sách ghế
            if (booking.getSeats() != null && !booking.getSeats().isEmpty()) {
                StringBuilder seatsText = new StringBuilder("Ghế: ");
                for (int i = 0; i < booking.getSeats().size(); i++) {
                    BookingDetail.SeatInfo seat = booking.getSeats().get(i);
                    seatsText.append(seat.getSeatLabel());
                    if (i < booking.getSeats().size() - 1) {
                        seatsText.append(", ");
                    }
                }
                tvSeats.setText(seatsText.toString());
            } else {
                tvSeats.setText("Ghế: N/A");
            }

            // Format giá tiền
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvTotalPrice.setText("Tổng tiền: " + formatter.format(booking.getTotalAmount()) + " VNĐ");

            // Payment method
            tvPaymentMethod.setText("Thanh toán: " + (booking.getPaymentMethod() != null ? booking.getPaymentMethod() : "N/A"));

            // Format booking date
            String createdAt = booking.getCreatedAt();
            if (createdAt != null) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    Date date = inputFormat.parse(createdAt);
                    tvBookingDate.setText("Đặt lúc: " + outputFormat.format(date));
                } catch (ParseException e) {
                    tvBookingDate.setText("Đặt lúc: " + createdAt);
                }
            } else {
                tvBookingDate.setText("Đặt lúc: N/A");
            }

            // Status with color
            String status = booking.getStatus() != null ? booking.getStatus() : "N/A";
            tvStatus.setText(getStatusText(status));
            tvStatus.setTextColor(getStatusColor(status));
        }

        private String getStatusText(String status) {
            switch (status.toLowerCase()) {
                case "confirmed":
                    return "Đã xác nhận";
                case "pending":
                    return "Đang chờ";
                case "cancelled":
                    return "Đã hủy";
                case "expired":
                    return "Hết hạn";
                default:
                    return status;
            }
        }

        private int getStatusColor(String status) {
            switch (status.toLowerCase()) {
                case "confirmed":
                    return 0xFF4CAF50; // Green
                case "pending":
                    return 0xFFFFA726; // Orange
                case "cancelled":
                    return 0xFFF44336; // Red
                case "expired":
                    return 0xFF9E9E9E; // Gray
                default:
                    return 0xFF2196F3; // Blue
            }
        }
    }
}
