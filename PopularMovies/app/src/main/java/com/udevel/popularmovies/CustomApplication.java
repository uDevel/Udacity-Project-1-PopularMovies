package com.udevel.popularmovies;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.udevel.popularmovies.data.network.NetworkApi;

/**
 * Created by benny on 7/22/2015.
 */
public class CustomApplication extends Application {
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            NetworkApi.clearServiceCache();
        }
    }
}