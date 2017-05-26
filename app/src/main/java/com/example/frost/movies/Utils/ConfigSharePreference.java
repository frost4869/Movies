package com.example.frost.movies.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.frost.movies.StartUpActivity;

import java.util.HashMap;

/**
 * Created by Frost on 5/24/2017.
 */

public class ConfigSharePreference {

    SharedPreferences sharedPreferences;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Config_Pref";
    public static final String KEY_BASE_URL = "BASE_URL";
    public static final String KEY_SECURE_BASE_URL = "SECURE_BASE_URL";
    public static final String KEY_BACKDROP_SIZE = "BACKDROP_SIZE";
    public static final String KEY_LOGO_SIZE = "LOGO_SIZE";
    public static final String KEY_POSTER_SIZE = "POSTER_SIZE";
    public static final String KEY_PROFILE_SIZE = "PROFILE_SIZE";
    public static final String KEY_STILL_SIZE = "STILL_SIZE";

    private static final String HAS_CONFIG = "HAS_CONFIG";



    public ConfigSharePreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createConfig(String baseUrl, String secureBaseUrl, String backdropSize,
                             String logoSize, String posterSize, String profileSize, String stillSize) {
        editor.putString(KEY_BASE_URL, baseUrl);
        editor.putString(KEY_SECURE_BASE_URL, secureBaseUrl);
        editor.putString(KEY_BACKDROP_SIZE, backdropSize);
        editor.putString(KEY_LOGO_SIZE, logoSize);
        editor.putString(KEY_POSTER_SIZE, posterSize);
        editor.putString(KEY_PROFILE_SIZE, profileSize);
        editor.putString(KEY_STILL_SIZE, stillSize);

        editor.putBoolean(HAS_CONFIG, true);
        editor.commit();
    }

    public HashMap<String, String> getConfig() {
        HashMap<String, String> config = new HashMap<>();
        config.put(KEY_BASE_URL, sharedPreferences.getString(KEY_BASE_URL, null));
        config.put(KEY_SECURE_BASE_URL, sharedPreferences.getString(KEY_SECURE_BASE_URL, null));
        config.put(KEY_BACKDROP_SIZE, sharedPreferences.getString(KEY_BACKDROP_SIZE, null));
        config.put(KEY_LOGO_SIZE, sharedPreferences.getString(KEY_LOGO_SIZE, null));
        config.put(KEY_POSTER_SIZE, sharedPreferences.getString(KEY_POSTER_SIZE, null));
        config.put(KEY_PROFILE_SIZE, sharedPreferences.getString(KEY_PROFILE_SIZE, null));
        config.put(KEY_STILL_SIZE, sharedPreferences.getString(KEY_STILL_SIZE, null));

        return config;
    }

    public void checkConfig() {
        if (!this.hasConfig()) {
            Intent intent = new Intent(context, StartUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public boolean hasConfig() {
        boolean hasConfig = sharedPreferences.getBoolean(HAS_CONFIG, false);
        return hasConfig;
    }

}
