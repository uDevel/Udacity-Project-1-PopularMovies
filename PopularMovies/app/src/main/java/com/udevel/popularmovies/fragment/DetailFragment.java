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

import com.squareup.picasso.Picasso;
import com.udevel.popularmovies.R;
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

public class DetailFragment extends Fragment {
    private static final String ARG_KEY_MOVIE_ID = "ARG_KEY_MOVIE_ID";

    private int movieId;

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private TextView tv_title;
    private ImageView iv_poster;

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
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        iv_poster = ((ImageView) root.findViewById(R.id.iv_poster));
        tv_title = ((TextView) root.findViewById(R.id.tv_title));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Movie movie = DataManager.getMovieById(getActivity(), movieId);
        Uri uri = Uri.parse(Movie.baseUrlForImage).buildUpon().appendPath("w500").appendEncodedPath(movie.getPosterPath()).build();
        Picasso.with(getActivity())
                .load(uri)
                .into(iv_poster);
        tv_title.setText(movie.getOriginalTitle());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

}
