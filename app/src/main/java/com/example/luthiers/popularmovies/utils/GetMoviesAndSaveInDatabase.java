package com.example.luthiers.popularmovies.utils;

import android.content.Context;
import android.util.Log;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.repository.room.MovieDatabase;

import java.io.IOException;
import java.util.List;


public class GetMoviesAndSaveInDatabase {
    
    public static void getMoviesAndSaveInDatabase(String filter, Context context) {
        //Query for the most popular movies
        try {
            String jsonMovies = MovieNetworkDataSource.getMoviesFromNetwork(filter);
            
            Log.d("MoviesFilter", "The jsonMovies is: " + jsonMovies);

            
            /*
            Since we're pre fetching the movies, we don't know the state of the user's network speed at runtime,
            save the movie poster as it comes directly from the json response, so that we can set the proper poster size
            at runtime.
            */
            //Get the list of movies objects from the json response gotten from the GetMoviesFromNetworkWorker, use empty string because of previous explanation
            List<Movie> movies = MovieUtils.getMoviesFromJsonResponse(jsonMovies, filter);
            
            //Insert the list of movies in the database
            MovieDatabase.getInstance(context).mMovieDao().insertListMovies(movies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}