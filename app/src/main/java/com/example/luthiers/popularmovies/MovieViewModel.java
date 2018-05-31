package com.example.luthiers.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.MovieRepository;

import java.util.ArrayList;

public class MovieViewModel extends ViewModel {
    
    public MutableLiveData<String> mFilter = new MutableLiveData<>();
    
    private LiveData<ArrayList<Movie>> mMovies;
    private MovieRepository mMovieRepository = new MovieRepository();
    
    public LiveData<ArrayList<Movie>> getMoviesFromRepository() {
        /*
        * This allow us to get the movies from the repository if it has not been initialized,
        and if it is initialized, then get the current movies
        * */
        if (mMovies == null) {
            mMovies = Transformations.switchMap(mFilter, filter -> mMovieRepository.getMoviesFromNetwork(filter));
        }
        
        return mMovies;
    }
}
