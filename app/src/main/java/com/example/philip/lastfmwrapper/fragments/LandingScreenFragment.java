package com.example.philip.lastfmwrapper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.philip.lastfmwrapper.R;


/**
 * to handle interaction events.
 * Use the {@link LandingScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingScreenFragment extends Fragment {

    public LandingScreenFragment() {
        // Required empty public constructor
    }


    public static LandingScreenFragment newInstance() {
        LandingScreenFragment fragment = new LandingScreenFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated =  inflater.inflate(R.layout.fragment_landing_screen, container, false);
        return inflated;
    }

}
