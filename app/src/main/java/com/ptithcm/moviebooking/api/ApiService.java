package com.ptithcm.moviebooking.api;

import com.ptithcm.moviebooking.schema.AuthResponse;
import com.ptithcm.moviebooking.schema.BookingRequest;
import com.ptithcm.moviebooking.schema.BookingResponse;
import com.ptithcm.moviebooking.schema.BookingsResponse;
import com.ptithcm.moviebooking.schema.ChangePasswordRequest;
import com.ptithcm.moviebooking.schema.ChangePasswordResponse;
import com.ptithcm.moviebooking.schema.LoginRequest;
import com.ptithcm.moviebooking.schema.MovieDetailResponse;
import com.ptithcm.moviebooking.schema.MoviesResponse;
import com.ptithcm.moviebooking.schema.RegisterRequest;
import com.ptithcm.moviebooking.schema.ShowtimeDetailResponse;
import com.ptithcm.moviebooking.schema.ShowtimesResponse;
import com.ptithcm.moviebooking.schema.UpdateProfileRequest;
import com.ptithcm.moviebooking.schema.UpdateProfileResponse;
import com.ptithcm.moviebooking.schema.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @GET("user/profile")
    Call<UserResponse> getUserProfile();

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

    // Lấy tất cả phim với phân trang
    @GET("movies/get-all")
    Call<MoviesResponse> getMoviesWithPagination(@Query("page") int page);

    // Tìm kiếm phim theo từ khóa
    @GET("movies/get-all")
    Call<MoviesResponse> searchMovies(@Query("q") String query);

    // Cập nhật thông tin tài khoản
    @PUT("user/update")
    Call<UpdateProfileResponse> updateProfile(@Body UpdateProfileRequest updateProfileRequest);

    // Đổi mật khẩu
    @POST("auth/change-password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    // Lấy danh sách showtimes
    @GET("showtimes")
    Call<ShowtimesResponse> getShowtimes();

    // Lấy showtimes theo movie ID
    @GET("showtimes")
    Call<ShowtimesResponse> getShowtimesByMovie(@Query("movie_id") int movieId);

    // Lấy chi tiết showtime (bao gồm ghế ngồi)
    @GET("showtimes/{id}")
    Call<ShowtimeDetailResponse> getShowtimeDetail(@Path("id") String showtimeId);

    // Đặt vé
    @POST("bookings/create")
    Call<BookingResponse> createBooking(@Body BookingRequest bookingRequest);

    // Lấy danh sách bookings của user
    @GET("bookings")
    Call<BookingsResponse> getUserBookings();
}
