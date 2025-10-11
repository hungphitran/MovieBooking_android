package com.ptithcm.moviebooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.models.Booking;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<Booking> bookingList;

    public BookingAdapter(Context context) {
        this.context = context;
        this.bookingList = new ArrayList<>();
    }

    public void setBookingList(List<Booking> bookingList) {
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
        Booking booking = bookingList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookingId;
        private TextView tvShowtimeId;
        private TextView tvSeats;
        private TextView tvTotalPrice;
        private TextView tvBookingDate;
        private TextView tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvShowtimeId = itemView.findViewById(R.id.tvShowtimeId);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(Booking booking) {
            tvBookingId.setText("Mã đặt: " + booking.getId());
            tvShowtimeId.setText("Suất chiếu: " + booking.getShowtimeId());

            // Format danh sách ghế
            if (booking.getSeatIds() != null && !booking.getSeatIds().isEmpty()) {
                StringBuilder seatsText = new StringBuilder("Ghế: ");
                for (int i = 0; i < booking.getSeatIds().size(); i++) {
                    String seatId = booking.getSeatIds().get(i);
                    // Lấy số ghế từ ID (format: A1_showtimeId)
                    String seatNumber = seatId.split("_")[0];
                    seatsText.append(seatNumber);
                    if (i < booking.getSeatIds().size() - 1) {
                        seatsText.append(", ");
                    }
                }
                tvSeats.setText(seatsText.toString());
            } else {
                tvSeats.setText("Ghế: N/A");
            }

            // Format giá tiền
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvTotalPrice.setText("Tổng tiền: " + formatter.format(booking.getTotalPrice()) + " VNĐ");

            tvBookingDate.setText("Ngày đặt: " + booking.getBookingDate());
            tvStatus.setText("Trạng thái: " + booking.getStatus());
        }
    }
}

