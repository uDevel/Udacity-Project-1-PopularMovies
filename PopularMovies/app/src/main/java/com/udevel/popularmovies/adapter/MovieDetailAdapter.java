package com.udevel.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.listener.AdapterItemClickListener;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.local.entity.Review;
import com.udevel.popularmovies.data.local.entity.YouTubeTrailer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by benny on 7/12/2015.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MOVIE_INFO = 0;
    private static final int VIEW_TYPE_TRAILER = 1;
    private static final int VIEW_TYPE_REVIEW = 2;
    private static final int VIEW_TYPE_SEPARATOR = 4;

    private static final String TAG = MovieDetailAdapter.class.getSimpleName();
    private final Fragment fragment;
    private final Context applicationContext;
    private Movie movie;
    private List<YouTubeTrailer> youTubeTrailerList;
    private List<Review> reviewList;
    private AdapterItemClickListener adapterItemClickListener;
    private boolean hasTrailers;
    private boolean hasReviews;
    private boolean animateEntry = true;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieDetailAdapter(Movie movie, List<YouTubeTrailer> youTubeTrailers, List<Review> reviews, Fragment fragment) {
        this.fragment = fragment;
        applicationContext = fragment.getContext().getApplicationContext();
        updateMovieDetail(movie, youTubeTrailers, reviews);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MOVIE_INFO:
                return MovieInfoViewHolder.inflate(parent, new ViewHolderClickListener() {
                    @Override
                    public void onClick(View v, int adapterPosition) {
                        if (movie.getPosterPath() != null) {
                            if (adapterItemClickListener != null) {
                                adapterItemClickListener.adapterItemClick(AdapterItemClickListener.ACTION_OPEN_POSTER_FULLSCREEN, v, movie.getPosterPath());
                            }
                        }
                    }
                });
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(format.parse(movie.getReleaseDate()));
                    movieInfoViewHolder.tv_release_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
                    movieInfoViewHolder.tv_release_month_date.setText(" " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                            " " + cal.get(Calendar.DATE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                movieInfoViewHolder.tv_rating.setText(applicationContext.getString(R.string.format_avg_vote, movie.getVoteAverage(), movie.getVoteCount()));
                movieInfoViewHolder.tv_popularity.setText(applicationContext.getString(R.string.format_popularity, movie.getPopularity()));
                movieInfoViewHolder.tv_overview.setText(movie.getOverview());

                Uri uri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.POSTER_THUMBNAIL_IMAGE_WIDTH).appendEncodedPath(movie.getPosterPath()).build();
                Glide.with(fragment)
                        .load(uri)
                        .error(R.drawable.ic_image_error)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into(movieInfoViewHolder.iv_poster);

                if (animateEntry) {
                    movieInfoViewHolder.fl_poster.setTranslationX(-1000f);
                    movieInfoViewHolder.tv_title.setTranslationX(1000f);
                    movieInfoViewHolder.tv_release_year.setTranslationX(1000f);
                    movieInfoViewHolder.tv_release_month_date.setTranslationX(1000f);
                    movieInfoViewHolder.tv_rating.setTranslationX(1000f);
                    movieInfoViewHolder.tv_popularity.setTranslationX(1000f);
                    movieInfoViewHolder.tv_overview.setScaleX(0.7f);
                    movieInfoViewHolder.tv_overview.setScaleY(0.7f);

                    movieInfoViewHolder.fl_poster.animate().translationX(0f).setDuration(500l).setInterpolator(new DecelerateInterpolator(2)).start();
                    movieInfoViewHolder.tv_title.animate().translationX(0f).setDuration(500l).setInterpolator(new DecelerateInterpolator(2)).start();
                    movieInfoViewHolder.tv_release_year.animate().translationX(0f).setDuration(600l).setInterpolator(new DecelerateInterpolator(2)).start();
                    movieInfoViewHolder.tv_release_month_date.animate().translationX(0f).setDuration(600l).setInterpolator(new DecelerateInterpolator(2)).start();
                    movieInfoViewHolder.tv_rating.animate().translationX(0f).setDuration(600l).setInterpolator(new DecelerateInterpolator(2)).start();
                    movieInfoViewHolder.tv_popularity.animate().translationX(0f).setDuration(600l).setInterpolator(new DecelerateInterpolator(2)).start();
                    movieInfoViewHolder.tv_overview.animate().scaleX(1f).scaleY(1f).setDuration(50l).setInterpolator(new DecelerateInterpolator()).start();

                    animateEntry = false;
                }
                break;
            case VIEW_TYPE_TRAILER:
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
                YouTubeTrailer youTubeTrailer = getYouTubeTrailerFromAdapterPosition(position);
                if (youTubeTrailer == null) {
                    return;
                }
                trailerViewHolder.tv_trailer_name.setText(youTubeTrailer.getName());
                String thumbnailUrl = youTubeTrailer.getThumbnailUrl();
                Glide.with(fragment)
                        .load(thumbnailUrl)
                        .fitCenter()
                        .crossFade(1000)
                        .error(R.drawable.ic_image_error)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(trailerViewHolder.iv_trailer_thumbnail);
                trailerViewHolder.iv_hd.setVisibility(youTubeTrailer.isHD() ? View.VISIBLE : View.GONE);
                break;
            case VIEW_TYPE_REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                Review review = getReviewFromAdapterPosition(position);
                if (review == null) {
                    return;
                }
                reviewViewHolder.tv_review.setText(review.getContent());
                reviewViewHolder.tv_reviewer.setText(fragment.getString(R.string.reviewed_by, review.getAuthor()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_MOVIE_INFO;
        } else if (hasTrailers && position == 1) {
            return VIEW_TYPE_SEPARATOR;
        } else if (hasTrailers && position < youTubeTrailerList.size() + 2) {    // 1 for INFO, 1 for separator
            return VIEW_TYPE_TRAILER;
        } else if (hasReviews && position == (hasTrailers ? youTubeTrailerList.size() + 2 : 1)) {
            return VIEW_TYPE_SEPARATOR;
        } else if (hasReviews) {
            return VIEW_TYPE_REVIEW;
        } else {
            return VIEW_TYPE_SEPARATOR;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1 + //For info
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
        this.animateEntry = true;

        hasTrailers = youTubeTrailerList != null && youTubeTrailerList.size() > 0;
        hasReviews = reviewList != null && reviewList.size() > 0;
        notifyDataSetChanged();
    }

    @Nullable
    private YouTubeTrailer getYouTubeTrailerFromAdapterPosition(int position) {
        if (youTubeTrailerList == null) {
            return null;
        }

        int youTubeTrailerPosition = position - 2;
        if (youTubeTrailerPosition < 0 || youTubeTrailerPosition >= youTubeTrailerList.size()) {
            return null;
        }

        return youTubeTrailerList.get(youTubeTrailerPosition);
    }

    @Nullable
    private Review getReviewFromAdapterPosition(int position) {
        if (reviewList == null) {
            return null;
        }

        int reviewPosition = position - 1 - (youTubeTrailerList == null ? 0 : youTubeTrailerList.size() + 1) - 1;
        if (reviewPosition < 0 || reviewPosition >= reviewList.size()) {
            return null;
        }

        return reviewList.get(reviewPosition);
    }

    interface ViewHolderClickListener {
        void onClick(View v, int adapterPosition);
    }

    public static class MovieInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final FrameLayout fl_poster;
        private final TextView tv_title;
        private final TextView tv_release_year;
        private final TextView tv_release_month_date;
        private final TextView tv_popularity;
        private final TextView tv_rating;
        private final TextView tv_overview;
        private final ImageView iv_poster;
        private final ViewHolderClickListener viewHolderClickListener;

        public MovieInfoViewHolder(View v, ViewHolderClickListener viewHolderClickListener) {
            super(v);
            this.viewHolderClickListener = viewHolderClickListener;
            iv_poster = ((ImageView) v.findViewById(R.id.iv_poster));
            fl_poster = ((FrameLayout) v.findViewById(R.id.fl_poster));
            tv_title = ((TextView) v.findViewById(R.id.tv_title));
            tv_release_year = ((TextView) v.findViewById(R.id.tv_release_year));
            tv_release_month_date = ((TextView) v.findViewById(R.id.tv_release_month_date));
            tv_popularity = ((TextView) v.findViewById(R.id.tv_popularity));
            tv_rating = ((TextView) v.findViewById(R.id.tv_rating));
            tv_overview = ((TextView) v.findViewById(R.id.tv_overview));
            fl_poster.setOnClickListener(this);
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
