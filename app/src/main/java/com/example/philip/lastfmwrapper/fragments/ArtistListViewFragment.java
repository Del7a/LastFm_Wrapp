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
import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.listAdapters.ChartArtistAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistListViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ChartArtistAdapter listAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private IFragmentInteraction.OnFragmentInteractionListener mListener;

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }

    public ArtistListViewFragment() {
        // Required empty public constructor
    }

    public static ArtistListViewFragment newInstance() {
        ArtistListViewFragment fragment = new ArtistListViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cView = inflater.inflate(R.layout.fragment_top_artist_chart, container, false);;
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
            mListener.onFragmentInteraction(name, imageUrl, mbid, plays,listeners);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(String name, String imageUrl, String mbid, String plays, String listeners);
//    }
}
