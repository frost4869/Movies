package com.example.frost.movies.API.API.Movies;

import com.example.frost.movies.Utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Frost on 5/25/2017.
 */

public class MovieService {

    public Call<Movie> getAllMovies(int page){
        Retrofit retrofit = getMovieApi();
        MovieAPI movieAPI = retrofit.create(MovieAPI.class);

        return movieAPI.GetMovies(Constant.getAPI_KEY(), page);
    }

    public Retrofit getMovieApi(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
