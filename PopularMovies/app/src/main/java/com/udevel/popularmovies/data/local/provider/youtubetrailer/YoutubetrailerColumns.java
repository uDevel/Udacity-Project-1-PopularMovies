package com.udevel.popularmovies.data.local.provider.youtubetrailer;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udevel.popularmovies.data.local.provider.MovieContentProvider;
import com.udevel.popularmovies.data.local.provider.movie.MovieColumns;

/**
 * Columns for the {@code youtubetrailer} table.
 */
public class YoutubetrailerColumns implements BaseColumns {
    public static final String TABLE_NAME = "youtubetrailer";
    public static final Uri CONTENT_URI = Uri.parse(MovieContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String YOU_TUBE_TRAILER_ID = "you_tube_trailer_id";

    public static final String NAME = "name";

    public static final String SIZE = "size";

    public static final String MOVIE_ID = "youtubetrailer__movie_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            YOU_TUBE_TRAILER_ID,
            NAME,
            SIZE,
            MOVIE_ID
    };
    // @formatter:on
    public static final String PREFIX_MOVIE = TABLE_NAME + "__" + MovieColumns.TABLE_NAME;

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(YOU_TUBE_TRAILER_ID) || c.contains("." + YOU_TUBE_TRAILER_ID)) return true;
            if (c.equals(NAME) || c.contains("." + NAME)) return true;
            if (c.equals(SIZE) || c.contains("." + SIZE)) return true;
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
        }
        return false;
    }
}
