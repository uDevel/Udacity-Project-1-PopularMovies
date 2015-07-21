package com.udevel.popularmovies.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.MovieAdapter;
import com.udevel.popularmovies.adapter.SpinnerAdapter;
import com.udevel.popularmovies.adapter.listener.OnMovieAdapterItemClickListener;
import com.udevel.popularmovies.data.local.AppPreferences;
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.network.NetworkApi;
import com.udevel.popularmovies.data.network.api.DiscoverMovieResult;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment implements OnMovieAdapterItemClickListener {
    private static final String TAG = ListFragment.class.getSimpleName();
    private static final String BUNDLE_KEY_IS_SORT_BY_POPULARITY = "BUNDLE_KEY_IS_SORT_BY_POPULARITY";
    private static final int NUM_LAST_ITEM_BEFORE_LOADING = 10;
    private static final int MAX_PAGE_CACHE = 500;
    private static final int NUM_COLUMNS_IN_LANDSCAPE = 5;
    private static final int NUM_COLUMNS_IN_PORTRAIT = 3;
    private final AtomicBoolean loadingFromNetwork = new AtomicBoolean(false);
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private boolean isSortByPopularity = true;
    private MovieAdapter movieAdapter;
    private Toolbar tb_popular_movies;
    private RecyclerView rv_popular_movies;
    private View root;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onFragmentInteractionListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = setupViews(inflater, container);
            // Only auto fetch when new.
            if (savedInstanceState != null) {
                isSortByPopularity = savedInstanceState.getBoolean(BUNDLE_KEY_IS_SORT_BY_POPULARITY);
                List<Movie> movies = DataManager.getMovies(getActivity());
                if (movies == null || movies.isEmpty()) {
                    getMovieListFromNetwork(true);
                } else {
                    updateRecyclerView(movies);
                }
            } else {
                getMovieListFromNetwork(true);
            }
        }
        setupRecyclerView();
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_KEY_IS_SORT_BY_POPULARITY, isSortByPopularity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public void onMovieAdapterItemClick(View v, int movieId) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_OPEN_MOVIE_DETAIL, movieId);
        }
    }

    private void getMovieListFromNetwork(boolean isRefresh) {
        if (loadingFromNetwork.compareAndSet(false, true)) {
            Log.d(TAG, "getMovieListFromNetwork");
            final int currentPage = isRefresh ? 1 : AppPreferences.getMoviePage(getActivity()) + 1;

            // This is to avoid over limit of String characters
            if (currentPage > MAX_PAGE_CACHE) {
                return;
            }

            if (isSortByPopularity) {
                NetworkApi.getMoviesByPopularity(currentPage, new Callback<DiscoverMovieResult>() {
                    @Override
                    public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                        if (response.getStatus() == 200 && discoverMovieResult != null) {
                            List<Movie> movies = updateData(discoverMovieResult, currentPage);
                            updateRecyclerView(movies);
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
                            updateRecyclerView(movies);
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
        int spanCount = currentOrientation == Configuration.ORIENTATION_LANDSCAPE ? NUM_COLUMNS_IN_LANDSCAPE : NUM_COLUMNS_IN_PORTRAIT;

        rv_popular_movies.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        rv_popular_movies.clearOnScrollListeners();
        rv_popular_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager gridLayoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
                if (gridLayoutManager.getItemCount() > 0 && gridLayoutManager.findLastVisibleItemPosition() >= gridLayoutManager.getItemCount() - NUM_LAST_ITEM_BEFORE_LOADING) {
                    Log.d(TAG, "onScrolled getMovie");
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
            return DataManager.saveMovies(getActivity(), movies, 1);
        } else {
            return DataManager.addMovies(getActivity(), movies, currentPage);
        }
    }

    private void updateRecyclerView(List<Movie> movies) {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(movies);
            rv_popular_movies.swapAdapter(movieAdapter, true);

        } else {
            movieAdapter.updateMovies(movies);
        }
        movieAdapter.setOnMovieAdapterItemClickListener(this);
    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        rv_popular_movies = ((RecyclerView) root.findViewById(R.id.rv_popular_movies));
        tb_popular_movies = ((Toolbar) root.findViewById(R.id.tb_popular_movies));

        setupToolbar(tb_popular_movies);
        return root;
    }

    private void setupToolbar(Toolbar tb_popular_movies) {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(tb_popular_movies);
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(getString(R.string.title_list_movies));
                supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

                setupSpinner(activity);
            }
        }
    }

    private void setupSpinner(FragmentActivity activity) {
        View spinnerContainer = LayoutInflater.from(activity).inflate(R.layout.spinner_actionbar, tb_popular_movies, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.RIGHT;
        tb_popular_movies.addView(spinnerContainer, lp);
        final String[] stringArray = activity.getResources().getStringArray(R.array.spinner_items_array);
        SpinnerAdapter adapter = new SpinnerAdapter(Arrays.asList(stringArray));
        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.sp_main);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view == null) {
                    return;
                }
                // Log.d(TAG, view.getClass().getSimpleName());
                String displayingText = ((TextView) view).getText().toString();
                if (displayingText.equals(stringArray[0]) && !isSortByPopularity) {
                    isSortByPopularity = true;
                    getMovieListFromNetwork(true);
                } else if (displayingText.equals(stringArray[1]) && isSortByPopularity) {
                    isSortByPopularity = false;
                    getMovieListFromNetwork(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
