package com.example.luthiers.popularmovies.workers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.GetMoviesAndSaveInDatabase;

import androidx.work.Worker;

public class GetTopRatedMoviesFromNetworkAndSaveInDatabaseWorker extends Worker {
    
    private final MovieNetworkDataSource mMovieNetworkDataSource = new MovieNetworkDataSource();
    
    @NonNull
    @Override
    public WorkerResult doWork() {
        Log.d("TopRated", "Getting top rated movies");
        /*
         * The work doing here is going to be in the background by default
         * */
        
        return GetMoviesAndSaveInDatabase.getMoviesAndSaveInDatabase(mMovieNetworkDataSource, Constants.TOP_RATED, getApplicationContext());
    }
}