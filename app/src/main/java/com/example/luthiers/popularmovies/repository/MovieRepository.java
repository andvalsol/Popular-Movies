package com.example.luthiers.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.network.MovieNetworkDataSource;
import com.example.luthiers.popularmovies.repository.network.NetworkBoundResource;
import com.example.luthiers.popularmovies.repository.network.models.Resource;
import com.example.luthiers.popularmovies.repository.room.MovieDao;
import com.example.luthiers.popularmovies.utils.AppExecutors;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.MovieUtils;

import java.io.IOException;
import java.util.List;

public class MovieRepository {
    
    private final MovieDao mMovieDao;
    private final MovieNetworkDataSource mMovieNetworkDataSource;
    private final AppExecutors mAppExecutors;
    
    public MovieRepository(MovieDao movieDao, AppExecutors appExecutors) {
        //Initialize the mMovieDao instance
        mMovieDao = movieDao;
        
        //Initialize the mMovieNetworkDataSource instance
        mMovieNetworkDataSource = new MovieNetworkDataSource();
        
        //Initialize the mAppExecutors instance
        mAppExecutors = appExecutors;
    }
    
    /*
     * From the Android Architecture Guide we get:
     * Use the database as single source of truth, because if the repository were to return the response from the web,
     * our UI could potentially show inconsistency
     * */
    public LiveData<Resource<List<Movie>>> getMovies(String filter) {
        return new NetworkBoundResource<List<Movie>, String>(mAppExecutors) {
            
            @Override
            protected void saveCallResult(@NonNull String jsonMovies) {
                //Use the MovieUtils.getMoviesFromJsonResponse
                List movies = MovieUtils.getMoviesFromJsonResponse(jsonMovies);
                
                //Insert the movies into the MovieDatabase
                mMovieDao.insertListMovies(movies);
            }
            
            @Override
            protected boolean shouldFetch(@Nullable List<Movie> movies) {
                return (movies == null || movies.isEmpty());
            }
            
            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                //Load from the database depending on the desired filter
                if (filter.equals(Constants.MOST_POPULAR)) return mMovieDao.getMostPopularMovies();
                else return mMovieDao.getTopRatedMovies();
            }
            
            @NonNull
            @Override
            protected LiveData<String> createCall() {
                MutableLiveData moviesMutableLiveData = new MutableLiveData<String>();
                
                //Since we're doing a network request, we need to do via in the diskIO thread
                mAppExecutors.diskIO().execute(() -> {
                    //We need to use post value since setValue cannot be invoke on a background thread
                    try {
                        moviesMutableLiveData.postValue(mMovieNetworkDataSource.getMoviesFromNetwork(filter));
                        
                    } catch (IOException e) {
                        moviesMutableLiveData.postValue(null);
                    }
                });
                //Create a Mutable Live Data
                
                return moviesMutableLiveData;
            }
        }.getAsLiveData();
    }
}