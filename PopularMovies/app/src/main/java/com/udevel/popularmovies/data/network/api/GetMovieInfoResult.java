package com.udevel.popularmovies.data.network.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by benny on 7/12/2015.
 */
public class GetMovieInfoResult {
    /**
     * budget : 170000000
     * vote_average : 6.3
     * backdrop_path : /o4I5sHdjzs29hBWzHtS2MKD3JsM.jpg
     * genres : [{"id":878,"name":"Science Fiction"},{"id":28,"name":"Action"},{"id":53,"name":"Thriller"},{"id":12,"name":"Adventure"}]
     * status : Released
     * runtime : 125
     * spoken_languages : [{"iso_639_1":"en","name":"English"}]
     * adult : false
     * homepage : http://www.terminatormovie.com/
     * id : 87101
     * production_countries : [{"name":"United States of America","iso_3166_1":"US"}]
     * title : Terminator Genisys
     * original_language : en
     * overview : The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.
     * production_companies : [{"id":4,"name":"Paramount Pictures"},{"id":6277,"name":"Skydance Productions"}]
     * belongs_to_collection : null
     * imdb_id : tt1340138
     * release_date : 2015-07-01
     * original_title : Terminator Genisys
     * vote_count : 254
     * poster_path : /5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg
     * video : false
     * tagline : Reset the future
     * revenue : 0
     * popularity : 58.479106
     */
    @SerializedName("budget")
    private int budget;
    @SerializedName("vote_average")
    private double vote_average;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("genres")
    private List<GenresEntity> genres;
    @SerializedName("status")
    private String status;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("spoken_languages")
    private List<Spoken_languagesEntity> spoken_languages;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("id")
    private int id;
    @SerializedName("production_countries")
    private List<Production_countriesEntity> production_countries;
    @SerializedName("title")
    private String title;
    @SerializedName("original_language")
    private String original_language;
    @SerializedName("overview")
    private String overview;
    @SerializedName("production_companies")
    private List<Production_companiesEntity> production_companies;
    @SerializedName("belongs_to_collection")
    private String belongs_to_collection;
    @SerializedName("imdb_id")
    private String imdb_id;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("vote_count")
    private int vote_count;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("video")
    private boolean video;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("revenue")
    private int revenue;
    @SerializedName("popularity")
    private double popularity;

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setGenres(List<GenresEntity> genres) {
        this.genres = genres;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setSpoken_languages(List<Spoken_languagesEntity> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduction_countries(List<Production_countriesEntity> production_countries) {
        this.production_countries = production_countries;
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

    public void setProduction_companies(List<Production_companiesEntity> production_companies) {
        this.production_companies = production_companies;
    }

    public void setBelongs_to_collection(String belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
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

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getBudget() {
        return budget;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public List<GenresEntity> getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<Spoken_languagesEntity> getSpoken_languages() {
        return spoken_languages;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public List<Production_countriesEntity> getProduction_countries() {
        return production_countries;
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

    public List<Production_companiesEntity> getProduction_companies() {
        return production_companies;
    }

    public String getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
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

    public String getTagline() {
        return tagline;
    }

    public int getRevenue() {
        return revenue;
    }

    public double getPopularity() {
        return popularity;
    }

    public class GenresEntity {
        /**
         * id : 878
         * name : Science Fiction
         */
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class Spoken_languagesEntity {
        /**
         * iso_639_1 : en
         * name : English
         */
        @SerializedName("iso_639_1")
        private String iso_639_1;
        @SerializedName("name")
        private String name;

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getName() {
            return name;
        }
    }

    public class Production_countriesEntity {
        /**
         * name : United States of America
         * iso_3166_1 : US
         */
        @SerializedName("name")
        private String name;
        @SerializedName("iso_3166_1")
        private String iso_3166_1;

        public void setName(String name) {
            this.name = name;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getName() {
            return name;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }
    }

    public class Production_companiesEntity {
        /**
         * id : 4
         * name : Paramount Pictures
         */
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
