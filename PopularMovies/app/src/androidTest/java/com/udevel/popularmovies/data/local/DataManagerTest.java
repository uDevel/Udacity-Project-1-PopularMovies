package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;

import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.local.entity.Review;
import com.udevel.popularmovies.data.local.entity.YouTubeTrailer;
import com.udevel.popularmovies.misc.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 9/5/2015.
 */
public class DataManagerTest extends AndroidTestCase {

    public void setUp() throws Exception {
        super.setUp();
        DataManager.clearData(getContext());
    }

    public void tearDown() throws Exception {

    }

    public void testSaveMovies() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();

        List<Movie> noMovies = DataManager.getMovies(context);
        assertNull(noMovies);

        Movie expectedMovie = createMovieAndSave(context, expectedMovieId);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 1);
        assertTrue(targetMovies.get(0).getMovieId() == expectedMovieId);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovies.get(0)));
    }

    public void testAddMovies() throws Exception {
        Context context = getContext();
        int movieId1 = Utils.getUniqueIntId();
        int movieId2 = Utils.getUniqueIntId();

        createMovieAndSave(context, movieId1);
        List<Movie> dummyMovies = DataManager.getMovies(context);
        assertNotNull(dummyMovies);
        assertTrue(dummyMovies.size() == 1);

        List<Movie> dummyMovies2 = new ArrayList<>();
        dummyMovies2.add(createMovie(movieId2));
        DataManager.addMovies(context, dummyMovies2, 0);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 2);
        assertTrue(targetMovies.get(1).getMovieId() == movieId2);
    }

    public void testGetMovies() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        Movie expectedMovie = createMovieAndSave(context, expectedMovieId);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 1);
        assertTrue(targetMovies.get(0).getMovieId() == expectedMovieId);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovies.get(0)));
    }

    public void testGetMovieById() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        Movie expectedMovie = createMovieAndSave(context, expectedMovieId);

        Movie targetMovie = DataManager.getMovieById(context, expectedMovieId);
        assertNotNull(targetMovie);
        assertTrue(targetMovie.getMovieId() == expectedMovieId);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovie));
    }

    public void testGetFavoriteMovieList() throws Exception {
        Context context = getContext();
        final int expectedMovieId = Utils.getUniqueIntId();
        Movie expectedMovie = createMovie(expectedMovieId);

        List<Movie> targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 0);

        Long rowId = DataManager.addFavoriteMovie(context, expectedMovie);
        assertNotNull(rowId);

        targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 1);

        Movie targetMovie = targetMovieList.get(0);
        assertNotNull(targetMovie);
        assertTrue(targetMovie.getMovieId() == expectedMovieId);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovie));
    }

    public void testAddFavoriteMovieReviewTrailer() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        final int numOfReviews = 5;
        final int numOfYouTubeTrailers = 4;

        List<Movie> targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 0);

        Movie expectedMovie = createMovie(expectedMovieId);
        List<Review> reviews = createReviews(numOfReviews);
        List<YouTubeTrailer> youTubeTrailers = createYouTubeTrailers(numOfYouTubeTrailers);

        DataManager.addFavoriteMovieReviewTrailer(context, expectedMovie, reviews, youTubeTrailers);

        Movie targetMovie = DataManager.getFavoriteMovieById(context, expectedMovieId);
        assertNotNull(targetMovie);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovie));

        List<Review> reviewsByMovieId = DataManager.getReviewsByMovieId(context, targetMovie.getMovieId());
        assertNotNull(reviewsByMovieId);
        assertTrue(reviewsByMovieId.size() == numOfReviews);

        List<YouTubeTrailer> youTubeTrailerByMovieId = DataManager.getYouTubeTrailerByMovieId(context, targetMovie.getMovieId());
        assertNotNull(youTubeTrailerByMovieId);
        assertTrue(youTubeTrailerByMovieId.size() == numOfYouTubeTrailers);
    }

    public void testRemoveFavoriteMovie() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        final int numOfReviews = 5;
        final int numOfYouTubeTrailers = 4;

        List<Movie> targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 0);

        Movie expectedMovie = createMovie(expectedMovieId);
        List<Review> reviews = createReviews(numOfReviews);
        List<YouTubeTrailer> youTubeTrailers = createYouTubeTrailers(numOfYouTubeTrailers);

        DataManager.addFavoriteMovieReviewTrailer(context, expectedMovie, reviews, youTubeTrailers);

        Movie targetMovie = DataManager.getFavoriteMovieById(context, expectedMovieId);
        assertNotNull(targetMovie);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovie));

        DataManager.removeFavoriteMovie(context, targetMovie);
        targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 0);

        List<Review> reviewsByMovieId = DataManager.getReviewsByMovieId(context, targetMovie.getMovieId());
        assertTrue(reviewsByMovieId == null || reviewsByMovieId.size() == 0);

        List<YouTubeTrailer> youTubeTrailerByMovieId = DataManager.getYouTubeTrailerByMovieId(context, targetMovie.getMovieId());
        assertNotNull(youTubeTrailerByMovieId);
        assertTrue(youTubeTrailerByMovieId.size() == 0);
    }

    public void testGetFavoriteMovieById() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();

        List<Movie> targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 0);

        Movie expectedMovie = createMovie(expectedMovieId);
        DataManager.addFavoriteMovie(context, expectedMovie);

        Movie targetMovie = DataManager.getFavoriteMovieById(context, expectedMovieId);
        assertNotNull(targetMovie);
        assertTrue(targetMovie.getMovieId() == expectedMovieId);
        assertTrue(fuzzyCompareMovie(expectedMovie, targetMovie));

    }

    @NonNull
    private Movie createMovieAndSave(Context context, int expectedMovieId) {
        List<Movie> movies = new ArrayList<>();
        Movie movie = createMovie(expectedMovieId);
        movies.add(movie);
        DataManager.saveMovies(context, movies, 0);
        return movie;
    }

    @NonNull
    private Movie createMovie(int expectedMovieId) {
        Movie movie = new Movie();
        movie.setMovieId(expectedMovieId);

        return movie;
    }

    @NonNull
    private YouTubeTrailer createYouTubeTrailer() {
        YouTubeTrailer youTubeTrailer = new YouTubeTrailer();
        youTubeTrailer.setYouTubeTrailerId(Utils.getUniqueStringId());
        return youTubeTrailer;
    }

    @NonNull
    private List<YouTubeTrailer> createYouTubeTrailers(int numOfYouTubeTrailer) {
        List<YouTubeTrailer> youTubeTrailers = new ArrayList<>();
        for (int i = 0; i < numOfYouTubeTrailer; i++) {
            youTubeTrailers.add(createYouTubeTrailer());
        }
        return youTubeTrailers;
    }

    @NonNull
    private Review createReview() {
        Review review = new Review();
        review.setReviewId(Utils.getUniqueStringId());
        return review;
    }

    @NonNull
    private List<Review> createReviews(int numOfReviews) {
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < numOfReviews; i++) {
            reviews.add(createReview());
        }
        return reviews;
    }

    private boolean fuzzyCompareMovie(Movie movie1, Movie movie2) {
        if (movie1 == null) {
            return movie2 == null;
        } else if (movie2 == null) {
            return false;
        }

        return movie1.getMovieId() == movie2.getMovieId();
        /*
        if (movie1.getMovieId() == movie2.getMovieId()) {
            List<YouTubeTrailer> youTubeTrailers1 = movie1.getYouTubeTrailers();
            List<YouTubeTrailer> youTubeTrailers2 = movie2.getYouTubeTrailers();
            if (youTubeTrailers1 != null && youTubeTrailers2 != null) {
                if (youTubeTrailers1.size() != youTubeTrailers2.size()) {
                    return false;
                }

                for (int i = 0; i < youTubeTrailers1.size(); i++) {
                    if (!youTubeTrailers1.get(i).getYouTubeTrailerId().equals(youTubeTrailers2.get(i).getYouTubeTrailerId())) {
                        return false;
                    }
                }
            } else if (youTubeTrailers1 != youTubeTrailers2) {
                return false;
            }

            List<Review> reviews1 = movie1.getReviews();
            List<Review> reviews2 = movie2.getReviews();
            if (reviews1 != null && reviews2 != null) {
                if (reviews1.size() != reviews2.size()) {
                    return false;
                }

                for (int i = 0; i < reviews1.size(); i++) {
                    if (!reviews1.get(i).getReviewId().equals(reviews2.get(i).getReviewId())) {
                        return false;
                    }
                }
            } else if (reviews1 != reviews2) {
                return false;
            }
        }

        return true;*/

    }
}