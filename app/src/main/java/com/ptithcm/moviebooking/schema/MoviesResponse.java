package com.ptithcm.moviebooking.schema;

import com.google.gson.annotations.SerializedName;
import com.ptithcm.moviebooking.models.Movie;

import java.util.List;

public class MoviesResponse {
    private boolean success;
    private String message;
    private List<Movie> data;

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("total_pages")
    private int totalPages;

    public MoviesResponse() {
    }

    public MoviesResponse(boolean success, String message, List<Movie> data, int currentPage, int totalPages) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
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

    public List<Movie> getData() {
        return data;
    }

    public void setData(List<Movie> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
