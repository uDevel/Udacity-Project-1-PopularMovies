package com.udevel.popularmovies.data.local.provider.movie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udevel.popularmovies.data.local.provider.MovieContentProvider;
import com.udevel.popularmovies.data.local.provider.movie.MovieColumns;
import com.udevel.popularmovies.data.local.provider.review.ReviewColumns;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerColumns;

/**
 * Columns for the {@code movie} table.
 */
public class MovieColumns implements BaseColumns {
    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = Uri.parse(MovieContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String MOVIE_ID = "movie__movie_id";

    public static final String ORIGINAL_TITLE = "original_title";

    public static final String VOTE_AVERAGE = "vote_average";

    public static final String POSTER_PATH = "poster_path";

    public static final String OVERVIEW = "overview";

    public static final String RELEASE_DATE = "release_date";

    public static final String POPULARITY = "popularity";

    public static final String VOTE_COUNT = "vote_count";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MOVIE_ID,
            ORIGINAL_TITLE,
            VOTE_AVERAGE,
            POSTER_PATH,
            OVERVIEW,
            RELEASE_DATE,
            POPULARITY,
            VOTE_COUNT
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(ORIGINAL_TITLE) || c.contains("." + ORIGINAL_TITLE)) return true;
            if (c.equals(VOTE_AVERAGE) || c.contains("." + VOTE_AVERAGE)) return true;
            if (c.equals(POSTER_PATH) || c.contains("." + POSTER_PATH)) return true;
            if (c.equals(OVERVIEW) || c.contains("." + OVERVIEW)) return true;
            if (c.equals(RELEASE_DATE) || c.contains("." + RELEASE_DATE)) return true;
            if (c.equals(POPULARITY) || c.contains("." + POPULARITY)) return true;
            if (c.equals(VOTE_COUNT) || c.contains("." + VOTE_COUNT)) return true;
        }
        return false;
    }

}
