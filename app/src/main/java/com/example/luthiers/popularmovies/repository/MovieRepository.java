package com.example.luthiers.popularmovies.repository;

import android.arch.lifecycle.LiveData;

import com.example.luthiers.popularmovies.entities.Movie;

import java.util.ArrayList;

public class MovieRepository {
    
    public LiveData<ArrayList<Movie>> getMoviesFromNetwork(String filter) {
        MovieNetworkDataSource movieNetworkDataSource = new MovieNetworkDataSource();
        movieNetworkDataSource.getMoviesFromMovieDB(filter);
        
        return movieNetworkDataSource.mMovies;
    }
}
