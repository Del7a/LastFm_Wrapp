package com.example.philip.lastfmwrapper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.models.Track;
import com.example.philip.lastfmwrapper.listAdapters.ChartTrackAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TracksListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TracksListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TracksListViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ChartTrackAdapter listAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnTrackInteractionListener mListener;

    public TracksListViewFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static TracksListViewFragment newInstance() {
        TracksListViewFragment fragment = new TracksListViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cView = inflater.inflate(R.layout.fragment_tracks_list_view, container, false);
        ArrayList<Track> trList = new ArrayList<Track>();
        listAdapter = new ChartTrackAdapter(getActivity().getBaseContext(), trList);
        final ListView trackListView = (ListView) cView.findViewById(R.id.fragmentTracksListView);
        trackListView.setAdapter(listAdapter);

        trackListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track a = listAdapter.getItem(position);
                onButtonPressed(a.songName + " " + a.artistName);
            }
        });

        return  cView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String trackName) {
        if (mListener != null) {
            mListener.onTrackInteraction(trackName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrackInteractionListener) {
            mListener = (OnTrackInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addTrack(Track tr){
        listAdapter.add(tr);
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
    public interface OnTrackInteractionListener {
        // TODO: Update argument type and name
        void onTrackInteraction(String trackName);
    }
}
