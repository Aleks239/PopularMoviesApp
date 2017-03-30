package com.example.aleksandrromanov.popularmoviesapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener,LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static String sApiKey;
    private RecyclerView mRecycleView;
    private List<Movie> mDataSource;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private static int MOVIE_POSTER_LOADER_ID = 0;










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

        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(MOVIE_POSTER_LOADER_ID,bundleForLoader,this);

        //loadMoviePoster("popular");


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
                //loadMoviePoster("top_rated");
                mMovieAdapter.setMoviePosterPaths(null);
                getSupportLoaderManager().restartLoader(MOVIE_POSTER_LOADER_ID,null,this);
                return true;
            case R.id.sort_by_popular:
                //loadMoviePoster("popular");
                mMovieAdapter.setMoviePosterPaths(null);
                getSupportLoaderManager().restartLoader(MOVIE_POSTER_LOADER_ID,null,this);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    @Override
    public void onClick(Movie movie) {

        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent movieDetailIntent = new Intent(context,destinationClass);
        movieDetailIntent.putExtra("com.example.aleksandrromanov.popularmoviesapp.Movie", movie);
        startActivity(movieDetailIntent);


    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> movies = null;


            @Override
            public List<Movie> loadInBackground() {
                List<Movie> movies = null;
                URL url = NetworkUtility.buildMovieURL("popular",sApiKey);
                if(url != null){
                    try{
                        String movieJSON = NetworkUtility.getMovieJson(url);
                        if(movieJSON != null){
                            movies = NetworkUtility.extractMoviesFromResponse(movieJSON);
                        }
                        else {
                            return null;
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
                return movies;
            }

            @Override
            protected void onStartLoading() {
                if(movies!=null){
                    deliverResult(movies);
                }else{
                    showLoadingIndicator();
                    forceLoad();
                }

            }

            public void deliverResult(List<Movie> data){
                movies = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mMovieAdapter.setMoviePosterPaths(data);
        if(data == null){
            showErrorMessage();
        }
        else{
            showMoviePosters();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }


    /**
     * Async Task subclass which fetches JSON from MOVIE DB API
     * on the background thread
     */
    class FetchPopularMoviesTask extends AsyncTask<URL,Void,List<Movie>>{
        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL movieURL = urls[0];
            try {
                String movieJSON = NetworkUtility.getMovieJson(movieURL);
                if(movieJSON != null){
                    //TODO Change the method to get the Movie objects instead of the posters
                    mDataSource = NetworkUtility.extractMoviesFromResponse(movieJSON);
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
        protected void onPostExecute(List<Movie> posters) {
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
