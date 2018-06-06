package com.example.luthiers.popularmovies.utils;


import android.util.Log;

import com.example.luthiers.popularmovies.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieUtils {
    
    //Create strings for the json keys
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_RATING = "vote_average";
    private static final String MOVIE_ID = "id";
    
    //Get a movie pojo from the json string
    public static List<Movie> getMoviesFromJsonResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");
            
            ArrayList<Movie> movies = new ArrayList<>();
            
            for (int n = 0; n < results.length(); n++) {
                //Get the each object from the JSON array
                JSONObject jsonMovie = results.getJSONObject(n);
    
                Log.d("UniqueID", "The ids are: " + jsonMovie.getInt(MOVIE_ID));
                
                //Create each movie from each json result
                Movie movie = new Movie(
                        jsonMovie.getInt(MOVIE_ID),
                        jsonMovie.getString(MOVIE_TITLE),
                        jsonMovie.getString(MOVIE_POSTER),
                        jsonMovie.getString(MOVIE_OVERVIEW),
                        jsonMovie.getString(MOVIE_RELEASE_DATE),
                        (float) jsonMovie.getInt(MOVIE_RATING)
                );
                
                //Add the object to the movies array list
                movies.add(movie);
            }
            
            return movies;
            
        } catch (JSONException e) {
            return null;
        }
    }
}