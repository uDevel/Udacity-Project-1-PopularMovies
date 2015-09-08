package com.udevel.popularmovies.data.network.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailInfoResult {

    @Expose
    private Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("belongs_to_collection")
    @Expose
    private BelongsToCollection belongsToCollection;
    @Expose
    private Integer budget;
    @Expose
    private List<Genre> genres = new ArrayList<>();
    @Expose
    private String homepage;
    @Expose
    private Integer id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @Expose
    private String overview;
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = new ArrayList<>();
    @SerializedName("production_countries")
    @Expose
    private List<ProductionCountry> productionCountries = new ArrayList<>();
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @Expose
    private Long revenue;
    @Expose
    private Integer runtime;
    @SerializedName("spoken_languages")
    @Expose
    private List<SpokenLanguage> spokenLanguages = new ArrayList<>();
    @Expose
    private String status;
    @Expose
    private String tagline;
    @Expose
    private String title;
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @Expose
    private Trailers trailers;
    @Expose
    private Reviews reviews;

    /**
     * @return The adult
     */
    public Boolean getAdult() {
        return adult;
    }

    /**
     * @param adult The adult
     */
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    /**
     * @return The backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * @param backdropPath The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /**
     * @return The belongsToCollection
     */
    public BelongsToCollection getBelongsToCollection() {
        return belongsToCollection;
    }

    /**
     * @param belongsToCollection The belongs_to_collection
     */
    public void setBelongsToCollection(BelongsToCollection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    /**
     * @return The budget
     */
    public Integer getBudget() {
        return budget;
    }

    /**
     * @param budget The budget
     */
    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    /**
     * @return The genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * @param genres The genres
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     * @return The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The imdbId
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * @param imdbId The imdb_id
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     * @return The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * @param originalLanguage The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     * @return The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * @param originalTitle The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @param overview The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return The popularity
     */
    public Double getPopularity() {
        return popularity;
    }

    /**
     * @param popularity The popularity
     */
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @param posterPath The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * @return The productionCompanies
     */
    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    /**
     * @param productionCompanies The production_companies
     */
    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    /**
     * @return The productionCountries
     */
    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    /**
     * @param productionCountries The production_countries
     */
    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    /**
     * @return The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return The revenue
     */
    public Long getRevenue() {
        return revenue;
    }

    /**
     * @param revenue The revenue
     */
    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    /**
     * @return The runtime
     */
    public Integer getRuntime() {
        return runtime;
    }

    /**
     * @param runtime The runtime
     */
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     * @return The spokenLanguages
     */
    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    /**
     * @param spokenLanguages The spoken_languages
     */
    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The tagline
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * @param tagline The tagline
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The video
     */
    public Boolean getVideo() {
        return video;
    }

    /**
     * @param video The video
     */
    public void setVideo(Boolean video) {
        this.video = video;
    }

    /**
     * @return The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }

    /**
     * @param voteAverage The vote_average
     */
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     * @return The voteCount
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     * @param voteCount The vote_count
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Trailers getTrailers() {
        return trailers;
    }

    public void setTrailers(Trailers trailers) {
        this.trailers = trailers;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public class ProductionCompany {

        @Expose
        private String name;
        @Expose
        private Integer id;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The id
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Integer id) {
            this.id = id;
        }

    }

    public class ProductionCountry {

        @SerializedName("iso_3166_1")
        @Expose
        private String iso31661;
        @Expose
        private String name;

        /**
         * @return The iso31661
         */
        public String getIso31661() {
            return iso31661;
        }

        /**
         * @param iso31661 The iso_3166_1
         */
        public void setIso31661(String iso31661) {
            this.iso31661 = iso31661;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

    }

    public class SpokenLanguage {

        @SerializedName("iso_639_1")
        @Expose
        private String iso6391;
        @Expose
        private String name;

        /**
         * @return The iso6391
         */
        public String getIso6391() {
            return iso6391;
        }

        /**
         * @param iso6391 The iso_639_1
         */
        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

    }

    public class BelongsToCollection {

        @Expose
        private Integer id;
        @Expose
        private String name;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;

        /**
         * @return The id
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The posterPath
         */
        public String getPosterPath() {
            return posterPath;
        }

        /**
         * @param posterPath The poster_path
         */
        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        /**
         * @return The backdropPath
         */
        public String getBackdropPath() {
            return backdropPath;
        }

        /**
         * @param backdropPath The backdrop_path
         */
        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

    }

    public class Genre {

        @Expose
        private Integer id;
        @Expose
        private String name;

        /**
         * @return The id
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

    }

    public class Reviews {

        @Expose
        private Integer page;
        @Expose
        private List<Result> results = new ArrayList<>();
        @SerializedName("total_pages")
        @Expose
        private Integer totalPages;
        @SerializedName("total_results")
        @Expose
        private Integer totalResults;

        /**
         * @return The page
         */
        public Integer getPage() {
            return page;
        }

        /**
         * @param page The page
         */
        public void setPage(Integer page) {
            this.page = page;
        }

        /**
         * @return The results
         */
        public List<Result> getResults() {
            return results;
        }

        /**
         * @param results The results
         */
        public void setResults(List<Result> results) {
            this.results = results;
        }

        /**
         * @return The totalPages
         */
        public Integer getTotalPages() {
            return totalPages;
        }

        /**
         * @param totalPages The total_pages
         */
        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        /**
         * @return The totalResults
         */
        public Integer getTotalResults() {
            return totalResults;
        }

        /**
         * @param totalResults The total_results
         */
        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

        public class Result {

            @Expose
            private String id;
            @Expose
            private String author;
            @Expose
            private String content;
            @Expose
            private String url;

            /**
             * @return The id
             */
            public String getId() {
                return id;
            }

            /**
             * @param id The id
             */
            public void setId(String id) {
                this.id = id;
            }

            /**
             * @return The author
             */
            public String getAuthor() {
                return author;
            }

            /**
             * @param author The author
             */
            public void setAuthor(String author) {
                this.author = author;
            }

            /**
             * @return The content
             */
            public String getContent() {
                return content;
            }

            /**
             * @param content The content
             */
            public void setContent(String content) {
                this.content = content;
            }

            /**
             * @return The url
             */
            public String getUrl() {
                return url;
            }

            /**
             * @param url The url
             */
            public void setUrl(String url) {
                this.url = url;
            }

        }

    }

    public class Trailers {

        @Expose
        private List<Object> quicktime = new ArrayList<>();
        @Expose
        private List<Youtube> youtube = new ArrayList<>();

        /**
         * @return The quicktime
         */
        public List<Object> getQuicktime() {
            return quicktime;
        }

        /**
         * @param quicktime The quicktime
         */
        public void setQuicktime(List<Object> quicktime) {
            this.quicktime = quicktime;
        }

        /**
         * @return The youtube
         */
        public List<Youtube> getYoutube() {
            return youtube;
        }

        /**
         * @param youtube The youtube
         */
        public void setYoutube(List<Youtube> youtube) {
            this.youtube = youtube;
        }

    }

    public class Youtube {

        @Expose
        private String name;
        @Expose
        private String size;
        @Expose
        private String source;
        @Expose
        private String type;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The size
         */
        public String getSize() {
            return size;
        }

        /**
         * @param size The size
         */
        public void setSize(String size) {
            this.size = size;
        }

        /**
         * @return The source
         */
        public String getSource() {
            return source;
        }

        /**
         * @param source The source
         */
        public void setSource(String source) {
            this.source = source;
        }

        /**
         * @return The type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The type
         */
        public void setType(String type) {
            this.type = type;
        }

    }

}