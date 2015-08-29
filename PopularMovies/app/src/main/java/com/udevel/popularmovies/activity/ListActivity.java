package com.udevel.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.udevel.popularmovies.data.local.DataManager;
import com.udevel.popularmovies.data.local.entity.Movie;
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

            int lastMovieIdDetailViewed = AppPreferences.getLastMovieIdDetailViewed(this);
            if (lastMovieIdDetailViewed != -1) {
                Movie movieById = DataManager.getMovieById(this, lastMovieIdDetailViewed);
                if (movieById != null) {
                    showDetailFragment(lastMovieIdDetailViewed);
                }
            }
        } else {
            Fragment detailFragment = getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
            if (detailFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
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
                    listFragment.setMovieListType(position);
                }
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
                        showDetailFragment((int) asset);
                    } else {
                        Intent intent = DetailActivity.createIntent(this, (int) asset);
                        startActivity(intent);
                    }
                    break;
                }
        }
    }

    private void showDetailFragment(int movieId) {
        View tv_instruction = findViewById(R.id.tv_instruction);
        if (tv_instruction != null) {
            tv_instruction.setVisibility(View.GONE);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragment_holder, DetailFragment.newInstance(movieId), TAG_DETAIL_FRAGMENT)
                .commit();
    }
}
