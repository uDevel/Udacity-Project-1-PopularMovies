package com.udevel.popularmovies.data.network.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by benny on 7/12/2015.
 */
public class DiscoverMovieInfoResult {
    /**
     * vote_average : 7.0
     * backdrop_path : /dkMD5qlogeRMiEixC4YNPUvax2T.jpg
     * adult : false
     * id : 135397
     * title : Jurassic World
     * original_language : en
     * overview : Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.
     * genre_ids : [28,12,878,53]
     * original_title : Jurassic World
     * release_date : 2015-06-12
     * vote_count : 986
     * poster_path : /uXZYawqUsChGSj54wcuBtEdUJbh.jpg
     * video : false
     * popularity : 70.529253
     */

    public static final String baseUrlForImage = "http://image.tmdb.org/t/p/";

    @SerializedName("vote_average")
    private double vote_average;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("original_language")
    private String original_language;
    @SerializedName("overview")
    private String overview;
    @SerializedName("genre_ids")
    private List<Integer> genre_ids;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("vote_count")
    private int vote_count;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("video")
    private boolean video;
    @SerializedName("popularity")
    private double popularity;

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOverview() {
        return overview;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public boolean isVideo() {
        return video;
    }

    public double getPopularity() {
        return popularity;
    }
}
