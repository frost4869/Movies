package com.example.frost.movies.Utils;

import com.example.frost.movies.API.API.Genre.Genres;

import java.util.List;

/**
 * Created by Frost on 5/24/2017.
 */

public class Constant {

    public static final String BaseURL = "https://api.themoviedb.org/3/";

    private static final String API_KEY = "09e4cc13c99312bf18cad8339e83bc82";

    private static String image_base_url;
    private static String secure_image_base_url;
    private static List<String> backdrop_image_size;
    private static List<String> logo_image_size;
    private static List<String> poster_image_size;
    private static List<String> profile_image_size;
    private static List<String> still_image_size;

    private static Genres genres;

    public static Genres getGenres() {
        return genres;
    }

    public static void setGenres(Genres genres) {
        Constant.genres = genres;
    }

    public static String getBaseURL() {
        return BaseURL;
    }

    public static String getAPI_KEY() {
        return API_KEY;
    }

    public static String getImage_base_url() {
        return image_base_url;
    }

    public static void setImage_base_url(String image_base_url) {
        Constant.image_base_url = image_base_url;
    }

    public static String getSecure_image_base_url() {
        return secure_image_base_url;
    }

    public static void setSecure_image_base_url(String secure_image_base_url) {
        Constant.secure_image_base_url = secure_image_base_url;
    }

    public static List<String> getBackdrop_image_size() {
        return backdrop_image_size;
    }

    public static void setBackdrop_image_size(List<String> backdrop_image_size) {
        Constant.backdrop_image_size = backdrop_image_size;
    }

    public static List<String> getLogo_image_size() {
        return logo_image_size;
    }

    public static void setLogo_image_size(List<String> logo_image_size) {
        Constant.logo_image_size = logo_image_size;
    }

    public static List<String> getPoster_image_size() {
        return poster_image_size;
    }

    public static void setPoster_image_size(List<String> poster_image_size) {
        Constant.poster_image_size = poster_image_size;
    }

    public static List<String> getProfile_image_size() {
        return profile_image_size;
    }

    public static void setProfile_image_size(List<String> profile_image_size) {
        Constant.profile_image_size = profile_image_size;
    }

    public static List<String> getStill_image_size() {
        return still_image_size;
    }

    public static void setStill_image_size(List<String> still_image_size) {
        Constant.still_image_size = still_image_size;
    }
}
