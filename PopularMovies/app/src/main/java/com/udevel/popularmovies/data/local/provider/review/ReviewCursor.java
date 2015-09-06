package com.udevel.popularmovies.data.local.provider.review;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udevel.popularmovies.data.local.provider.base.AbstractCursor;
import com.udevel.popularmovies.data.local.provider.movie.*;

/**
 * Cursor wrapper for the {@code review} table.
 */
public class ReviewCursor extends AbstractCursor implements ReviewModel {
    public ReviewCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ReviewColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code review_id} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getReviewId() {
        String res = getStringOrNull(ReviewColumns.REVIEW_ID);
        if (res == null)
            throw new NullPointerException("The value of 'review_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code author} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getAuthor() {
        String res = getStringOrNull(ReviewColumns.AUTHOR);
        return res;
    }

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getContent() {
        String res = getStringOrNull(ReviewColumns.CONTENT);
        return res;
    }

    /**
     * Get the {@code movie_id} value.
     */
    public long getMovieId() {
        Long res = getLongOrNull(ReviewColumns.MOVIE_ID);
        if (res == null)
            throw new NullPointerException("The value of 'movie_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code movie_id} value.
     */
    public int getMovieMovieId() {
        Integer res = getIntegerOrNull(MovieColumns.MOVIE_ID);
        if (res == null)
            throw new NullPointerException("The value of 'movie_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code original_title} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieOriginalTitle() {
        String res = getStringOrNull(MovieColumns.ORIGINAL_TITLE);
        return res;
    }

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getMovieVoteAverage() {
        Double res = getDoubleOrNull(MovieColumns.VOTE_AVERAGE);
        return res;
    }

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getMoviePosterPath() {
        String res = getStringOrNull(MovieColumns.POSTER_PATH);
        return res;
    }

    /**
     * Get the {@code overview} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieOverview() {
        String res = getStringOrNull(MovieColumns.OVERVIEW);
        return res;
    }

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieReleaseDate() {
        String res = getStringOrNull(MovieColumns.RELEASE_DATE);
        return res;
    }

    /**
     * Get the {@code popularity} value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getMoviePopularity() {
        Double res = getDoubleOrNull(MovieColumns.POPULARITY);
        return res;
    }

    /**
     * Get the {@code vote_count} value.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getMovieVoteCount() {
        Integer res = getIntegerOrNull(MovieColumns.VOTE_COUNT);
        return res;
    }
}
