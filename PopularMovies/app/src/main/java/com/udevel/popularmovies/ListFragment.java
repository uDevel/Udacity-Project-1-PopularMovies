package com.udevel.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udevel.popularmovies.adapter.MovieAdapter;
import com.udevel.popularmovies.data.local.AppPreferences;
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.network.NetworkApi;
import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    private static final String BUNDLE_KEY_IS_SORT_BY_POPULARITY = "BUNDLE_KEY_IS_SORT_BY_POPULARITY";
    private static final int NUM_LAST_ITEM_BEFORE_LOADING = 6;
    private RecyclerView rv_popular_movies;
    private boolean isSortByPopularity = true;
    private AtomicBoolean loadingFromNetwork = new AtomicBoolean(false);
    private MovieAdapter movieAdapter;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setupViews(inflater, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupRecyclerView();

        // Only auto fetch when new.
        if (savedInstanceState != null) {
            isSortByPopularity = savedInstanceState.getBoolean(BUNDLE_KEY_IS_SORT_BY_POPULARITY);
            List<Movie> movies = DataManager.getMovies(getActivity());
            if (movies == null || movies.isEmpty()) {
                getMovieListFromNetwork(true);
            } else {
                updateRecyclerView(movies, true);
            }
        } else {
            getMovieListFromNetwork(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_KEY_IS_SORT_BY_POPULARITY, isSortByPopularity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_movie_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
         /*   case R.id.action_settings:
                // Not implemented here
                return false;*/
            case R.id.action_sort_by_popularity:
                isSortByPopularity = true;
                getMovieListFromNetwork(true);
                return true;
            case R.id.action_sort_by_rating:
                isSortByPopularity = false;
                getMovieListFromNetwork(true);
                return true;
            default:
                break;
        }

        return false;
    }

    private void getMovieListFromNetwork(boolean isRefresh) {
        if (loadingFromNetwork.compareAndSet(false, true)) {
            Log.d("ListFragment", "getMovieListFromNetwork");
            final int currentPage = isRefresh ? 1 : AppPreferences.getMoviePage(getActivity()) + 1;
            if (isSortByPopularity) {
                NetworkApi.getMoviesByPopularity(currentPage, new Callback<DiscoverMovieResult>() {
                    @Override
                    public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                        if (response.getStatus() == 200 && discoverMovieResult != null) {
                            List<Movie> movies = updateData(discoverMovieResult, currentPage);
                            updateRecyclerView(movies, currentPage == 1);
                        } else {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Unable to get movie list -" + response.getReason(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        loadingFromNetwork.set(false);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        loadingFromNetwork.set(false);
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Unable to get movie list -" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                NetworkApi.getMoviesByRating(currentPage, new Callback<DiscoverMovieResult>() {
                    @Override
                    public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                        if (response.getStatus() == 200 && discoverMovieResult != null) {
                            List<Movie> movies = updateData(discoverMovieResult, currentPage);
                            updateRecyclerView(movies, currentPage == 1);
                        } else {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Unable to get movie list -" + response.getReason(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        loadingFromNetwork.set(false);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        loadingFromNetwork.set(false);
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Unable to get movie list -" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    private void setupRecyclerView() {
        int currentOrientation = getResources().getConfiguration().orientation;
        int spanCount = currentOrientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3;

        rv_popular_movies.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        rv_popular_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager gridLayoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
                if (gridLayoutManager.getItemCount() > 0 && gridLayoutManager.findLastVisibleItemPosition() >= gridLayoutManager.getItemCount() - NUM_LAST_ITEM_BEFORE_LOADING) {
                    getMovieListFromNetwork(false);
                }
            }
        });

    }

    private List<Movie> updateData(DiscoverMovieResult discoverMovieResult, int currentPage) {
        if (getActivity() == null) {
            return null;
        }
        List<Movie> movies = Movie.convertDiscoverMovieInfoResults(discoverMovieResult.getResults());
        if (currentPage == 1) {
            DataManager.saveMovies(getActivity(), movies, 1);
        } else {
            DataManager.addMovies(getActivity(), movies, currentPage);
        }

        return movies;
    }

    private void updateRecyclerView(List<Movie> movies, boolean isRefresh) {
        if (isRefresh || movieAdapter == null) {
            movieAdapter = new MovieAdapter(movies);
            rv_popular_movies.swapAdapter(movieAdapter, true);

        } else {
            movieAdapter.addMovies(movies);
            movieAdapter.notifyDataSetChanged();
        }

    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        rv_popular_movies = ((RecyclerView) root.findViewById(R.id.rv_popular_movies));

        return root;
    }
}
