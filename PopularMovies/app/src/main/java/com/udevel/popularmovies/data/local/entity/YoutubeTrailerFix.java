package com.udevel.popularmovies.data.local.entity;

/**
 * Created by benny on 8/9/2015.
 */
public class YoutubeTrailerFix {
    private static final String URL_YOUTUBE_WATCH = "https://www.youtube.com/watch?v=";
    private static final String URL_YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/";
    private static final String URL_YOUTUBE_THUMBNAIL_SIZE_FULLSIZE = "/0.jpg";
    private static final String URL_YOUTUBE_THUMBNAIL_SIZE_LARGE = "/1.jpg";
    private static final String SIZE_HD = "HD";
    private String size;
    private String name;
    private String youTubeTrailerId;

    public String getYouTubeTrailerId() {
        return youTubeTrailerId;
    }

    public void setYouTubeTrailerId(String youTubeTrailerId) {
        this.youTubeTrailerId = youTubeTrailerId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return URL_YOUTUBE_WATCH + youTubeTrailerId;
    }

    public String getThumbnailUrl() {
        return URL_YOUTUBE_THUMBNAIL + youTubeTrailerId + URL_YOUTUBE_THUMBNAIL_SIZE_LARGE;
    }

    public boolean isHD() {
        return size != null && size.equals(SIZE_HD);
    }
}
