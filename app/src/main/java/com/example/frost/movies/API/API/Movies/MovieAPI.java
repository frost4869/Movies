package com.example.frost.movies.API.API.Movies;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Frost on 5/25/2017.
 */

public interface MovieAPI {

    @FormUrlEncoded
    @POST("discover/movie")
    Call<Movie> GetMovies(@Field("api_key") String api_key, @Field("page") int page);
}
