package com.example.luthiers.popularmovies.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.luthiers.popularmovies.utils.Constants;
import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.utils.MovieJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieNetworkDataSource {
    
    public MutableLiveData<ArrayList<Movie>> mMovies = new MutableLiveData<>();
    
    @SuppressLint("StaticFieldLeak")
    class FetchMovies extends AsyncTask<String, Void, String> {
        
        @Override
        protected String doInBackground(String... strings) {
            try {
                return setupHttConnection(strings[0]);
            } catch (IOException e) {
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String s) {
            //Check that the received response is not empty
            if (s != null) mMovies.setValue(MovieJson.getMovieFromJson(s));
            else mMovies.setValue(null);
        }
    }
    
    public void getMoviesFromMovieDB(String filter) {
        new FetchMovies().execute(filter);
    }
    
    private String setupHttConnection(String filter) throws IOException {
        //Get the URL from the buildUrl method
        URL weatherRequestUrl = buildUrl(filter);
        
        HttpURLConnection urlConnection = (HttpURLConnection) weatherRequestUrl.openConnection();
        try {
            //Open a connection using the received url
            HttpURLConnection con = (HttpURLConnection) weatherRequestUrl.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            //print in String
            return response.toString();
        } finally {
            urlConnection.disconnect();
        }
    }
    
    private URL buildUrl(String filter) {
        //Format the requestUrl
        String MOVIE_DB_API_REQUEST = "https://api.themoviedb.org/3";
        String MOVIE_API_KEY = "?&api_key=" + Constants.MOVIE_DB_API_KEY;
        
        //Setup the requestUrl
        String requestUrl = MOVIE_DB_API_REQUEST + filter + MOVIE_API_KEY;
        
        Uri builtUri = Uri.parse(requestUrl)
                .buildUpon()
                .build();
        
        URL url;
        try {
            url = new URL(builtUri.toString());
            
            return url;
    
        } catch (MalformedURLException e) {
            return null;
        }
    }
}