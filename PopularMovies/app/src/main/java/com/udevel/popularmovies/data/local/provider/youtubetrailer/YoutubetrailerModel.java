package com.udevel.popularmovies.data.local.provider.youtubetrailer;

import com.udevel.popularmovies.data.local.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Data model for the {@code youtubetrailer} table.
 */
public interface YoutubetrailerModel extends BaseModel {

    /**
     * Get the {@code you_tube_trailer_id} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getYouTubeTrailerId();

    /**
     * Get the {@code name} value.
     * Can be {@code null}.
     */
    @Nullable
    String getName();

    /**
     * Get the {@code size} value.
     * Can be {@code null}.
     */
    @Nullable
    String getSize();

    /**
     * Get the {@code movie_id} value.
     */
    long getMovieId();
}
