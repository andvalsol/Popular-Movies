package com.example.luthiers.popularmovies.repository.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.luthiers.popularmovies.repository.network.models.Resource;
import com.example.luthiers.popularmovies.utils.AppExecutors;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private AppExecutors mAppExecutors;
    
    @MainThread
    protected NetworkBoundResource(AppExecutors appExecutors) {
        //Initialize the value of mAppExecutors
        mAppExecutors = appExecutors;
        
        result.setValue(Resource.loading(null));
        
        LiveData<ResultType> databaseSource = loadFromDb();
        
        result.addSource(databaseSource, data -> {
            result.removeSource(databaseSource);
            
            if (shouldFetch(data)) {
                //We should fetch data since there's no data available in the Database
                Log.d("Fetch", "should fetch data");
//                fetchFromNetwork(databaseSource);
            } else {
                Log.d("Fetch", "shouldn't fetch data");
//                result.addSource(databaseSource, newData ->
//                        updateValue(Resource.success(newData)));
            }
        });
    }
    
    private void fetchFromNetwork(final LiveData<ResultType> databaseSource) {
        LiveData<RequestType> networkResponse = createCall();
        
        //Re attach databaseSource as new source, so it can dispatch the latest value it has
        result.addSource(databaseSource, newData ->
                result.setValue(Resource.loading(newData))
        );
        
        //Attach the network response also to the result MediatorLiveData
        result.addSource(networkResponse, response -> {
                    //We are getting a change, therefore remove all the sources result instance has attached
                    result.removeSource(networkResponse);
                    result.removeSource(databaseSource);
                    
                    //Check that the response is not null
                    if (response != null) {
                        //We got data from the network request
                        //Use the diskIO thread to execute the
                        mAppExecutors.diskIO().execute(() -> {
                            saveCallResult(response);
                            
                            /*
                            After we've saved the data into the database we need to get a new live data
                            in order to get the latest values
                            */
                            mAppExecutors.mainThread().execute(() ->
                                    result.addSource(loadFromDb(), newData ->
                                            updateValue(Resource.success(newData))
                                    )
                            );
                        });
                        
                    } else {
                        //We got a null value from the network request
                        result.addSource(databaseSource, newData ->
                                updateValue(Resource.error("There was an error getting data from the network", newData)));
                    }
                }
        );
    }
    
    @MainThread
    private void updateValue(Resource<ResultType> newValue) {
        //Check that the new value is not the same as the last value from the database
        if (result.getValue() != newValue) {
            //Set the new value to the result Mediator Live Data
            result.setValue(newValue);
        }
    }
    
    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType items);
    
    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);
    
    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();
    
    // Called to create the API call.
    @WorkerThread
    protected abstract LiveData<RequestType> createCall();
    
    // returns a LiveData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}