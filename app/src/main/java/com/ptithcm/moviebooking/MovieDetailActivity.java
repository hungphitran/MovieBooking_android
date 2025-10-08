package com.ptithcm.moviebooking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.ptithcm.moviebooking.api.ApiService;
import com.ptithcm.moviebooking.api.RetrofitClient;
import com.ptithcm.moviebooking.models.MovieDetail;
import com.ptithcm.moviebooking.schema.MovieDetailResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "movie_id";

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivBackdrop, ivPoster;
    private TextView tvTitle, tvReleaseDate, tvRuntime, tvRating, tvStatus;
    private TextView tvGenres, tvOverview;
    private MaterialButton btnBookTicket, btnHomepage;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private int movieId;
    private MovieDetail movieDetail;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get movie ID from intent
        movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Invalid movie ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupButtons();

        apiService = RetrofitClient.getInstance(this).getApiService();
        loadMovieDetail();
    }

    private void initViews() {
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.toolbar);
        ivBackdrop = findViewById(R.id.ivBackdrop);
        ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvRuntime = findViewById(R.id.tvRuntime);
        tvRating = findViewById(R.id.tvRating);
        tvStatus = findViewById(R.id.tvStatus);
        tvGenres = findViewById(R.id.tvGenres);
        tvOverview = findViewById(R.id.tvOverview);
        btnBookTicket = findViewById(R.id.btnBookTicket);
        btnHomepage = findViewById(R.id.btnHomepage);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupButtons() {
        btnBookTicket.setOnClickListener(v -> {
            if (movieDetail != null) {
                Toast.makeText(this, R.string.booking_feature_coming_soon, Toast.LENGTH_SHORT).show();
                // TODO: Navigate to booking screen
            }
        });

        btnHomepage.setOnClickListener(v -> {
            if (movieDetail != null && movieDetail.getHomepage() != null && !movieDetail.getHomepage().isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieDetail.getHomepage()));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, R.string.no_homepage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovieDetail() {
        showLoading(true);

        apiService.getMovieDetail(movieId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailResponse> call, @NonNull Response<MovieDetailResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    MovieDetailResponse movieResponse = response.body();
                    if (movieResponse.isSuccess() && movieResponse.getData() != null) {
                        movieDetail = movieResponse.getData();
                        displayMovieDetail();
                    } else {
                        showError(getString(R.string.failed_load_movie_details));
                    }
                } else {
                    showError(getString(R.string.server_error, response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailResponse> call, @NonNull Throwable t) {
                showLoading(false);
                showError(getString(R.string.network_error, t.getMessage()));
            }
        });
    }

    private void displayMovieDetail() {
        // Set title in collapsing toolbar
        collapsingToolbar.setTitle(movieDetail.getTitle());

        // Set basic info
        tvTitle.setText(movieDetail.getTitle());
        tvReleaseDate.setText(movieDetail.getRelease_date() != null ? movieDetail.getRelease_date() : "N/A");
        tvRuntime.setText(movieDetail.getFormattedRuntime());
        tvRating.setText(String.format(Locale.getDefault(), "⭐ %.1f", movieDetail.getVote_average()));
        tvStatus.setText(movieDetail.getStatus() != null ? movieDetail.getStatus() : getString(R.string.status_unknown));

        // Load images using Glide
        Glide.with(this)
                .load(movieDetail.getBackdrop_path())
                .placeholder(R.drawable.movie_item)
                .error(R.drawable.movie_item)
                .into(ivBackdrop);
        Glide.with(this)
                .load(movieDetail.getPoster_path())
                .placeholder(R.drawable.movie_item)
                .error(R.drawable.movie_item)
                .into(ivPoster);
        // Set genres
        tvGenres.setText(movieDetail.getGenresString());

        // Set overview
        if (movieDetail.getOverview() != null && !movieDetail.getOverview().isEmpty()) {
            tvOverview.setText(movieDetail.getOverview());
        } else {
            tvOverview.setText(R.string.no_overview);
        }

        // Show/hide homepage button based on availability
        if (movieDetail.getHomepage() == null || movieDetail.getHomepage().isEmpty()) {
            btnHomepage.setVisibility(View.GONE);
        } else {
            btnHomepage.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}

