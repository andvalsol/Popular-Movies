package com.example.luthiers.popularmovies.utils;


import com.example.luthiers.popularmovies.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieJson {
    
    //Create strings for the json keys
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_RATING = "vote_average";
    private static final String MOVIE_ID = "id";
    
    //Get a movie pojo from the json string
    public static ArrayList<Movie> getMovieFromJson(String jsonResponse, String properImageSize) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");
        
            ArrayList<Movie> movies = new ArrayList<>();
            
            for(int n = 0; n < results.length(); n++) {
                //Get the each object from the JSON array
                JSONObject jsonMovie = results.getJSONObject(n);
    
                Movie movie = new Movie(
                        jsonMovie.getString(MOVIE_TITLE),
                        getMoviePosterUrl(jsonMovie.getString(MOVIE_POSTER), properImageSize),
                        jsonMovie.getString(MOVIE_OVERVIEW),
                        jsonMovie.getString(MOVIE_RELEASE_DATE),
                        (float) jsonMovie.getInt(MOVIE_RATING),
                        jsonMovie.getInt(MOVIE_ID)
                );
                
                //Add the object to the movies array list
                movies.add(movie);
            }
        
            return movies;
            
        } catch (JSONException e) {
            return null;
        }
    }
    
    private static String getMoviePosterUrl(String moviePoster, String properImageSize) {
        return  "http://image.tmdb.org/t/p/" + properImageSize + moviePoster;
    }
}