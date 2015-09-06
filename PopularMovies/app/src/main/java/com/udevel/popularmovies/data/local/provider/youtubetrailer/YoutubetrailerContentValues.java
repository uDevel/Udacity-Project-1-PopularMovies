package com.udevel.popularmovies.data.local.provider.youtubetrailer;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udevel.popularmovies.data.local.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code youtubetrailer} table.
 */
public class YoutubetrailerContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return YoutubetrailerColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable YoutubetrailerSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable YoutubetrailerSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public YoutubetrailerContentValues putYouTubeTrailerId(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("youTubeTrailerId must not be null");
        mContentValues.put(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }


    public YoutubetrailerContentValues putName(@Nullable String value) {
        mContentValues.put(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerContentValues putNameNull() {
        mContentValues.putNull(YoutubetrailerColumns.NAME);
        return this;
    }

    public YoutubetrailerContentValues putSize(@Nullable String value) {
        mContentValues.put(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerContentValues putSizeNull() {
        mContentValues.putNull(YoutubetrailerColumns.SIZE);
        return this;
    }

    public YoutubetrailerContentValues putMovieId(long value) {
        mContentValues.put(YoutubetrailerColumns.MOVIE_ID, value);
        return this;
    }

}
