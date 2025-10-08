package com.ptithcm.moviebooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.moviebooking.MovieDetailActivity;
import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.adapters.MovieAdapter;
import com.ptithcm.moviebooking.api.ApiService;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.MoviesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvNowShowing, rvComingSoon;
    private MovieAdapter nowShowingAdapter, comingSoonAdapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo API service
        apiService = RetrofitClient.getInstance(requireContext()).getApiService();

        initViews(view);
        setupRecyclerViews();
        loadMovies();
    }

    private void initViews(View view) {
        rvNowShowing = view.findViewById(R.id.rvNowShowing);
        rvComingSoon = view.findViewById(R.id.rvComingSoon);
    }

    private void setupRecyclerViews() {
        // Setup Now Showing RecyclerView
        rvNowShowing.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        nowShowingAdapter = new MovieAdapter(new ArrayList<>(), movie -> {
            openMovieDetail(movie.getId());
        });
        rvNowShowing.setAdapter(nowShowingAdapter);

        // Setup Coming Soon RecyclerView
        rvComingSoon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        comingSoonAdapter = new MovieAdapter(new ArrayList<>(), movie -> {
            openMovieDetail(movie.getId());
        });
        rvComingSoon.setAdapter(comingSoonAdapter);
    }

    private void openMovieDetail(int movieId) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieId);
        startActivity(intent);
    }

    private void loadMovies() {
        // Gọi API lấy phim đang chiếu
        loadNowShowingMovies();

        // Gọi API lấy phim sắp chiếu
        loadComingSoonMovies();
    }

    private void loadNowShowingMovies() {
        apiService.getNowShowingMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse.isSuccess() && moviesResponse.getData() != null) {
                        nowShowingAdapter.updateMovies(moviesResponse.getData());
                    } else {
                        Toast.makeText(getContext(),
                            "Không thể tải phim đang chiếu: " + moviesResponse.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                        "Lỗi tải phim đang chiếu: " + response.code(),
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getContext(),
                    "Lỗi kết nối: " + t.getMessage(),
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComingSoonMovies() {
        apiService.getComingSoonMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse.isSuccess() && moviesResponse.getData() != null) {
                        comingSoonAdapter.updateMovies(moviesResponse.getData());
                    } else {
                        Toast.makeText(getContext(),
                            "Không thể tải phim sắp chiếu: " + moviesResponse.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                        "Lỗi tải phim sắp chiếu: " + response.code(),
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getContext(),
                    "Lỗi kết nối: " + t.getMessage(),
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
}
