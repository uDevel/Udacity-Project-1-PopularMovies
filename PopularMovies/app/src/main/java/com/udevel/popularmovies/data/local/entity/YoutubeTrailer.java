package com.udevel.popularmovies.data.local.entity;

/**
 * Created by benny on 8/9/2015.
 */
public class YouTubeTrailer {
    public static final String SITE_NAME = "youtube";
    private static final String URL_YOUTUBE_WATCH = "https://www.youtube.com/watch?v=";
    private static final String URL_YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/";
    private static final String URL_YOUTUBE_THUMBNAIL_SIZE_FULLSIZE = "/0.jpg";
    private static final String URL_YOUTUBE_THUMBNAIL_SIZE_LARGE = "/1.jpg";
    private String key;
    private String name;
    private String url;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return URL_YOUTUBE_WATCH + key;
    }

    public String getThumbnailUrl() {
        return URL_YOUTUBE_THUMBNAIL + key + URL_YOUTUBE_THUMBNAIL_SIZE_LARGE;
    }
}
