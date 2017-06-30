package com.example.philip.lastfmwrapper.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.philip.lastfmwrapper.listAdapters.ArtistCursorAdapter;
import com.example.philip.lastfmwrapper.IFragmentInteraction;
import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.database.ArtistContentProvider;
import com.example.philip.lastfmwrapper.database.ArtistTable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ArtistsListViewCursourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsListViewCursourFragment extends Fragment {

    private ArtistCursorAdapter cursorAdapter;
    private Cursor cursor;
    private final String TAG = "ArtistsCursour";

    private IFragmentInteraction.OnFragmentInteractionListener mListener;

    public final   String[] projection = new String[]{
            ArtistTable.COLUMN_ID,
            ArtistTable.COLUMN_NAME,
            ArtistTable.COLUMN_PLAYS,
            ArtistTable.COLUMN_LISTENERS,
            ArtistTable.COLUMN_IMG_URL
    };

    public ArtistsListViewCursourFragment() {
        // Required empty public constructor
    }

    public static ArtistsListViewCursourFragment newInstance() {
        ArtistsListViewCursourFragment fragment = new ArtistsListViewCursourFragment();
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

        ContentResolver cr = container.getContext().getContentResolver();
        Cursor c = cr.query(ArtistContentProvider.CONTENT_URI, projection, null, null, null);
        cursor = c;

        cursorAdapter = new ArtistCursorAdapter(getActivity().getBaseContext(), c);
        final ListView trackListView = (ListView) cView.findViewById(R.id.fragmentTracksListView);
        trackListView.setAdapter(cursorAdapter);

        trackListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cur = (Cursor) cursorAdapter.getItem(position);
                cur.moveToPosition(position);
                String artistName = cursorAdapter.getCursor().getString(cur.getColumnIndexOrThrow(ArtistTable.COLUMN_NAME));
                String imageLargeUrl = cursorAdapter.getCursor().getString(cur.getColumnIndexOrThrow(ArtistTable.COLUMN_IMG_URL));
                //String mbid = cursorAdapter.getCursor().getString(cur.getColumnIndexOrThrow(ArtistTable.COLUMN_LASTFM_ID));
                String playcount = cursorAdapter.getCursor().getString(cur.getColumnIndexOrThrow(ArtistTable.COLUMN_NAME));
                String listeners = cursorAdapter.getCursor().getString(cur.getColumnIndexOrThrow(ArtistTable.COLUMN_LISTENERS));
                onButtonPressed(artistName, imageLargeUrl, null, playcount, listeners);
            }
        });

        TextView emptyText = (TextView) cView.findViewById(R.id.emptyTrackList);
        trackListView.setEmptyView(emptyText);

        return  cView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: Quick fix before presentation. Make a proper null check!
        try {
            ViewGroup container = (ViewGroup) getView().getParent();
            ContentResolver cr = container.getContext().getContentResolver();
            Cursor c = cr.query(ArtistContentProvider.CONTENT_URI, projection, null, null, null);
            cursorAdapter.changeCursor(c);
            cursorAdapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cursor.close();
        mListener = null;
    }


    public void UpdateView(){
        this.cursorAdapter.notifyDataSetChanged();
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
