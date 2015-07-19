package com.udevel.popularmovies.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.data.network.NetworkApi;
import com.udevel.popularmovies.data.network.api.MovieDetailInfo;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragment extends Fragment {
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
    private TextView tv_runtime;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null) {
            root = setupViews(inflater, container);
            NetworkApi.getMovieById(movieId, new retrofit.Callback<MovieDetailInfo>() {
                @Override
                public void success(final MovieDetailInfo movieDetailInfo, Response response) {
                    Uri uri = Uri.parse(Movie.BASE_URL_FOR_IMAGE).buildUpon().appendPath(Movie.DETAIL_IMAGE_WIDTH).appendEncodedPath(movieDetailInfo.getPosterPath()).build();
                    Picasso.with(getActivity())
                            .load(uri)
                            .into(iv_poster, new Callback() {
                                @Override
                                public void onSuccess() {
                                    setMovieInfoUI(movieDetailInfo);
                                }

                                @Override
                                public void onError() {
                                    if (getActivity() != null) {
                                        Toast.makeText(getActivity(), "Error on getting data from Internet", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    tv_title.setText(movieDetailInfo.getOriginalTitle());
                }

                @Override
                public void failure(RetrofitError error) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Error on getting data from Internet.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    private void setMovieInfoUI(MovieDetailInfo movieDetailInfo) {
        tv_overview.setText(movieDetailInfo.getOverview());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(format.parse(movieDetailInfo.getReleaseDate()));
            tv_release_year.setText(String.valueOf(cal.get(Calendar.YEAR)));
            tv_release_month_date.setText(" " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                    " " + cal.get(Calendar.DATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_rating.setText(String.valueOf(movieDetailInfo.getVoteAverage()) + "/10");
        tv_runtime.setText(String.valueOf(movieDetailInfo.getRuntime()) + " min");
    }

    private View setupViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        iv_poster = ((ImageView) root.findViewById(R.id.iv_poster));
        tv_title = ((TextView) root.findViewById(R.id.tv_title));
        tv_overview = ((TextView) root.findViewById(R.id.tv_overview));
        tv_release_year = ((TextView) root.findViewById(R.id.tv_release_year));
        tv_release_month_date = ((TextView) root.findViewById(R.id.tv_release_month_date));
        tv_runtime = ((TextView) root.findViewById(R.id.tv_runtime));
        tv_rating = ((TextView) root.findViewById(R.id.tv_rating));
        return root;
    }

}
