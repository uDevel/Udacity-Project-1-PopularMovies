package com.udevel.popularmovies.adapter;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.listener.AdapterItemClickListener;
import com.udevel.popularmovies.data.local.entity.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by benny on 7/12/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_MOVIE = 0;
    public static final int VIEW_TYPE_FOOTER = 1;
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final Fragment fragment;
    private boolean showFooter = true;
    private List<Movie> movieList;
    private AdapterItemClickListener adapterItemClickListener;

    // Provide a suitable constructor (depends on the kind of data set)
    public MovieAdapter(List<Movie> movieList, Fragment fragment, int movieListType) {
        this.fragment = fragment;
        setHasStableIds(true);
        updateMovies(movieList, movieListType);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MOVIE:
                return MovieViewHolder.inflate(parent, new MovieViewHolder.ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        if (adapterItemClickListener != null) {
                            adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_MOVIE_DETAIL, v, getOriginalItemId(adapterPosition));
                        }
                    }
                });
            case VIEW_TYPE_FOOTER:
                return FooterViewHolder.inflate(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_MOVIE:

                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

                // This is avoid double loading of animation and image.
                if (movieViewHolder.position.getAndSet(position) == position) {
                    return;
                }

                Movie movieInfo = movieList.get(position);
                Uri uri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.THUMBNAIL_IMAGE_WIDTH).appendEncodedPath(movieInfo.getPosterPath()).build();
                Glide.with(fragment)//((MovieViewHolder) holder).iv_poster.getContext())
                        .load(uri)
                        .error(R.drawable.ic_image_error)
                        .fitCenter()
                        .animate(R.anim.fade_in_rise)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(movieViewHolder.iv_poster);
                break;
            case VIEW_TYPE_FOOTER:
                ProgressWheel pw_main = ((FooterViewHolder) holder).pw_main;
                pw_main.spin();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position == movieList.size()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_MOVIE;
        }
    }

    @Override
    public long getItemId(int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_MOVIE:
                return movieList.get(position).getMovieId();
            case VIEW_TYPE_FOOTER:
                return 1L;
            default:
                return 0L;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (showFooter) {
            return movieList.size() == 0 ? 0 : movieList.size() + 1;
        } else {
            return movieList.size();
        }
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public void updateMovies(List<Movie> movieList, int movieListType) {
        if (movieList == null) {
            this.movieList = new ArrayList<>();
        } else {
            this.movieList = movieList;
        }

        showFooter = movieListType != Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE;
        notifyDataSetChanged();
    }

    private int getOriginalItemId(int position) {
        return movieList.get(position).getMovieId();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_poster;
        private final ViewHolderClickListener viewHolderClickListener;
        private AtomicInteger position = new AtomicInteger(-1);
        // each data item is just a string in this case

        public MovieViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;
            iv_poster = ((ImageView) v.findViewById(R.id.iv_poster));
            v.setOnClickListener(this);
        }

        static MovieViewHolder inflate(ViewGroup parent, ViewHolderClickListener viewHolderClickListener) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false), viewHolderClickListener);
        }

        @Override
        public void onClick(View v) {
            viewHolderClickListener.onClick(v, getAdapterPosition());
        }

        interface ViewHolderClickListener {
            void onClick(View v, int adapterPosition);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        private final ProgressWheel pw_main;

        public FooterViewHolder(View itemView) {
            super(itemView);
            pw_main = ((ProgressWheel) itemView.findViewById(R.id.pw_main));
        }

        static FooterViewHolder inflate(ViewGroup parent) {
            return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_footer, parent, false));
        }
    }
}
