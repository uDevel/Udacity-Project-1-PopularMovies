package com.udevel.popularmovies.data.local.provider.review;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udevel.popularmovies.data.local.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code review} table.
 */
public class ReviewContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ReviewColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable ReviewSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable ReviewSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ReviewContentValues putReviewId(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("reviewId must not be null");
        mContentValues.put(ReviewColumns.REVIEW_ID, value);
        return this;
    }


    public ReviewContentValues putAuthor(@Nullable String value) {
        mContentValues.put(ReviewColumns.AUTHOR, value);
        return this;
    }

    public ReviewContentValues putAuthorNull() {
        mContentValues.putNull(ReviewColumns.AUTHOR);
        return this;
    }

    public ReviewContentValues putContent(@Nullable String value) {
        mContentValues.put(ReviewColumns.CONTENT, value);
        return this;
    }

    public ReviewContentValues putContentNull() {
        mContentValues.putNull(ReviewColumns.CONTENT);
        return this;
    }

    public ReviewContentValues putMovieId(long value) {
        mContentValues.put(ReviewColumns.MOVIE_ID, value);
        return this;
    }

}
