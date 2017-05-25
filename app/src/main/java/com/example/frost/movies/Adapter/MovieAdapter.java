package com.example.frost.movies.Adapter;

import android.content.ContentProvider;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private OnLoadMoreListener onLoadMoreListener;
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView movieTitle;
        public ImageView cover;

        public ViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.title);
            cover = (ImageView) itemView.findViewById(R.id.cover);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result movie = movieList.get(position);
        holder.movieTitle.setText(movie.original_title);
        Glide.with(context)
                .load(Constant.getImage_base_url() + "original/" + movie.poster_path)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
