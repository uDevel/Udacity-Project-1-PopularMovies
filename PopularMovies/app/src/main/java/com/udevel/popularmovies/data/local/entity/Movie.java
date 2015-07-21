package com.udevel.popularmovies.data.local.entity;

import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 7/13/2015.
 */
public class Movie {
    public static final String BASE_URL_FOR_IMAGE = DiscoverMovieResult.BASE_URL_FOR_IMAGE;
    public static final String DETAIL_IMAGE_WIDTH = "w342";
    public static final String THUMBNAIL_IMAGE_WIDTH = "w185";

    private int id;
    private String originalTitle;
    private double voteAverage;
    private String posterPath;
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
        movie.setId(sourceMovieInfo.getId());
        movie.setOriginalTitle(sourceMovieInfo.getOriginalTitle());
        movie.setPosterPath(sourceMovieInfo.getPosterPath());
        movie.setVoteAverage(sourceMovieInfo.getVoteAverage());
        movie.setOverview(sourceMovieInfo.getOverview());
        movie.setReleaseDate(sourceMovieInfo.getReleaseDate());
        movie.setPopularity(sourceMovieInfo.getPopularity());
        movie.setVoteCount(sourceMovieInfo.getVoteCount());
        return movie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
