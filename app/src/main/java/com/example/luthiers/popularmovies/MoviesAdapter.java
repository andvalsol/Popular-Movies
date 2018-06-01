package com.example.luthiers.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luthiers.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    
    //Create a global movies array list
    private ArrayList<Movie> mMovies = new ArrayList<>();
    
    private final MovieItemClicked mMovieItemClicked;
    
    //Setup a constructor getting the movieItemClicked interface
    public MoviesAdapter(MovieItemClicked movieItemClicked) {
        mMovieItemClicked = movieItemClicked;
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
    
    public void addList(ArrayList<Movie> movies) {
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
            Picasso.get().load(movie.getImage()).into(mMoviePosterImage);
        }
        
        @Override
        public void onClick(View v) {
            //Get adapter position
            int position = getAdapterPosition();
            
            mMovieItemClicked.onMovieItemClicked(mMovies.get(position), this);
        }
    }
}