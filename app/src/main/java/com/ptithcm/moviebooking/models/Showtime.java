package com.ptithcm.moviebooking.models;

import com.google.gson.annotations.SerializedName;

public class Showtime {
    @SerializedName("showtimeId")
    private String id;

    @SerializedName("movieId")
    private int movieId;

    @SerializedName("showDate")
    private String showDate;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("availableSeats")
    private int availableSeats;

    @SerializedName("totalSeats")
    private int totalSeats;

    @SerializedName("movie")
    private Movie movie;

    public Showtime() {
    }

    public Showtime(String id, int movieId, String showDate, String startTime, String endTime,
                    int availableSeats, int totalSeats) {
        this.id = id;
        this.movieId = movieId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableSeats = availableSeats;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return startTime;
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

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

