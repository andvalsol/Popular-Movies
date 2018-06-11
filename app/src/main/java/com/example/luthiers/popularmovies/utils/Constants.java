package com.example.luthiers.popularmovies.utils;

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
    
    public static final String MOVIE_DB_API_KEY = ""; //TODO add the proper API Key
    public static final String MOST_POPULAR = "/discover/movie?sort_by=popularity.desc";
    public static final String TOP_RATED = " /discover/movie/?certification_country=US&sort_by=vote_average.desc";

    private Constants() {
        //Set private constructor to prevent instantiation of this class
    }
}