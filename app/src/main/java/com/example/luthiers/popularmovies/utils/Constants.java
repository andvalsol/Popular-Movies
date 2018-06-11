package com.example.luthiers.popularmovies.utils;

import com.example.luthiers.popularmovies.BuildConfig;

/*
* Use static final variables instead of ENUMs for better overall performance
* */
public final class Constants {
    
    //For image resolutions
    /*
    * According to the implementation for the movies app guide from Udacity
    * Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
    *
    * We're going to set only 3 image sizes: w92, w185 and w500
    * */
    public static final String LOW_RES_IMAGE_SIZE = "w92";
    public static final String MEDIUM_RES_IMAGE_SIZE = "w185";
    public static final String HIGH_RES_IMAGE_SIZE = "w500";
    
    public static final String MOVIE_DB_API_KEY = BuildConfig.API_KEY;
    public static final String MOST_POPULAR = "popular";
    public static final String TOP_RATED = " top_rated";

    private Constants() {
        //Set private constructor to prevent instantiation of this class
    }
}