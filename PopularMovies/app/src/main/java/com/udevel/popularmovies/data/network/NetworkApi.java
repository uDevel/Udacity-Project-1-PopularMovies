package com.udevel.popularmovies.data.network;

import com.squareup.okhttp.OkHttpClient;
import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.data.network.api.MovieDetailInfo;
import com.udevel.popularmovies.data.network.api.TheMovieDBService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by benny on 7/12/2015.
 */
public class NetworkApi {
    private static TheMovieDBService theMovieDBServiceCache;

    private static TheMovieDBService getTheMovieDBService() {
        if (theMovieDBServiceCache == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TheMovieDBService.endPoint)
                    .setClient(new OkClient(new OkHttpClient()))
                    .build();
            theMovieDBServiceCache = restAdapter.create(TheMovieDBService.class);
        }

        return theMovieDBServiceCache;
    }

    public static void clearServiceCache() {
        theMovieDBServiceCache = null;
    }

    public static void getMoviesByPopularity(int page, Callback<DiscoverMovieResult> movieResultCallback) {
        getTheMovieDBService().getMovies(TheMovieDBService.popularSortBy, 0, page, TheMovieDBService.apiKey, movieResultCallback);
    }

    // Minimum vote count is needed in order to have meaningful list.
    public static void getMoviesByRating(int page, Callback<DiscoverMovieResult> movieResultCallback) {
        getTheMovieDBService().getMovies(TheMovieDBService.voteSortBy, 200, page, TheMovieDBService.apiKey, movieResultCallback);
    }

    public static void getMovieById(int id, Callback<MovieDetailInfo> movieDetailInfoCallback) {
        getTheMovieDBService().getMovieById(id, TheMovieDBService.apiKey, movieDetailInfoCallback);
    }
}
