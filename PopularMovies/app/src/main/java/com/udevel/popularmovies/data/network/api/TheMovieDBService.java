package com.udevel.popularmovies.data.network.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by benny on 7/12/2015.
 */
public interface TheMovieDBService {
    //  String testAPi = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]";
    String endPoint = "https://api.themoviedb.org/3";
    String popularSortBy = "popularity.desc";
    String voteSortBy = "vote_average.desc";

    //TODO update api key
    String apiKey = //Update api key;

    @GET("/discover/movie")
    void getMovies(@Query("sort_by") String sort, @Query("vote_count.gte") int minVote, @Query("page") int page, @Query("api_key") String key, Callback<DiscoverMovieResult> callback);

    @GET("/movie/{id}")
    void getMovieById(@Path("id") int id, @Query("api_key") String key, Callback<MovieDetailInfo> callback);
}
