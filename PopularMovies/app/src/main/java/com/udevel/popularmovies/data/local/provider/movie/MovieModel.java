package com.udevel.popularmovies.data.local.provider.movie;

import com.udevel.popularmovies.data.local.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Data model for the {@code movie} table.
 */
public interface MovieModel extends BaseModel {

    /**
     * Get the {@code movie_id} value.
     */
    int getMovieId();

    /**
     * Get the {@code original_title} value.
     * Can be {@code null}.
     */
    @Nullable
    String getOriginalTitle();

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    Double getVoteAverage();

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterPath();

    /**
     * Get the {@code overview} value.
     * Can be {@code null}.
     */
    @Nullable
    String getOverview();

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleaseDate();

    /**
     * Get the {@code popularity} value.
     * Can be {@code null}.
     */
    @Nullable
    Double getPopularity();

    /**
     * Get the {@code vote_count} value.
     * Can be {@code null}.
     */
    @Nullable
    Integer getVoteCount();
}
