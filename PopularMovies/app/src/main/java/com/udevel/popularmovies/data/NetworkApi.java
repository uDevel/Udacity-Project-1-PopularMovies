package com.udevel.popularmovies.data;

import com.udevel.popularmovies.data.api.TheMovieDBService;

import retrofit.RestAdapter;

/**
 * Created by benny on 7/12/2015.
 */
public class NetworkApi {
    private static TheMovieDBService getTheMovieDBService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TheMovieDBService.endPoint)
                .build();

        return restAdapter.create(TheMovieDBService.class);

    }
}
