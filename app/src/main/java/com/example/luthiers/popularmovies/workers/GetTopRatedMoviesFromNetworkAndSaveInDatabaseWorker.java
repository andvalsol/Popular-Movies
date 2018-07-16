package com.example.luthiers.popularmovies.workers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.GetMoviesAndSaveInDatabase;

import androidx.work.Worker;

public class GetTopRatedMoviesFromNetworkAndSaveInDatabaseWorker extends Worker {
    
    @NonNull
    @Override
    public WorkerResult doWork() {
        Log.d("MoviesFilter", "1The query action is: " + Constants.TOP_RATED);
        
        /*
         * The work doing here is going to be in the background by default
         * */
        
        return GetMoviesAndSaveInDatabase.getMoviesAndSaveInDatabase(Constants.TOP_RATED, getApplicationContext());
    }
}