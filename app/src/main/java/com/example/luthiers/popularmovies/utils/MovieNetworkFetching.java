package com.example.luthiers.popularmovies.utils;


import com.example.luthiers.popularmovies.CinephileApplication;
import com.example.luthiers.popularmovies.jobServices.GetPopularMoviesFromNetworkAndSaveInDatabaseWorker;
import com.example.luthiers.popularmovies.jobServices.GetTopRatedMoviesFromNetworkAndSaveInDatabaseWorker;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MovieNetworkFetching {
    
    private static final String MOVIE_NETWORK_JOB_TAG = "movie_network_job_tag";
    
    private static boolean sInitialized;
    
    private static FirebaseJobDispatcher sJob = new FirebaseJobDispatcher(new GooglePlayDriver(CinephileApplication.getAppContext()));
    
    
    synchronized public static void scheduleMovieNetworkFetching() {
        //Check that the scheduling hasn't been initialized
        if (sInitialized) return;
        
        //Set the recurring task
        addRecurringJobs();
        
        
        //Set the MovieNetworkFetching class as initialized
        sInitialized = true;
    }
    
    private static void addRecurringJobs() {
        Job getPopularMoviesJob = sJob
                .newJobBuilder()
                .setService(GetPopularMoviesFromNetworkAndSaveInDatabaseWorker.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setTag(MOVIE_NETWORK_JOB_TAG)
                .setTrigger(Trigger.executionWindow(10, 30))
                .build();
    
        Job getTopRatedMoviesJob = sJob
                .newJobBuilder()
                .setService(GetTopRatedMoviesFromNetworkAndSaveInDatabaseWorker.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setTag(MOVIE_NETWORK_JOB_TAG)
                .setTrigger(Trigger.executionWindow(5, 20))
                .build();
        
        sJob.mustSchedule(getTopRatedMoviesJob);
        sJob.mustSchedule(getPopularMoviesJob);
    }
}