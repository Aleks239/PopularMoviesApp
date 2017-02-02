package com.example.aleksandrromanov.popularmoviesapp;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aleksandrromanov on 02/02/2017.
 */

public final class NetworkUtility {

    private static String LOG_TAG = NetworkUtility.class.getName();
    private static String BASE_API_URL = "https://api.themoviedb.org/3/movie";
    private static String LANGUAGE = "en-US";

    public URL buildMovieURL(String path){
        URL movieURL = null;
        Uri.Builder builder = new Uri.Builder();
        String API_KEY = String.valueOf(R.string.MOVIE_DB_API_KEY);
        Uri movieUri = builder.appendPath(BASE_API_URL).appendPath(path)
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("language",LANGUAGE).build();

        try{
            movieURL = new URL(movieUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return movieURL;
    }





}
