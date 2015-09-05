package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;

import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.misc.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void testGetMovies() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        createMovieAndSave(context, expectedMovieId);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 1);
        assertTrue(targetMovies.get(0).getId() == expectedMovieId);
    }

    public void testGetMovieById() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        createMovieAndSave(context, expectedMovieId);

        Movie targetMovie = DataManager.getMovieById(context, expectedMovieId);
        assertNotNull(targetMovie);
        assertTrue(targetMovie.getId() == expectedMovieId);
    }

    public void testSaveMovies() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();

        List<Movie> noMovies = DataManager.getMovies(context);
        assertNull(noMovies);

        createMovieAndSave(context, expectedMovieId);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 1);
        assertTrue(targetMovies.get(0).getId() == expectedMovieId);
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
        DataManager.addMovies(context, dummyMovies2, 0, Movie.MOVIE_LIST_TYPE_POPULARITY);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 2);
        assertTrue(targetMovies.get(1).getId() == movieId2);
    }

    public void testGetFavoriteMovieMap() throws Exception {
        Context context = getContext();
        int expectedMovieId = Utils.getUniqueIntId();
        createMovieAndSave(context, expectedMovieId);

        List<Movie> targetMovies = DataManager.getMovies(context);
        assertNotNull(targetMovies);
        assertTrue(targetMovies.size() == 1);
        assertTrue(targetMovies.get(0).getId() == expectedMovieId);
    }

    public void testGetFavoriteMovieList() throws Exception {

    }

    public void testSaveFavoriteMovieMap() throws Exception {

    }

    public void testAddFavoriteMovie() throws Exception {

    }

    public void testRemoveFavoriteMovie() throws Exception {

    }

    public void testGetFavoriteMovieById() throws Exception {

    }

    private void createMovieAndSave(Context context, int expectedMovieId) {
        List<Movie> movies = new ArrayList<>();
        Movie movie = createMovie(expectedMovieId);
        movies.add(movie);
        DataManager.saveMovies(context, movies, 0, Movie.MOVIE_LIST_TYPE_POPULARITY);
    }

    private void createMovieAndSaveFavorite(Context context, int expectedMovieId) {
        Map<Integer, Movie> movies = new HashMap<>();
        Movie movie = createMovie(expectedMovieId);
        movies.put(movie.getId(), movie);
        DataManager.saveFavoriteMovieMap(context, movies);
    }

    @NonNull
    private Movie createMovie(int expectedMovieId) {
        Movie movie = new Movie();
        movie.setId(expectedMovieId);
        return movie;
    }
}