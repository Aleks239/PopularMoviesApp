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

 class NetworkUtility {

    private NetworkUtility(){

    }

    private static final String LOG_TAG = NetworkUtility.class.getName();
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String PROTOCOL = "https";
    private static final String LANGUAGE = "en-US";
    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185";
    private static List<Movie> mMovieList = new ArrayList<>();




    /**
     * Generates a random page within a range of 984 as given
     * in the API docs.
     * http://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
     * @return String representing a page number in the API
     */

    private static int generateRandomPage(int num){
        Random random = new Random();
        int pageNumber = random.nextInt(num) + 1;
        return pageNumber;

    }

    /**
     * Builds the URI representing Movie DB endpoint and
     * generates the URL object.
     * @param path
     * @param API_KEY
     * @return Movie DB API URL
     */
    public static URL buildMovieURL(String path, String API_KEY){
        int page = 0;

        switch (path){
            case "top_rated":
                page = generateRandomPage(235);
                break;
            case "popular":
                page = generateRandomPage(979);
                break;
            default:
                break;

        }

        URL movieURL = null;
        Uri.Builder builder = new Uri.Builder();
        Uri movieUri = builder.scheme(PROTOCOL)
                .authority(AUTHORITY)
                .appendPath("3")
                .appendPath("movie").appendPath(path)
                .appendQueryParameter("api_key",API_KEY).appendQueryParameter("language",LANGUAGE)
                .appendQueryParameter("page",String.valueOf(page))
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




    //TODO change the method to construct Movie object as data sources
    public static List<Movie> extractMoviesFromResponse(String json) throws JSONException{

        if(mMovieList.size() != 0){
            mMovieList.clear();
        }
        JSONObject obj = new JSONObject(json);
        if(obj != null){
            JSONArray results = obj.getJSONArray("results");
            if(results != null){
                for(int i = 0; i < results.length(); i++){
                    Log.d(LOG_TAG, results.getJSONObject(i).toString());
                    String title = results.getJSONObject(i).getString("original_title");
                    String moviePoster = makeAbsolutePathForImages(results.getJSONObject(i).getString("poster_path"));
                    String synopsis = results.getJSONObject(i).getString("overview");
                    String voteAverage = results.getJSONObject(i).getString("vote_average");
                    String releaseDate = results.getJSONObject(i).getString("release_date");
                    Movie.MovieBuilder builder = Movie.newBuilder();
                    Movie movie = builder.withTitle(title)
                            .addPoster(moviePoster)
                            .addSynopsis(synopsis).addRating(voteAverage)
                            .withDate(releaseDate).build();
                    mMovieList.add(movie);


                }
            }
            else{
                Log.d(LOG_TAG,"Error parsing json");
            }
        }
        else{
            Log.d(LOG_TAG,"Error parsing JSON");
        }

        if(mMovieList.size() != 0 || mMovieList != null){
            for (Movie movie:mMovieList) {
                Log.d(LOG_TAG,"Movie: " + movie.getRating());
            }
            return mMovieList;
        }
        else{
            return null;
        }
    }
}
