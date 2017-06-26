package com.example.frost.movies.API.API.Genre;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Frost on 5/26/2017.
 */

public interface GenreAPI {

    @GET("genre/movie/list")
    Call<Genres> GetAllGenres(@Query("api_key") String api_key, @Query("language") String language);

}
