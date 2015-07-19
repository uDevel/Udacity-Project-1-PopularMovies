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

    public static boolean saveMoviesJsonStr(Context context, String savingStr) {
    /*    String originalStr = getMoviesJsonStr(context);
        if (originalStr == null) {*/
            return getEditor(context).putString(KEY_MOVIES_JSON_STR, savingStr).commit();
       /* } else {
            return false;
        }*/
    }

    public static String getMoviesJsonStr(Context context) {
        return getSharedPreferences(context).getString(KEY_MOVIES_JSON_STR, null);
    }

    public static boolean saveMoviePage(Context context, int page) {
        return getEditor(context).putInt(KEY_MOVIES_PAGE, page).commit();
    }

    public static int getMoviePage(Context context) {
        return getSharedPreferences(context).getInt(KEY_MOVIES_PAGE, DEF_VALUE_MOVIE_PAGE);
    }

}
