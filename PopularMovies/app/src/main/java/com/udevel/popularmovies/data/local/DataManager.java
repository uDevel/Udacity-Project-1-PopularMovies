package com.udevel.popularmovies.data.local;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udevel.popularmovies.data.local.entity.Movie;

import java.lang.reflect.Type;
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

    public static List<Movie> saveMovies(Context context, List<Movie> movies, int page) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(movies);
        return AppPreferences.saveMoviesJsonStr(context, jsonStr) && AppPreferences.saveMoviePage(context, page) ? movies : null;
    }

    public static List<Movie> addMovies(Context context, List<Movie> movies, int page) {
        List<Movie> origMovies = getMovies(context);

        if (origMovies == null) {
            origMovies = movies;
        } else {
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
        }

        return saveMovies(context, origMovies, page);
    }

}
