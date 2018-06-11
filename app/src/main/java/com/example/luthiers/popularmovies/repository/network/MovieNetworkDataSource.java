package com.example.luthiers.popularmovies.repository.network;

import android.net.Uri;
import android.util.Log;

import com.example.luthiers.popularmovies.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieNetworkDataSource {
    
    //This method could throw an IOException
    public String getMoviesFromNetwork(String filter) throws IOException {
    
        Log.d("NetworkResponse", "The response from the network is: " + setupHttConnection(filter));
        
        //Get the json response
        return setupHttConnection(filter);
    }
    
    private static String setupHttConnection(String filter) throws IOException {
        //Get the URL from the buildUrl method
        URL movieRequestUrl = buildUrl(filter);
        
        HttpURLConnection urlConnection = (HttpURLConnection) movieRequestUrl.openConnection();
        try {
            //Open a connection using the received url
            HttpURLConnection con = (HttpURLConnection) movieRequestUrl.openConnection();
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
    
    private static URL buildUrl(String filter) {
        //Format the requestUrl
        final String MOVIE_DB_API_REQUEST = "https://api.themoviedb.org/3/movie/";
        final String MOVIE_API_KEY = "?&api_key=" + Constants.MOVIE_DB_API_KEY;
        final String QUERY_PARAMS = "&language=en-US&page=1";
        
        //Setup the requestUrl
        String requestUrl = MOVIE_DB_API_REQUEST + filter + MOVIE_API_KEY + QUERY_PARAMS;
        
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