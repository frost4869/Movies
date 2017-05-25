package com.example.frost.movies.API.API.Config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Frost on 5/24/2017.
 */

public interface ConfigAPI {
    @GET("configuration")
    Call<Config> GetConfigs(@Query("api_key") String api_key);
}
