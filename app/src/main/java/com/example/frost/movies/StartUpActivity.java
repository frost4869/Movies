package com.example.frost.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frost.movies.API.API.Config.Config;
import com.example.frost.movies.API.API.Config.ConfigService;
import com.example.frost.movies.Utils.SessionManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        final TextView text = (TextView) findViewById(R.id.status);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

        ConfigService service = new ConfigService();
        service.GetConfig().enqueue(new Callback<Config>() {
            @Override
            public void onResponse(Call<Config> call, Response<Config> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    SessionManager manager = new SessionManager(getApplicationContext());

                    String baseURL = response.body().getImages().getBase_url();
                    String secureBaseURL = response.body().getImages().getSecure_base_url();
                    String jsonBackDropSize = gson.toJson(response.body().getImages().getBackdrop_sizes());
                    String jsonLogoSize = gson.toJson(response.body().getImages().getLogo_sizes());
                    String jsonPosterSize = gson.toJson(response.body().getImages().getPoster_sizes());
                    String jsonProfileSize = gson.toJson(response.body().getImages().getProfile_sizes());
                    String jsonStillSize = gson.toJson(response.body().getImages().getStill_sizes());

                    manager.createConfig(baseURL, secureBaseURL, jsonBackDropSize, jsonLogoSize, jsonPosterSize,
                            jsonProfileSize, jsonStillSize);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Config> call, Throwable t) {
                text.setText("Error");
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }
}
