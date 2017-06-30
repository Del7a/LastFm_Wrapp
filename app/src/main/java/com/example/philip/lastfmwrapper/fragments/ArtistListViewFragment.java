package com.example.philip.lastfmwrapper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.philip.lastfmwrapper.IFragmentInteraction;
import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.listAdapters.ChartArtistAdapter;
import com.example.philip.lastfmwrapper.models.Artist;

import java.util.ArrayList;


public class ArtistListViewFragment extends Fragment {
    private static final String ARG_PARAM2 = "param2";

    private ChartArtistAdapter listAdapter;

    private IFragmentInteraction.OnFragmentInteractionListener mListener;

    public ArtistListViewFragment() {
        // Required empty public constructor
    }

    public static ArtistListViewFragment newInstance() {
        ArtistListViewFragment fragment = new ArtistListViewFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cView = inflater.inflate(R.layout.fragment_top_artist_chart, container, false);
        ArrayList<Artist> artL = new ArrayList<Artist>();
        listAdapter = new ChartArtistAdapter(getActivity().getBaseContext(), artL);
        final ListView artistListView = (ListView) cView.findViewById(R.id.fragmentArtistListView);
        artistListView.setAdapter(listAdapter);

        artistListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist a = listAdapter.getItem(position);
                onButtonPressed(a.artistName, a.imageLargeUrl, a.mbid, a.playcount, a.listeners);
            }
        });


        return cView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String name, String imageUrl, String mbid, String plays, String listeners) {
        if (mListener != null) {
            mListener.onFragmentInteraction(name, imageUrl, mbid, plays, listeners);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentInteraction.OnFragmentInteractionListener) {
            mListener = (IFragmentInteraction.OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addArtist(Artist art) {
        this.listAdapter.add(art);
    }

    public void removeArtistAt(int position) {

        Artist toRemove = this.listAdapter.getItem(position);
        this.listAdapter.remove(toRemove);
    }
}
