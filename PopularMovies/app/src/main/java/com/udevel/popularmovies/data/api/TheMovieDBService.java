package com.udevel.popularmovies.data.api;

import com.udevel.popularmovies.data.MovieResult;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by benny on 7/12/2015.
 */
public interface TheMovieDBService {
  //  String testAPi = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]";
    String endPoint = "http://api.themoviedb.org/3";

    @GET("/discover/movie")
    MovieResult getMovies(@Query("sort_by") String sort, @Query("api_key") String key);
}
