package com.example.luthiers.popularmovies.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    
    private TextView mTitle, mOverview, mReleaseDate;
    private ImageView mMoviePoster;
    private RatingBar mRatingBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        
        //Initialize the text views
        mTitle = findViewById(R.id.tv_title);
        mOverview = findViewById(R.id.tv_overview);
        mReleaseDate = findViewById(R.id.tv_release_date);
        
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
            mRatingBar.setVisibility(View.VISIBLE);
            mRatingBar.setRating(movie.getRating());
        }
        
        //Sometimes the release date is not available
        //Use append since we want to say -> Release Date: ##/##/####
        if (!movie.getReleaseDate().equals("")) {
            mReleaseDate.setVisibility(View.VISIBLE);
            mReleaseDate.append(movie.getReleaseDate());
        }
        
        Picasso.get().load(movie.getImage()).into(mMoviePoster);
    }
}