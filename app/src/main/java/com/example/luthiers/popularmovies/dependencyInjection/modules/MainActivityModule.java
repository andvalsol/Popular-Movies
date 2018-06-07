package com.example.luthiers.popularmovies.dependencyInjection.modules;

import com.example.luthiers.popularmovies.MoviesAdapter;
import com.example.luthiers.popularmovies.dependencyInjection.scopes.MainActivityScope;
import com.example.luthiers.popularmovies.views.MainActivity;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    
    private final MoviesAdapter.MovieItemClicked mMovieItemClicked;
    
    //    @Provides
//    @MainActivityScope
//    public MoviesAdapter moviesAdapter(Picasso picasso) {
//        return new MoviesAdapter(mMovieItemClicked, picasso);
//    }
    
    //The previous code can be replaced by the following
    /*
     * Steps:
     * 1- Need to make MoviesAdapter.MovieItemClicked injectable, it needs to be part of the graph
     * 2- Provide the right return instance
     * */
    
    public MainActivityModule(MoviesAdapter.MovieItemClicked movieItemClicked) {
        mMovieItemClicked = movieItemClicked;
    }
    
    //Step 2
    @Provides
    @MainActivityScope
    public MoviesAdapter.MovieItemClicked movieItemClicked() {
        return mMovieItemClicked;
    }
}