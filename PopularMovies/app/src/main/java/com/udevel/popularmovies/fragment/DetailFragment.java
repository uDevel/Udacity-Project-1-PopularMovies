package com.udevel.popularmovies.fragment;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.MovieDetailAdapter;
import com.udevel.popularmovies.adapter.listener.AdapterItemClickListener;
import com.udevel.popularmovies.data.local.AppPreferences;
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.local.entity.Review;
import com.udevel.popularmovies.data.local.entity.YouTubeTrailer;
import com.udevel.popularmovies.data.network.NetworkApi;
import com.udevel.popularmovies.data.network.api.MovieDetailInfoResult;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;
import com.udevel.popularmovies.misc.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragment extends Fragment {
    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String ARG_KEY_MOVIE_ID = "ARG_KEY_MOVIE_ID";
    private static final String TAG_REVIEW_DIALOG = "TAG_REVIEW_DIALOG";

    private ImageView iv_backdrop;
    private View root;
    private Toolbar tb_movie_detail;
    private CollapsingToolbarLayout ctl_movie_detail;
    private AppBarLayout abl_movie_detail;
    private RecyclerView rv_movie_detail;
    private ProgressWheel pw_movie_detail;
    private boolean hasToolbar;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private int movieId = -1;
    private boolean starred;
    private Movie movie;
    private List<Review> reviews;
    private List<YouTubeTrailer> youTubeTrailers;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int movieId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_KEY_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            movieId = savedInstanceState.getInt(ARG_KEY_MOVIE_ID, -1);
        }

        if (movieId == -1 && getArguments() != null) {
            movieId = getArguments().getInt(ARG_KEY_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null) {
            root = setupViews(inflater, container);
            if (movieId != -1) {
                setMovie(movieId);
            }
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_KEY_MOVIE_ID, movieId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iv_backdrop != null) {
            Glide.clear(iv_backdrop);
        }

        if (pw_movie_detail != null) {
            pw_movie_detail.stopSpinning();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuFavorite = menu.findItem(R.id.menu_favorite_toggle);
        MenuItem menuShare = menu.findItem(R.id.menu_item_share);

        if (menuFavorite != null) {
            menuFavorite.setVisible(movie != null);
            menuFavorite.setIcon(starred ? R.drawable.ic_star_filtered : R.drawable.ic_star_outline);
            menuFavorite.setTitle(starred ? R.string.menu_item_star : R.string.menu_item_unstar);
        }

        if (menuShare != null) {
            boolean hasTrailer = youTubeTrailers != null && youTubeTrailers.size() > 0;
            menuShare.setVisible(hasTrailer);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite_toggle:
                starred = !starred;
                getActivity().invalidateOptionsMenu();
                if (starred) {
                    DataManager.addFavoriteMovieReviewTrailer(getActivity(), movie, reviews, youTubeTrailers);
                } else {
                    DataManager.removeFavoriteMovie(getActivity(), movie);
                }
                break;
            case R.id.menu_item_share:
                launchShareActivity();
                break;
        }
        return true;

    }

    public void setMovie(int movieId) {
        this.movieId = movieId;

        setMovieInfoUI();
        Context context = getContext();
        if (context != null) {
            AppPreferences.setLastMovieIdDetailViewed(context, movieId);
        }
    }

    private void launchShareActivity() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, movie.getOriginalTitle() + "\n" + YouTubeTrailer.getUrlForWeb(youTubeTrailers.get(0).getYouTubeTrailerId()));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "select"));
    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        iv_backdrop = ((ImageView) root.findViewById(R.id.iv_backdrop));
        rv_movie_detail = ((RecyclerView) root.findViewById(R.id.rv_movie_detail));
        pw_movie_detail = ((com.pnikosis.materialishprogress.ProgressWheel) root.findViewById(R.id.pw_movie_detail));
        abl_movie_detail = ((AppBarLayout) root.findViewById(R.id.abl_movie_detail));

        hasToolbar = abl_movie_detail != null;
        pw_movie_detail.setInstantProgress(0.0f);
        if (hasToolbar) {
            tb_movie_detail = ((Toolbar) root.findViewById(R.id.tb_movie_detail));
            ctl_movie_detail = ((CollapsingToolbarLayout) root.findViewById(R.id.ctl_movie_detail));
            setupToolbar();
        }
        return root;
    }

    private void setupToolbar() {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(tb_movie_detail);
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(getString(R.string.title_movie_detail));
                supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
                tb_movie_detail.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();

                        }
                    }
                });
            }
        }
    }

    private void setMovieInfoUI() {
        if (Utils.isNetworkConnected(getContext())) {
            pw_movie_detail.spin();

            NetworkApi.getMovieById(movieId, new Callback<MovieDetailInfoResult>() {
                @Override
                public void success(MovieDetailInfoResult movieDetailInfoResult, Response response) {
                    if (getActivity() == null || !isAdded()) {
                        return;
                    }

                    if (movieDetailInfoResult != null) {
                        movie = Movie.convertMovieDetailInfoResult(movieDetailInfoResult);

                        MovieDetailInfoResult.Trailers trailers = movieDetailInfoResult.getTrailers();
                        if (trailers != null) {
                            List<MovieDetailInfoResult.Youtube> youtubes = trailers.getYoutube();
                            if (youtubes != null) {
                                youTubeTrailers = new ArrayList<>();
                                for (MovieDetailInfoResult.Youtube youtube : youtubes) {

                                    YouTubeTrailer youTubeTrailer = new YouTubeTrailer();
                                    youTubeTrailer.setYouTubeTrailerId(youtube.getSource());
                                    youTubeTrailer.setSize(youtube.getSize());
                                    youTubeTrailer.setName(youtube.getName());
                                    youTubeTrailers.add(youTubeTrailer);
                                }
                            }
                        }

                        MovieDetailInfoResult.Reviews reviewsFromNetwork = movieDetailInfoResult.getReviews();
                        if (reviewsFromNetwork != null) {
                            List<MovieDetailInfoResult.Reviews.Result> results = reviewsFromNetwork.getResults();
                            if (results != null) {
                                reviews = new ArrayList<>();
                                for (MovieDetailInfoResult.Reviews.Result result : results) {
                                    Review review = new Review();
                                    review.setReviewId(result.getId());
                                    review.setAuthor(result.getAuthor());
                                    review.setContent(result.getContent());
                                    reviews.add(review);
                                }
                            }
                        }

                        pw_movie_detail.stopSpinning();

                        if (hasToolbar) {
                            setMovieInfoUIToolbar();
                        }
                        updateMovieDetailRecyclerView();
                        getActivity().invalidateOptionsMenu();

                        // Update local storage if it's a favorite movie.
                        Context context = getContext();
                        if (context != null) {
                            Movie favoriteMovieById = DataManager.getFavoriteMovieById(context, movieId);
                            if (favoriteMovieById != null) {
                                if (!movie.equals(favoriteMovieById)) {
                                    DataManager.addFavoriteMovieReviewTrailer(context, movie, reviews, youTubeTrailers);
                                }
                                starred = true;
                            } else {
                                starred = false;
                            }
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (getActivity() == null || !isAdded()) {
                        return;
                    }
                    // Try to get find movie from favorite storage
                    pw_movie_detail.stopSpinning();
                    tryToLocalStorageNetworkFails();
                }
            });
        } else {
            tryToLocalStorageNetworkFails();
        }
    }

    private void tryToLocalStorageNetworkFails() {
        Context context = getContext();
        if (context == null) {
            return;
        }

        Movie favoriteMovieById = DataManager.getFavoriteMovieById(context, movieId);
        List<YouTubeTrailer> youTubeTrailerByMovieId = DataManager.getYouTubeTrailerByMovieId(context, movieId);
        List<Review> reviewsByMovieId = DataManager.getReviewsByMovieId(context, movieId);
        if (favoriteMovieById != null) {
            movie = favoriteMovieById;
            youTubeTrailers = youTubeTrailerByMovieId;
            reviews = reviewsByMovieId;

            starred = true;
            if (hasToolbar) {
                setMovieInfoUIToolbar();
            }
            getActivity().invalidateOptionsMenu();

            // This is needed to avoid choppy animation.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        updateMovieDetailRecyclerView();
                    }
                }
            }, 300);
        } else {
            starred = false;
            Toast.makeText(context, getString(R.string.msg_error_data_connection_error), Toast.LENGTH_SHORT).show();
            if (onFragmentInteractionListener != null) {
                onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_CLOSE_MOVIE_DETAIL, null);
            }
        }
    }

    private void setMovieInfoUIToolbar() {
        ctl_movie_detail.setTitle(movie.getOriginalTitle());

        iv_backdrop.setVisibility(View.INVISIBLE);
        Uri backdropUrl = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.MEDIUM_BACKDROP_IMAGE_WIDTH).appendEncodedPath(movie.getBackdropPath()).build();

        Glide.with(this)
                .load(backdropUrl)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<Uri, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (getActivity() == null || !isAdded()) {
                            return true;
                        }

                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette p) {
                                int backupColor = p.getDarkMutedColor(ContextCompat.getColor(getContext(), R.color.primary));
                                int backgroundColor = p.getDarkVibrantColor(backupColor);
                                if (ctl_movie_detail != null) {
                                    ctl_movie_detail.setContentScrimColor(backgroundColor);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    reveal();
                                }
                                iv_backdrop.setVisibility(View.VISIBLE);
                            }
                        });
                        return false;
                    }

                }).into(iv_backdrop);
    }

    private void updateMovieDetailRecyclerView() {
        if (rv_movie_detail == null) {
            return;
        }

        if (rv_movie_detail.getLayoutManager() == null) {
            rv_movie_detail.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        rv_movie_detail.scrollToPosition(0);

        MovieDetailAdapter adapter = (MovieDetailAdapter) rv_movie_detail.getAdapter();
        if (adapter == null) {
            adapter = new MovieDetailAdapter(movie, youTubeTrailers, reviews, this);
            rv_movie_detail.setAdapter(adapter);
        } else {
            adapter.updateMovieDetail(movie, youTubeTrailers, reviews);
        }
        adapter.setAdapterItemClickListener(new AdapterItemClickListener() {
            @Override
            public void adapterItemClick(String action, View v, Object data) {
                switch (action) {
                    case AdapterItemClickListener.ACTION_OPEN_YOUTUBE_TRAILER: {
                        if (data instanceof String) {
                            String youTubeId = ((String) data);
                            try {
                                String urlForApp = YouTubeTrailer.getUrlForApp(youTubeId);
                                Uri parse = Uri.parse(urlForApp);

                                Intent i = new Intent(Intent.ACTION_VIEW, parse);
                                startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                // youtube is not installed.  use other app.
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(YouTubeTrailer.getUrlForWeb(youTubeId)));
                                startActivity(i);
                            }
                        }
                        break;

                    }
                    case AdapterItemClickListener.ACTION_OPEN_REVIEW_DIALOG: {
                        if (data instanceof Review) {
                            FragmentManager fm = getChildFragmentManager();
                            ReviewDialogFragment dialogFragment = ReviewDialogFragment.newInstance(((Review) data));
                            dialogFragment.show(fm, TAG_REVIEW_DIALOG);
                        }
                        break;
                    }
                    case AdapterItemClickListener.ACTION_OPEN_POSTER_FULLSCREEN: {
                        if (movie != null) {
                            String posterPath = movie.getPosterPath();
                            if (posterPath != null) {
                                if (onFragmentInteractionListener != null) {
                                    onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_OPEN_FULLSCREEN_POSTER, posterPath);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void reveal() {
        if (iv_backdrop != null) {
            View targetView = iv_backdrop;
            int cx = (targetView.getLeft() + targetView.getRight()) / 2;
            int cy = (targetView.getTop() + targetView.getBottom()) / 2;

            int finalRadius = Math.max(targetView.getWidth(), targetView.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0, finalRadius);
            anim.start();
        }
    }
}
