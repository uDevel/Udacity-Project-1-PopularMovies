package com.udevel.popularmovies.data.local.provider.movie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udevel.popularmovies.data.local.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movie} table.
 */
public class MovieContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MovieSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MovieSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MovieContentValues putMovieId(int value) {
        mContentValues.put(MovieColumns.MOVIE_ID, value);
        return this;
    }


    public MovieContentValues putOriginalTitle(@Nullable String value) {
        mContentValues.put(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public MovieContentValues putOriginalTitleNull() {
        mContentValues.putNull(MovieColumns.ORIGINAL_TITLE);
        return this;
    }

    public MovieContentValues putVoteAverage(@Nullable Double value) {
        mContentValues.put(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public MovieContentValues putVoteAverageNull() {
        mContentValues.putNull(MovieColumns.VOTE_AVERAGE);
        return this;
    }

    public MovieContentValues putPosterPath(@Nullable String value) {
        mContentValues.put(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public MovieContentValues putPosterPathNull() {
        mContentValues.putNull(MovieColumns.POSTER_PATH);
        return this;
    }

    public MovieContentValues putOverview(@Nullable String value) {
        mContentValues.put(MovieColumns.OVERVIEW, value);
        return this;
    }

    public MovieContentValues putOverviewNull() {
        mContentValues.putNull(MovieColumns.OVERVIEW);
        return this;
    }

    public MovieContentValues putReleaseDate(@Nullable String value) {
        mContentValues.put(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public MovieContentValues putReleaseDateNull() {
        mContentValues.putNull(MovieColumns.RELEASE_DATE);
        return this;
    }

    public MovieContentValues putPopularity(@Nullable Double value) {
        mContentValues.put(MovieColumns.POPULARITY, value);
        return this;
    }

    public MovieContentValues putPopularityNull() {
        mContentValues.putNull(MovieColumns.POPULARITY);
        return this;
    }

    public MovieContentValues putVoteCount(@Nullable Integer value) {
        mContentValues.put(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public MovieContentValues putVoteCountNull() {
        mContentValues.putNull(MovieColumns.VOTE_COUNT);
        return this;
    }
}
