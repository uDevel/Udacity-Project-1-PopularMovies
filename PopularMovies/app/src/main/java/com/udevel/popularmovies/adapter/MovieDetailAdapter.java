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
    private static final int VIEW_TYPE_MOVIE_INFO = 0;
    private static final int VIEW_TYPE_MOVIE_OVERVIEW = 1;
    private static final int VIEW_TYPE_TRAILER = 2;
    private static final int VIEW_TYPE_REVIEW = 3;
    private static final int VIEW_TYPE_SEPARATOR = 4;

    private static final String TAG = MovieDetailAdapter.class.getSimpleName();
    private final Fragment fragment;
    private final boolean isShowingMovieInfo;
    private Movie movie;
    private List<YouTubeTrailer> youTubeTrailerList;
    private List<Review> reviewList;
    private AdapterItemClickListener adapterItemClickListener;
    private boolean hasTrailers;
    private boolean hasReviews;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieDetailAdapter(Movie movie, List<YouTubeTrailer> youTubeTrailers, List<Review> reviews, Fragment fragment, boolean isShowingMovieInfo) {
        this.fragment = fragment;
        updateMovieDetail(movie, youTubeTrailers, reviews);
        this.isShowingMovieInfo = isShowingMovieInfo;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MOVIE_INFO:
                return MovieInfoViewHolder.inflate(parent,new ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        if (movie.getPosterPath() != null) {
                            if (adapterItemClickListener != null) {
                                adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_POSTER_FULLSCREEN, v, movie.getPosterPath());
                            }
                        }
                    }
                });
            case VIEW_TYPE_MOVIE_OVERVIEW:
                return MovieOverviewViewHolder.inflate(parent);
            case VIEW_TYPE_TRAILER:
                return TrailerViewHolder.inflate(parent, new ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPosition(adapterPosition);
                        if (youTubeTrailer != null) {
                            if (adapterItemClickListener != null) {
                                adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_YOUTUBE_TRAILER, v, youTubeTrailer.getYouTubeTrailerId());
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
            case VIEW_TYPE_SEPARATOR:
                return SeparatorViewHolder.inflate(parent);
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
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
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
                trailerViewHolder.iv_hd.setVisibility(youTubeTrailer.isHD() ? View.VISIBLE : View.GONE);
                break;
            case VIEW_TYPE_REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                Review review = getReviewFromAdapterPosition(position);
                reviewViewHolder.tv_review.setText(review.getContent());
                reviewViewHolder.tv_reviewer.setText(fragment.getString(R.string.reviewed_by, review.getAuthor()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowingMovieInfo) {
            position--;
        }

        if (position == -1) {
            return VIEW_TYPE_MOVIE_INFO;
        } else if (position == 0) {
            return VIEW_TYPE_MOVIE_OVERVIEW;
        } else if (hasTrailers && position == 1) {
            return VIEW_TYPE_SEPARATOR;
        } else if (hasTrailers && position < youTubeTrailerList.size() + 2) {    // 1 for overview, 1 for separator
            return VIEW_TYPE_TRAILER;
        } else if (hasReviews && position == (hasTrailers ? youTubeTrailerList.size() + 2 : 1)) {
            return VIEW_TYPE_SEPARATOR;
        } else if (hasReviews) {
            return VIEW_TYPE_REVIEW;
        } else {
            return VIEW_TYPE_MOVIE_OVERVIEW;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (isShowingMovieInfo ? 1 : 0) + // For movie info
                1 + //For overview
                (hasTrailers ? youTubeTrailerList.size() + 1 : 0) +  // 1 for separator
                (hasReviews ? reviewList.size() + 1 : 0); // 1 for separator
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public void updateMovieDetail(Movie movie, List<YouTubeTrailer> youTubeTrailers, List<Review> reviews) {
        this.movie = movie;
        this.youTubeTrailerList = youTubeTrailers;
        this.reviewList = reviews;

        hasTrailers = youTubeTrailerList != null && youTubeTrailerList.size() > 0;
        hasReviews = reviewList != null && reviewList.size() > 0;
        notifyDataSetChanged();
    }

    private YouTubeTrailer getYouTubeTrailerFromAdapterPosition(int position) {
        return youTubeTrailerList == null ? null : youTubeTrailerList.get(position - 2 - (isShowingMovieInfo ? 1 : 0));
    }

    private Review getReviewFromAdapterPosition(int position) {
        return reviewList == null ? null : reviewList.get(position - 2 - (youTubeTrailerList == null ? 0 : youTubeTrailerList.size()) - 1 - (isShowingMovieInfo ? 1 : 0));
    }

    interface ViewHolderClickListener {
        void onClick(View v, int adapterPosition);
    }

    public static class MovieInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_title;
        private final ImageView iv_poster;
        private final TextView tv_release_popularity_rating;
        private final ViewHolderClickListener viewHolderClickListener;

        public MovieInfoViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;

            iv_poster = ((ImageView) v.findViewById(R.id.iv_poster));
            tv_release_popularity_rating = ((TextView) v.findViewById(R.id.tv_release_popularity_rating));
            tv_title = ((TextView) v.findViewById(R.id.tv_title));
            iv_poster.setOnClickListener(this);
        }

        static MovieInfoViewHolder inflate(ViewGroup parent, ViewHolderClickListener viewHolderClickListener) {
            return new MovieInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_detail_info, parent, false),
                    viewHolderClickListener);
        }

        @Override
        public void onClick(View v) {
            viewHolderClickListener.onClick(v, getAdapterPosition());
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
        private final ImageView iv_hd;

        public TrailerViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;
            tv_trailer_name = ((TextView) v.findViewById(R.id.tv_trailer_name));
            iv_trailer_thumbnail = ((ImageView) v.findViewById(R.id.iv_trailer_thumbnail));
            iv_hd = ((ImageView) v.findViewById(R.id.iv_hd));
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

    public static class SeparatorViewHolder extends RecyclerView.ViewHolder {

        public SeparatorViewHolder(View itemView) {
            super(itemView);
        }

        static SeparatorViewHolder inflate(ViewGroup parent) {
            return new SeparatorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_separator, parent, false));
        }
    }
}
