package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udevel.popularmovies.data.local.entity.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benny on 5/16/2015.
 */
public class DataManager {
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
                if (movie.getId() == targetId) {
                    return movie;
                }
            }
        }
        return null;
    }

    public static List<Movie> saveMovies(Context context, List<Movie> movies, int page, int movieListType) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(movies);
        AppPreferences.setMoviesJsonStr(context, jsonStr);
        AppPreferences.setMoviePage(context, page);
        AppPreferences.setLastMovieListType(context, movieListType);
        return movies;
    }

    public static List<Movie> addMovies(Context context, List<Movie> movies, int page, int movieListType) {
        List<Movie> origMovies = getMovies(context);

        if (movies == null) {
            return origMovies;
        }

        if (origMovies == null) {
            return movies;
        }
        SparseArray<Integer> existingMovieIdIndex = new SparseArray<>(origMovies.size());
        for (int i = 0; i < origMovies.size(); i++) {
            existingMovieIdIndex.put(origMovies.get(i).getId(), i);
        }

        // Check if exist.
        for (int i = movies.size() - 1; i >= 0; i--) {
            Movie movie = movies.get(i);
            Integer index = existingMovieIdIndex.get(movie.getId());
            if (index != null) {
                movies.remove(i);
            }
        }

        origMovies.addAll(movies);

        return saveMovies(context, origMovies, page, movieListType);
    }

    public static Map<Integer, Movie> getFavoriteMovieMap(Context context) {
        Map<Integer, Movie> movieMap = null;
        String MoviesJsonStr = AppPreferences.getFavoriteMoviesJsonStr(context);
        if (MoviesJsonStr != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Map<Integer, Movie>>() {
            }.getType();
            movieMap = gson.fromJson(MoviesJsonStr, collectionType);
        }

        return movieMap;
    }

    public static List<Movie> getFavoriteMovieList(Context context) {
        Map<Integer, Movie> favoriteMovies = DataManager.getFavoriteMovieMap(context);
        if (favoriteMovies != null) {
            return new ArrayList<>(favoriteMovies.values());
        } else {
            return null;
        }
    }

    public static Map<Integer, Movie> saveFavoriteMovieMap(Context context, Map<Integer, Movie> movieMap) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(movieMap);
        AppPreferences.setFavoriteMoviesJsonStr(context, jsonStr);
        return movieMap;
    }

    public static void addFavoriteMovie(Context context, Movie movie) {
        Map<Integer, Movie> favoriteMovies = getFavoriteMovieMap(context);

        if (favoriteMovies == null) {
            favoriteMovies = new HashMap<>();
        }

        favoriteMovies.put(movie.getId(), movie);
        saveFavoriteMovieMap(context, favoriteMovies);
    }

    public static void removeFavoriteMovie(Context context, Movie movie) {
        if (movie == null) {
            return;
        }

        Map<Integer, Movie> favoriteMovies = getFavoriteMovieMap(context);

        if (favoriteMovies != null) {
            if (favoriteMovies.containsKey(movie.getId())) {
                favoriteMovies.remove(movie.getId());
                saveFavoriteMovieMap(context, favoriteMovies);
            }
        }
    }

    public static Movie getFavoriteMovieById(Context context, int targetId) {
        Map<Integer, Movie> favoriteMovies = getFavoriteMovieMap(context);
        if (favoriteMovies != null) {
            return favoriteMovies.get(targetId);
        }
        return null;
    }
}
