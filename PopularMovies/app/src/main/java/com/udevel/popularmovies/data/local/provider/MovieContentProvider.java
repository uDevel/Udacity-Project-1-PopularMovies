package com.udevel.popularmovies.data.local.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.udevel.popularmovies.BuildConfig;
import com.udevel.popularmovies.data.local.provider.base.BaseContentProvider;
import com.udevel.popularmovies.data.local.provider.movie.MovieColumns;
import com.udevel.popularmovies.data.local.provider.review.ReviewColumns;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerColumns;

import java.util.Arrays;

public class MovieContentProvider extends BaseContentProvider {
    public static final String AUTHORITY = "com.udevel.popularmovies.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;
    private static final String TAG = MovieContentProvider.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";
    private static final int URI_TYPE_MOVIE = 0;
    private static final int URI_TYPE_MOVIE_ID = 1;

    private static final int URI_TYPE_REVIEW = 2;
    private static final int URI_TYPE_REVIEW_ID = 3;

    private static final int URI_TYPE_YOUTUBETRAILER = 4;
    private static final int URI_TYPE_YOUTUBETRAILER_ID = 5;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, MovieColumns.TABLE_NAME, URI_TYPE_MOVIE);
        URI_MATCHER.addURI(AUTHORITY, MovieColumns.TABLE_NAME + "/#", URI_TYPE_MOVIE_ID);
        URI_MATCHER.addURI(AUTHORITY, ReviewColumns.TABLE_NAME, URI_TYPE_REVIEW);
        URI_MATCHER.addURI(AUTHORITY, ReviewColumns.TABLE_NAME + "/#", URI_TYPE_REVIEW_ID);
        URI_MATCHER.addURI(AUTHORITY, YoutubetrailerColumns.TABLE_NAME, URI_TYPE_YOUTUBETRAILER);
        URI_MATCHER.addURI(AUTHORITY, YoutubetrailerColumns.TABLE_NAME + "/#", URI_TYPE_YOUTUBETRAILER_ID);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_MOVIE:
            case URI_TYPE_MOVIE_ID:
                res.table = MovieColumns.TABLE_NAME;
                res.idColumn = MovieColumns._ID;
                res.tablesWithJoins = MovieColumns.TABLE_NAME;
                res.orderBy = MovieColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_REVIEW:
            case URI_TYPE_REVIEW_ID:
                res.table = ReviewColumns.TABLE_NAME;
                res.idColumn = ReviewColumns._ID;
                res.tablesWithJoins = ReviewColumns.TABLE_NAME;
                if (MovieColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + MovieColumns.TABLE_NAME + " AS " + ReviewColumns.PREFIX_MOVIE + " ON " + ReviewColumns.TABLE_NAME + "." + ReviewColumns.MOVIE_ID + "=" + ReviewColumns.PREFIX_MOVIE + "." + MovieColumns._ID;
                }
                res.orderBy = ReviewColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_YOUTUBETRAILER:
            case URI_TYPE_YOUTUBETRAILER_ID:
                res.table = YoutubetrailerColumns.TABLE_NAME;
                res.idColumn = YoutubetrailerColumns._ID;
                res.tablesWithJoins = YoutubetrailerColumns.TABLE_NAME;
                if (MovieColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + MovieColumns.TABLE_NAME + " AS " + YoutubetrailerColumns.PREFIX_MOVIE + " ON " + YoutubetrailerColumns.TABLE_NAME + "." + YoutubetrailerColumns.MOVIE_ID + "=" + YoutubetrailerColumns.PREFIX_MOVIE + "." + MovieColumns._ID;
                }
                res.orderBy = YoutubetrailerColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_MOVIE_ID:
            case URI_TYPE_REVIEW_ID:
            case URI_TYPE_YOUTUBETRAILER_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return MovieSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG)
            Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_MOVIE:
                return TYPE_CURSOR_DIR + MovieColumns.TABLE_NAME;
            case URI_TYPE_MOVIE_ID:
                return TYPE_CURSOR_ITEM + MovieColumns.TABLE_NAME;

            case URI_TYPE_REVIEW:
                return TYPE_CURSOR_DIR + ReviewColumns.TABLE_NAME;
            case URI_TYPE_REVIEW_ID:
                return TYPE_CURSOR_ITEM + ReviewColumns.TABLE_NAME;

            case URI_TYPE_YOUTUBETRAILER:
                return TYPE_CURSOR_DIR + YoutubetrailerColumns.TABLE_NAME;
            case URI_TYPE_YOUTUBETRAILER_ID:
                return TYPE_CURSOR_ITEM + YoutubetrailerColumns.TABLE_NAME;

        }
        return null;
    }
}
