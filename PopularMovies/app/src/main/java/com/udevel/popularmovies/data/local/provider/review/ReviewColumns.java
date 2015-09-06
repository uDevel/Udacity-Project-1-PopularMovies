package com.udevel.popularmovies.data.local.provider.review;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udevel.popularmovies.data.local.provider.MovieContentProvider;
import com.udevel.popularmovies.data.local.provider.movie.MovieColumns;
import com.udevel.popularmovies.data.local.provider.review.ReviewColumns;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerColumns;

/**
 * Columns for the {@code review} table.
 */
public class ReviewColumns implements BaseColumns {
    public static final String TABLE_NAME = "review";
    public static final Uri CONTENT_URI = Uri.parse(MovieContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String REVIEW_ID = "review_id";

    public static final String AUTHOR = "author";

    public static final String CONTENT = "content";

    public static final String MOVIE_ID = "review__movie_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            REVIEW_ID,
            AUTHOR,
            CONTENT,
            MOVIE_ID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(REVIEW_ID) || c.contains("." + REVIEW_ID)) return true;
            if (c.equals(AUTHOR) || c.contains("." + AUTHOR)) return true;
            if (c.equals(CONTENT) || c.contains("." + CONTENT)) return true;
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
        }
        return false;
    }

    public static final String PREFIX_MOVIE = TABLE_NAME + "__" + MovieColumns.TABLE_NAME;
}
