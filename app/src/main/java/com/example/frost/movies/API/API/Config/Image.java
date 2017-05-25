package com.example.frost.movies.API.API.Config;

import java.util.List;

/**
 * Created by Frost on 5/25/2017.
 */

public class Image {
    private String base_url;
    private String secure_base_url;
    private List<String> backdrop_sizes;
    private List<String> logo_sizes;
    private List<String> poster_sizes;
    private List<String> profile_sizes;
    private List<String> still_sizes;

    public String getBase_url() {
        return base_url;
    }

    public String getSecure_base_url() {
        return secure_base_url;
    }

    public List<String> getBackdrop_sizes() {
        return backdrop_sizes;
    }

    public List<String> getLogo_sizes() {
        return logo_sizes;
    }

    public List<String> getPoster_sizes() {
        return poster_sizes;
    }

    public List<String> getProfile_sizes() {
        return profile_sizes;
    }

    public List<String> getStill_sizes() {
        return still_sizes;
    }
}
