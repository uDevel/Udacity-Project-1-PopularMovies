package com.udevel.popularmovies.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.ViewTreeObserver;
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
public class ListFragment extends Fragment implements OnMovieAdapterItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ListFragment.class.getSimpleName();
    private static final String BUNDLE_KEY_IS_SORT_BY_POPULARITY = "BUNDLE_KEY_IS_SORT_BY_POPULARITY";
    private static final int LIST_POSITION_TO_MOVE_FAB = 20;
    private static final int LIST_POSITION_TO_HIDE_FAB = 17;
    private static final int LIST_POSITION_JUMP_TO_FOR_GO_TO_TOP = 80;
    private static final int NUM_LAST_ITEM_BEFORE_LOADING = 10;
    private static final int MAX_PAGE_CACHE = 500;
    private static final int NUM_COLUMNS_IN_LANDSCAPE = 5;
    private static final int NUM_COLUMNS_IN_PORTRAIT = 3;
    private final AtomicBoolean loadingFromNetwork = new AtomicBoolean(false);
    private boolean isSortByPopularity = true;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private SaveMovieDataTask saveMovieDataTask;
    private MovieAdapter movieAdapter;
    private Toolbar tb_popular_movies;
    private RecyclerView rv_popular_movies;
    private View root;
    private SwipeRefreshLayout srl_popular_movies;
    private FloatingActionButton fab_go_to_top;
    private TextView tv_empty_view_error;
    private View cl_root;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private static List<Movie> updateData(Context context, DiscoverMovieResult discoverMovieResult, int currentPage) {
        List<Movie> movies = Movie.convertDiscoverMovieInfoResults(discoverMovieResult.getResults());
        if (currentPage == 1) {
            return DataManager.saveMovies(context, movies, 1);
        } else {
            return DataManager.addMovies(context, movies, currentPage);
        }
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
            }

            List<Movie> movies = DataManager.getMovies(getActivity());
            if (movies == null || movies.isEmpty()) {
                getMovieListFromNetwork(true);
            } else {
                updateRecyclerView(movies);
                checkIfNewerUpdate();
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
    public void onStop() {
        super.onStop();
        if (saveMovieDataTask != null) {
            saveMovieDataTask.cancel(false);
        }
        loadingFromNetwork.set(false);
        srl_popular_movies.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        root = null;
        tb_popular_movies = null;
        rv_popular_movies = null;
        srl_popular_movies = null;
        fab_go_to_top = null;
        tv_empty_view_error = null;
        movieAdapter = null;
        super.onDestroyView();
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

    @Override
    public void onRefresh() {
        getMovieListFromNetwork(true);
    }

    private void checkIfNewerUpdate() {
        if (isSortByPopularity) {
            NetworkApi.getMoviesByPopularity(1, new Callback<DiscoverMovieResult>() {
                @Override
                public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                    if (!isAdded() || getActivity() == null) {
                        return;
                    }

                    if (response.getStatus() == 200 && discoverMovieResult != null) {
                        List<Movie> newFetchedMovies = Movie.convertDiscoverMovieInfoResults(discoverMovieResult.getResults());
                        List<Movie> existingMovies = DataManager.getMovies(getActivity());
                        if (newFetchedMovies != null && existingMovies != null) {
                            if (newFetchedMovies.size() > existingMovies.size()) {
                                showSnackBar();
                            } else {
                                for (int i = 0; i < newFetchedMovies.size(); i++) {
                                    if (newFetchedMovies.get(i).getId() != existingMovies.get(i).getId()) {
                                        showSnackBar();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    // Do show error for this silent check.
                }
            });
        } else {
            NetworkApi.getMoviesByRating(1, new Callback<DiscoverMovieResult>() {
                @Override
                public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                    if (response.getStatus() == 200 && discoverMovieResult != null) {
                        List<Movie> newFetchedMovies = Movie.convertDiscoverMovieInfoResults(discoverMovieResult.getResults());
                        List<Movie> existingMovies = DataManager.getMovies(getActivity());
                        if (newFetchedMovies != null && existingMovies != null) {
                            if (newFetchedMovies.size() > existingMovies.size()) {
                                showSnackBar();
                            } else {
                                for (int i = 0; i < newFetchedMovies.size(); i++) {
                                    if (newFetchedMovies.get(i).getId() != existingMovies.get(i).getId()) {
                                        showSnackBar();
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    // Do show error for this silent check.
                }
            });
        }
    }

    private void showSnackBar() {
        Snackbar.make(cl_root, R.string.msg_movies_update_available, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        srl_popular_movies.setRefreshing(true);
                        onRefresh();
                    }
                })
                .show(); // Don’t forget to show!
    }

    private void setupRecyclerView() {
        int currentOrientation = getResources().getConfiguration().orientation;
        final int spanCount = currentOrientation == Configuration.ORIENTATION_LANDSCAPE ? NUM_COLUMNS_IN_LANDSCAPE : NUM_COLUMNS_IN_PORTRAIT;

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        rv_popular_movies.setLayoutManager(layoutManager);
        rv_popular_movies.clearOnScrollListeners();
        rv_popular_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.getItemCount() > 0) {
                    if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - NUM_LAST_ITEM_BEFORE_LOADING) {
                        getMovieListFromNetwork(false);
                    }

                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    if (layoutManager.findFirstVisibleItemPosition() > LIST_POSITION_TO_MOVE_FAB) {
                        if (dy < 0) {
                            fab_go_to_top.setTranslationY(Math.max(0, fab_go_to_top.getTranslationY() + ((float) dy)));
                        } else {
                            fab_go_to_top.setY(Math.min(((View) fab_go_to_top.getParent()).getHeight(), fab_go_to_top.getY() + ((float) dy)));
                        }
                    } else if (firstVisibleItemPosition < LIST_POSITION_TO_HIDE_FAB) {
                        fab_go_to_top.setY(((View) fab_go_to_top.getParent()).getHeight());
                    }
                }
            }
        });

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (movieAdapter != null) {
                    switch (movieAdapter.getItemViewType(position)) {
                        case MovieAdapter.VIEW_TYPE_FOOTER:
                            return spanCount;
                        case MovieAdapter.VIEW_TYPE_MOVIE:
                        default:
                            return 1;
                    }
                }
                return 1;
            }
        });
    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        cl_root = root.findViewById(R.id.cl_root);
        rv_popular_movies = ((RecyclerView) root.findViewById(R.id.rv_popular_movies));
        tb_popular_movies = ((Toolbar) root.findViewById(R.id.tb_popular_movies));
        tv_empty_view_error = ((TextView) root.findViewById(R.id.tv_empty_view_error));
        srl_popular_movies = ((SwipeRefreshLayout) root.findViewById(R.id.srl_popular_movies));
        fab_go_to_top = ((FloatingActionButton) root.findViewById(R.id.fab_go_to_top));

        setupFab();
        setupSwipeRefreshLayout();
        setupToolbar();
        return root;
    }

    private void setupFab() {
        fab_go_to_top.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fab_go_to_top.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                fab_go_to_top.setY(((View) fab_go_to_top.getParent()).getHeight());
            }
        });
        fab_go_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv_popular_movies != null) {
                    if (((GridLayoutManager) rv_popular_movies.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > LIST_POSITION_JUMP_TO_FOR_GO_TO_TOP) {
                        rv_popular_movies.scrollToPosition(LIST_POSITION_JUMP_TO_FOR_GO_TO_TOP);
                    }
                    rv_popular_movies.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void setupSwipeRefreshLayout() {
        srl_popular_movies.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2);
        srl_popular_movies.setOnRefreshListener(this);
    }

    private void setupToolbar() {
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
        lp.gravity = Gravity.END;
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

    private void getMovieListFromNetwork(boolean isRefresh) {
        if (loadingFromNetwork.compareAndSet(false, true)) {
            final int currentPage = isRefresh ? 1 : AppPreferences.getMoviePage(getActivity()) + 1;

            // This is to avoid over limit of String characters
            if (currentPage > MAX_PAGE_CACHE) {
                return;
            }

            if (isSortByPopularity) {
                NetworkApi.getMoviesByPopularity(currentPage, new Callback<DiscoverMovieResult>() {
                    @Override
                    public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                        processNetworkSuccessResult(discoverMovieResult, response, currentPage);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        processNetworkFailureResult(error);
                    }
                });
            } else {
                NetworkApi.getMoviesByRating(currentPage, new Callback<DiscoverMovieResult>() {
                    @Override
                    public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                        processNetworkSuccessResult(discoverMovieResult, response, currentPage);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        processNetworkFailureResult(error);
                    }
                });
            }
        } else {
            srl_popular_movies.setRefreshing(false);
        }

    }

    private void processNetworkFailureResult(RetrofitError error) {
        loadingFromNetwork.set(false);
        srl_popular_movies.setRefreshing(false);

        if (getActivity() != null) {
            Log.e(TAG, error.getMessage());
            Toast.makeText(getActivity(), getString(R.string.msg_error_data_connection_error), Toast.LENGTH_LONG).show();
            tv_empty_view_error.setVisibility(View.VISIBLE);
        }
    }

    private void processNetworkSuccessResult(DiscoverMovieResult discoverMovieResult, Response response, int currentPage) {
        if (!isAdded() || getActivity() == null) {
            return;
        }

        if (response.getStatus() == 200 && discoverMovieResult != null) {
            saveMovieDataTask = new SaveMovieDataTask(getActivity(), currentPage);
            saveMovieDataTask.execute(discoverMovieResult);
        } else {
            loadingFromNetwork.set(false);
            srl_popular_movies.setRefreshing(false);
            Log.e(TAG, response.getReason());
            Toast.makeText(getActivity(), getString(R.string.msg_error_data_connection_error), Toast.LENGTH_SHORT).show();
            tv_empty_view_error.setVisibility(View.VISIBLE);
        }

    }

    private void updateRecyclerView(List<Movie> movies) {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(movies, this);
            rv_popular_movies.swapAdapter(movieAdapter, true);
            movieAdapter.setOnMovieAdapterItemClickListener(this);
        } else {
            movieAdapter.updateMovies(movies);
        }
    }

    private class SaveMovieDataTask extends AsyncTask<DiscoverMovieResult, Void, List<Movie>> {

        private final Context context;
        private final int currentPage;

        public SaveMovieDataTask(Context context, int currentPage) {
            this.context = context.getApplicationContext();
            this.currentPage = currentPage;
        }

        protected List<Movie> doInBackground(DiscoverMovieResult... results) {
            if (results.length == 1) {
                return updateData(context, results[0], currentPage);
            }

            return null;
        }

        protected void onPostExecute(List<Movie> movies) {
            if (!isAdded()) {
                return;
            }

            if (movies == null) {
                Toast.makeText(context, "Unable to save data to local storage.", Toast.LENGTH_SHORT).show();
            } else {
                updateRecyclerView(movies);
            }

            loadingFromNetwork.set(false);
            srl_popular_movies.setRefreshing(false);
            saveMovieDataTask = null;
            tv_empty_view_error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadingFromNetwork.set(false);
            srl_popular_movies.setRefreshing(false);
        }
    }

}
