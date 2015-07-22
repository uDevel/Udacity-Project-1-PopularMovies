package com.udevel.popularmovies.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String ARG_KEY_MOVIE_ID = "ARG_KEY_MOVIE_ID";

    private int movieId;

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private TextView tv_title;
    private ImageView iv_poster;
    private View root;
    private TextView tv_overview;
    private TextView tv_release_year;
    private TextView tv_release_month_date;
    private TextView tv_rating;
    private TextView tv_popularity;
    private Toolbar tb_movie_detail;
    private AppBarLayout abl_movie_detail;
    private LinearLayout ll_collapse;
    private TextView tv_title_collapse;
    private TextView tv_release_runtime_rating_collapse;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onFragmentInteractionListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_KEY_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null) {
            root = setupViews(inflater, container);
            Movie movieById = DataManager.getMovieById(getActivity(), movieId);
            if (movieById != null) {
                Uri uri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.THUMBNAIL_IMAGE_WIDTH).appendEncodedPath(movieById.getPosterPath()).build();
                Glide.with(this)
                        .load(uri)
                        .error(R.drawable.ic_image_error)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_poster);
                setMovieInfoUI(movieById);
            } else {
                Toast.makeText(getActivity(), "Error on getting data from Internet.", Toast.LENGTH_LONG).show();
            }
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

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

    private void setMovieInfoUI(Movie movie) {
        tv_title.setText(movie.getOriginalTitle());
        tv_title_collapse.setText(movie.getOriginalTitle());
        tv_overview.setText(movie.getOverview());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
                " " + tv_release_month_date.getText() +
                " - " + tv_popularity.getText() +
                " - " + tv_rating.getText());
    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        iv_poster = ((ImageView) root.findViewById(R.id.iv_poster));
        tv_title = ((TextView) root.findViewById(R.id.tv_title));
        tv_overview = ((TextView) root.findViewById(R.id.tv_overview));
        tv_release_year = ((TextView) root.findViewById(R.id.tv_release_year));
        tv_release_month_date = ((TextView) root.findViewById(R.id.tv_release_month_date));
        tv_popularity = ((TextView) root.findViewById(R.id.tv_popularity));
        tv_rating = ((TextView) root.findViewById(R.id.tv_rating));
        abl_movie_detail = ((AppBarLayout) root.findViewById(R.id.abl_movie_detail));
        tb_movie_detail = ((Toolbar) root.findViewById(R.id.tb_movie_detail));
        ll_collapse = ((LinearLayout) root.findViewById(R.id.ll_collapse));
        tv_title_collapse = ((TextView) ll_collapse.findViewById(R.id.tv_title_collapse));
        tv_release_runtime_rating_collapse = ((TextView) ll_collapse.findViewById(R.id.tv_release_popularity_rating_collapse));

        iv_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movieById = DataManager.getMovieById(getActivity(), movieId);
                if (movieById != null && onFragmentInteractionListener != null) {
                    onFragmentInteractionListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_OPEN_FULLSCREEN_POSTER, movieById.getPosterPath());
                }
            }
        });
        abl_movie_detail.addOnOffsetChangedListener(this);
        setupToolbar(tb_movie_detail);
        return root;
    }

    private void setupToolbar(Toolbar tb_popular_movies) {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(tb_popular_movies);
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(getString(R.string.title_movie_detail));
                supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
                tb_popular_movies.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void setMovieInfoGroupAlpha(float alpha) {
        tv_title.setAlpha(alpha);
        tv_release_year.setAlpha(alpha);
        tv_release_month_date.setAlpha(alpha);
        tv_popularity.setAlpha(alpha);
        tv_rating.setAlpha(alpha);
    }

}
