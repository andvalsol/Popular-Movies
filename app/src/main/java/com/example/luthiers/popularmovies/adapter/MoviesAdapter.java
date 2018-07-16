package com.example.luthiers.popularmovies.adapter;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.utils.LatencyGauging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    
    //Create a global movies array list
    private List<Movie> mMovies = new ArrayList<>();
    
    private final MovieItemClicked mMovieItemClicked;
    private final Picasso mPicasso;
    private final String mProperImageSize;
    
    //Setup a constructor getting the movieItemClicked interface
    @Inject
    public MoviesAdapter(MovieItemClicked movieItemClicked, Picasso picasso) {
        mMovieItemClicked = movieItemClicked;
        mPicasso = picasso;
        
        //Get the proper image size depending on the current network latency as in the creation of the adapter
        mProperImageSize = LatencyGauging.getNetworkLatency();
    }
    
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize the specific view for this MovieViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_item, parent, false);
        
        return new MovieViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //Bind each item to the correspond views
        holder.bind(mMovies.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mMovies.size();
    }
    
    public void updateAdapter(List<Movie> newMovies) {
        //In order to update the adapter in the most efficient way we're going to use DiffUtil
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                new DiffUtilCallback(mMovies, newMovies), true); //We pass the detectMoves as true since we want to add animations to the moving cards
        
        //Update movies
        updateList(newMovies);
        
        result.dispatchUpdatesTo(this);
    }
    
    @MainThread
    private void updateList(List<Movie> newMovies) {
        //Clear the mMovies list
        mMovies.clear();
        
        //Set the newMovies list to the mMovies instance
        mMovies = newMovies;
    }
    
    //Create a public interface to handle movie item clicks
    public interface MovieItemClicked {
        void onMovieItemClicked(Movie movie, MovieViewHolder holder);
    }
    
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        public final ImageView mMoviePosterImage;
        
        MovieViewHolder(View itemView) {
            super(itemView);
            
            //Initialize the movie poster image view
            mMoviePosterImage = itemView.findViewById(R.id.iv_movie_poster);
            mMoviePosterImage.setOnClickListener(this);
        }
        
        void bind(Movie movie) {
            //Use Picasso to load each image
            mPicasso.load(getMoviePosterUrl(movie.getImage(), mProperImageSize)).into(mMoviePosterImage);
        }
        
        @Override
        public void onClick(View v) {
            //We need to send the proper url for the image so that when the DetailActivity opens it has the proper image url
            setProperMoviePosterUrl(getAdapterPosition());
            
            //Send now the movie object with the proper movie poster url
            mMovieItemClicked.onMovieItemClicked(mMovies.get(getAdapterPosition()), this);
        }
    }
    
    /*
    * This method is used because we store in the database just the moviePoster id, and we need the movie poster url.
    * We don't store the url since the have to pass the image height, that height should be set depending on the
    * user's network latency
    * */
    private void setProperMoviePosterUrl(int position) {
        //We need to check that the current movie has not yet the proper movie poster url
        //Get the movie poster from the image that wants to be clicked
        String moviePosterId = mMovies.get(position).getImage();
        
        if (!moviePosterId.startsWith("http")) {
            //The current movie item has not the proper movie poster url
            mMovies.get(position).setImage(getMoviePosterUrl(moviePosterId, mProperImageSize));
        }
    }
    
    //Set the proper url depending on the properImageSize depending on the cellphone's current network latency
    private String getMoviePosterUrl(String moviePoster, String properImageSize) {
        if (moviePoster.startsWith("http")) {
            return moviePoster;
        }
        
        return  "http://image.tmdb.org/t/p/" + properImageSize + moviePoster;
    }
    
    private class DiffUtilCallback extends DiffUtil.Callback {
        
        private final List<Movie> mOldMovies;
        private final List<Movie> mNewMovies;
        
        DiffUtilCallback(List<Movie> oldMovies, List<Movie> newMovies) {
            mOldMovies = oldMovies;
            mNewMovies = newMovies;
        }
        
        @Override
        public int getOldListSize() {
            return mOldMovies.size();
        }
        
        @Override
        public int getNewListSize() {
            return mNewMovies.size();
        }
        
        @Override
        public boolean areItemsTheSame(int oldMoviePosition, int newMoviePosition) {
            //Compare the items using the id
            return mOldMovies.get(oldMoviePosition).getId() == mNewMovies.get(newMoviePosition).getId();
        }
        
        @Override
        public boolean areContentsTheSame(int oldMoviePosition, int newMoviePosition) {
            //This method is called when areItemsTheSame() returns true
            //Since we never change the content of the items, then it will always be true
            return true;
        }
    }
}