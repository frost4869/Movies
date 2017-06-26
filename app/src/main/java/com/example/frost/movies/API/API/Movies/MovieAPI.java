package com.example.frost.movies.API.API.Movies;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Frost on 5/25/2017.
 */

public interface MovieAPI {

    @FormUrlEncoded
    @POST("discover/movie")
    Call<Movie> GetMovies(@Field("api_key") String api_key, @Field("page") int page);

    @GET("movie/{movie_id}")
    Call<MovieDetails> GetMovieDetails(@Path("movie_id") int movieId, @Field("api_key") String api_key, @Field("language") String language);

    @GET("movie/{movie_id}/images")
    Call<Images> GetMovieImages(@Path("movie_id") int movie_id, @Path("api_key") String api_key, @Field("language") String language);
}
