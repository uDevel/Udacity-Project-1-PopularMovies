package com.udevel.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udevel.popularmovies.R;
import com.udevel.popularmovies.fragment.DetailFragment;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

public class DetailActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private static final String ARG_KEY_MOVIE_ID = "ARG_KEY_MOVIE_ID";
    private boolean dismissOnOrientationChange;

    public static Intent createIntent(Context context, int movieId) {
        Intent startIntent = new Intent(context, DetailActivity.class);
        startIntent.putExtra(ARG_KEY_MOVIE_ID, movieId);
        return startIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int movieId = intent.getIntExtra(ARG_KEY_MOVIE_ID, -1);

        setContentView(R.layout.activity_detail);
        dismissOnOrientationChange = getResources().getBoolean(R.bool.back_to_list_activity_on_landscape_orientation);
        DetailFragment detailFragment = ((DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail));
        detailFragment.setMovie(movieId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dismissOnOrientationChange && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(String action, Object asset) {
        switch (action) {
            case OnFragmentInteractionListener.ACTION_OPEN_FULLSCREEN_POSTER:
                if (asset != null) {
                    Intent intent = new Intent(this, PosterFullscreenActivity.class);
                    intent.putExtra(PosterFullscreenActivity.ARG_KEY_POSTER_PATH, (String) asset);
                    startActivity(intent);
                }
                break;
            case OnFragmentInteractionListener.ACTION_CLOSE_MOVIE_DETAIL:
                onBackPressed();
                break;
        }
    }
}
