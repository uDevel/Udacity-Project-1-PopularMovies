package com.udevel.popularmovies.data.network;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.data.network.api.MovieDetailInfoResult;
import com.udevel.popularmovies.data.network.api.ReviewsResult;
import com.udevel.popularmovies.data.network.api.TheMovieDBService;
import com.udevel.popularmovies.data.network.api.TrailersResult;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by benny on 7/12/2015.
 */
public class NetworkApi {
    public static final int RETRY_COUNT = 6;
    private static final long CONNECT_TIMEOUT_MILLIS = 5000;
    private static final long READ_TIMEOUT_MILLIS = 3000;
    private static TheMovieDBService theMovieDBServiceCache;

    private static TheMovieDBService getTheMovieDBService() {
        if (theMovieDBServiceCache == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TheMovieDBService.endPoint)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(getRetryOkHttpClient()))
                    .build();
            theMovieDBServiceCache = restAdapter.create(TheMovieDBService.class);
        }

        return theMovieDBServiceCache;
    }

    private static OkHttpClient getRetryOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {

                com.squareup.okhttp.Request request = chain.request();

                // try the request
                com.squareup.okhttp.Response response = chain.proceed(request);

                int tryCount = 0;
                while (!response.isSuccessful() && tryCount < RETRY_COUNT) {

                    Log.d("intercept", "Request is not successful - " + tryCount);

                    tryCount++;

                    // retry the request
                    response = chain.proceed(request);
                }

                // otherwise just pass the original response on
                return response;
            }
        });

        return client;
    }

    public static void clearServiceCache() {
        theMovieDBServiceCache = null;
    }

    public static void getMoviesByPopularity(int page, Callback<DiscoverMovieResult> movieResultCallback) {
        // page for the api starts with 1
        getTheMovieDBService().getMovies(TheMovieDBService.popularSortBy, 0, page + 1, TheMovieDBService.apiKey, movieResultCallback);
    }

    // Minimum vote count is needed in order to have meaningful list.
    public static void getMoviesByRating(int page, int minimumVoteCount, Callback<DiscoverMovieResult> movieResultCallback) {
        getTheMovieDBService().getMovies(TheMovieDBService.voteSortBy, minimumVoteCount, page + 1, TheMovieDBService.apiKey, movieResultCallback);
    }

    public static void getMovieById(int id, Callback<MovieDetailInfoResult> movieDetailInfoCallback) {
        getTheMovieDBService().getMovieById(id, TheMovieDBService.apiKey, TheMovieDBService.appendTrailersAndReviews, movieDetailInfoCallback);
    }

    public static void getMovieTrailers(int id, Callback<TrailersResult> movieTrailersCallback) {
        getTheMovieDBService().getMovieTrailers(id, TheMovieDBService.apiKey, movieTrailersCallback);
    }

    public static void getMovieReviews(int id, Callback<ReviewsResult> movieReviewsCallback) {
        getTheMovieDBService().getMovieReviews(id, TheMovieDBService.apiKey, movieReviewsCallback);
    }

}
