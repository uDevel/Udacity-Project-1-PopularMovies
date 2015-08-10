package com.udevel.popularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.listener.AdapterItemClickListener;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.local.entity.YouTubeTrailer;

import java.util.List;

/**
 * Created by benny on 7/12/2015.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_MOVIE_OVERVIEW = 0;
    public static final int VIEW_TYPE_TRAILER = 1;
    public static final int VIEW_TYPE_FOOTER = 2;
    private static final String TAG = MovieDetailAdapter.class.getSimpleName();
    private final Fragment fragment;
    private boolean showFooter = true;
    private Movie movie;
    private List<YouTubeTrailer> youTubeTrailerList;
    private AdapterItemClickListener adapterItemClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieDetailAdapter(Movie movie, List<YouTubeTrailer> youTubeTrailerList, Fragment fragment) {
        this.fragment = fragment;
        // setHasStableIds(true);
        updateMovieDetail(movie, youTubeTrailerList);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MOVIE_OVERVIEW:
                return MovieOverviewViewHolder.inflate(parent);
            case VIEW_TYPE_TRAILER:
                return TrailerViewHolder.inflate(parent, new ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        if (youTubeTrailerList != null) {
                            YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPostion(adapterPosition);
                            if (youTubeTrailer != null) {
                                if (adapterItemClickListener != null) {
                                    adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_YOUTUBE_TRAILER, v, youTubeTrailer.getUrl());
                                }
                            }
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
            case VIEW_TYPE_MOVIE_OVERVIEW:
                MovieOverviewViewHolder movieOverviewViewHolder = (MovieOverviewViewHolder) holder;
                movieOverviewViewHolder.tv_overview.setText(movie.getOverview());
                break;
            case VIEW_TYPE_TRAILER:
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
                YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPostion(position);
                trailerViewHolder.tv_trailer_name.setText(youTubeTrailer.getName());
                String thumbnailUrl = youTubeTrailer.getThumbnailUrl();
                Glide.with(fragment)
                        .load(thumbnailUrl)
                        .fitCenter()
                        .error(R.drawable.ic_image_error)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(trailerViewHolder.iv_trailer_thumbnail);
                break;
            case VIEW_TYPE_FOOTER:
                ProgressWheel pw_main = ((FooterViewHolder) holder).pw_main;
                pw_main.spin();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_MOVIE_OVERVIEW;
        } else {
            return VIEW_TYPE_TRAILER;
        }
       /* if (showFooter && position == movieList.size()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_MOVIE;
        }*/
    }

    @Override
    public long getItemId(int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_MOVIE_OVERVIEW:
                return 0L;
            case VIEW_TYPE_TRAILER:
                YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPostion(position);
                return youTubeTrailer.getKey().hashCode();
            case VIEW_TYPE_FOOTER:
                return 2L;
            default:
                return 0L;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1 + (youTubeTrailerList != null ? youTubeTrailerList.size() : 0);
      /*  if (showFooter) {
            return movieList.size() == 0 ? 0 : movieList.size() + 1;
        } else {
            return movieList.size();
        }*/
    }

    private YouTubeTrailer getYouTubeTrailerFromAdapterPostion(int position) {
        return youTubeTrailerList == null ? null : youTubeTrailerList.get(position - 1);
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public void updateMovieDetail(Movie movie, List<YouTubeTrailer> youTubeTrailerList) {
        this.movie = movie;
        this.youTubeTrailerList = youTubeTrailerList;

        notifyDataSetChanged();
    }

    interface ViewHolderClickListener {
        void onClick(View v, int adapterPosition);
    }

    public static class MovieOverviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_overview;

        public MovieOverviewViewHolder(View v) {
            super(v);
            tv_overview = ((TextView) v.findViewById(R.id.tv_overview));
        }

        static MovieOverviewViewHolder inflate(ViewGroup parent) {
            return new MovieOverviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_detail_overview, parent, false));
        }
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_trailer_name;
        private final ImageView iv_trailer_thumbnail;
        private final ViewHolderClickListener viewHolderClickListener;

        public TrailerViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;
            tv_trailer_name = ((TextView) v.findViewById(R.id.tv_trailer_name));
            iv_trailer_thumbnail = ((ImageView) v.findViewById(R.id.iv_trailer_thumbnail));
            v.setOnClickListener(this);
        }

        static TrailerViewHolder inflate(ViewGroup parent, ViewHolderClickListener viewHolderClickListener) {
            return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_detail_trailer, parent, false),
                    viewHolderClickListener);
        }

        @Override
        public void onClick(View v) {
            viewHolderClickListener.onClick(v, getAdapterPosition());
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
