package com.udevel.popularmovies.data.local.entity;

import com.udevel.popularmovies.data.network.api.DiscoverMovieInfoResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 7/13/2015.
 */
public class Movie {
    public static final String baseUrlForImage = DiscoverMovieInfoResult.baseUrlForImage;

    private int id;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String posterPath;
    private String releaseDate;

    public static List<Movie> convertDiscoverMovieInfoResults(List<DiscoverMovieInfoResult> discoverMovieInfoResults) {
        List<Movie> movies = new ArrayList<>(((Double) Math.ceil(discoverMovieInfoResults.size() * 1.5)).intValue());
        for (int i = 0; i < discoverMovieInfoResults.size(); i++) {
            DiscoverMovieInfoResult sourceMovieInfo = discoverMovieInfoResults.get(i);
            movies.add(convertDiscoverMovieInfoResult(sourceMovieInfo));

        }

        return movies;
    }

    private static Movie convertDiscoverMovieInfoResult(DiscoverMovieInfoResult sourceMovieInfo) {
        if (sourceMovieInfo == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setId(sourceMovieInfo.getId());
        movie.setOriginalTitle(sourceMovieInfo.getOriginal_title());
        movie.setOverview(sourceMovieInfo.getOverview());
        movie.setPosterPath(sourceMovieInfo.getPoster_path());
        movie.setReleaseDate(sourceMovieInfo.getRelease_date());
        movie.setVoteAverage(sourceMovieInfo.getVote_average());
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
