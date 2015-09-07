package com.udevel.popularmovies.data.local.entity;

import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.data.network.api.MovieDetailInfoResult;
import com.udevel.popularmovies.misc.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 7/13/2015.
 */
public class Movie {
    public static final String BASE_URL_FOR_IMAGE = DiscoverMovieResult.BASE_URL_FOR_IMAGE;
    public static final String THUMBNAIL_IMAGE_WIDTH = "w185";
    public static final String FULLSIZE_IMAGE_WIDTH = "original";
    public static final int MOVIE_LIST_TYPE_POPULARITY = 0;
    public static final int MOVIE_LIST_TYPE_RATING = 1;
    public static final int MOVIE_LIST_TYPE_LOCAL_FAVOURITE = 2;

    private int movieId;
    private String originalTitle;
    private double voteAverage;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private double popularity;
    private int voteCount;

    public static List<Movie> convertDiscoverMovieInfoResults(List<DiscoverMovieResult.Result> discoverMovieInfoResults) {
        List<Movie> movies = new ArrayList<>(((Double) Math.ceil(discoverMovieInfoResults.size() * 1.5)).intValue());
        for (int i = 0; i < discoverMovieInfoResults.size(); i++) {
            DiscoverMovieResult.Result sourceMovieInfo = discoverMovieInfoResults.get(i);
            movies.add(convertDiscoverMovieInfoResult(sourceMovieInfo));
        }

        return movies;
    }

    private static Movie convertDiscoverMovieInfoResult(DiscoverMovieResult.Result sourceMovieInfo) {
        if (sourceMovieInfo == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setMovieId(sourceMovieInfo.getId());
        movie.setOriginalTitle(sourceMovieInfo.getOriginalTitle());
        movie.setPosterPath(sourceMovieInfo.getPosterPath());
        movie.setBackdropPath(sourceMovieInfo.getBackdropPath());
        movie.setVoteAverage(sourceMovieInfo.getVoteAverage());
        movie.setOverview(sourceMovieInfo.getOverview());
        movie.setReleaseDate(sourceMovieInfo.getReleaseDate());
        movie.setPopularity(sourceMovieInfo.getPopularity());
        movie.setVoteCount(sourceMovieInfo.getVoteCount());
        return movie;
    }

    public static Movie convertMovieDetailInfoResult(MovieDetailInfoResult sourceMovieInfo) {
        if (sourceMovieInfo == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setMovieId(sourceMovieInfo.getId());
        movie.setOriginalTitle(sourceMovieInfo.getOriginalTitle());
        movie.setPosterPath(sourceMovieInfo.getPosterPath());
        movie.setBackdropPath(sourceMovieInfo.getBackdropPath());
        movie.setVoteAverage(sourceMovieInfo.getVoteAverage());
        movie.setOverview(sourceMovieInfo.getOverview());
        movie.setReleaseDate(sourceMovieInfo.getReleaseDate());
        movie.setPopularity(sourceMovieInfo.getPopularity());
        movie.setVoteCount(sourceMovieInfo.getVoteCount());
        return movie;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;

        if (movieId != movie.movieId) return false;
        if (!Utils.compareString(originalTitle, movie.originalTitle)) return false;
        if (voteAverage != movie.voteAverage) return false;
        if (!Utils.compareString(posterPath, movie.posterPath)) return false;
        if (!Utils.compareString(backdropPath, movie.backdropPath)) return false;
        if (!Utils.compareString(overview, movie.overview)) return false;
        if (!Utils.compareString(releaseDate, movie.releaseDate)) return false;
        if (popularity != movie.popularity) return false;
        return voteCount == movie.voteCount;

    }
}
