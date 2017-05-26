package com.example.frost.movies.Adapter;

import android.content.ContentProvider;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frost.movies.API.API.Movies.Movie;
import com.example.frost.movies.API.API.Movies.Result;
import com.example.frost.movies.R;
import com.example.frost.movies.Utils.Constant;
import com.example.frost.movies.Utils.OnLoadMoreListener;

import java.util.List;

/**
 * Created by Frost on 5/25/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnLoadMoreListener onLoadMoreListener;
    private View itemView;
    public List<Result> movieList;
    public Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public MovieAdapter(List<Result> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return movieList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView movieTitle, movieYear;
        public ImageView cover;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.title);
            movieYear = (TextView) itemView.findViewById(R.id.year);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.movie_info);

            relativeLayout.bringToFront();
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_card, parent, false);
            return new ViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_row, parent, false);
            return new LoadingViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Result movie = movieList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.movieTitle.setText(movie.original_title);
            Glide.with(context)
                    .load(Constant.getImage_base_url() + "original/" + movie.poster_path)
                    .into(viewHolder.cover);
            viewHolder.movieYear.setText(movie.release_date.substring(0, 4));
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }


}
