package com.udevel.popularmovies.adapter;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.udevel.popularmovies.data.local.entity.Review;
import com.udevel.popularmovies.data.local.entity.YouTubeTrailer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by benny on 7/12/2015.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_MOVIE_INFO = 0;
    public static final int VIEW_TYPE_MOVIE_OVERVIEW = 1;
    public static final int VIEW_TYPE_TRAILER = 2;
    public static final int VIEW_TYPE_REVIEW = 3;
    public static final int VIEW_TYPE_FOOTER = 4;
    private static final String TAG = MovieDetailAdapter.class.getSimpleName();
    private final Fragment fragment;
    private final boolean isShowingMovieInfo;
    private boolean showFooter = true;
    private Movie movie;
    private List<YouTubeTrailer> youTubeTrailerList;
    private List<Review> reviewList;
    private AdapterItemClickListener adapterItemClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieDetailAdapter(Movie movie, List<YouTubeTrailer> youTubeTrailerList, List<Review> reviewList, Fragment fragment, boolean isShowingMovieInfo) {
        this.fragment = fragment;
        setHasStableIds(true);
        updateMovieDetail(movie, youTubeTrailerList, reviewList);
        this.isShowingMovieInfo = isShowingMovieInfo;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MOVIE_INFO:
                return MovieInfoViewHolder.inflate(parent);
            case VIEW_TYPE_MOVIE_OVERVIEW:
                return MovieOverviewViewHolder.inflate(parent);
            case VIEW_TYPE_TRAILER:
                return TrailerViewHolder.inflate(parent, new ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPosition(adapterPosition);
                        if (youTubeTrailer != null) {
                            if (adapterItemClickListener != null) {
                                adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_YOUTUBE_TRAILER, v, youTubeTrailer.getUrl());
                            }
                        }
                    }
                });
            case VIEW_TYPE_REVIEW:
                return ReviewViewHolder.inflate(parent, new ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        Review review = getReviewFromAdapterPosition(adapterPosition);
                        if (review != null) {
                            if (adapterItemClickListener != null) {
                                adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_REVIEW_DIALOG, v, review);
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
            case VIEW_TYPE_MOVIE_INFO:
                MovieInfoViewHolder movieInfoViewHolder = (MovieInfoViewHolder) holder;
                movieInfoViewHolder.tv_title.setText(movie.getOriginalTitle());
                movieInfoViewHolder.tv_release_popularity_rating.setText(movie.getOverview());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(format.parse(movie.getReleaseDate()));

                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage());
                }
                movieInfoViewHolder.tv_release_popularity_rating.setText(String.valueOf(cal.get(Calendar.YEAR)) +
                        " " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                        " " + cal.get(Calendar.DATE) +
                        " - " + fragment.getString(R.string.format_popularity, movie.getPopularity()) +
                        " - " + fragment.getString(R.string.format_avg_vote, movie.getVoteAverage(), movie.getVoteCount()));

                Uri uri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.THUMBNAIL_IMAGE_WIDTH).appendEncodedPath(movie.getPosterPath()).build();
                Glide.with(fragment)
                        .load(uri)
                        .error(R.drawable.ic_image_error)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(movieInfoViewHolder.iv_poster);
                break;
            case VIEW_TYPE_MOVIE_OVERVIEW:
                MovieOverviewViewHolder movieOverviewViewHolder = (MovieOverviewViewHolder) holder;
                movieOverviewViewHolder.tv_overview.setText(movie.getOverview());
                break;
            case VIEW_TYPE_TRAILER:
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
                YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPosition(position);
                trailerViewHolder.tv_trailer_name.setText(youTubeTrailer.getName());
                String thumbnailUrl = youTubeTrailer.getThumbnailUrl();
                Glide.with(fragment)
                        .load(thumbnailUrl)
                        .fitCenter()
                        .error(R.drawable.ic_image_error)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(trailerViewHolder.iv_trailer_thumbnail);
                break;
            case VIEW_TYPE_REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                Review review = getReviewFromAdapterPosition(position);
                reviewViewHolder.tv_review.setText(review.getContent());
                reviewViewHolder.tv_reviewer.setText(fragment.getString(R.string.reviewed_by, review.getAuthor()));
                break;
            case VIEW_TYPE_FOOTER:
                ProgressWheel pw_main = ((FooterViewHolder) holder).pw_main;
                pw_main.spin();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isShowingMovieInfo){
            position--;
        }

        if (position == -1) {
            return VIEW_TYPE_MOVIE_INFO;
        } else if (position == 0) {
            return VIEW_TYPE_MOVIE_OVERVIEW;
        } else if (youTubeTrailerList != null && position < youTubeTrailerList.size() + 1) {
            return VIEW_TYPE_TRAILER;
        } else if (reviewList != null) {
            return VIEW_TYPE_REVIEW;
        } else {
            return VIEW_TYPE_MOVIE_OVERVIEW;
        }
    }

    @Override
    public long getItemId(int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_MOVIE_INFO:
                return 0L;
            case VIEW_TYPE_MOVIE_OVERVIEW:
                return 1L;
            case VIEW_TYPE_TRAILER:
                YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPosition(position);
                return youTubeTrailer.getId().hashCode();
            case VIEW_TYPE_REVIEW:
                Review review = getReviewFromAdapterPosition(position);
                return review.getId().hashCode();
            case VIEW_TYPE_FOOTER:
                return 2L;
            default:
                return 0L;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (isShowingMovieInfo ? 1 : 0) + // For movie info
                1 + //For overview
                (youTubeTrailerList != null ? youTubeTrailerList.size() : 0) +
                (reviewList != null ? reviewList.size() : 0);
    }

    private YouTubeTrailer getYouTubeTrailerFromAdapterPosition(int position) {
        return youTubeTrailerList == null ? null : youTubeTrailerList.get(position - 1 - (isShowingMovieInfo ? 1 : 0));
    }

    private Review getReviewFromAdapterPosition(int position) {
        return reviewList == null ? null : reviewList.get(position - 1 - (youTubeTrailerList == null ? 0 : youTubeTrailerList.size()) - (isShowingMovieInfo ? 1 : 0));
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public void updateMovieDetail(Movie movie, List<YouTubeTrailer> youTubeTrailerList, List<Review> reviewList) {
        this.movie = movie;
        this.youTubeTrailerList = youTubeTrailerList;
        this.reviewList = reviewList;

        notifyDataSetChanged();
    }

    interface ViewHolderClickListener {
        void onClick(View v, int adapterPosition);
    }

    public static class MovieInfoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final ImageView iv_poster;
        private final TextView tv_release_popularity_rating;

        public MovieInfoViewHolder(View v) {
            super(v);
            iv_poster = ((ImageView) v.findViewById(R.id.iv_poster));
            tv_release_popularity_rating = ((TextView) v.findViewById(R.id.tv_release_popularity_rating));
            tv_title = ((TextView) v.findViewById(R.id.tv_title));
        }

        static MovieInfoViewHolder inflate(ViewGroup parent) {
            return new MovieInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_detail_info, parent, false));
        }
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

    public static class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_review;
        private final TextView tv_reviewer;
        private final ViewHolderClickListener viewHolderClickListener;

        public ReviewViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;

            tv_review = ((TextView) v.findViewById(R.id.tv_review));
            tv_reviewer = ((TextView) v.findViewById(R.id.tv_reviewer));
            v.setOnClickListener(this);

        }

        static ReviewViewHolder inflate(ViewGroup parent, ViewHolderClickListener viewHolderClickListener) {
            return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_detail_review, parent, false),
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
