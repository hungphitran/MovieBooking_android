package com.ptithcm.moviebooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.moviebooking.adapters.MovieAdapter;
import com.ptithcm.moviebooking.api.ApiService;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.models.Movie;
import com.ptithcm.moviebooking.schema.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private LinearLayout paginationLayout;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView pageInfoTextView;
    private ApiService apiService;

    private int currentPage = 1;
    private int totalPages = 1;
    private List<Movie> currentMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        apiService = RetrofitClient.getInstance(this).getApiService();

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadMoviesFromApi(currentPage);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_movies);
        paginationLayout = findViewById(R.id.pagination_layout);
        progressBar = findViewById(R.id.progressBar);
        pageInfoTextView = findViewById(R.id.page_info_text);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.all_movies);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(currentMovies, movie -> {
            Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMoviesFromApi(int page) {
        // Hiển thị loading
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        paginationLayout.setVisibility(View.GONE);
        if (pageInfoTextView != null) {
            pageInfoTextView.setVisibility(View.GONE);
        }

        apiService.getMoviesWithPagination(page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse.isSuccess() && moviesResponse.getData() != null) {
                        currentMovies = moviesResponse.getData();

                        // Lấy current_page và total_pages từ API response
                        currentPage = moviesResponse.getCurrentPage();
                        totalPages = moviesResponse.getTotalPages();

                        // Cập nhật adapter với dữ liệu mới
                        adapter.updateMovies(currentMovies);

                        // Setup pagination với thông tin từ API
                        setupPagination();

                        // Cập nhật thông tin trang
                        updatePageInfo();

                        recyclerView.setVisibility(View.VISIBLE);
                        paginationLayout.setVisibility(View.VISIBLE);
                        if (pageInfoTextView != null) {
                            pageInfoTextView.setVisibility(View.VISIBLE);
                        }

                        // Scroll to top
                        recyclerView.smoothScrollToPosition(0);
                    } else {
                        showError("Không thể tải danh sách phim: " + moviesResponse.getMessage());
                    }
                } else {
                    showError("Lỗi tải danh sách phim: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void updatePageInfo() {
        if (pageInfoTextView != null) {
            pageInfoTextView.setText(String.format("Trang %d / %d", currentPage, totalPages));
        }
    }

    private void setupPagination() {
        paginationLayout.removeAllViews();

        // Add "First" button if not on first page
        if (currentPage > 1) {
            Button btnFirst = createPaginationButton("⏮ Đầu");
            btnFirst.setOnClickListener(v -> loadMoviesFromApi(1));
            paginationLayout.addView(btnFirst);
        }

        // Add "Previous" button
        if (currentPage > 1) {
            Button btnPrev = createPaginationButton("◀ Trước");
            btnPrev.setOnClickListener(v -> loadMoviesFromApi(currentPage - 1));
            paginationLayout.addView(btnPrev);
        }

        // Add page number buttons (show 5 pages at a time)
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        // Adjust to always show 5 pages if possible
        if (endPage - startPage < 4) {
            if (startPage == 1) {
                endPage = Math.min(totalPages, startPage + 4);
            } else if (endPage == totalPages) {
                startPage = Math.max(1, endPage - 4);
            }
        }

        for (int i = startPage; i <= endPage; i++) {
            Button btn = createPaginationButton(String.valueOf(i));
            final int pageNumber = i;

            if (i == currentPage) {
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                btn.setTextColor(getResources().getColor(android.R.color.white));
            }

            btn.setOnClickListener(v -> loadMoviesFromApi(pageNumber));
            paginationLayout.addView(btn);
        }

        // Add "Next" button
        if (currentPage < totalPages) {
            Button btnNext = createPaginationButton("Sau ▶");
            btnNext.setOnClickListener(v -> loadMoviesFromApi(currentPage + 1));
            paginationLayout.addView(btnNext);
        }

        // Add "Last" button if not on last page
        if (currentPage < totalPages) {
            Button btnLast = createPaginationButton("Cuối ⏭");
            btnLast.setOnClickListener(v -> loadMoviesFromApi(totalPages));
            paginationLayout.addView(btnLast);
        }
    }

    private Button createPaginationButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setPadding(16, 8, 16, 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(4, 0, 4, 0);
        btn.setLayoutParams(params);
        return btn;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        recyclerView.setVisibility(View.GONE);
        paginationLayout.setVisibility(View.GONE);
        if (pageInfoTextView != null) {
            pageInfoTextView.setVisibility(View.GONE);
        }
    }
}
