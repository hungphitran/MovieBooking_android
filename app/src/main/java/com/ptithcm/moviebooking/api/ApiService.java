package com.ptithcm.moviebooking.api;

import com.ptithcm.moviebooking.models.AuthResponse;
import com.ptithcm.moviebooking.models.LoginRequest;
import com.ptithcm.moviebooking.models.MovieDetailResponse;
import com.ptithcm.moviebooking.models.MoviesResponse;
import com.ptithcm.moviebooking.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @GET("movies/detail/{id}")
    Call<MovieDetailResponse> getMovieDetail(@Path("id") int movieId);

    // Lấy danh sách phim đang chiếu
    @GET("movies/get-all")
    Call<MoviesResponse> getNowShowingMovies();

    // Lấy danh sách phim sắp chiếu
    @GET("movies/get-all")
    Call<MoviesResponse> getComingSoonMovies();

    // Lấy tất cả phim
    @GET("movies")
    Call<MoviesResponse> getAllMovies();
}
