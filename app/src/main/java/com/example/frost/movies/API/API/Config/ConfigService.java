package com.example.frost.movies.API.API.Config;

import com.example.frost.movies.Utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Frost on 5/24/2017.
 */

public class ConfigService {

    public Call<Config> GetConfig() {
        Retrofit retrofit = getConfigAPI();

        ConfigAPI configAPI = retrofit.create(ConfigAPI.class);

        return configAPI.GetConfigs(Constant.getAPI_KEY());
    }


    public Retrofit getConfigAPI() {
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
