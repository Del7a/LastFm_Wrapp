package com.example.philip.lastfmwrapper.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.philip.lastfmwrapper.activities.ArtistProfile;
import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.httpRequester.Requester;
import com.example.philip.lastfmwrapper.utils.JsonMapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    private EditText searchText;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchButton = (Button) inflated.findViewById(R.id.artistSearchBtn);
        searchText = (EditText) inflated.findViewById(R.id.searchArtistInput);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputString = searchText.getText().toString();
                if(inputString != null && inputString.length() > 0){
                    new SearchArtistTask().execute("artist.search&artist=" + inputString + "&limit=1");
                }else{
                    final Toast toast = Toast.makeText(getContext(), "Enter some text", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


        return inflated;
    }




    class SearchArtistTask extends AsyncTask<String, Void, String> {
        private  final int animationDelay = 350;

        protected String doInBackground(String... urls) {
            return new Requester().getJson(urls[0]);
        }
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(String response) {
            try {
                Artist[] art = new JsonMapper().mapArtistSearchResult(response);
                Artist a = art[0];


                Intent i = new Intent(getContext(), ArtistProfile.class);
                i.putExtra("name", a.artistName);
                i.putExtra("image_url", a.imageLargeUrl);
                i.putExtra("mbid", a.mbid);
                i.putExtra("plays", a.playcount);
                i.putExtra("listenets", a.listeners);
                startActivity(i);


            }catch (Exception ex){
                Log.d("PARSE", ex.toString());
                Toast.makeText(getContext(), "Error occurred. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }


}
