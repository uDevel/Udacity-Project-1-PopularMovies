package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udevel.popularmovies.data.local.entity.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Movie> getFavouriteMovies(Context context) {
        List<Movie> movies = null;
        String MoviesJsonStr = AppPreferences.getFavouriteMoviesJsonStr(context);
        if (MoviesJsonStr != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Movie>>() {
            }.getType();
            movies = gson.fromJson(MoviesJsonStr, collectionType);
        }

        return movies;
    }

    private static List<Movie> saveFavouriteMovies(Context context, List<Movie> movies) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(movies);
        AppPreferences.setFavouriteMoviesJsonStr(context, jsonStr);
        return movies;
    }

    public static void addFavouriteMovie(Context context, Movie movie) {
        List<Movie> favouriteMovies = getFavouriteMovies(context);

        if (favouriteMovies == null) {
            favouriteMovies = new ArrayList<>();
        }

        favouriteMovies.add(movie);
        saveFavouriteMovies(context, favouriteMovies);
    }

    public static void removeFavouriteMovie(Context context, Movie movie) {
        List<Movie> favouriteMovies = getFavouriteMovies(context);

        if (favouriteMovies != null) {
            for (Movie favouriteMovie : favouriteMovies) {
                if (favouriteMovie.getId() == movie.getId()) {
                    favouriteMovies.remove(favouriteMovie);
                    saveFavouriteMovies(context, favouriteMovies);
                    return;
                }
            }
        }
    }
}
