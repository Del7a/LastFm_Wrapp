package com.example.philip.lastfmwrapper.listAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.philip.lastfmwrapper.models.Track;
import com.example.philip.lastfmwrapper.R;

import java.util.ArrayList;

/**
 * Created by philip on 23.04.17.
 */

public class ChartTrackAdapter extends ArrayAdapter<Track>{

    private final String TAG = "ChartTrackAdapter";
    private static String TAGs = "ChartTrackAdapter";


    public ChartTrackAdapter(Context context, ArrayList resource) {
        super(context, R.layout.track_row, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View artistChartView = inflater.inflate(R.layout.track_row, parent, false);

        Track tr = getItem(position);
        TextView tracktName = (TextView) artistChartView.findViewById(R.id.trackName);
        TextView artistPlays = (TextView) artistChartView.findViewById(R.id.trackPlays);
        TextView artistListeners = (TextView) artistChartView.findViewById(R.id.trackListeners);

        tracktName.setText(tr.songName + " - " + tr.artistName);
        artistPlays.setText(tr.playcount);
        artistListeners.setText(tr.listeners);

        return  artistChartView;
    }


}
