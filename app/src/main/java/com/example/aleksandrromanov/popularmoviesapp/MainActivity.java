package com.example.aleksandrromanov.popularmoviesapp;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static String sApiKey;
    private RecyclerView mRecycleView;
    private List<String> mDataSource;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sApiKey = getString(R.string.MOVIE_DB_API_KEY);

        mErrorTextView = (TextView) findViewById(R.id.tv_error_occurred);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecycleView = (RecyclerView)findViewById(R.id.rv_posters_view);

        mRecycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecycleView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this,mDataSource);
        mRecycleView.setAdapter(mMovieAdapter);

        loadMoviePoster();


    }

    private void loadMoviePoster(){
        URL url = NetworkUtility.buildMovieURL("popular",sApiKey);
        if(url != null){
            new FetchPopularMoviesTask().execute(url);
        }
        else{
            showErrorMessage();
        }
    }

    private void showMoviePosters(){
        mRecycleView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
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
                loadMoviePoster();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    /**
     * Async Task subclass which fetches JSON from MOVIE DB API
     * on the background thread
     */
    class FetchPopularMoviesTask extends AsyncTask<URL,Void,List<String>>{
        @Override
        protected List<String> doInBackground(URL... urls) {
            URL movieURL = urls[0];
            try {
                String movieJSON = NetworkUtility.getMovieJson(movieURL);
                if(movieJSON != null){
                    mDataSource = NetworkUtility.extractMoviePostersFromResponse(movieJSON);
                }
                else{
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mDataSource;

        }

        @Override
        protected void onPreExecute() {
            showLoadingIndicator();
        }

        @Override
        protected void onPostExecute(List<String> posters) {
            if(posters != null){
                mMovieAdapter.setMoviePosterPaths(posters);
                showMoviePosters();

            }
            else{
                showErrorMessage();
                Log.d(LOG_TAG,"Failed to fetch JSON");
            }


        }
    }
}
