package com.example.frost.movies.API.API.Movies

import com.example.frost.movies.Utils.Constant

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Frost on 5/25/2017.
 */

class MovieService {

    fun getAllMovies(page: Int): Call<Movie> {
        val retrofit = movieApi
        val movieAPI = retrofit.create(MovieAPI::class.java)

        return movieAPI.GetMovies(Constant.getAPI_KEY(), page)
    }

    fun GetMovieDetails(movie_id: Int, language: String): Call<MovieDetails> {
        val retrofit = movieApi
        val movieAPI = retrofit.create(MovieAPI::class.java)

        return movieAPI.GetMovieDetails(movie_id, Constant.getAPI_KEY(), language)
    }

    val movieApi: Retrofit
        get() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient().newBuilder()
                    .addInterceptor(logging)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.getBaseURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofit
        }
}
