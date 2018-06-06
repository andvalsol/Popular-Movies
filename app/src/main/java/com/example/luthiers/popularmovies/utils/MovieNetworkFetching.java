package com.example.luthiers.popularmovies.utils;

import com.example.luthiers.popularmovies.workers.GetMoviesFromNetworkAndSaveInDatabaseWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MovieNetworkFetching {
    
    private static final String MOVIE_NETWORK_JOB_TAG = "movie_network_job_tag";
    private static boolean sInitialized;
    
    synchronized public static void scheduleMovieNetworkFetching() {
        //Check that the scheduling hasn't been initialized
        if (sInitialized) return;
        
        //Set the recurring task
        initRecurringTask();
        
        //Set the MovieNetworkFetching class as initialized
        sInitialized = true;
    }
    
    private static void initRecurringTask() {
        int requestTimeInDays = 7; //TODO set this value via Firebase Remote Config
        /* Because not every day there's a movie release, schedule the network request for movies every 7 days,
         * this value can be tested better and set via Firebase Remote Config for better user experience
         * */
        PeriodicWorkRequest.Builder movieNetworkFetchingBuilder =
                new PeriodicWorkRequest.Builder(GetMoviesFromNetworkAndSaveInDatabaseWorker.class, requestTimeInDays, TimeUnit.DAYS);
        
        //Setup task constraints
        Constraints movieNetworkFetchingConstraints = setConstraints();
        
        //Create the GetMoviesFromNetworkAndSaveInDatabaseWorker instance
        PeriodicWorkRequest movieNetworkFetchingWorkRequest = movieNetworkFetchingBuilder
                .setConstraints(movieNetworkFetchingConstraints)
                .addTag(MOVIE_NETWORK_JOB_TAG)
                .build();
        
        //Initialize a WorkManager
        WorkManager moviesWorkManager = WorkManager.getInstance();
        //Add the movieNetworkFetchingWorkRequest to the queue
        moviesWorkManager.enqueue(movieNetworkFetchingWorkRequest);
    }
    
    private static Constraints setConstraints() {
        //setRequiresDeviceIdle requires SDK>= M
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return new Constraints.Builder()
                    .setRequiresStorageNotLow(true) //This make the app not making making any network requests if the storage space is low
                    .setRequiresDeviceIdle(true) //This ensures that the work wont run if the device is in active use
                    .build();
        } else {
            return new Constraints.Builder()
                    .setRequiresStorageNotLow(true) //This make the app not making making any network requests if the storage space is low
                    .build();
        }
    }
}