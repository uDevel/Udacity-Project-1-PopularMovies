package com.udevel.popularmovies.data.network;

import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.data.network.api.GetMovieInfoResult;
import com.udevel.popularmovies.data.network.api.TheMovieDBService;

import retrofit.Callback;
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

    public static void getMoviesByPopularity(int page, Callback<DiscoverMovieResult> movieResultCallback) {
        getTheMovieDBService().getMovies(TheMovieDBService.popularSortBy, 0, page, TheMovieDBService.apiKey, movieResultCallback);
    }

    // Minimum vote count is needed in order to have meaningful list.
    public static void getMoviesByRating(int page, Callback<DiscoverMovieResult> movieResultCallback) {
        getTheMovieDBService().getMovies(TheMovieDBService.voteSortBy, 200, page, TheMovieDBService.apiKey, movieResultCallback);
    }

    public static void getMovieById(int id, Callback<GetMovieInfoResult> movieInfoResultCallback) {
        getTheMovieDBService().getMovieById(id, TheMovieDBService.apiKey, movieInfoResultCallback);
    }
}
