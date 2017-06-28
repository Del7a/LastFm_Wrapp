package com.example.philip.lastfmwrapper;

/**
 * Created by philip on 05.05.17.
 */

public interface IFragmentInteraction {
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name, String imageUrl, String mbid, String plays, String listeners);
    }
}
