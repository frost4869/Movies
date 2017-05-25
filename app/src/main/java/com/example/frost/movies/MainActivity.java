package com.example.frost.movies;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frost.movies.API.API.Movies.Movie;
import com.example.frost.movies.API.API.Movies.MovieService;
import com.example.frost.movies.API.API.Movies.Result;
import com.example.frost.movies.Adapter.MovieAdapter;
import com.example.frost.movies.Utils.Constant;
import com.example.frost.movies.Utils.OnLoadMoreListener;
import com.example.frost.movies.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Result> movieList;
    private TextView status;
    private ProgressBar pb;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GetConfigurations();
        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        status = (TextView) findViewById(R.id.status);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(movieList, this);
        movieAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }

            }
        });
        recyclerView.setAdapter(movieAdapter);

        MovieService service = new MovieService();
        service.getAllMovies(1).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.isSuccessful() && response.body().results != null){
                    status.post(new Runnable() {
                        @Override
                        public void run() {
                            status.setVisibility(View.INVISIBLE);
                            pb.setVisibility(View.INVISIBLE);
                        }
                    });

                    movieList.addAll(response.body().results);
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(final Call<Movie> call, final Throwable t) {
                status.post(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("Failed: " + t.getMessage());
                        pb.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }

    public void GetConfigurations(){
        SessionManager manager = new SessionManager(getApplicationContext());
        manager.checkConfig();

        HashMap<String, String> configs = manager.getConfig();

        String base_url = configs.get(SessionManager.KEY_BASE_URL);
        String secure_base_url = configs.get(SessionManager.KEY_SECURE_BASE_URL);
        String backdrop_size = configs.get(SessionManager.KEY_BACKDROP_SIZE);
        String logo_size = configs.get(SessionManager.KEY_LOGO_SIZE);
        String poster_size = configs.get(SessionManager.KEY_POSTER_SIZE);
        String profile_size = configs.get(SessionManager.KEY_PROFILE_SIZE);
        String still_size = configs.get(SessionManager.KEY_STILL_SIZE);

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> BackDropSizes = gson.fromJson(backdrop_size, type);
        List<String> LogoSizes = gson.fromJson(logo_size, type);
        List<String> PosterSizes = gson.fromJson(poster_size, type);
        List<String> ProfileSizes = gson.fromJson(profile_size, type);
        List<String> StillSizes = gson.fromJson(still_size, type);

        Constant.setImage_base_url(base_url);
        Constant.setSecure_image_base_url(secure_base_url);
        Constant.setBackdrop_image_size(BackDropSizes);
        Constant.setLogo_image_size(LogoSizes);
        Constant.setPoster_image_size(PosterSizes);
        Constant.setProfile_image_size(ProfileSizes);
        Constant.setStill_image_size(StillSizes);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.colapsing_bar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
