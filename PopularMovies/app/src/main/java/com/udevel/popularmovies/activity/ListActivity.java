package com.udevel.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.udevel.popularmovies.fragment.DetailFragment;
import com.udevel.popularmovies.fragment.ListFragment;
import com.udevel.popularmovies.fragment.listener.OnFragmentInteractionListener;

import java.util.Arrays;

public class ListActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";
    private boolean isTwoPanes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        isTwoPanes = findViewById(R.id.fl_fragment_holder) != null;
        if (isTwoPanes) {
            Toolbar tb_main = (Toolbar) findViewById(R.id.tb_main);
            setSupportActionBar(tb_main);
            setupSpinner(tb_main);
        }
        if (savedInstanceState == null) {
            if (isTwoPanes) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_fragment_holder, DetailFragment.newInstance(-1), TAG_DETAIL_FRAGMENT)
                        .commit();
            }
        }
    }

    private void setupSpinner(ViewGroup tb_main) {
        int movieListType = AppPreferences.getLastMovieListType(this);
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

                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list);
                if (listFragment != null) {
                    listFragment.setListType(position);
                }
/*
                    if (refreshSnackbar != null) {
                        refreshSnackbar.dismiss();
                        refreshSnackbar = null;
                    }

                    String displayingText = ((TextView) view).getText().toString();

                    if (displayingText.equals(stringArray[Movie.MOVIE_LIST_TYPE_POPULARITY]) && movieListType != Movie.MOVIE_LIST_TYPE_POPULARITY) {
                        movieListType = Movie.MOVIE_LIST_TYPE_POPULARITY;
                        getMovieList(true);
                    } else if (displayingText.equals(stringArray[Movie.MOVIE_LIST_TYPE_RATING]) && movieListType != Movie.MOVIE_LIST_TYPE_RATING) {
                        movieListType = Movie.MOVIE_LIST_TYPE_RATING;
                        getMovieList(true);
                    } else if (displayingText.equals(stringArray[Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE]) && movieListType != Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE) {
                        movieListType = Movie.MOVIE_LIST_TYPE_LOCAL_FAVOURITE;
                        AppPreferences.setLastMovieListType(getActivity(), movieListType);
                        getMovieList(true);
                    }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(String action, Object asset) {
        switch (action) {
            case OnFragmentInteractionListener.ACTION_OPEN_MOVIE_DETAIL:
                if (asset != null && asset instanceof Integer) {
                    if (isTwoPanes) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_fragment_holder, DetailFragment.newInstance((int) asset), TAG_DETAIL_FRAGMENT)
                                .commit();
                    } else {
                        Intent intent = DetailActivity.createIntent(this, (int) asset);
                        startActivity(intent);
                    }
                    break;
                }
        }
    }
}
