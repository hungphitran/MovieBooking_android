package com.ptithcm.moviebooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ptithcm.moviebooking.MovieDetailActivity;
import com.ptithcm.moviebooking.R;
import com.ptithcm.moviebooking.adapters.MovieAdapter;
import com.ptithcm.moviebooking.models.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private LinearLayout paginationLayout;
    private HorizontalScrollView paginationScrollView;
    private int currentPage = 1;
    private int totalPages = 1;
    private static final int PAGE_SIZE = 10;
    private List<Movie> allMovies = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_movies);
        paginationLayout = view.findViewById(R.id.pagination_layout);
        paginationScrollView = view.findViewById(R.id.pagination_scroll_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMovies();
        return view;
    }

    private void loadMovies() {
        // TODO: Replace with real data loading from API
        allMovies = getDummyMovies(53); // Example: 53 movies
        totalPages = (int) Math.ceil((double) allMovies.size() / PAGE_SIZE);
        showPage(currentPage);
        setupPagination();
    }

    private void showPage(int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, allMovies.size());
        List<Movie> pageMovies = allMovies.subList(start, end);

        adapter = new MovieAdapter(pageMovies, movie -> {
            // Handle movie click - open detail activity
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra("movie_id", movie.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupPagination() {
        paginationLayout.removeAllViews();

        // Scroll về đầu để hiển thị nút đầu tiên
        if (paginationScrollView != null) {
            paginationScrollView.post(() -> paginationScrollView.smoothScrollTo(0, 0));
        }

        // Add "Previous" button
        if (currentPage > 1) {
            Button btnPrev = createPaginationButton("◀ Trước");
            btnPrev.setOnClickListener(v -> {
                currentPage--;
                showPage(currentPage);
                setupPagination();
            });
            paginationLayout.addView(btnPrev);
        }

        // Add page number buttons (show 5 pages at a time)
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        for (int i = startPage; i <= endPage; i++) {
            Button btn = createPaginationButton(String.valueOf(i));
            final int pageNumber = i;

            if (i == currentPage) {
                btn.setSelected(true);
            }

            btn.setOnClickListener(v -> {
                currentPage = pageNumber;
                showPage(currentPage);
                setupPagination();
            });
            paginationLayout.addView(btn);
        }

        // Add "Next" button
        if (currentPage < totalPages) {
            Button btnNext = createPaginationButton("Sau ▶");
            btnNext.setOnClickListener(v -> {
                currentPage++;
                showPage(currentPage);
                setupPagination();
            });
            paginationLayout.addView(btnNext);
        }
    }

    private Button createPaginationButton(String text) {
        Button btn = (Button) getLayoutInflater().inflate(R.layout.button_pagination, paginationLayout, false);
        btn.setText(text);
        return btn;
    }

    // Dummy data for demonstration - replace with real API call
    private List<Movie> getDummyMovies(int count) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            movies.add(new Movie(
                i,
                "Phim " + i,
                "https://via.placeholder.com/500x750",
                "Hành động, Phiêu lưu",
                "120 phút",
                7.5 + (i % 3),
                "2024-01-01",
                "Mô tả cho phim " + i
            ));
        }
        return movies;
    }
}
