package com.example.luthiers.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.MovieRepository;
import com.example.luthiers.popularmovies.repository.network.models.Resource;
import com.example.luthiers.popularmovies.repository.room.MovieDatabase;
import com.example.luthiers.popularmovies.utils.AppExecutors;

import java.util.List;

//We extend from AndroidViewModel since we need to get the Application context to initialize the MovieDatabase
public class MovieViewModel extends AndroidViewModel {
    
    public final MutableLiveData<String> mFilter = new MutableLiveData<>();
    
    private LiveData<Resource<List<Movie>>> mMovies;
    
    public MovieViewModel(@NonNull Application application) {
        super(application);
    }
    
    public LiveData<Resource<List<Movie>>> getMoviesFromRepository() {
        /*
        * This allow us to get the movies from the repository if it has not been initialized,
        and if it is initialized, then get the current movies
        * */
        if (mMovies == null) {
            //Init a MovieDatabase instance
            MovieDatabase movieDatabase = MovieDatabase.getInstance(getApplication().getApplicationContext());
            
            //Init a MovieRepository instance
            //Create an AppExecutors instance, even though the MovieViewModel is created only once, the AppExecutors is a singleton
            MovieRepository movieRepository = new MovieRepository(movieDatabase.mMovieDao(), AppExecutors.getInstance());
            
            //Add a switchMap since we would like to listen to an specific live data when a filter is set
            mMovies = Transformations.switchMap(mFilter, movieRepository::getMovies);
        }
        
        return mMovies;
    }
}
