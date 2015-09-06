package com.udevel.popularmovies.data.local;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.local.entity.Review;
import com.udevel.popularmovies.data.local.entity.YouTubeTrailer;
import com.udevel.popularmovies.data.local.provider.MovieContentProvider;
import com.udevel.popularmovies.data.local.provider.movie.MovieColumns;
import com.udevel.popularmovies.data.local.provider.movie.MovieContentValues;
import com.udevel.popularmovies.data.local.provider.movie.MovieCursor;
import com.udevel.popularmovies.data.local.provider.movie.MovieSelection;
import com.udevel.popularmovies.data.local.provider.review.ReviewColumns;
import com.udevel.popularmovies.data.local.provider.review.ReviewContentValues;
import com.udevel.popularmovies.data.local.provider.review.ReviewCursor;
import com.udevel.popularmovies.data.local.provider.review.ReviewSelection;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerColumns;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerContentValues;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerCursor;
import com.udevel.popularmovies.data.local.provider.youtubetrailer.YoutubetrailerSelection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 5/16/2015.
 */
public class DataManager {

    private static final String TAG = DataManager.class.getCanonicalName();

    // For testing purpose only.
    public static void clearData(Context context) {
        AppPreferences.clearSharedPreference(context);
        int deletedRows = context.getContentResolver().delete(MovieColumns.CONTENT_URI, "_id != -1", null);
        Log.d(TAG, "deletedRows:" + deletedRows);

        deletedRows = context.getContentResolver().delete(ReviewColumns.CONTENT_URI, "_id != -1", null);
        Log.d(TAG, "deletedRows:" + deletedRows);

        deletedRows = context.getContentResolver().delete(YoutubetrailerColumns.CONTENT_URI, "_id != -1", null);
        Log.d(TAG, "deletedRows:" + deletedRows);
    }

    @Nullable
    public static List<Movie> getMovies(Context context) {
        List<Movie> Movies = null;
        String MoviesJsonStr = AppPreferences.getMoviesJsonStr(context);
        if (MoviesJsonStr != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Movie>>() {
            }.getType();
            Movies = gson.fromJson(MoviesJsonStr, collectionType);
        }

        return Movies;
    }

    public static Movie getMovieById(Context context, int targetId) {
        List<Movie> movies = getMovies(context);
        if (movies != null) {
            for (Movie movie : movies) {
                if (movie.getMovieId() == targetId) {
                    return movie;
                }
            }
        }
        return null;
    }

