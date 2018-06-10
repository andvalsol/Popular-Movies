package com.example.luthiers.popularmovies.utils;

import android.content.Context;
import android.util.Log;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.repository.room.MovieDatabase;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;

public class GetMoviesAndSaveInDatabase {
    
    public static Worker.WorkerResult getMoviesAndSaveInDatabase(MovieNetworkDataSource movieNetworkDataSource, String filter, Context context) {
        //Query for the most popular movies
        try {
            String jsonMovies = movieNetworkDataSource.getMoviesFromNetwork(Constants.TOP_RATED);
            
            /*
            Since we're pre fetching the movies, we don't know the state of the user's network speed at runtime,
            save the movie poster as it comes directly from the json response, so that we can set the proper poster size
            at runtime.
            */
            //Get the list of movies objects from the json response gotten from the GetMoviesFromNetworkWorker, use empty string because of previous explanation
            List<Movie> movies = MovieUtils.getMoviesFromJsonResponse(jsonMovies);
            
            //Insert the list of movies in the database
            Long[] wereSaved = MovieDatabase.getInstance(context).mMovieDao().insertListMovies(movies);
            
            //Check if the list of movies were successfully saved, if they were return SUCCESS if not return FAILURE
            for (long wasSaved : wereSaved) {
                if (wasSaved == -1) {
                    Log.d("Fetching", "Failure");
                    //If one data wasn't saved then set the result as FAILURE, so that if can be tried again later
                    return Worker.WorkerResult.FAILURE;
                }
            }
            
            //The list of movies were successfully saved
            return Worker.WorkerResult.SUCCESS;
        } catch (IOException e) {
            Log.d("Fetching", "Failure");
            
            //There was an error retrieving the movies
            return Worker.WorkerResult.FAILURE;
        }
    }
}
