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
import java.util.Random;

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
        assertTrue(compareMovie(expectedMovie, targetMovies.get(0)));
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
        assertTrue(compareMovie(expectedMovie, targetMovies.get(0)));
    }

    public void testGetMovieById() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        Movie expectedMovie = createMovieAndSave(context, expectedMovieId);

        Movie targetMovie = DataManager.getMovieById(context, expectedMovieId);
        assertNotNull(targetMovie);
        assertTrue(targetMovie.getMovieId() == expectedMovieId);
        assertTrue(compareMovie(expectedMovie, targetMovie));
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
        assertTrue(compareMovie(expectedMovie, targetMovie));
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
        assertTrue(compareMovie(expectedMovie, targetMovie));

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
        assertTrue(compareMovie(expectedMovie, targetMovie));

        DataManager.removeFavoriteMovie(context, targetMovie);
        targetMovieList = DataManager.getFavoriteMovieList(context);
        assertTrue(targetMovieList.size() == 0);

        List<Review> reviewsByMovieId = DataManager.getReviewsByMovieId(context, targetMovie.getMovieId());
        assertTrue(reviewsByMovieId.size() == 0);

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
        assertTrue(compareMovie(expectedMovie, targetMovie));

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
        Random random = new Random();
        Movie movie = new Movie();
        movie.setMovieId(expectedMovieId);
        movie.setVoteCount(random.nextInt());
        movie.setVoteAverage(random.nextDouble());
        movie.setPosterPath(String.valueOf(random.nextInt()));
        movie.setBackdropPath(String.valueOf(random.nextInt()));
        movie.setOriginalTitle(String.valueOf(random.nextInt()));
        movie.setOverview(String.valueOf(random.nextInt()));
        movie.setReleaseDate(String.valueOf(random.nextInt()));
        movie.setPopularity(random.nextDouble());

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

    private boolean compareMovie(Movie movie1, Movie movie2) {
        return movie1.equals(movie2);
    }
}