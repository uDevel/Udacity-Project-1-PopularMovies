package com.udevel.popularmovies.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.listener.OnMovieAdapterItemClickListener;
import com.udevel.popularmovies.data.local.entity.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 7/12/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movieList;
    private OnMovieAdapterItemClickListener onMovieAdapterItemClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieAdapter(List<Movie> movieList) {
        setHasStableIds(true);
        updateMovies(movieList);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.inflate(parent, new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onClick(View v, int adapterPosition) {
                if (onMovieAdapterItemClickListener != null) {
                    onMovieAdapterItemClickListener.onMovieAdapterItemClick(v, getOriginalItemId(adapterPosition));
                }
            }
        });
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movieInfo = movieList.get(position);
        Uri uri = Uri.parse(Movie.baseUrlForImage).buildUpon().appendPath("w185").appendEncodedPath(movieInfo.getPosterPath()).build();
        Picasso.with(holder.iv_poster.getContext())
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

    public void setOnMovieAdapterItemClickListener(OnMovieAdapterItemClickListener onMovieAdapterItemClickListener) {
        this.onMovieAdapterItemClickListener = onMovieAdapterItemClickListener;
    }

    public void removeOnMovieAdapterItemClickListener() {
        this.onMovieAdapterItemClickListener = null;
    }

    private int getOriginalItemId(int position) {
        return movieList.get(position).getId();
    }

    public void updateMovies(List<Movie> movieList) {
        if (movieList == null) {
            this.movieList = new ArrayList<>();
        } else {
            this.movieList = movieList;
        }

        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_poster;
        private final ViewHolderClickListener viewHolderClickListener;
        // each data item is just a string in this case

        public ViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;
            iv_poster = ((ImageView) v.findViewById(R.id.iv_poster));
            iv_poster.setOnClickListener(this);
        }

        static ViewHolder inflate(ViewGroup parent, ViewHolderClickListener viewHolderClickListener) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false), viewHolderClickListener);
        }

        @Override
        public void onClick(View v) {
            viewHolderClickListener.onClick(v, getAdapterPosition());
        }

        interface ViewHolderClickListener {
            void onClick(View v, int adapterPosition);
        }
    }
}
