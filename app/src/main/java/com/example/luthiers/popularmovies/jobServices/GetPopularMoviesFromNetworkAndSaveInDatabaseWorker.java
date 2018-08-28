package com.example.luthiers.popularmovies.jobServices;

import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.GetMoviesAndSaveInDatabase;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class GetPopularMoviesFromNetworkAndSaveInDatabaseWorker extends JobService {
    
    @Override
    public boolean onStartJob(JobParameters job) {
        new Thread(() -> GetMoviesAndSaveInDatabase.getMoviesAndSaveInDatabase(Constants.MOST_POPULAR, getApplicationContext())).start();
        
        return false;
    }
    
    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}