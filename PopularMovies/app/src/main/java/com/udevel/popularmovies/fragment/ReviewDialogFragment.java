package com.udevel.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udevel.popularmovies.R;
import com.udevel.popularmovies.data.local.entity.Review;

/**
 * Created by benny on 8/9/2015.
 */
public class ReviewDialogFragment extends DialogFragment {

    private static final String ARG_KEY_AUTHOR = "ARG_KEY_AUTHOR";
    private static final String ARG_KEY_CONTENT = "ARG_KEY_CONTENT";
    private String author;
    private String content;

    public static ReviewDialogFragment newInstance(Review review) {
        ReviewDialogFragment reviewDialogFragment = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_AUTHOR, review.getAuthor());
        args.putString(ARG_KEY_CONTENT, review.getContent());
        reviewDialogFragment.setArguments(args);
        return reviewDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            author = getArguments().getString(ARG_KEY_AUTHOR);
            content = getArguments().getString(ARG_KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.title_review_dialogfragment);
        View rootView = inflater.inflate(R.layout.dialogfragment_review, container, false);
        TextView tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        TextView tv_author = (TextView) rootView.findViewById(R.id.tv_author);
        tv_author.setText(getString(R.string.reviewed_by, author));
        tv_content.setText(content);
        return rootView;
    }
}