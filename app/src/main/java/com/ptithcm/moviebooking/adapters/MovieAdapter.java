package com.ptithcm.moviebooking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView ivPoster;
        private TextView tvTitle, tvGenre, tvReleaseDate, tvRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardMovie);
            ivPoster = itemView.findViewById(R.id.ivMoviePoster);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvGenre = itemView.findViewById(R.id.tvMovieGenre);
            tvReleaseDate = itemView.findViewById(R.id.tvMovieReleaseDate);
            tvRating = itemView.findViewById(R.id.tvMovieRating);
        }

        public void bind(Movie movie, OnMovieClickListener listener) {
            tvTitle.setText(movie.getTitle());
            tvGenre.setText(movie.getGenre());
            tvReleaseDate.setText("📅 " + movie.getReleaseDate());
            tvRating.setText(String.format("⭐ %.1f", movie.getRating()));

            // Load image from URL using Glide
            Glide.with(itemView.getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.movie_item)
                    .error(R.drawable.movie_item)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPoster);

            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}
