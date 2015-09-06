package com.udevel.popularmovies.data.local.provider.youtubetrailer;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.udevel.popularmovies.data.local.provider.base.AbstractSelection;
import com.udevel.popularmovies.data.local.provider.movie.*;

/**
 * Selection for the {@code youtubetrailer} table.
 */
public class YoutubetrailerSelection extends AbstractSelection<YoutubetrailerSelection> {
    @Override
    protected Uri baseUri() {
        return YoutubetrailerColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code YoutubetrailerCursor} object, which is positioned before the first entry, or null.
     */
    public YoutubetrailerCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new YoutubetrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public YoutubetrailerCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code YoutubetrailerCursor} object, which is positioned before the first entry, or null.
     */
    public YoutubetrailerCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new YoutubetrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public YoutubetrailerCursor query(Context context) {
        return query(context, null);
    }


    public YoutubetrailerSelection id(long... value) {
        addEquals("youtubetrailer." + YoutubetrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public YoutubetrailerSelection idNot(long... value) {
        addNotEquals("youtubetrailer." + YoutubetrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public YoutubetrailerSelection orderById(boolean desc) {
        orderBy("youtubetrailer." + YoutubetrailerColumns._ID, desc);
        return this;
    }

    public YoutubetrailerSelection orderById() {
        return orderById(false);
    }

    public YoutubetrailerSelection youTubeTrailerId(String... value) {
        addEquals(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }

    public YoutubetrailerSelection youTubeTrailerIdNot(String... value) {
        addNotEquals(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }

    public YoutubetrailerSelection youTubeTrailerIdLike(String... value) {
        addLike(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }

    public YoutubetrailerSelection youTubeTrailerIdContains(String... value) {
        addContains(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }

    public YoutubetrailerSelection youTubeTrailerIdStartsWith(String... value) {
        addStartsWith(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }

    public YoutubetrailerSelection youTubeTrailerIdEndsWith(String... value) {
        addEndsWith(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, value);
        return this;
    }

    public YoutubetrailerSelection orderByYouTubeTrailerId(boolean desc) {
        orderBy(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, desc);
        return this;
    }

    public YoutubetrailerSelection orderByYouTubeTrailerId() {
        orderBy(YoutubetrailerColumns.YOU_TUBE_TRAILER_ID, false);
        return this;
    }

    public YoutubetrailerSelection name(String... value) {
        addEquals(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerSelection nameNot(String... value) {
        addNotEquals(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerSelection nameLike(String... value) {
        addLike(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerSelection nameContains(String... value) {
        addContains(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerSelection nameStartsWith(String... value) {
        addStartsWith(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerSelection nameEndsWith(String... value) {
        addEndsWith(YoutubetrailerColumns.NAME, value);
        return this;
    }

    public YoutubetrailerSelection orderByName(boolean desc) {
        orderBy(YoutubetrailerColumns.NAME, desc);
        return this;
    }

    public YoutubetrailerSelection orderByName() {
        orderBy(YoutubetrailerColumns.NAME, false);
        return this;
    }

    public YoutubetrailerSelection size(String... value) {
        addEquals(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerSelection sizeNot(String... value) {
        addNotEquals(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerSelection sizeLike(String... value) {
        addLike(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerSelection sizeContains(String... value) {
        addContains(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerSelection sizeStartsWith(String... value) {
        addStartsWith(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerSelection sizeEndsWith(String... value) {
        addEndsWith(YoutubetrailerColumns.SIZE, value);
        return this;
    }

    public YoutubetrailerSelection orderBySize(boolean desc) {
        orderBy(YoutubetrailerColumns.SIZE, desc);
        return this;
    }

    public YoutubetrailerSelection orderBySize() {
        orderBy(YoutubetrailerColumns.SIZE, false);
        return this;
    }

    public YoutubetrailerSelection movieId(long... value) {
        addEquals(YoutubetrailerColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public YoutubetrailerSelection movieIdNot(long... value) {
        addNotEquals(YoutubetrailerColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public YoutubetrailerSelection movieIdGt(long value) {
        addGreaterThan(YoutubetrailerColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection movieIdGtEq(long value) {
        addGreaterThanOrEquals(YoutubetrailerColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection movieIdLt(long value) {
        addLessThan(YoutubetrailerColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection movieIdLtEq(long value) {
        addLessThanOrEquals(YoutubetrailerColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieId(boolean desc) {
        orderBy(YoutubetrailerColumns.MOVIE_ID, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieId() {
        orderBy(YoutubetrailerColumns.MOVIE_ID, false);
        return this;
    }

    public YoutubetrailerSelection movieMovieId(int... value) {
        addEquals(MovieColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public YoutubetrailerSelection movieMovieIdNot(int... value) {
        addNotEquals(MovieColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public YoutubetrailerSelection movieMovieIdGt(int value) {
        addGreaterThan(MovieColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection movieMovieIdGtEq(int value) {
        addGreaterThanOrEquals(MovieColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection movieMovieIdLt(int value) {
        addLessThan(MovieColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection movieMovieIdLtEq(int value) {
        addLessThanOrEquals(MovieColumns.MOVIE_ID, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieMovieId(boolean desc) {
        orderBy(MovieColumns.MOVIE_ID, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieMovieId() {
        orderBy(MovieColumns.MOVIE_ID, false);
        return this;
    }

    public YoutubetrailerSelection movieOriginalTitle(String... value) {
        addEquals(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public YoutubetrailerSelection movieOriginalTitleNot(String... value) {
        addNotEquals(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public YoutubetrailerSelection movieOriginalTitleLike(String... value) {
        addLike(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public YoutubetrailerSelection movieOriginalTitleContains(String... value) {
        addContains(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public YoutubetrailerSelection movieOriginalTitleStartsWith(String... value) {
        addStartsWith(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public YoutubetrailerSelection movieOriginalTitleEndsWith(String... value) {
        addEndsWith(MovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieOriginalTitle(boolean desc) {
        orderBy(MovieColumns.ORIGINAL_TITLE, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieOriginalTitle() {
        orderBy(MovieColumns.ORIGINAL_TITLE, false);
        return this;
    }

    public YoutubetrailerSelection movieVoteAverage(Double... value) {
        addEquals(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteAverageNot(Double... value) {
        addNotEquals(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteAverageGt(double value) {
        addGreaterThan(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteAverageGtEq(double value) {
        addGreaterThanOrEquals(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteAverageLt(double value) {
        addLessThan(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteAverageLtEq(double value) {
        addLessThanOrEquals(MovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieVoteAverage(boolean desc) {
        orderBy(MovieColumns.VOTE_AVERAGE, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieVoteAverage() {
        orderBy(MovieColumns.VOTE_AVERAGE, false);
        return this;
    }

    public YoutubetrailerSelection moviePosterPath(String... value) {
        addEquals(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public YoutubetrailerSelection moviePosterPathNot(String... value) {
        addNotEquals(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public YoutubetrailerSelection moviePosterPathLike(String... value) {
        addLike(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public YoutubetrailerSelection moviePosterPathContains(String... value) {
        addContains(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public YoutubetrailerSelection moviePosterPathStartsWith(String... value) {
        addStartsWith(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public YoutubetrailerSelection moviePosterPathEndsWith(String... value) {
        addEndsWith(MovieColumns.POSTER_PATH, value);
        return this;
    }

    public YoutubetrailerSelection orderByMoviePosterPath(boolean desc) {
        orderBy(MovieColumns.POSTER_PATH, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMoviePosterPath() {
        orderBy(MovieColumns.POSTER_PATH, false);
        return this;
    }

    public YoutubetrailerSelection movieOverview(String... value) {
        addEquals(MovieColumns.OVERVIEW, value);
        return this;
    }

    public YoutubetrailerSelection movieOverviewNot(String... value) {
        addNotEquals(MovieColumns.OVERVIEW, value);
        return this;
    }

    public YoutubetrailerSelection movieOverviewLike(String... value) {
        addLike(MovieColumns.OVERVIEW, value);
        return this;
    }

    public YoutubetrailerSelection movieOverviewContains(String... value) {
        addContains(MovieColumns.OVERVIEW, value);
        return this;
    }

    public YoutubetrailerSelection movieOverviewStartsWith(String... value) {
        addStartsWith(MovieColumns.OVERVIEW, value);
        return this;
    }

    public YoutubetrailerSelection movieOverviewEndsWith(String... value) {
        addEndsWith(MovieColumns.OVERVIEW, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieOverview(boolean desc) {
        orderBy(MovieColumns.OVERVIEW, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieOverview() {
        orderBy(MovieColumns.OVERVIEW, false);
        return this;
    }

    public YoutubetrailerSelection movieReleaseDate(String... value) {
        addEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public YoutubetrailerSelection movieReleaseDateNot(String... value) {
        addNotEquals(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public YoutubetrailerSelection movieReleaseDateLike(String... value) {
        addLike(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public YoutubetrailerSelection movieReleaseDateContains(String... value) {
        addContains(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public YoutubetrailerSelection movieReleaseDateStartsWith(String... value) {
        addStartsWith(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public YoutubetrailerSelection movieReleaseDateEndsWith(String... value) {
        addEndsWith(MovieColumns.RELEASE_DATE, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieReleaseDate(boolean desc) {
        orderBy(MovieColumns.RELEASE_DATE, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieReleaseDate() {
        orderBy(MovieColumns.RELEASE_DATE, false);
        return this;
    }

    public YoutubetrailerSelection moviePopularity(Double... value) {
        addEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public YoutubetrailerSelection moviePopularityNot(Double... value) {
        addNotEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public YoutubetrailerSelection moviePopularityGt(double value) {
        addGreaterThan(MovieColumns.POPULARITY, value);
        return this;
    }

    public YoutubetrailerSelection moviePopularityGtEq(double value) {
        addGreaterThanOrEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public YoutubetrailerSelection moviePopularityLt(double value) {
        addLessThan(MovieColumns.POPULARITY, value);
        return this;
    }

    public YoutubetrailerSelection moviePopularityLtEq(double value) {
        addLessThanOrEquals(MovieColumns.POPULARITY, value);
        return this;
    }

    public YoutubetrailerSelection orderByMoviePopularity(boolean desc) {
        orderBy(MovieColumns.POPULARITY, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMoviePopularity() {
        orderBy(MovieColumns.POPULARITY, false);
        return this;
    }

    public YoutubetrailerSelection movieVoteCount(Integer... value) {
        addEquals(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteCountNot(Integer... value) {
        addNotEquals(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteCountGt(int value) {
        addGreaterThan(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteCountGtEq(int value) {
        addGreaterThanOrEquals(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteCountLt(int value) {
        addLessThan(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public YoutubetrailerSelection movieVoteCountLtEq(int value) {
        addLessThanOrEquals(MovieColumns.VOTE_COUNT, value);
        return this;
    }

    public YoutubetrailerSelection orderByMovieVoteCount(boolean desc) {
        orderBy(MovieColumns.VOTE_COUNT, desc);
        return this;
    }

    public YoutubetrailerSelection orderByMovieVoteCount() {
        orderBy(MovieColumns.VOTE_COUNT, false);
        return this;
    }
}
