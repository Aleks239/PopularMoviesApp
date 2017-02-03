package com.example.aleksandrromanov.popularmoviesapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aleksandrromanov on 02/02/2017.
 */

public final class NetworkUtility {

    private static final String LOG_TAG = NetworkUtility.class.getName();
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String PROTOCOL = "https";
    private static final String LANGUAGE = "en-US";
    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185";
    private static List<String> pathsToImages = new ArrayList<String>();



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
    public static String getMovieJson(URL movieURL){
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

    private static String makeAbsolutePathForImages(String relativePath){
        String absolutePathToImage = IMAGE_BASE_PATH + relativePath;
        return absolutePathToImage;
    }





    public static List<String> extractMoviePostersFromResponse(String json) throws JSONException{

        if(pathsToImages.size() != 0){
            pathsToImages.clear();
        }
        JSONObject obj = new JSONObject(json);
        if(obj != null){
            JSONArray results = obj.getJSONArray("results");
            if(results != null){
                for(int i = 0; i < results.length(); i++){
                    String relativePath = results.getJSONObject(i).getString("poster_path");
                    pathsToImages.add(makeAbsolutePathForImages(relativePath));

                }
            }
            else{
                Log.d(LOG_TAG,"Error parsing json");
            }
        }
        else{
            Log.d(LOG_TAG,"Error parsing JSON");
        }

        if(pathsToImages.size() != 0 || pathsToImages != null){
            for (String path:pathsToImages) {
                Log.d(LOG_TAG,"Path: " + path);
            }
            return pathsToImages;
        }
        else{
            return null;
        }
    }
}
