package com.example.luthiers.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.utils.LatencyGauging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    
    //Create a global movies array list
    private List<Movie> mMovies = new ArrayList<>();
    
    private final MovieItemClicked mMovieItemClicked;
    
    private String mProperImageSize;
    
    //Setup a constructor getting the movieItemClicked interface
    public MoviesAdapter(MovieItemClicked movieItemClicked) {
        mMovieItemClicked = movieItemClicked;
        
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
    
    public void addList(List<Movie> movies) {
        mMovies = movies;
    
        //Notify the adapter the adding of the list
        notifyDataSetChanged();
    }
    
    //Create a public interface to handle movie item clicks
    public interface MovieItemClicked {
        void onMovieItemClicked(Movie movie, MovieViewHolder holder);
    }
    
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        public ImageView mMoviePosterImage;
        
        MovieViewHolder(View itemView) {
            super(itemView);
            
            //Initialize the movie poster image view
            mMoviePosterImage = itemView.findViewById(R.id.iv_movie_poster);
            mMoviePosterImage.setOnClickListener(this);
        }
        
        void bind(Movie movie) {
            //Use Picasso to load each image
            Picasso.get().load(getMoviePosterUrl(movie.getImage(), mProperImageSize)).into(mMoviePosterImage);
        }
        
        @Override
        public void onClick(View v) {
            //We need to send the proper url for the image so that when the DetailActivity opens it has the proper image url
            setProperMoviePosterUrl(getAdapterPosition());
            
            //Send now the movie object with the proper movie poster url
            mMovieItemClicked.onMovieItemClicked(mMovies.get(getAdapterPosition()), this);
        }
    }
    
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
        return "http://image.tmdb.org/t/p/" + properImageSize + moviePoster;
    }
}