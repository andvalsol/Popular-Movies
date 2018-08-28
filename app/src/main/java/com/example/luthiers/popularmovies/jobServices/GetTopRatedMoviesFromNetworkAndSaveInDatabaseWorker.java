package com.example.luthiers.popularmovies.jobServices;

import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.GetMoviesAndSaveInDatabase;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


public class GetTopRatedMoviesFromNetworkAndSaveInDatabaseWorker extends JobService {
    
    
    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(() -> GetMoviesAndSaveInDatabase.getMoviesAndSaveInDatabase(Constants.TOP_RATED, getApplicationContext())).start();
        
        return true;
    }
    
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}