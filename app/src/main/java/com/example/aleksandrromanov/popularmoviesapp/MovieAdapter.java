package com.example.aleksandrromanov.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aleksandrromanov on 03/02/2017.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviePosterViewHolder> {

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder{


        public MoviePosterViewHolder(View itemView) {
            super(itemView);
        }
    }


}
