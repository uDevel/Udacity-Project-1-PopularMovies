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
/*    public static boolean saveGames(Context context, List<Game> games) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(games);
        return AppPreferences.saveGamesJsonStr(context, jsonStr);
    }

    public static boolean saveGame(Context context, Game game) {
        List<Game> games = getGames(context);
        if (games == null) {
            games = new ArrayList<>();
        }

        // Replace with early exit
        for (int i = 0; i < games.size(); i++) {
            Game tempGame = games.get(i);
            if (tempGame.getId().equals(game.getId())) {
                games.set(i, game);
                return saveGames(context, games);
            }
        }

        // Add
        games.add(game);
        return saveGames(context, games);
    }*/

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

    public static boolean saveMovies(Context context, List<Movie> movies, int page) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(movies);
        return AppPreferences.saveMoviesJsonStr(context, jsonStr) && AppPreferences.saveMoviePage(context, page);
    }

    public static boolean addMovies(Context context, List<Movie> movies, int page) {
        List<Movie> origMovies = getMovies(context);

        if (origMovies == null) {
            origMovies = movies;
        } else {
            SparseArray<Integer> existingMovieIdIndex = new SparseArray<>(origMovies.size());
            for (int i = 0; i < origMovies.size(); i++) {
                existingMovieIdIndex.put(origMovies.get(i).getId(), i);
            }

            // Check if exist.
            for (Movie movie : movies) {
                Integer index = existingMovieIdIndex.get(movie.getId());

                // Already exist, remove old one.
                if (index != null) {
                    existingMovieIdIndex.remove(movie.getId());
                    origMovies.remove(index.intValue());
                }
            }
            origMovies.addAll(movies);
        }

        return saveMovies(context, origMovies, page);
    }

}
