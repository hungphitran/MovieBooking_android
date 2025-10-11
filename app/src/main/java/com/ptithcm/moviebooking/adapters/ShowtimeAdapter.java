package com.ptithcm.moviebooking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.ShowtimeDetailActivity;
import com.ptithcm.moviebooking.models.Showtime;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {
    private Context context;
    private List<Showtime> showtimeList;

    public ShowtimeAdapter(Context context) {
        this.context = context;
        this.showtimeList = new ArrayList<>();
    }

    public void setShowtimeList(List<Showtime> showtimeList) {
        this.showtimeList = showtimeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);
        holder.bind(showtime);
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieTitle;
        private TextView tvShowDate;
        private TextView tvStartTime;
        private TextView tvEndTime;
        private TextView tvAvailableSeats;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvShowDate = itemView.findViewById(R.id.tvShowDate);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvAvailableSeats = itemView.findViewById(R.id.tvAvailableSeats);
        }

        public void bind(Showtime showtime) {
            if (showtime.getMovie() != null) {
                tvMovieTitle.setText(showtime.getMovie().getTitle());
            } else {
                tvMovieTitle.setText("Movie ID: " + showtime.getMovieId());
            }

            tvShowDate.setText(showtime.getShowDate());
            tvStartTime.setText(showtime.getStartTime());
            tvEndTime.setText(showtime.getEndTime());
            tvAvailableSeats.setText(showtime.getAvailableSeats() + "/" + showtime.getTotalSeats() + " ghế trống");

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ShowtimeDetailActivity.class);
                intent.putExtra("showtimeId", showtime.getId());
                intent.putExtra("movieTitle", showtime.getMovie() != null ? showtime.getMovie().getTitle() : "");
                context.startActivity(intent);
            });
        }
    }
}

