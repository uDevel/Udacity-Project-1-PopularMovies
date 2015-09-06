package com.udevel.popularmovies.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String ARG_KEY_MOVIE_ID = "ARG_KEY_MOVIE_ID";

    private TextView tv_title;
    private ImageView iv_poster;
    private View root;
    private TextView tv_release_year;
    private TextView tv_release_month_date;
    private TextView tv_rating;
    private TextView tv_popularity;
    private Toolbar tb_movie_detail;
    private AppBarLayout abl_movie_detail;
    private LinearLayout ll_collapse;
    private TextView tv_title_collapse;
    private TextView tv_release_runtime_rating_collapse;
    private RecyclerView rv_movie_detail;
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
        if (getArguments() != null) {
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
    public void onDestroyView() {
        super.onDestroyView();
        if (abl_movie_detail != null) {
            abl_movie_detail.removeOnOffsetChangedListener(this);
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
        MenuItem item = menu.findItem(R.id.menu_favorite_toggle);
        if (item != null) {
            item.setVisible(movie != null);
            item.setIcon(starred ? R.drawable.ic_star_filtered : R.drawable.ic_star_outline);
            item.setTitle(starred ? R.string.menu_item_star : R.string.menu_item_unstar);
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
        }
        return true;

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        iv_poster.setPivotY(iv_poster.getHeight());
        iv_poster.setPivotX(0);

        float expandPercentage = Math.min(0, (float) (i + tb_movie_detail.getHeight()) / (appBarLayout.getTotalScrollRange() - tb_movie_detail.getHeight()));
        float shrink = 0.5f;
        float scale = 1 + (expandPercentage * shrink);
        iv_poster.setScaleY(scale);
        iv_poster.setScaleX(scale);
        setMovieInfoGroupAlpha(1 + expandPercentage);

        ll_collapse.setVisibility(expandPercentage <= -0.9f ? View.VISIBLE : View.GONE);
    }

    public void setMovie(int movieId) {
        this.movieId = movieId;
        setMovieInfoUI();
        Context context = getContext();
        if (context != null) {
            AppPreferences.setLastMovieIdDetailViewed(context, movieId);
        }
    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        iv_poster = ((ImageView) root.findViewById(R.id.iv_poster));
        tv_title = ((TextView) root.findViewById(R.id.tv_title));
        rv_movie_detail = ((RecyclerView) root.findViewById(R.id.rv_movie_detail));
        tv_release_year = ((TextView) root.findViewById(R.id.tv_release_year));
        tv_release_month_date = ((TextView) root.findViewById(R.id.tv_release_month_date));
        tv_popularity = ((TextView) root.findViewById(R.id.tv_popularity));
        tv_rating = ((TextView) root.findViewById(R.id.tv_rating));
        abl_movie_detail = ((AppBarLayout) root.findViewById(R.id.abl_movie_detail));

        hasToolbar = abl_movie_detail != null;

        if (hasToolbar) {
            tb_movie_detail = ((Toolbar) root.findViewById(R.id.tb_movie_detail));
            ll_collapse = ((LinearLayout) root.findViewById(R.id.ll_collapse));
            tv_title_collapse = ((TextView) ll_collapse.findViewById(R.id.tv_title_collapse));
            tv_release_runtime_rating_collapse = ((TextView) ll_collapse.findViewById(R.id.tv_release_popularity_rating_collapse));

            if (iv_poster != null) {
                iv_poster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (movie != null) {
                            String posterPath = movie.getPosterPath();
                            if (posterPath != null) {
                                if (onFragmentInteractionListener != null) {
                                    onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_OPEN_FULLSCREEN_POSTER, posterPath);
                                }
                            }
                        }
                    }
                });
            }

            abl_movie_detail.addOnOffsetChangedListener(this);
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
        NetworkApi.getMovieById(movieId, new Callback<MovieDetailInfoResult>() {
            @Override
            public void success(MovieDetailInfoResult movieDetailInfoResult, Response response) {
                if (getActivity() == null) {
                    return;
                }

                if (movieDetailInfoResult != null) {
                    movie = new Movie();
                    movie.setMovieId(movieDetailInfoResult.getId());
                    movie.setOriginalTitle(movieDetailInfoResult.getOriginalTitle());
                    movie.setOverview(movieDetailInfoResult.getOverview());
                    movie.setPopularity(movieDetailInfoResult.getPopularity());
                    movie.setPosterPath(movieDetailInfoResult.getPosterPath());
                    movie.setReleaseDate(movieDetailInfoResult.getReleaseDate());
                    movie.setVoteAverage(movieDetailInfoResult.getVoteAverage());
                    movie.setVoteCount(movieDetailInfoResult.getVoteCount());

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

                    // Update local storage if it's a favorite movie.
                    Context context = getContext();
                    if (context != null) {
                        Movie favoriteMovieById = DataManager.getFavoriteMovieById(context, movieId);
                        if (favoriteMovieById != null) {
                            DataManager.addFavoriteMovieReviewTrailer(context, movie, reviews, youTubeTrailers);
                            starred = true;
                        } else {
                            starred = false;
                        }
                    }
                    if (hasToolbar) {
                        setMovieInfoUIToolbar();
                    }

                    updateMovieDetailRecyclerView();
                    getActivity().invalidateOptionsMenu();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDetached()) {
                    return;
                }
                // Try to get find movie from favorite storage
                Context context = getContext();
                if (context != null) {
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

                        updateMovieDetailRecyclerView();
                        getActivity().invalidateOptionsMenu();
                    } else {
                        starred = false;
                        Toast.makeText(context, getString(R.string.msg_error_data_connection_error), Toast.LENGTH_SHORT).show();
                        if (onFragmentInteractionListener != null) {
                            onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_CLOSE_MOVIE_DETAIL, null);
                        }
                    }
                }
            }
        });
    }

    private void setMovieInfoUIToolbar() {
        Uri uri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.THUMBNAIL_IMAGE_WIDTH).appendEncodedPath(movie.getPosterPath()).build();
        Glide.with(this)
                .load(uri)
                .error(R.drawable.ic_image_error)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv_poster);
        tv_title.setText(movie.getOriginalTitle());
        tv_title_collapse.setText(movie.getOriginalTitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(format.parse(movie.getReleaseDate()));
            tv_release_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
            tv_release_month_date.setText(" " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                    " " + cal.get(Calendar.DATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_rating.setText(getString(R.string.format_avg_vote, movie.getVoteAverage(), movie.getVoteCount()));
        tv_popularity.setText(getString(R.string.format_popularity, movie.getPopularity()));
        tv_release_runtime_rating_collapse.setText(tv_release_year.getText() +
                "" + tv_release_month_date.getText() +
                " - " + tv_popularity.getText() +
                " - " + tv_rating.getText());
    }

    private void updateMovieDetailRecyclerView() {
        if (rv_movie_detail != null) {
            if (rv_movie_detail.getLayoutManager() == null) {
                rv_movie_detail.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            MovieDetailAdapter adapter = (MovieDetailAdapter) rv_movie_detail.getAdapter();
            if (adapter == null) {
                adapter = new MovieDetailAdapter(movie, youTubeTrailers, reviews, this, !hasToolbar);
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
                                startActivity(Utils.getYouTubeIntent(getActivity().getPackageManager(), ((String) data)));
                            }
                            break;

                        }
                        case AdapterItemClickListener.ACTION_OPEN_REVIEW_DIALOG: {
                            if (data instanceof Review) {
                                FragmentManager fm = getChildFragmentManager();
                                ReviewDialogFragment dialogFragment = ReviewDialogFragment.newInstance(((Review) data));
                                dialogFragment.show(fm, "Review Dialog");
                            }
                            break;
                        }
                        case AdapterItemClickListener.ACTION_OPEN_POSTER_FULLSCREEN: {
                            break;
                        }
                    }
                }
            });
        }
    }

    private void setMovieInfoGroupAlpha(float alpha) {
        tv_title.setAlpha(alpha);
        tv_release_year.setAlpha(alpha);
        tv_release_month_date.setAlpha(alpha);
        tv_popularity.setAlpha(alpha);
        tv_rating.setAlpha(alpha);
    }

}
