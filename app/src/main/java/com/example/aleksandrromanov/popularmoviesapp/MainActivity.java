package com.example.aleksandrromanov.popularmoviesapp;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);






        mRecycleView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this,mDataSource,this);
        mRecycleView.setAdapter(mMovieAdapter);

        loadMoviePoster("popular");


    }

    private void loadMoviePoster(String type){
        URL url = NetworkUtility.buildMovieURL(type,sApiKey);
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
            case R.id.sort_by_top_rated:
                loadMoviePoster("top_rated");
                return true;
            case R.id.sort_by_popular:
                loadMoviePoster("popular");
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    @Override
    public void onClick(String movie) {

        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent movieDetailIntent = new Intent(context,destinationClass);
        movieDetailIntent.putExtra(Intent.EXTRA_TEXT, movie);
        startActivity(movieDetailIntent);

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