    public static List<Movie> saveMovies(Context context, List<Movie> movies, int page) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(movies);
        AppPreferences.setMoviesJsonStr(context, jsonStr);
        AppPreferences.setMoviePage(context, page);
        return movies;
    }

    public static List<Movie> addMovies(Context context, List<Movie> movies, int page) {
        List<Movie> origMovies = getMovies(context);

        if (movies == null) {
            return origMovies;
        }

        if (origMovies == null) {
            return movies;
        }
        SparseArray<Integer> existingMovieIdIndex = new SparseArray<>(origMovies.size());
        for (int i = 0; i < origMovies.size(); i++) {
            existingMovieIdIndex.put(origMovies.get(i).getMovieId(), i);
        }

        // Check if exist.
        for (int i = movies.size() - 1; i >= 0; i--) {
            Movie movie = movies.get(i);
            Integer index = existingMovieIdIndex.get(movie.getMovieId());
            if (index != null) {
                movies.remove(i);
            }
        }

        origMovies.addAll(movies);

        return saveMovies(context, origMovies, page);
    }

    @NonNull
    public static List<Movie> getFavoriteMovieList(Context context) {
        List<Movie> movieList = new ArrayList<>();
        MovieSelection selection = new MovieSelection();
        MovieCursor movieCursor = selection.query(context);

        while (movieCursor.moveToNext()) {
            Movie movie = convertToMovie(movieCursor);
            movieList.add(movie);
        }
        return movieList;
    }

    @Nullable
    public static Long addFavoriteMovie(Context context, Movie movie){
        MovieContentValues movieContentValues = convertToMovieContentValues(movie);
        if (movieContentValues != null) {
            Uri uri = movieContentValues.insert(context);
            if(uri != null) {
                return ContentUris.parseId(uri);
            }
        }

        return null;
    }

    @NonNull
    public static List<Review> getReviewsByMovieId(Context context, int movieId) {
        ReviewSelection selection = new ReviewSelection();
        selection.movieMovieId(movieId);
        ReviewCursor reviewCursor = selection.query(context);

        List<Review> reviews = new ArrayList<>();
        while (reviewCursor.moveToNext()) {
            reviews.add(convertToReview(reviewCursor));
        }

        return reviews;
    }

    @NonNull
    public static List<YouTubeTrailer> getYouTubeTrailerByMovieId(Context context, int movieId) {
        YoutubetrailerSelection selection = new YoutubetrailerSelection();
        selection.movieMovieId(movieId);
        YoutubetrailerCursor youtubetrailerCursor = selection.query(context);

        List<YouTubeTrailer> youTubeTrailers = new ArrayList<>();
        while (youtubetrailerCursor.moveToNext()) {
            youTubeTrailers.add(convertToYouTubeTrailers(youtubetrailerCursor));
        }

        return youTubeTrailers;
    }

    public static Long addFavoriteMovieReviewTrailer(Context context, Movie movie, List<Review> reviews, List<YouTubeTrailer> youTubeTrailers) {
        MovieContentValues movieContentValues = convertToMovieContentValues(movie);
        List<ReviewContentValues> reviewContentValuesList = convertToReviewContentValuesList(reviews);
        List<YoutubetrailerContentValues> youtubetrailerContentValuesList = convertToYoutubetrailerContentValuesList(youTubeTrailers);

        if (movieContentValues != null) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ops.add(ContentProviderOperation.newInsert(MovieColumns.CONTENT_URI)
                    .withValues(movieContentValues.values())
                    .build());

            if (reviewContentValuesList != null && reviewContentValuesList.size() > 0) {
                for (ReviewContentValues values : reviewContentValuesList) {
                    ops.add(ContentProviderOperation.newInsert(ReviewColumns.CONTENT_URI)
                            .withValues(values.values())
                            .withValueBackReference(ReviewColumns.MOVIE_ID, 0)
                            .build());
                }
            }

            if (youtubetrailerContentValuesList != null && youtubetrailerContentValuesList.size() > 0) {
                for (YoutubetrailerContentValues values : youtubetrailerContentValuesList) {
                    ops.add(ContentProviderOperation.newInsert(YoutubetrailerColumns.CONTENT_URI)
                            .withValues(values.values())
                            .withValueBackReference(YoutubetrailerColumns.MOVIE_ID, 0)
                            .build());
                }
            }

            try {
                ContentProviderResult[] contentProviderResults = context.getContentResolver().applyBatch(MovieContentProvider.AUTHORITY, ops);
                Uri uri = contentProviderResults[0].uri;
                if (uri != null) {
                    return ContentUris.parseId(uri);
                }
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return null;
    }

    public static void removeFavoriteMovie(Context context, Movie movie) {
        if (movie == null) {
            return;
        }

        MovieSelection movieSelection = new MovieSelection();
        movieSelection.movieId(movie.getMovieId());
        movieSelection.delete(context);
    }

    public static Movie getFavoriteMovieById(Context context, int targetId) {

        MovieSelection movieSelection = new MovieSelection();
        movieSelection.movieId(targetId);
        MovieCursor movieCursor = movieSelection.query(context);
        if (movieCursor.moveToNext()) {
            return convertToMovie(movieCursor);
        } else {
            return null;
        }
    }

    @Nullable
    private static MovieContentValues convertToMovieContentValues(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieContentValues values = new MovieContentValues();
        values.putMovieId(movie.getMovieId());
        values.putOriginalTitle(movie.getOriginalTitle());
        values.putOverview(movie.getOverview());
        values.putPopularity(movie.getPopularity());
        values.putPosterPath(movie.getPosterPath());
        values.putReleaseDate(movie.getReleaseDate());
        values.putVoteAverage(movie.getVoteAverage());
        values.putVoteCount(movie.getVoteCount());
        return values;
    }

    @Nullable
    private static List<ReviewContentValues> convertToReviewContentValuesList(List<Review> reviews) {
        if (reviews == null || reviews.size() == 0) {
            return null;
        }

        List<ReviewContentValues> reviewContentValuesList = new ArrayList<>();

        for (Review review : reviews) {
            ReviewContentValues values = new ReviewContentValues();
            values.putAuthor(review.getAuthor());
            values.putContent(review.getContent());
            values.putReviewId(review.getReviewId());
            reviewContentValuesList.add(values);
        }

        return reviewContentValuesList;
    }

    @Nullable
    private static List<YoutubetrailerContentValues> convertToYoutubetrailerContentValuesList(List<YouTubeTrailer> youTubeTrailers) {
        if (youTubeTrailers == null || youTubeTrailers.size() == 0) {
            return null;
        }

        List<YoutubetrailerContentValues> youtubetrailerContentValuesList = new ArrayList<>();

        for (YouTubeTrailer youTubeTrailer : youTubeTrailers) {
            YoutubetrailerContentValues values = new YoutubetrailerContentValues();
            values.putName(youTubeTrailer.getName());
            values.putSize(youTubeTrailer.getSize());
            values.putYouTubeTrailerId(youTubeTrailer.getYouTubeTrailerId());
            youtubetrailerContentValuesList.add(values);
        }

        return youtubetrailerContentValuesList;
    }

    @Nullable
    private static Movie convertToMovie(MovieCursor movieCursor) {
        if (movieCursor == null || movieCursor.isAfterLast()) {
            return null;
        }
        Movie movie = new Movie();

        try {
            movie.setMovieId(movieCursor.getMovieId());
            movie.setOriginalTitle(movieCursor.getOriginalTitle());
            movie.setOverview(movieCursor.getOverview());
            movie.setPopularity(movieCursor.getPopularity());
            movie.setPosterPath(movieCursor.getPosterPath());
            movie.setReleaseDate(movieCursor.getReleaseDate());
            movie.setVoteAverage(movieCursor.getVoteAverage());
            movie.setVoteCount(movieCursor.getVoteCount());
        } catch (NullPointerException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            return movie;
        }
    }

    private static Review convertToReview(ReviewCursor reviewCursor) {
        if (reviewCursor == null || reviewCursor.isAfterLast()) {
            return null;
        }

        Review review = new Review();

        review.setAuthor(reviewCursor.getAuthor());
        review.setReviewId(reviewCursor.getReviewId());
        review.setContent(reviewCursor.getContent());

        return review;
    }

    private static YouTubeTrailer convertToYouTubeTrailers(YoutubetrailerCursor youtubetrailerCursor) {
        if (youtubetrailerCursor == null || youtubetrailerCursor.isAfterLast()) {
            return null;
        }

        YouTubeTrailer youTubeTrailer = new YouTubeTrailer();
        youTubeTrailer.setYouTubeTrailerId(youtubetrailerCursor.getYouTubeTrailerId());
        youTubeTrailer.setSize(youtubetrailerCursor.getSize());
        youTubeTrailer.setName(youtubetrailerCursor.getName());

        return youTubeTrailer;
    }
}
