package com.example.luthiers.popularmovies.views;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.adapter.ReviewsAdapter;
import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.entities.Review;
import com.example.luthiers.popularmovies.repository.network.GetMovieTrailerAsyncTask;
import com.example.luthiers.popularmovies.repository.network.GetReviewsFromMovieAsyncTask;
import com.example.luthiers.popularmovies.utils.AppExecutors;
import com.example.luthiers.popularmovies.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements ReviewsAdapter.ReviewsInterface {
    
    private TextView mTitle, mOverview, mReleaseDate, mMovieReviews;
    private ImageView mMoviePoster;
    private RatingBar mRatingBar;
    private ImageView mMovieTrailerButton;
    private Group mGroup;
    private RecyclerView mReviews;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_alt);
        
        //Initialize the text views
        mTitle = findViewById(R.id.tv_title);
        mOverview = findViewById(R.id.tv_overview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mMovieReviews = findViewById(R.id.tv_watch_reviews);
        
        mGroup = findViewById(R.id.group);
        
        mReviews = findViewById(R.id.rv_movie_reviews);
        
        //Initialize the rating bar
        mRatingBar = findViewById(R.id.rb_movie_rating);
        
        //Initialize the image views
        mMoviePoster = findViewById(R.id.iv_movie_poster);
        mMovieTrailerButton = findViewById(R.id.iv_movie_trailer);
        
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
        new GetReviewsFromMovieAsyncTask(movieReviews -> {
            if (movieReviews != null && movieReviews.size() > 0) {
                setInfoLayoutVisibility();
                
                
                //Set the visibility of the WATCH MOVIE REVIEWS text view to VISIBLE
                mMovieReviews.setVisibility(View.VISIBLE);
                
                //Set the movie review to the correspond recycler view
                setMovieReviews(movieReviews);
            }
        }).executeOnExecutor(AppExecutors.getInstance().networkIO(), movieId);
    }
    
    private void setInfoLayoutVisibility() {
        mMovieReviews.setOnClickListener(v -> {
            //Hide the information of the movie and make the reviews visible to the user
            mGroup.setVisibility(View.GONE);
            mReviews.setVisibility(View.VISIBLE);
        });
    }
    
    private void setMovieReviews(List<Review> reviews) {
        //The size of each item from the recycler view won't change, so set this value to be true, to let recycler view do some optimizations
        mReviews.setHasFixedSize(true);
        mReviews.setLayoutManager(new LinearLayoutManager(this));
        //Set the moviesAdapter to the recycler view
        mReviews.setAdapter(new ReviewsAdapter(reviews, this::recyclerViewOnClick));
    }
    
    private void getMovieTrailers(int movieId) {
        new GetMovieTrailerAsyncTask(movieTrailerKey -> {
            if (!movieTrailerKey.isEmpty()) {
                mMovieTrailerButton.setOnClickListener(v -> launchYouTubeTrailer(movieTrailerKey));
                
                mMovieTrailerButton.setVisibility(View.VISIBLE);
            }
        }).executeOnExecutor(AppExecutors.getInstance().networkIO(), movieId);
    }
    
    private void launchYouTubeTrailer(String movieTrailerKey) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MovieUtils.getMovieTrailer(movieTrailerKey))));
    }
    
    private void populateUI(Movie movie) {
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
        
        Picasso.get()
                .load(movie.getImage())
                .into(mMoviePoster);
    }
    
    @Override
    public void recyclerViewOnClick() {
        Log.d("Reviews", "mReviews recycler view being clicked");
        //Hide the movie reviews and make the information of the movie visible to the user
        mReviews.setVisibility(View.GONE);
        mGroup.setVisibility(View.VISIBLE);
    }
}