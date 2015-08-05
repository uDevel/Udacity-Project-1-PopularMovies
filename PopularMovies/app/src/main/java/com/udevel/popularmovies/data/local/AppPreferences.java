package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udevel.popularmovies.data.local.entity.Movie;

/**
 * Created by benny on 5/16/2015.
 */
public class AppPreferences {

    private static final String KEY_FAVOURITE_MOVIES_JSON_STR = "KEY_FAVOURITE_MOVIES_JSON_STR";
    private static final String KEY_MOVIES_JSON_STR = "KEY_MOVIES_JSON_STR";
    private static final String KEY_MOVIES_PAGE = "KEY_MOVIES_PAGE";
    private static final String KEY_LAST_MOVIE_LIST_TYPE = "KEY_LAST_MOVIE_LIST_TYPE";
    private static final int DEF_VALUE_MOVIE_PAGE = 1;
    private static final int DEF_VALUE_MOVIE_TYPE = Movie.MOVIE_LIST_TYPE_POPULARITY;


    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void setLastMovieListType(Context context, int lastMoviesType) {
        getEditor(context).putInt(KEY_LAST_MOVIE_LIST_TYPE, lastMoviesType).apply();
    }

    public static int getLastMovieListType(Context context) {
        return getSharedPreferences(context).getInt(KEY_LAST_MOVIE_LIST_TYPE, DEF_VALUE_MOVIE_TYPE);
    }

    public static void setMoviesJsonStr(Context context, String savingStr) {
        getEditor(context).putString(KEY_MOVIES_JSON_STR, savingStr).apply();
    }

    public static String getMoviesJsonStr(Context context) {
        return getSharedPreferences(context).getString(KEY_MOVIES_JSON_STR, null);
    }

    public static void setFavoriteMoviesJsonStr(Context context, String savingStr) {
        getEditor(context).putString(KEY_FAVOURITE_MOVIES_JSON_STR, savingStr).apply();
    }

    public static String getFavoriteMoviesJsonStr(Context context) {
        return getSharedPreferences(context).getString(KEY_FAVOURITE_MOVIES_JSON_STR, null);
    }

    public static void setMoviePage(Context context, int page) {
        getEditor(context).putInt(KEY_MOVIES_PAGE, page).apply();
    }

    public static int getMoviePage(Context context) {
        return getSharedPreferences(context).getInt(KEY_MOVIES_PAGE, DEF_VALUE_MOVIE_PAGE);
    }

}
