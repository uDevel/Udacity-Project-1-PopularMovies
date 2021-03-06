package com.udevel.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.udevel.popularmovies.R;
import com.udevel.popularmovies.adapter.SpinnerAdapter;
import com.udevel.popularmovies.data.local.AppPreferences;
import com.udevel.popularmovies.data.local.entity.Movie;
import com.udevel.popularmovies.fragment.DetailFragment;
import com.udevel.popularmovies.fragment.ListFragment;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;
import com.udevel.popularmovies.misc.Utils;

import java.util.Arrays;

public class ListActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";
    private boolean isTwoPanes = false;
    private int movieListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        isTwoPanes = findViewById(R.id.fl_fragment_holder) != null;
        if (isTwoPanes) {
            Toolbar tb_main = (Toolbar) findViewById(R.id.tb_main);
            setSupportActionBar(tb_main);
            setupSpinner(tb_main);

            int lastMovieIdDetailViewed = AppPreferences.getLastMovieIdDetailViewed(this);
            if (lastMovieIdDetailViewed != -1) {
                showDetailFragment(lastMovieIdDetailViewed);
            }
        } else {
            Fragment detailFragment = getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
            if (detailFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isTwoPanes) {
            // User always mistakenly back out of the app when they are in favorite list, so we let fragment to decide if we really back out the app.
            ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
            if (listFragment.onBackPressed()) {
                return;
            }
        } else {
            if (movieListType == Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE) {
                Spinner spinner = (Spinner) findViewById(R.id.sp_main);
                if (spinner != null) {
                    if (Utils.isNetworkConnected(this)) {
                        spinner.setSelection(Movie.MOVIE_LIST_TYPE_POPULARITY);
                        return;
                    }
                }
            }

        }
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(String action, Object asset) {
        switch (action) {
            case OnFragmentInteractionListener.ACTION_OPEN_MOVIE_DETAIL:
                if (asset != null && asset instanceof Integer) {
                    if (isTwoPanes) {
                        showDetailFragment((int) asset);
                    } else {
                        Intent intent = DetailActivity.createIntent(this, (int) asset);
                        startActivity(intent);
                    }
                    break;
                }
            case OnFragmentInteractionListener.ACTION_OPEN_FULLSCREEN_POSTER:
                if (asset != null) {
                    Intent intent = new Intent(this, PosterFullscreenActivity.class);
                    intent.putExtra(PosterFullscreenActivity.ARG_KEY_POSTER_PATH, (String) asset);
                    startActivity(intent);
                }
                break;
            case OnFragmentInteractionListener.ACTION_CLOSE_MOVIE_DETAIL:
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(TAG_DETAIL_FRAGMENT);
                supportFragmentManager.beginTransaction().remove(fragmentByTag).commit();
                View tv_instruction = findViewById(R.id.tv_error_msg);
                if (tv_instruction != null) {
                    tv_instruction.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setupSpinner(ViewGroup tb_main) {
        movieListType = AppPreferences.getLastMovieListType(this);
        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.spinner_actionbar, tb_main, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.END;
        tb_main.addView(spinnerContainer, lp);
        final String[] stringArray = getResources().getStringArray(R.array.spinner_items_array);
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

                movieListType = position;

                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
                if (listFragment != null) {
                    listFragment.setMovieListType(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDetailFragment(int movieId) {
        View tv_instruction = findViewById(R.id.tv_error_msg);
        if (tv_instruction != null) {
            tv_instruction.setVisibility(View.GONE);
        }

        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
        if (detailFragment != null) {
            detailFragment.setMovie(movieId);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_holder, DetailFragment.newInstance(movieId), TAG_DETAIL_FRAGMENT)
                    .commit();
        }
    }
}
