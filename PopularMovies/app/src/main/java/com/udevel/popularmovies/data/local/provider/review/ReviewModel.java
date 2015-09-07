package com.udevel.popularmovies.data.local.provider.review;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udevel.popularmovies.data.local.provider.base.BaseModel;

/**
 * Data model for the {@code review} table.
 */
public interface ReviewModel extends BaseModel {

    /**
     * Get the {@code review_id} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getReviewId();

    /**
     * Get the {@code author} value.
     * Can be {@code null}.
     */
    @Nullable
    String getAuthor();

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    String getContent();

    /**
     * Get the {@code movie_id} value.
     */
    long getMovieId();
}
