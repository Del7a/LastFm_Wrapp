package com.example.philip.lastfmwrapper.listAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.philip.lastfmwrapper.models.Tag;
import com.example.philip.lastfmwrapper.R;

import java.util.ArrayList;

/**
 * Created by philip on 23.04.17.
 */

public class ChartTagAdapter extends ArrayAdapter<Tag>{

    private final String TAG = "ChartTagAdapter";
    private static String TAGs = "ChartTagAdapter";


    public ChartTagAdapter(Context context, ArrayList<Tag> resource) {
        super(context, R.layout.tag_row, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View artistChartView = inflater.inflate(R.layout.tag_row, parent, false);

        Tag tag = getItem(position);
        TextView tagName = (TextView) artistChartView.findViewById(R.id.tagName);
        TextView reach = (TextView) artistChartView.findViewById(R.id.tagReach);
        TextView taggings = (TextView) artistChartView.findViewById(R.id.tagTaggings);

        tagName.setText(tag.tagName);
        reach.setText(tag.reach);
        taggings.setText(tag.taggings);

        return  artistChartView;
    }


}
