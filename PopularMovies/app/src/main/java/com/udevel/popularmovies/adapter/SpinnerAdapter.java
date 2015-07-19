package com.udevel.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udevel.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benny on 7/16/2015.
 */
public class SpinnerAdapter extends BaseAdapter {
    private List<String> mItems = new ArrayList<>();
    private LayoutInflater inflater;

    public SpinnerAdapter(List<String> mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            view = inflater.inflate(R.layout.spinner_item, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            view = inflater.inflate(R.layout.spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
    }
}
