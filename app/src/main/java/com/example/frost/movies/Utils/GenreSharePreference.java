package com.example.frost.movies.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;

import com.example.frost.movies.API.API.Genre.Genres;
import com.example.frost.movies.StartUpActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Frost on 5/26/2017.
 */

public class GenreSharePreference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    int PRIVATE_MODE = 0;

    private static final String GENRE_PREP_NAME = "Genre_Pref";
    public static final String KEY_GENRES = "Genres";
    private static final String HAS_GENRE = "Has_Genre";

    public GenreSharePreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(GENRE_PREP_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createGenre(List<Genres.Genre> genres){
        Gson gson = new Gson();
        String genreJson = gson.toJson(genres);

        editor.putString(KEY_GENRES, genreJson);
        editor.commit();
    }

    public HashMap<String, String> getGenreJsonString(){
        HashMap<String, String> genres = new HashMap<>();
        genres.put(KEY_GENRES, sharedPreferences.getString(KEY_GENRES, null));

        return genres;
    }

    public void checkGenre(){
        if(!this.hasGenre()){
            Intent intent = new Intent(context, StartUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public boolean hasGenre(){
        return sharedPreferences.getBoolean(HAS_GENRE, false);
    }
}
