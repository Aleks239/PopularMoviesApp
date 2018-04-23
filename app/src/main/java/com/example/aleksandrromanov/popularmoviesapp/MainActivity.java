package com.example.aleksandrromanov.popularmoviesapp;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;
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
    private EndlessRecyclerViewScrollListener mScrollListener;
    private static List<Integer> mFavouriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sApiKey = getString(R.string.MOVIE_DB_API_KEY);





        mErrorTextView = (TextView) findViewById(R.id.tv_error_occurred);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecycleView = (RecyclerView)findViewById(R.id.rv_posters_view);

        mRecycleView.setHasFixedSize(true);

        mDataSource = new ArrayList<>();
        mFavouriteMovies = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;

        mRecycleView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this,mDataSource,this);
        mRecycleView.setAdapter(mMovieAdapter);

        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(MOVIE_POSTER_LOADER_ID,bundleForLoader,MainActivity.this);


        mScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getSupportLoaderManager().restartLoader(MOVIE_POSTER_LOADER_ID,null,MainActivity.this);
            }
        };

        mRecycleView.addOnScrollListener(mScrollListener);

try{
    List<MovieEntity> mList = MovieEntity.findWithQuery(MovieEntity.class, "Select * from MOVIE_ENTITY where identity = ?" , "14125");

    for (MovieEntity e:mList
            ) {

        Log.d("HERE_IT_COMES", "Hello " + e.identity);

    }

}catch (Exception e){
    Log.d("HERE_IT_COMES", "ERROR!!!");
    e.printStackTrace();
}


    }


    public static boolean isFavouriteMovie(Integer movieId){
        return mFavouriteMovies.contains(movieId);
    }

    public static void toggleFavouriteMovie(Integer movieId){
        if(mFavouriteMovies.contains(movieId)){
            mFavouriteMovies.remove(movieId);
        }
        else{
            mFavouriteMovies.add(movieId);
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


    private void cleanDataOnNewSearch(){
        mMovieAdapter.setMoviePosterPaths(null);
        mDataSource.clear();
        mScrollListener.resetState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle loaderBundle = new Bundle();
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.sort_by_top_rated:
                cleanDataOnNewSearch();
                loaderBundle.putString(getString(R.string.search_criteria_key), getString(R.string.criteria_top_rated));
                getSupportLoaderManager().restartLoader(MOVIE_POSTER_LOADER_ID,loaderBundle,this);
                return true;
            case R.id.sort_by_popular:
                cleanDataOnNewSearch();
                loaderBundle.putString(getString(R.string.search_criteria_key),getString(R.string.criteria_popular));
                getSupportLoaderManager().restartLoader(MOVIE_POSTER_LOADER_ID,loaderBundle,this);
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

    private static class MovieListLoader extends AsyncTaskLoader<List<Movie>>{

        private class ContextWrapper{
            Context context;

            ContextWrapper(Context c){
                this.context = c;
            }

            private Context getContext(){
                return context;
            }
        }

        private Bundle args;
        List<Movie> movies = null;
        private ContextWrapper wrapper;



        MovieListLoader(Context context, Bundle args){
            super(context);
            this.args = args;
            this.wrapper = new ContextWrapper(context);

        }

        @Nullable
        @Override
        public List<Movie> loadInBackground() {
            String searchCriteria = null;
            if(args != null){
                searchCriteria = args.getString(this.getContext().getString(R.string.search_criteria_key));
            }
            URL url;

            if(searchCriteria != null){
                url = NetworkUtility.buildMovieURL(searchCriteria,sApiKey);

            }
            else{
                url = NetworkUtility.buildMovieURL("popular",sApiKey);
            }
            if(url != null){
                Log.d(LOG_TAG,url.toString());
                try{
                    String movieJSON = NetworkUtility.getMovieJson(url);
                    if(movieJSON != null){
                        movies = NetworkUtility.extractMoviesFromResponse(movieJSON, sApiKey);
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
            MainActivity ma = (MainActivity) this.wrapper.getContext();
            if(movies!=null){
                deliverResult(movies);
            }else{
                if(ma.mDataSource.size() < 1){
                    ma.showLoadingIndicator();
                }
                forceLoad();
            }
        }

        @Override
        public void deliverResult(@Nullable List<Movie> data) {
            movies = data;
            super.deliverResult(data);
        }
    }



    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {

        return new MovieListLoader(MainActivity.this, args);


    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(data == null){
            showErrorMessage();
        }
        else{
            if(mDataSource!=null)
            Log.d(LOG_TAG,"Amount of items in the dataSource is: " + mDataSource.size());

            else
                Log.d(LOG_TAG,"First time is null");

            mDataSource.addAll(data);
            mMovieAdapter.setMoviePosterPaths(mDataSource);
            showMoviePosters();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }



}
