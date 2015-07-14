package com.udevel.popularmovies.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.data.local.entity.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 7/12/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movieList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieAdapter(List<Movie> movieList) {
        setHasStableIds(true);
        addMovies(movieList);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.inflate(parent);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movieInfo = movieList.get(position);
        holder.tv_title.setText(movieInfo.getOriginalTitle());
        Uri uri = Uri.parse(Movie.baseUrlForImage).buildUpon().appendPath("w185").appendEncodedPath(movieInfo.getPosterPath()).build();
        Picasso.with(holder.tv_title.getContext())
                .load(uri)
                .into(holder.iv_poster);
    }

    @Override
    public long getItemId(int position) {
        return movieList.get(position).getId();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void addMovies(List<Movie> movieList) {
        if (this.movieList == null) {
            this.movieList = new ArrayList<>();
        }
        this.movieList.addAll(movieList);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final ImageView iv_poster;
        // each data item is just a string in this case

        public ViewHolder(View v) {
            super(v);
            tv_title = ((TextView) v.findViewById(R.id.tv_title));
            iv_poster = ((ImageView) v.findViewById(R.id.iv_poster));
        }

        static ViewHolder inflate(ViewGroup parent) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false));
        }
    }
}
