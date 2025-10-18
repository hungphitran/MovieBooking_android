package com.ptithcm.moviebooking.schema;

import android.util.Log;
import com.ptithcm.moviebooking.models.Movie;
import com.ptithcm.moviebooking.models.Seat;
import com.ptithcm.moviebooking.models.Showtime;

import java.util.List;

public class ShowtimeDetailResponse {
    private boolean success;
    private String message;
    private ShowtimeDetail data;

    public ShowtimeDetailResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ShowtimeDetail getData() {
        return data;
    }

    public void setData(ShowtimeDetail data) {
        this.data = data;
    }

    public static class ShowtimeDetail {
        private int movieId;
        private String showDate;
        private String startTime;
        private String endTime;
        private String showtimeId;
        private Movie movie;
        private String createdAt;  // Changed from Date to String
        private String updatedAt;  // Changed from Date to String
        private List<Seat> seats;

        public ShowtimeDetail() {
        }

        public int getMovieId() {
            return movieId;
        }
        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }
        public String getShowDate() {
            return showDate;
        }
        public void setShowDate(String showDate) {
            this.showDate = showDate;
        }
        public String getStartTime() {
            return this.startTime;
        }
        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
        public String getEndTime() {
            return endTime;
        }
        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
        public String getShowtimeId() {
            return showtimeId;
        }
        public void setShowtimeId(String showtimeId) {
            this.showtimeId = showtimeId;
        }
        public Movie getMovie() {
            return movie;
        }
        public void setMovie(Movie movie) {
            this.movie = movie;
        }
        public String getCreatedAt() {
            return createdAt;
        }
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
        public String getUpdatedAt() {
            return updatedAt;
        }
        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<Seat> getSeats() {
            return seats;
        }

        public void setSeats(List<Seat> seats) {
            this.seats = seats;
        }

        /**
         * Convert ShowtimeDetail to Showtime object
         * Maps all fields correctly from ShowtimeDetail to Showtime
         */
        public Showtime toShowtime() {
            Log.d("ShowtimeDetail", "========== toShowtime() START ==========");
            Log.d("ShowtimeDetail", "showtimeId: " + this.showtimeId);
            Log.d("ShowtimeDetail", "movieId: " + this.movieId);
            Log.d("ShowtimeDetail", "showDate: " + this.showDate);
            Log.d("ShowtimeDetail", "startTime: " + this.startTime);
            Log.d("ShowtimeDetail", "endTime: " + this.endTime);
            Log.d("ShowtimeDetail", "movie: " + (this.movie != null ? "NOT NULL" : "NULL"));
            if (this.movie != null) {
                Log.d("ShowtimeDetail", "movie.getId(): " + this.movie.getId());
                Log.d("ShowtimeDetail", "movie.getTitle(): " + this.movie.getTitle());
                Log.d("ShowtimeDetail", "movie.getBudget(): " + this.movie.getBudget());
            }
            Log.d("ShowtimeDetail", "seats: " + (this.seats != null ? this.seats.size() + " seats" : "NULL"));

            Showtime showtime = new Showtime();
            showtime.setId(this.showtimeId);
            showtime.setMovieId(this.movieId);
            showtime.setShowDate(this.showDate);
            showtime.setStartTime(this.startTime);
            showtime.setEndTime(this.endTime);
            showtime.setMovie(this.movie);

            // Calculate available seats and total seats from the seats list
            if (this.seats != null) {
                showtime.setTotalSeats(this.seats.size());
                int availableSeats = 0;
                for (Seat seat : this.seats) {
                    if (seat.getStatus().equals("available")) {
                        availableSeats++;
                    }
                }
                showtime.setAvailableSeats(availableSeats);
                Log.d("ShowtimeDetail", "Total seats: " + this.seats.size() + ", Available: " + availableSeats);
            }

            Log.d("ShowtimeDetail", "Showtime created - movie in showtime: " + (showtime.getMovie() != null ? "NOT NULL" : "NULL"));
            if (showtime.getMovie() != null) {
                Log.d("ShowtimeDetail", "Showtime movie budget: " + showtime.getMovie().getBudget());
            }
            Log.d("ShowtimeDetail", "========== toShowtime() END ==========");

            return showtime;
        }
    }
}
