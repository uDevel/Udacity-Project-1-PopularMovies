package com.udevel.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udevel.popularmovies.fragment.DetailFragment;
import com.udevel.popularmovies.fragment.ListFragment;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT";
    private static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";
    private static final String TAG_POSTER_VIEW_FRAGMENT = "TAG_POSTER_VIEW_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_fragment_holder, ListFragment.newInstance(), TAG_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(String action, Object asset) {
        switch (action) {
            case OnFragmentInteractionListener.ACTION_OPEN_MOVIE_DETAIL:
                if (asset != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_fragment_holder, DetailFragment.newInstance(((int) asset)), TAG_DETAIL_FRAGMENT)
                            .addToBackStack("")
                            .commit();
                }
                break;
            case OnFragmentInteractionListener.ACTION_OPEN_FULLSCREEN_POSTER:
                if (asset != null) {
                    Intent intent = new Intent(this, PosterFullscreenActivity.class);
                    intent.putExtra(PosterFullscreenActivity.ARG_KEY_POSTER_PATH, (String) asset);
                    startActivity(intent);
                }
                break;
        }
    }
}
