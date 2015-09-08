package com.udevel.popularmovies.fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.MovieAdapter;
import com.udevel.popularmovies.adapter.SpinnerAdapter;
import com.udevel.popularmovies.adapter.listener.AdapterItemClickListener;
import com.udevel.popularmovies.data.local.AppPreferences;
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.local.provider.movie.MovieColumns;
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
public class ListFragment extends Fragment implements AdapterItemClickListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = ListFragment.class.getSimpleName();
    private static final int LIST_POSITION_TO_MOVE_FAB = 20;
    private static final int LIST_POSITION_TO_HIDE_FAB = 17;
    private static final int LIST_POSITION_JUMP_TO_FOR_GO_TO_TOP = 17;
    private static final int NUM_LAST_ITEM_BEFORE_LOADING = 19;
    private static final int MAX_PAGE_CACHE = 500;
    private static final int MINIMUM_VOTE_COUNT_FOR_SORT_BY_RATING = 200;  // This allows api to return meaningful data instead of 1-vote wonders.
    private final AtomicBoolean loadingFromNetwork = new AtomicBoolean(false);
    private Toolbar tb_popular_movies;
    private RecyclerView rv_popular_movies;
    private View root;
    private SwipeRefreshLayout srl_popular_movies;
    private FloatingActionButton fab_go_to_top;
    private TextView tv_empty_view;
    private View cl_root;
    private Snackbar refreshSnackbar;
    private Toast errorToast;
    private AppBarLayout abl_popular_movies;
    private FavoriteMoviesObserver favoriteMoviesObserver;

    private int movieListType;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private SaveMovieDataTask saveMovieDataTask;
    private MovieAdapter movieAdapter;
    private boolean hasToolbar;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private static List<Movie> updateData(Context context, DiscoverMovieResult discoverMovieResult, int currentPage, int movieListType) {
        List<Movie> movies = Movie.convertDiscoverMovieInfoResults(discoverMovieResult.getResults());
        if (currentPage == 0) {
            AppPreferences.setLastMovieListType(context, movieListType);
            return DataManager.saveMovies(context, movies, currentPage);
        } else {
            return DataManager.addMovies(context, movies, currentPage);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            Context context = getContext();
            movieListType = AppPreferences.getLastMovieListType(context);

            root = setupViews(inflater, container);
            List<Movie> movies;

            if (movieListType == Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE) {
                movies = DataManager.getFavoriteMovieList(context);
                registerFavoriteMovieListObserver();
            } else {
                movies = DataManager.getMovies(context);
            }

            if (movies == null || movies.isEmpty()) {
                getMovieList(true);
            } else {
                updateRecyclerView(movies);
                checkIfNewerUpdate();
            }
        }
        setupRecyclerView();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasToolbar) {
            abl_popular_movies.addOnOffsetChangedListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasToolbar) {
            abl_popular_movies.removeOnOffsetChangedListener(this);
        }
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
        movieListType = 0;
        fab_go_to_top = null;
        tv_empty_view = null;
        movieAdapter = null;
        unregisterFavoriteMovieListObserver();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public void adapterItemClick(String action, View v, Object data) {
        if (action.equals(AdapterItemClickListener.ACTION_OPEN_MOVIE_DETAIL) && data instanceof Integer) {
            if (onFragmentInteractionListener != null) {
                onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_OPEN_MOVIE_DETAIL, data);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (refreshSnackbar != null) {
            refreshSnackbar.dismiss();
            refreshSnackbar = null;
        }
        getMovieList(true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        srl_popular_movies.setEnabled(i == 0);
    }

    public void setMovieListType(int listType) {
        if (movieListType != listType) {
            movieListType = listType;
            AppPreferences.setLastMovieListType(getActivity(), movieListType);
            getMovieList(true);

            if (movieListType == Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE) {
                registerFavoriteMovieListObserver();
            } else {
                unregisterFavoriteMovieListObserver();
            }
        }
    }

    /***
     * This method is for activity to call, because users always mistakenly back out of the app when they are in favorite list.
     *
     * @return true if back press is absorbed by this method.  False if this fragment let the caller to this method to handle the back press action.
     */
    public boolean onBackPressed() {
        // User always mistakenly back out of the app when they are in favorite list, so we let fragment to decide if we really back out the app.
        if (movieListType == Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE) {
            if (root != null) {
                Spinner spinner = (Spinner) root.findViewById(R.id.sp_main);
                if (spinner != null) {
                    spinner.setSelection(Movie.MOVIE_LIST_TYPE_POPULARITY);
                    return true;
                }
            }
        }

        return false;
    }

    private void unregisterFavoriteMovieListObserver() {
        if (favoriteMoviesObserver != null) {
            Log.d(TAG, "*** unregistered");
            getContext().getContentResolver().unregisterContentObserver(favoriteMoviesObserver);
            favoriteMoviesObserver = null;
        }
    }

    private void registerFavoriteMovieListObserver() {
        if (favoriteMoviesObserver == null) {
            Log.d(TAG, "*** registered");
            favoriteMoviesObserver = new FavoriteMoviesObserver(new Handler(), getContext());
            getContext().getContentResolver().registerContentObserver(MovieColumns.CONTENT_URI, true, favoriteMoviesObserver);
        }
    }

    private void checkIfNewerUpdate() {
        switch (movieListType) {
            case Movie.MOVIE_LIST_TYPE_POPULARITY:
                NetworkApi.getMoviesByPopularity(0, new Callback<DiscoverMovieResult>() {
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
                                        if (newFetchedMovies.get(i).getMovieId() != existingMovies.get(i).getMovieId()) {
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
                break;
            case Movie.MOVIE_LIST_TYPE_RATING:
                NetworkApi.getMoviesByRating(0, MINIMUM_VOTE_COUNT_FOR_SORT_BY_RATING, new Callback<DiscoverMovieResult>() {
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
                                        if (newFetchedMovies.get(i).getMovieId() != existingMovies.get(i).getMovieId()) {
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
                break;
            case Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE:
                // Don't need to check.
                break;
        }
    }

    private void showSnackBar() {
        refreshSnackbar = Snackbar.make(cl_root, R.string.msg_movies_update_available, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        srl_popular_movies.setRefreshing(true);
                        onRefresh();
                    }
                });
        refreshSnackbar.show();
    }

    private void setupRecyclerView() {
        final int spanCount = getResources().getInteger(R.integer.grid_span_count);

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        rv_popular_movies.setLayoutManager(layoutManager);
        rv_popular_movies.clearOnScrollListeners();
        rv_popular_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.getItemCount() > 0) {
                    if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - NUM_LAST_ITEM_BEFORE_LOADING) {

                        getMovieList(false);
                    }

                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    if (layoutManager.findFirstVisibleItemPosition() > LIST_POSITION_TO_MOVE_FAB) {
                        if (dy < 0) {
                            fab_go_to_top.show();
                        } else {
                            fab_go_to_top.hide();
                        }
                    } else if (firstVisibleItemPosition < LIST_POSITION_TO_HIDE_FAB) {
                        fab_go_to_top.hide();
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
        abl_popular_movies = ((AppBarLayout) root.findViewById(R.id.abl_popular_movies));
        tv_empty_view = ((TextView) root.findViewById(R.id.tv_empty_view));
        srl_popular_movies = ((SwipeRefreshLayout) root.findViewById(R.id.srl_popular_movies));
        fab_go_to_top = ((FloatingActionButton) root.findViewById(R.id.fab_go_to_top));

        hasToolbar = tb_popular_movies != null;
        setupFab();
        setupSwipeRefreshLayout();

        if (hasToolbar) {
            setupToolbar();
        }
        return root;
    }

    private void setupFab() {
        fab_go_to_top.setVisibility(View.GONE);
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
        spinner.setSelection(movieListType, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (view == null) {
                    return;
                }

                if (refreshSnackbar != null) {
                    refreshSnackbar.dismiss();
                    refreshSnackbar = null;
                }

                setMovieListType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMovieList(boolean isRefresh) {
        if (loadingFromNetwork.compareAndSet(false, true)) {
            final int currentPage = isRefresh ? 0 : AppPreferences.getMoviePage(getActivity()) + 1;

            // This is to avoid over limit of String characters
            if (currentPage > MAX_PAGE_CACHE) {
                return;
            }

            final int thisMovieListType = movieListType;
            switch (thisMovieListType) {
                case Movie.MOVIE_LIST_TYPE_POPULARITY:
                    NetworkApi.getMoviesByPopularity(currentPage, new Callback<DiscoverMovieResult>() {
                        @Override
                        public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                            processNetworkSuccessResult(discoverMovieResult, response, currentPage, thisMovieListType);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            processNetworkFailureResult(error);
                        }
                    });
                    break;
                case Movie.MOVIE_LIST_TYPE_RATING:
                    NetworkApi.getMoviesByRating(currentPage, MINIMUM_VOTE_COUNT_FOR_SORT_BY_RATING, new Callback<DiscoverMovieResult>() {
                        @Override
                        public void success(DiscoverMovieResult discoverMovieResult, Response response) {
                            processNetworkSuccessResult(discoverMovieResult, response, currentPage, thisMovieListType);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            processNetworkFailureResult(error);
                        }
                    });
                    break;
                case Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE:
                    List<Movie> movies = DataManager.getFavoriteMovieList(getContext());
                    updateRecyclerView(movies);
                    loadingFromNetwork.set(false);
                    srl_popular_movies.setRefreshing(false);
                    tv_empty_view.setText(getText(R.string.msg_error_empty_favorite_movie_list));
                    tv_empty_view.setVisibility(movies.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                    rv_popular_movies.setVisibility(movies.isEmpty() ? View.INVISIBLE : View.VISIBLE);
                    break;
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
            if (errorToast == null || errorToast.getView() == null || !errorToast.getView().isShown()) {
                errorToast = Toast.makeText(getActivity(), getString(R.string.msg_error_data_connection_error), Toast.LENGTH_LONG);
                errorToast.show();
            }
            if (rv_popular_movies.getAdapter() == null || rv_popular_movies.getAdapter().getItemCount() == 0) {
                tv_empty_view.setText(getText(R.string.msg_error_data_connection_error));
                tv_empty_view.setVisibility(View.VISIBLE);
                rv_popular_movies.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void processNetworkSuccessResult(DiscoverMovieResult discoverMovieResult, Response response, int currentPage, int movieListType) {
        if (!isAdded() || getActivity() == null) {
            return;
        }

        if (response.getStatus() == 200 && discoverMovieResult != null) {
            saveMovieDataTask = new SaveMovieDataTask(getActivity(), currentPage, movieListType);
            saveMovieDataTask.execute(discoverMovieResult);
        } else {
            loadingFromNetwork.set(false);
            srl_popular_movies.setRefreshing(false);
            Log.e(TAG, response.getReason());
            if (errorToast == null || errorToast.getView() == null || !errorToast.getView().isShown()) {
                errorToast = Toast.makeText(getActivity(), getString(R.string.msg_error_data_connection_error), Toast.LENGTH_LONG);
                errorToast.show();
            }
            if (rv_popular_movies.getAdapter().getItemCount() == 0) {
                tv_empty_view.setText(getText(R.string.msg_error_data_connection_error));
                tv_empty_view.setVisibility(View.VISIBLE);
                rv_popular_movies.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateRecyclerView(List<Movie> movies) {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(movies, this, movieListType);
            rv_popular_movies.swapAdapter(movieAdapter, true);
            movieAdapter.setAdapterItemClickListener(this);
        } else {
            movieAdapter.updateMovies(movies, movieListType);
        }
    }

    private class SaveMovieDataTask extends AsyncTask<DiscoverMovieResult, Void, List<Movie>> {

        private final Context context;
        private final int currentPage;
        private final int movieListType;

        public SaveMovieDataTask(Context context, int currentPage, int movieListType) {
            this.context = context.getApplicationContext();
            this.currentPage = currentPage;
            this.movieListType = movieListType;
        }

        protected List<Movie> doInBackground(DiscoverMovieResult... results) {
            if (results.length == 1) {
                return updateData(context, results[0], currentPage, movieListType);
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
            tv_empty_view.setVisibility(View.INVISIBLE);
            rv_popular_movies.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadingFromNetwork.set(false);
            if (srl_popular_movies != null) {
                srl_popular_movies.setRefreshing(false);
            }
        }
    }

    private class FavoriteMoviesObserver extends ContentObserver {
        private final Context context;

        public FavoriteMoviesObserver(Handler handler, Context context) {
            super(handler);
            this.context = context.getApplicationContext();
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.d(TAG, "onChange " + selfChange + " " + uri.toString());
            if (movieListType == Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE) {
                getMovieList(true);
            }
        }
    }
}
