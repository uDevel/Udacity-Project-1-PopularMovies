package com.udevel.popularmovies.adapter.listener;

import android.view.View;

/**
 * Created by benny on 7/17/2015.
 */
public interface AdapterItemClickListener {
    String ACTION_OPEN_MOVIE_DETAIL = "ACTION_OPEN_MOVIE_DETAIL";
    String ACTION_OPEN_YOUTUBE_TRAILER = "ACTION_OPEN_YOUTUBE_TRAILER";

    void adapterItemClick(String action, View v, Object data);
}
