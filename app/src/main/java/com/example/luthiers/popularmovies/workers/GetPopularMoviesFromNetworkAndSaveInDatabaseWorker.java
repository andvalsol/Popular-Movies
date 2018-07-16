package com.example.luthiers.popularmovies.workers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.GetMoviesAndSaveInDatabase;

import androidx.work.Worker;

public class GetPopularMoviesFromNetworkAndSaveInDatabaseWorker extends Worker {
    
    @NonNull
    @Override
    public WorkerResult doWork() {
        Log.d("MoviesFilter", "1The query action is: " + Constants.MOST_POPULAR);
        
        return GetMoviesAndSaveInDatabase.getMoviesAndSaveInDatabase(Constants.MOST_POPULAR, getApplicationContext());
    }
}