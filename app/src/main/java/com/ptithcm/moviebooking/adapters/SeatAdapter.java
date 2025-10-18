package com.ptithcm.moviebooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.models.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private Context context;
    private List<Seat> seatList;
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }

    public SeatAdapter(Context context, OnSeatClickListener listener) {
        this.context = context;
        this.seatList = new ArrayList<>();
        this.listener = listener;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.bind(seat);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    class SeatViewHolder extends RecyclerView.ViewHolder {
        private CardView cardSeat;
        private TextView tvSeatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSeat = itemView.findViewById(R.id.cardSeat);
            tvSeatNumber = itemView.findViewById(R.id.tvSeatNumber);
        }

        public void bind(Seat seat) {
            tvSeatNumber.setText(seat.getSeatName());

            // Thiết lập màu sắc dựa trên trạng thái ghế
            if (seat.isBooked()) {
                // Ghế đã được đặt - màu xám
                cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_booked));
                cardSeat.setEnabled(false);
            } else if (seat.isReserved()) {
                // Ghế đang được giữ chỗ - màu vàng cam
                cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_reserved));
                cardSeat.setEnabled(false);
            } else if (seat.isSelected()) {
                // Ghế đang chọn - màu xanh lá
                cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_selected));
                cardSeat.setEnabled(true);
            } else {
                // Ghế trống - màu trắng
                cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_available));
                cardSeat.setEnabled(true);
            }

            // Xử lý sự kiện click
            cardSeat.setOnClickListener(v -> {
                if (!seat.isBooked() && !seat.isReserved() && listener != null) {
                    seat.setSelected(!seat.isSelected());
                    listener.onSeatClick(seat);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
