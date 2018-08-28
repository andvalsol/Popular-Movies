package com.example.luthiers.popularmovies.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.adapter.ReviewsAdapter;
import com.example.luthiers.popularmovies.adapter.TrailersAdapter;
import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.entities.Review;
import com.example.luthiers.popularmovies.repository.network.GetMovieTrailerAsyncTask;
import com.example.luthiers.popularmovies.repository.network.GetReviewsFromMovieAsyncTask;
import com.example.luthiers.popularmovies.repository.room.asyncTask.InsertMovieAsyncTask;
import com.example.luthiers.popularmovies.repository.room.asyncTask.RemoveMovieAsyncTask;
import com.example.luthiers.popularmovies.utils.AppExecutors;
import com.example.luthiers.popularmovies.utils.CheckableImageButton;
import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements ReviewsAdapter.ReviewsInterface, TrailersAdapter.TrailersInterface {
    
    private TextView mTitle, mOverview, mReleaseDate, mMovieReviews;
    private ImageView mMoviePoster;
    private RatingBar mRatingBar;
    private CheckableImageButton mFavoriteMovie;
    private ScrollView mScrollView;
    private RecyclerView mrvReviews, mrvTrailers;
    private List<Review> mReviews;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        //Initialize the text views
        mTitle = findViewById(R.id.tv_title);
        mOverview = findViewById(R.id.tv_overview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mMovieReviews = findViewById(R.id.tv_watch_reviews);
        
        mScrollView = findViewById(R.id.sv);
        
        mrvReviews = findViewById(R.id.rv_movie_reviews);
        mrvTrailers = findViewById(R.id.rv_movie_trailers);
        
        //Initialize the rating bar
        mRatingBar = findViewById(R.id.rb_movie_rating);
        
        //Initialize the image views
        mMoviePoster = findViewById(R.id.iv_movie_poster);
        mFavoriteMovie = findViewById(R.id.iv_movie_favorite);
        
        //Check the save instance state
        if (savedInstanceState != null) {
            mReviews = MovieUtils.getReviewsFromJsonReviews(savedInstanceState.getString("reviews", ""));
        }
        
        //Check if we have received an intent
        if (getIntent().getExtras() != null) {
            //Get the movie POJO
            Movie movie = getIntent().getParcelableExtra("movie");
            
            //Populate UI with the movie POJO gotten from the Main Activity
            populateUI(movie);
            
            //Get the trailers from the selected movie
            getMovieTrailers(movie.getId());
            
            //Get the movie reviews
            getMovieReviews(movie.getId());
        }
    }
    
    private void getMovieReviews(int movieId) {
        if (mReviews == null) {
            //There are already reviews gotten from the savedInstanceState, so there's no need to check for movie reviews
            new GetReviewsFromMovieAsyncTask(movieReviews -> {
                if (movieReviews != null && movieReviews.size() > 0) {
                    //Save the reviews, in case we need to use them for a configuration change
                    mReviews = movieReviews;
                    
                    setInfoLayoutVisibility();
                    
                    //Set the visibility of the WATCH MOVIE REVIEWS text view to VISIBLE
                    mMovieReviews.setVisibility(View.VISIBLE);
                    
                    //Set the movie review to the correspond recycler view
                    setMovieReviews(movieReviews);
                }
            }).executeOnExecutor(AppExecutors.getInstance().networkIO(), movieId);
        } else {
            //Set the movie reviews to the recycler view
            setMovieReviews(mReviews);
            
            //Add the proper logic for the movie reviews visibility
            setMovieReviewsToVisible();
        }
    }
    
    private void setInfoLayoutVisibility() {
        mMovieReviews.setOnClickListener(v -> setMovieReviewsToVisible());
    }
    
    private void setMovieReviewsToVisible() {
        //Hide the information of the movie and make the reviews visible to the user
        mScrollView.setVisibility(View.GONE);
        
        mrvReviews.setVisibility(View.VISIBLE);
    }
    
    private void setMovieReviews(List<Review> reviews) {
        //The size of each item from the recycler view won't change, so set this value to be true, to let recycler view do some optimizations
        mrvReviews.setHasFixedSize(true);
        mrvReviews.setLayoutManager(new LinearLayoutManager(this));
        //Set the moviesAdapter to the recycler view
        mrvReviews.setAdapter(new ReviewsAdapter(reviews, this));
    }
    
    private void getMovieTrailers(int movieId) {
        new GetMovieTrailerAsyncTask(movieTrailerKeys -> {
            if (!movieTrailerKeys.isEmpty()) {
                setMovieTrailers(movieTrailerKeys);
            }
        }).executeOnExecutor(AppExecutors.getInstance().networkIO(), movieId);
    }
    
    private void setMovieTrailers(List<String> movieTrailerKeys) {
        mrvTrailers.setHasFixedSize(true); // To improve performance
        mrvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvTrailers.setAdapter(new TrailersAdapter(movieTrailerKeys, this::launchYouTubeTrailer));
    }
    
    private void launchYouTubeTrailer(String movieTrailerKey) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MovieUtils.getMovieTrailer(movieTrailerKey))));
    }
    
    private void populateUI(Movie movie) {
        //Set the click listener for the favorite movie image view
        mFavoriteMovie.setOnClickListener(v -> {
            if (mFavoriteMovie.isChecked()) { //The user wants to set the movie as favorite
                //Set the movie as favorite
                movie.setQueryAction(Constants.ROOM_FAVORITE);
                
                new InsertMovieAsyncTask(this).executeOnExecutor(AppExecutors.getInstance().networkIO(), movie);
                
            } else { //The user wants to remove the movie from his/her favorites
                new RemoveMovieAsyncTask(this).executeOnExecutor(AppExecutors.getInstance().networkIO(), movie);
            }
        });
        
        mTitle.setText(movie.getTitle());
        //Sometimes we don't receive an overview, so check if its not empty
        if (!movie.getOverview().equals("")) {
            mOverview.setVisibility(View.VISIBLE);
            mOverview.setText(movie.getOverview());
        }
        
        //Sometimes the movie has not been rated yet
        if (movie.getRating() != 0.0) {
            //Divide the rating by 2 since the max rating is 10 and we use a max rating of 5
            mRatingBar.setVisibility(View.VISIBLE);
            mRatingBar.setRating(movie.getRating() / 2);
        }
        
        //Sometimes the release date is not available
        if (!movie.getReleaseDate().equals("")) {
            Log.d("MovieItem", "The release date is: " + movie.getReleaseDate());
            mReleaseDate.setText(movie.getReleaseDate());
        }
        
        checkIfMovieIsFavorite(movie);
        
        Picasso.get()
                .load(movie.getImage())
                .into(mMoviePoster);
    }
    
    private void checkIfMovieIsFavorite(Movie movie) {
        if (movie.getQueryAction() == Constants.ROOM_FAVORITE)
            mFavoriteMovie.setChecked(true);
        else mFavoriteMovie.setChecked(false);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        if (mrvReviews.getVisibility() == View.VISIBLE) {
            //The user is checking the reviews, so save this state
            outState.putString("reviews", MovieUtils.getJsonReviewsFromReviewsList(mReviews));
        }
    }
    
    @Override
    public void recyclerViewOnClick() {
        //Hide the movie reviews and make the information of the movie visible to the user
        mrvReviews.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void trailerOnClickListener(String movieTrailerKey) {
        launchYouTubeTrailer(movieTrailerKey);
    }
}