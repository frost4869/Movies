package com.example.frost.movies.API.API.Config

import com.example.frost.movies.Utils.Constant

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Frost on 5/24/2017.
 */

class ConfigService {

    fun GetConfig(): Call<Config> {
        val retrofit = configAPI

        val configAPI = retrofit.create(ConfigAPI::class.java)

        return configAPI.GetConfigs(Constant.getAPI_KEY())
    }


    val configAPI: Retrofit
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
