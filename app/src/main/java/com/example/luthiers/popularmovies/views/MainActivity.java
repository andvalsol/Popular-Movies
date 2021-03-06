package com.example.luthiers.popularmovies.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.luthiers.popularmovies.CinephileApplication;
import com.example.luthiers.popularmovies.dependencyInjection.components.DaggerMainActivityComponent;
import com.example.luthiers.popularmovies.dependencyInjection.components.MainActivityComponent;
import com.example.luthiers.popularmovies.dependencyInjection.modules.MainActivityModule;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.MovieViewModel;
import com.example.luthiers.popularmovies.adapter.MoviesAdapter;
import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.utils.MovieNetworkFetching;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClicked {
    
    private String mFilter, GRID_STATE_KEY = "gridStateKey";
    
    private MovieViewModel mMovieViewModel;
    
    private ImageButton mFab;
    
    private ConstraintLayout mFiltersLayout;
    
    private GridLayoutManager mGridLayoutManager;
    
    private Parcelable mGridLayoutManagerState;
    
    
    @Inject
    MoviesAdapter mMoviesAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Setup the dependency injection
        MainActivityComponent mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .cinephileApplicationComponent(CinephileApplication.get(this).getCinephileApplicationComponent()) //We need to talk to the CinephileApplication class and get the application component
                .build();
        
        //Initialize the filters layout
        mFiltersLayout = findViewById(R.id.filters_layout);
        
        //Initialize the fab
        mFab = findViewById(R.id.fab);
        //Set the click listener for the fab
        mFab.setOnClickListener(v -> {
            //Check if we need to start or exit the circular reveal animation
            if (mFiltersLayout.getVisibility() == View.INVISIBLE) doCircularReveal();
            else exitCircularReveal();
        });
        
        //Initialize the progress bar
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        
        //Initialize the radio group
        RadioGroup radioGroup = mFiltersLayout.findViewById(R.id.filter_group);
        //Add the listener for the radio group
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            //Check which radio button was checked
            if (checkedId == R.id.rb_top_rated) {
                //Set the proper value for the filter since the user wants to get the top rated movies
                mFilter = Constants.TOP_RATED;
                
            } else if (checkedId == R.id.rb_most_popular) {
                //Set the proper value for the filter since the user wants to get the most popular movies
                mFilter = Constants.MOST_POPULAR;
                
            } else {
                //Set the property value for the filter since the user wants to get the movies he/she marked as favorite
                mFilter = Constants.FAVORITE;
            }
            
            //Set the filter to the MovieViewModel
            mMovieViewModel.mFilter.setValue(mFilter);
            
            //Add the progress bar
            progressBar.setVisibility(View.VISIBLE);
        });
        
        //Create an instance of the MoviesAdapter using Dagger2
        mainActivityComponent.injectMainActivity(this);
        
        //Set the filter to be most popular as default
        mFilter = Constants.MOST_POPULAR;
        
        // Initialize the mGridLayoutManager
        mGridLayoutManager = new GridLayoutManager(this, setGridColumns());
        
        //Initialize a recycler view
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        //The size of each item from the recycler view won't change, so set this value to be true, to let recycler view do some optimizations
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mGridLayoutManager);
        //Set the moviesAdapter to the recycler view
        recyclerView.setAdapter(mMoviesAdapter);
        
        //Get the movies from the ViewModel
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        //Set the mFilter variable from the view model to be the current mFilter variable
        mMovieViewModel.mFilter.setValue(mFilter);
        //Listen to the movies gotten from the repository
        mMovieViewModel.getMoviesFromRepository().observe(this, movies -> {
            //We don't need to check if its loading, since initially we set the progress bar to be visible
            
            if (movies != null) {
                switch (movies.status) {
                    case SUCCESS:
                        //Remove the progress bar since we got new data :)
                        progressBar.setVisibility(View.GONE);
                        
                        //Add the list to the movies adapter
                        mMoviesAdapter.updateAdapter(movies.data);
                        break;
                    case ERROR:
                        //Check if the progress bar is still visible, if it is then remove it
                        progressBar.setVisibility(View.GONE);
                        
                        //Show a text to the user displaying the proper error message
                        Toast.makeText(this, R.string.error_displaying_movies, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        //Should be loading
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                }
            }
            
            mGridLayoutManager.onRestoreInstanceState(mGridLayoutManagerState);
        });
        
        //Set the periodic work request
        MovieNetworkFetching.scheduleMovieNetworkFetching();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the list state
        mGridLayoutManagerState = mGridLayoutManager.onSaveInstanceState();
        
        // Add the list state into the outState variable
        outState.putParcelable(GRID_STATE_KEY, mGridLayoutManagerState);
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
        // Retrieve the list state
        if (savedInstanceState != null) {
            mGridLayoutManagerState = savedInstanceState.getParcelable(GRID_STATE_KEY);
        }
    }
    
    private int setGridColumns() {
        //Set the maximum size for the grid item
        int gridItemMaxSize = getResources().getDimensionPixelSize(R.dimen.grid_item_max_size); //Max grid item is set as 160dp
        
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        
        //Get the screen width size
        int screenWidth = displaymetrics.widthPixels;
        
        int columns = screenWidth / gridItemMaxSize;
        
        //We want to make sure that the are only 3 columns max, since 4 doesn't look well visually
        return columns >= 3 ? 3 : columns;
    }
    
    private void doCircularReveal() {
        //Get the center of the fab so that the circular reveal starts there
        int centerX = (mFab.getLeft() + mFab.getRight()) / 2;
        int centerY = mFab.getHeight() / 2;
        
        float startRadius = 0;
        
        //The final end radius should the the max value for the filters layout
        float endRadius = (float) Math.hypot(mFiltersLayout.getWidth(), mFiltersLayout.getHeight());
        
        //Create the animator for the filter view
        Animator animation = ViewAnimationUtils.createCircularReveal(mFiltersLayout, centerX, centerY, startRadius, endRadius);
        animation.setDuration(getResources().getInteger(R.integer.rapid_animation));
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                
                //Change the image resource of the fab
                mFab.setImageResource(R.drawable.ic_close);
            }
        });
        
        //Set the filter layout to be invisible
        mFiltersLayout.setVisibility(View.VISIBLE);
        
        animation.start();
    }
    
    private void exitCircularReveal() {
        //Get the center of the fab so that the circular reveal starts there
        int centerX = (mFab.getLeft() + mFab.getRight()) / 2;
        int centerY = mFab.getHeight() / 2;
        
        float initialRadius = mFiltersLayout.getWidth();
        
        Animator animation = ViewAnimationUtils.createCircularReveal(mFiltersLayout, centerX, centerY, initialRadius, 0);
        animation.setDuration(getResources().getInteger(R.integer.rapid_animation));
        // make the view invisible when the animation is done
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //Set the visibility of the filters layout as View.INVISIBLE
                mFiltersLayout.setVisibility(View.INVISIBLE);
                
                //Change the image resource of the fab
                mFab.setImageResource(R.drawable.ic_filter);
            }
        });
        
        // start the animation
        animation.start();
    }
    
    @Override
    public void onMovieItemClicked(Movie movie, MoviesAdapter.MovieViewHolder holder) {
        //Check if the filters layout is visible, if it is then exitCircularReveal
        if (mFiltersLayout.getVisibility() == View.VISIBLE) {
            exitCircularReveal();
        }
        
        //Set the proper intent to open the DetailActivity and send the movie pojo
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", movie);
        //Make a transition effect for the shared image (movie poster)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                this, holder.mMoviePosterImage, holder.mMoviePosterImage.getTransitionName()).toBundle());
    }
}