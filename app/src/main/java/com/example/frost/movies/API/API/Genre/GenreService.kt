package com.example.frost.movies.API.API.Genre

import com.example.frost.movies.Utils.Constant

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Frost on 5/26/2017.
 */

class GenreService {

    fun GetAllGenres(language: String): Call<Genres> {
        val retrofit = genreService
        val genreAPI = retrofit.create(GenreAPI::class.java)

        return genreAPI.GetAllGenres(Constant.getAPI_KEY(), language)
    }

    val genreService: Retrofit
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
