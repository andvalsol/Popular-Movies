package com.example.luthiers.popularmovies.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.MovieViewModel;
import com.example.luthiers.popularmovies.MoviesAdapter;
import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.entities.Movie;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClicked {
    
    private String mFilter;//Initialize the filter as most popular by default
    private MovieViewModel mMovieViewModel;
    private FloatingActionButton mFab;
    private ConstraintLayout mFiltersLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        
        //Create an instance of the MoviesAdapter
        MoviesAdapter moviesAdapter = new MoviesAdapter(this);
        
        //Set the layout manager to the recycler view, it's going to be the GridLayoutManager with 2 columns
        int NUMBER_OF_COLUMNS = 2;
        
        //Set the filter to be most popular as default
        mFilter = Constants.MOST_POPULAR;
        
        //Initialize a recycler view
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        //The size of each item from the recycler view won't change, so set this value to be true, to let recycler view do some optimizations
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        //Set the moviesAdapter to the recycler view
        recyclerView.setAdapter(moviesAdapter);
        
        //Get the movies from the ViewModel
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        //Set the mFilter variable from the view model to be the current mFilter variable
        mMovieViewModel.mFilter.setValue(mFilter);
        //Listen to the movies gotten from the repository
        mMovieViewModel.getMoviesFromRepository().observe(this, movies -> {
            if (movies == null)
                Toast.makeText(this, R.string.error_displaying_movies, Toast.LENGTH_LONG).show();
            else moviesAdapter.addList(movies);
        });
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
        animation.setDuration(150);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                
                //Remove the surrounding shadow of the fab
                mFab.setElevation(0);
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
        animation.setDuration(150);
        // make the view invisible when the animation is done
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //Set the visibility of the filters layout as View.INVISIBLE
                mFiltersLayout.setVisibility(View.INVISIBLE);
                
                //Add the elevation to the fab again
                mFab.setElevation(6); //6dp elevation according to the Material Design guidelines
            }
        });
        
        // start the animation
        animation.start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.topRated) {
            //Set the proper value for the filter since the user wants to get the top rated movies
            mFilter = Constants.TOP_RATED;
            
            //Set the filter to the MovieViewModel
            mMovieViewModel.mFilter.setValue(mFilter);
        } else {
            //Set the proper value for the filter since the user wants to get the most popular movies
            mFilter = Constants.MOST_POPULAR;
            
            //Set the filter to the MovieViewModel
            mMovieViewModel.mFilter.setValue(mFilter);
        }
        
        return super.onOptionsItemSelected(item);
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