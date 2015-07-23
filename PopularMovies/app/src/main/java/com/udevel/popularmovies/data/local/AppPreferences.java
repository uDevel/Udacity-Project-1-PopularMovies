package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by benny on 5/16/2015.
 */
public class AppPreferences {

    private static final String KEY_MOVIES_JSON_STR = "KEY_MOVIES_JSON_STR";
    private static final String KEY_MOVIES_PAGE = "KEY_MOVIES_PAGE";
    private static final int DEF_VALUE_MOVIE_PAGE = 1;

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void saveMoviesJsonStr(Context context, String savingStr) {
        getEditor(context).putString(KEY_MOVIES_JSON_STR, savingStr).apply();
    }

    public static String getMoviesJsonStr(Context context) {
        return getSharedPreferences(context).getString(KEY_MOVIES_JSON_STR, null);
    }

    public static void saveMoviePage(Context context, int page) {
        getEditor(context).putInt(KEY_MOVIES_PAGE, page).apply();
    }

    public static int getMoviePage(Context context) {
        return getSharedPreferences(context).getInt(KEY_MOVIES_PAGE, DEF_VALUE_MOVIE_PAGE);
    }

}
