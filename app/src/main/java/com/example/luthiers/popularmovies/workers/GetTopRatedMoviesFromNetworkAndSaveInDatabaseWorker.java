package com.example.luthiers.popularmovies.workers;

import android.support.annotation.NonNull;

import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.GetMoviesAndSaveInDatabase;

import androidx.work.Worker;

public class GetTopRatedMoviesFromNetworkAndSaveInDatabaseWorker extends Worker {
    
    private MovieNetworkDataSource mMovieNetworkDataSource = new MovieNetworkDataSource();
    
    @NonNull
    @Override
    public WorkerResult doWork() {
        /*
         * The work doing here is going to be in the background by default
         * */
        
        return GetMoviesAndSaveInDatabase.getMoviesAndSaveInDatabase(mMovieNetworkDataSource, Constants.TOP_RATED, getApplicationContext());
    }
}