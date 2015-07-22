package com.udevel.popularmovies.fragment.listener;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnFragmentInteractionListener {
    String ACTION_OPEN_MOVIE_DETAIL = "ACTION_OPEN_MOVIE_DETAIL";
    String ACTION_OPEN_FULLSCREEN_POSTER = "ACTION_OPEN_FULLSCREEN_POSTER";

    void onFragmentInteraction(String action, Object asset);
}