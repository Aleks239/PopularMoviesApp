package com.example.aleksandrromanov.popularmoviesapp;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = MainActivity.class.getName();
    private static String API_KEY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = getString(R.string.MOVIE_DB_API_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.refresh_menu_item:
                URL url = NetworkUtility.buildMovieURL("popular",API_KEY);
                if(url != null){
                    new FetchPopularMoviesTask().execute(url);
                }
                else Log.d(LOG_TAG,"Error has occurred");
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    class FetchPopularMoviesTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL movieURL = urls[0];
            String movieJSON = NetworkUtility.getMovieJSON(movieURL);
            return movieJSON;

        }

        @Override
        protected void onPostExecute(String movieJsonString) {
            if(movieJsonString != null){
                Log.d(LOG_TAG,movieJsonString);
            }

            else{
                Log.d(LOG_TAG,"Failed to fetch JSON");
            }


        }
    }
}
