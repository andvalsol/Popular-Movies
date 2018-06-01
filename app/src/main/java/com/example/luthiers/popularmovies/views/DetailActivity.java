package com.example.luthiers.popularmovies.views;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    
    private TextView mTitle, mOverview, mReleaseDate, mTapToShowInfo;
    private ImageView mMoviePoster;
    private RatingBar mRatingBar;
    private ConstraintLayout mConstraintLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        //Initialize the text views
        mTitle = findViewById(R.id.tv_title);
        mOverview = findViewById(R.id.tv_overview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mTapToShowInfo = findViewById(R.id.tv_tap);
        
        //Initialize the constraint layout from the activity_detail
        mConstraintLayout = findViewById(R.id.layout);
        
        //Initialize the rating bar
        mRatingBar = findViewById(R.id.rb_movie_rating);
        
        //Initialize the image view
        mMoviePoster = findViewById(R.id.iv_movie_poster);
        
        //Check if we have received an intent
        if (getIntent().getExtras() != null) {
            //Get the movie POJO
            Movie movie = getIntent().getParcelableExtra("movie");
            
            //Populate UI with the movie POJO gotten from the Main Activity
            populateUI(movie);
        }
        
        //Create a constraint set
        ConstraintSet constraintSet = new ConstraintSet();
        
        //Create Change Bounds
        android.support.transition.ChangeBounds changeBounds = new android.support.transition.ChangeBounds();
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        changeBounds.setDuration(getResources().getInteger(R.integer.info_animation));
        
        //Set the info arrow click listener
        mMoviePoster.setOnClickListener(v -> {
            //Show the information layout
            setInfoLayoutVisibility(constraintSet, changeBounds);
        });
    }
    
    private void setInfoLayoutVisibility(ConstraintSet constraintSet, android.support.transition.ChangeBounds changeBounds) {
        if (mTapToShowInfo.getVisibility() == View.VISIBLE) {
            //Show the info layout
            //Clone the activity_detail_alt layout to the constraint set
            constraintSet.clone(this, R.layout.activity_detail_alt);
        } else {
            //Hide the info layout
            //Clone the activity_detail layout to the constraint set
            constraintSet.clone(this, R.layout.activity_detail);
        }
        
        TransitionManager.beginDelayedTransition(mConstraintLayout, changeBounds);
        constraintSet.applyTo(mConstraintLayout);
    }
    
    private void populateUI(Movie movie) {
        mTitle.setText(movie.getTitle());
        //Sometimes we don't receive an overview, so check if its not empty
        if (!movie.getOverview().equals("")) {
            Log.d("MovieItem", "The overview is: " + movie.getOverview());
            mOverview.setVisibility(View.VISIBLE);
            mOverview.setText(movie.getOverview());
        }
        
        //Sometimes the movie has not been rated yet
        if (movie.getRating() != 0.0) {
            //Divide the rating by 2 since the max rating is 10 and we use a max rating of 5
            Log.d("MovieItem", "The rating is: " + movie.getRating());
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
}