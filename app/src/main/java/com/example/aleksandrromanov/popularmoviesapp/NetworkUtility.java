package com.example.aleksandrromanov.popularmoviesapp;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aleksandrromanov on 02/02/2017.
 */

public final class NetworkUtility {

    private static String LOG_TAG = NetworkUtility.class.getName();
    private static String AUTHORITY = "api.themoviedb.org";
    private static String PROTOCOL = "https";
    private static String LANGUAGE = "en-US";



    /**
     * Generates a random page within a range of 984 as given
     * in the API docs.
     * http://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
     * @return String representing a page number in the API
     */

    private static String generateRandomPage(){
        Random random = new Random();
        int pageNumber = random.nextInt(984) + 1;
        return String.valueOf(pageNumber);

    }

    /**
     * Builds the URI representing Movie DB endpoint and
     * generates the URL object.
     * @param path
     * @param API_KEY
     * @return Movie DB API URL
     */
    public static URL buildMovieURL(String path, String API_KEY){
        String page = generateRandomPage();
        URL movieURL = null;
        Uri.Builder builder = new Uri.Builder();
        Uri movieUri = builder.scheme(PROTOCOL)
                .authority(AUTHORITY)
                .appendPath("3")
                .appendPath("movie").appendPath(path)
                .appendQueryParameter("api_key",API_KEY).appendQueryParameter("language",LANGUAGE)
                .appendQueryParameter("page",page)
                .build();

        try{
            movieURL = new URL(movieUri.toString());
            Log.d(LOG_TAG, movieURL.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return movieURL;
    }

    /**
     * Performs an HTTP request using OkHTTP library and retrieves
     * JSON string with popular movies
     * http://stackoverflow.com/questions/28221555/how-does-okhttp-get-json-string
     * @param movieURL
     * @return Temporary JSON string returned from API call
     */
    public static String getMovieJSON(URL movieURL){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(movieURL)
                .build();
        String movieJSON = null;

        try {
            Response response = okHttpClient.newCall(request).execute();
            movieJSON = response.body().string();

        }catch (IOException e){
            e.printStackTrace();
        }

        return movieJSON;

    }

}
