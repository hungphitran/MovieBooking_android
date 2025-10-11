package com.ptithcm.moviebooking.schema;

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
        private Showtime showtime;
        private List<Seat> seats;

        public ShowtimeDetail() {
        }

        public Showtime getShowtime() {
            return showtime;
        }

        public void setShowtime(Showtime showtime) {
            this.showtime = showtime;
        }

        public List<Seat> getSeats() {
            return seats;
        }

        public void setSeats(List<Seat> seats) {
            this.seats = seats;
        }
    }
}

