package com.example.luthiers.popularmovies.utils;

import android.util.Log;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.entities.Review;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieUtils {
    
    //Create strings for the json keys
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_RATING = "vote_average";
    private static final String MOVIE_POPULARITY = "popularity";
    private static final String MOVIE_ID = "id";
    
    //For movie trailer
    private static final String MOVIE_TRAILER_KEY = "key";
    
    //For movie review
    private static final String MOVIE_REVIEW_AUTHOR = "author";
    private static final String MOVIE_REVIEW_CONTENT = "content";
    
    //Get a movie pojo from the json string
    public static List<Movie> getMoviesFromJsonResponse(String jsonResponse, String filter) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");
            
            ArrayList<Movie> movies = new ArrayList<>();
            
            //Iterate over the results (JSONArray)
            for (int n = 0; n < results.length(); n++) {
                //Get the each object from the JSON array
                JSONObject jsonMovie = results.getJSONObject(n);
                
                int queryAction;
                if (filter.equals(Constants.MOST_POPULAR)) queryAction = Constants.ROOM_MOST_POPULAR;
                else queryAction = Constants.ROOM_TOP_RATED;
    
                
                //Create each movie from each json result
                Movie movie = new Movie(
                        jsonMovie.getInt(MOVIE_ID),
                        jsonMovie.getString(MOVIE_TITLE),
                        jsonMovie.getString(MOVIE_POSTER),
                        jsonMovie.getString(MOVIE_OVERVIEW),
                        jsonMovie.getString(MOVIE_RELEASE_DATE),
                        (float) jsonMovie.getInt(MOVIE_RATING),
                        (float) jsonMovie.getInt(MOVIE_POPULARITY),
                        queryAction
                );
                
                //Add the object to the movies array list
                movies.add(movie);
            }
            
            return movies;
            
        } catch (JSONException e) {
            return null;
        }
    }
    
    public static List<Review> getReviewsFromJsonMovie(String jsonMovieReviews) {
        JSONObject jsonObject;
    
        try {
            jsonObject = new JSONObject(jsonMovieReviews);
            //Get the json array from the results found from the json response
            JSONArray results = jsonObject.getJSONArray("results");
        
            ArrayList<Review> reviews = new ArrayList<>();
        
            //Iterate over the results (JSONArray)
            for (int n = 0; n < results.length(); n++) {
                //Get the each object from the JSON array
                JSONObject jsonReview = results.getJSONObject(n);
                
                Review review = new Review(
                        jsonReview.getString(MOVIE_REVIEW_AUTHOR),
                        jsonReview.getString(MOVIE_REVIEW_CONTENT)
                );
            
                //Add the object to the movies array list
                reviews.add(review);
            }
        
            return reviews;
        } catch (JSONException e) {
            return null;
        }
    }
    
    public static String getMovieTrailerKeyFromJsonResource(String jsonMovieTrailer) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonMovieTrailer);
            //Get the json array from the results found from the json response
            JSONArray results = jsonObject.getJSONArray("results");
    
            //Get the first object from the json array
            JSONObject firstArray = results.getJSONObject(0);
            return firstArray.getString(MOVIE_TRAILER_KEY);
            
        } catch (JSONException e) {
            return "";
        }
    }
    
    public static String getJsonReviewsFromReviewsList(List<Review> reviews) {
        //Create a new Gson object, Gson is the fastest library to get small sets of Json data
        Gson gson = new Gson();
    
        return gson.toJson(reviews);
    }
    
    public static List<Review> getReviewsFromJsonReviews(String jsonReviews) {
        //Create a new Gson object, Gson is the fastest library to get small sets of Json data
        Gson gson = new Gson();
    
        //Create a TypeToken for Gson to use
        Type type = new TypeToken<List<Review>>() {
        }.getType();
    
        return gson.fromJson(jsonReviews, type);
    }
    
    public static String getMovieTrailer(String keyId) {
        return "https://www.youtube.com/watch?v=" + keyId;
    }
}