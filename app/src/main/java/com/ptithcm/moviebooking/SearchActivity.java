package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.moviebooking.adapters.MovieAdapter;
import com.ptithcm.moviebooking.api.ApiService;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.MoviesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText etSearchQuery;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBar;
    private TextView tvNoResults;
    private MovieAdapter movieAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupSearchListener();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etSearchQuery = findViewById(R.id.etSearchQuery);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        progressBar = findViewById(R.id.progressBar);
        tvNoResults = findViewById(R.id.tvNoResults);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Tìm kiếm phim");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        // Sử dụng LinearLayoutManager để hiển thị danh sách theo chiều dọc
        // phù hợp với layout item_movie (layout ngang)
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(new ArrayList<>(), movie -> {
            Intent intent = new Intent(SearchActivity.this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        rvSearchResults.setAdapter(movieAdapter);
    }

    private void setupSearchListener() {
        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không làm gì
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    searchMovies(query);
                } else if (query.isEmpty()) {
                    // Xóa kết quả khi không có từ khóa
                    movieAdapter.updateMovies(new ArrayList<>());
                    tvNoResults.setVisibility(View.GONE);
                    rvSearchResults.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không làm gì
            }
        });

        // Focus vào ô tìm kiếm khi mở activity
        etSearchQuery.requestFocus();
    }

    private void searchMovies(String query) {
        // Hiển thị loading
        progressBar.setVisibility(View.VISIBLE);
        tvNoResults.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.GONE);

        apiService.searchMovies(query).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse.isSuccess() && moviesResponse.getData() != null) {
                        if (moviesResponse.getData().isEmpty()) {
                            // Không tìm thấy kết quả
                            tvNoResults.setVisibility(View.VISIBLE);
                            tvNoResults.setText("Không tìm thấy phim nào với từ khóa \"" + query + "\"");
                            rvSearchResults.setVisibility(View.GONE);
                        } else {
                            // Hiển thị kết quả
                            movieAdapter.updateMovies(moviesResponse.getData());
                            rvSearchResults.setVisibility(View.VISIBLE);
                            tvNoResults.setVisibility(View.GONE);
                        }
                    } else {
                        showError("Không thể tìm kiếm: " + moviesResponse.getMessage());
                    }
                } else {
                    showError("Lỗi tìm kiếm: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        tvNoResults.setVisibility(View.VISIBLE);
        tvNoResults.setText(message);
        rvSearchResults.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
