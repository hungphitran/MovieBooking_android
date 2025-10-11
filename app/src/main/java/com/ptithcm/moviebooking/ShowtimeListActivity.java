package com.ptithcm.moviebooking;

import android.os.Bundle;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ptithcm.moviebooking.adapters.ShowtimeAdapter;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.schema.ShowtimesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowtimeListActivity extends AppCompatActivity {
    private RecyclerView rvShowtimes;
    private ShowtimeAdapter showtimeAdapter;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private SwipeRefreshLayout swipeRefresh;
    private int movieId = -1;
    private String movieTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_list);

        // Get movieId from intent if provided
        if (getIntent().hasExtra("movieId")) {
            movieId = getIntent().getIntExtra("movieId", -1);
        }

        // Get movieTitle from intent if provided
        if (getIntent().hasExtra("movieTitle")) {
            movieTitle = getIntent().getStringExtra("movieTitle");
        }

        initViews();
        setupRecyclerView();
        loadShowtimes();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Set title based on whether viewing all showtimes or showtimes for a specific movie
            if (movieId != -1 && !movieTitle.isEmpty()) {
                getSupportActionBar().setTitle("Lịch chiếu - " + movieTitle);
            } else {
                getSupportActionBar().setTitle("Danh sách lịch chiếu");
            }
        }

        rvShowtimes = findViewById(R.id.rvShowtimes);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(this::loadShowtimes);
    }

    private void setupRecyclerView() {
        showtimeAdapter = new ShowtimeAdapter(this);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
        rvShowtimes.setAdapter(showtimeAdapter);
    }

    private void loadShowtimes() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        rvShowtimes.setVisibility(View.GONE);

        Call<ShowtimesResponse> call;
        if (movieId != -1) {
            call = RetrofitClient.getInstance(this).getApiService().getShowtimesByMovie(movieId);
        } else {
            call = RetrofitClient.getInstance(this).getApiService().getShowtimes();
        }

        call.enqueue(new Callback<ShowtimesResponse>() {
            @Override
            public void onResponse(@NonNull Call<ShowtimesResponse> call, @NonNull Response<ShowtimesResponse> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    ShowtimesResponse showtimesResponse = response.body();
                    if (showtimesResponse.isSuccess() && showtimesResponse.getData() != null && !showtimesResponse.getData().isEmpty()) {
                        showtimeAdapter.setShowtimeList(showtimesResponse.getData());
                        rvShowtimes.setVisibility(View.VISIBLE);
                        tvEmptyState.setVisibility(View.GONE);
                    } else {
                        tvEmptyState.setVisibility(View.VISIBLE);
                        rvShowtimes.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(ShowtimeListActivity.this, "Không thể tải danh sách lịch chiếu", Toast.LENGTH_SHORT).show();
                    tvEmptyState.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShowtimesResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ShowtimeListActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                tvEmptyState.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
