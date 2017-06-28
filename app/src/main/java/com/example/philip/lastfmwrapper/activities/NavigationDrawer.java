package com.example.philip.lastfmwrapper.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.philip.lastfmwrapper.IFragmentInteraction;
import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.activities.ArtistProfile;
import com.example.philip.lastfmwrapper.activities.SettingsActivity;
import com.example.philip.lastfmwrapper.fragments.ArtistListViewFragment;
import com.example.philip.lastfmwrapper.fragments.ArtistsListViewCursourFragment;
import com.example.philip.lastfmwrapper.fragments.LandingScreenFragment;
import com.example.philip.lastfmwrapper.fragments.SearchFragment;
import com.example.philip.lastfmwrapper.fragments.TagsListViewFragment;
import com.example.philip.lastfmwrapper.fragments.TracksListViewFragment;
import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.models.Tag;
import com.example.philip.lastfmwrapper.models.Track;
import com.example.philip.lastfmwrapper.httpRequester.Requester;
import com.example.philip.lastfmwrapper.utils.JsonMapper;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        IFragmentInteraction.OnFragmentInteractionListener,
        TracksListViewFragment.OnTrackInteractionListener {

    private ArtistListViewFragment topArtistFragment;
    private TracksListViewFragment topTracksFragment;
    private TagsListViewFragment topTagsFragment;
    private ArtistsListViewCursourFragment myArtistFragment;
    private ProgressBar mainProgressBar;
    private  final int animationDelay = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ProgressBar mainProgressBar = (ProgressBar) findViewById(R.id.mainContentProgressBar);
        mainProgressBar.setAlpha(0f);
        mainProgressBar.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LandingScreenFragment landingScreenFragment = LandingScreenFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainLineaLayout, landingScreenFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);



        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String displayName = sharedPref.getString("display_name","");
        String displayEmail = sharedPref.getString("display_email", "");
        TextView drawerUserName = (TextView) findViewById(R.id.drawerUserName);
        drawerUserName.setText(displayName);
        TextView drawerEmail = (TextView) findViewById(R.id.drawerUserEmail);
        drawerEmail.setText(displayEmail);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.artist_chart) {
            // Handle the camera action

            ArtistListViewFragment alvf = ArtistListViewFragment.newInstance();
            mainProgressBar = (ProgressBar) findViewById(R.id.mainContentProgressBar);
            mainProgressBar.setAlpha(0f);
            mainProgressBar.setVisibility(View.VISIBLE);
            mainProgressBar.animate().alpha(1f).setDuration(animationDelay);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainLineaLayout, alvf);
            fragmentTransaction.commit();

            topArtistFragment = alvf;

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String artistsLimit = sharedPref.getString("sync_artists", "20");

            new RetrieveArtistChartTask().execute("chart.gettopartists&limit=" + artistsLimit);

        }else if(id == R.id.artist_personal){
            ArtistsListViewCursourFragment alvf = ArtistsListViewCursourFragment.newInstance();
            mainProgressBar = (ProgressBar) findViewById(R.id.mainContentProgressBar);
            mainProgressBar.setAlpha(0f);
            mainProgressBar.setVisibility(View.VISIBLE);
            mainProgressBar.animate().alpha(1f).setDuration(animationDelay);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainLineaLayout, alvf);
            fragmentTransaction.commit();

            myArtistFragment = alvf;

            mainProgressBar.animate().alpha(0f).setDuration(animationDelay);


        }else if(id == R.id.track_chart){

            TracksListViewFragment tlvf = TracksListViewFragment.newInstance();
            mainProgressBar = (ProgressBar) findViewById(R.id.mainContentProgressBar);
            mainProgressBar.setAlpha(0f);
            mainProgressBar.setVisibility(View.VISIBLE);
            mainProgressBar.animate().alpha(1f).setDuration(animationDelay);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainLineaLayout, tlvf);
            fragmentTransaction.commit();

            topTracksFragment = tlvf;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String trackLimit = sharedPref.getString("sync_tracks", "20");

            new RetrieveTracksChartTask().execute("chart.gettoptracks&limit=" + trackLimit);
        }
        else if(id == R.id.tag_chart){
            TagsListViewFragment tlvf = TagsListViewFragment.newInstance();
            mainProgressBar = (ProgressBar) findViewById(R.id.mainContentProgressBar);
            mainProgressBar.setAlpha(0f);
            mainProgressBar.setVisibility(View.VISIBLE);
            mainProgressBar.animate().alpha(1f).setDuration(animationDelay);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainLineaLayout, tlvf);
            fragmentTransaction.commit();

            topTagsFragment = tlvf;

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String tagsLimit = sharedPref.getString("sync_tags", "20");
            new RetrieveTagsChartTask().execute("chart.gettoptags&limit=" + tagsLimit);
        }
        else if (id == R.id.artist_search) {
            SearchFragment sf = SearchFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainLineaLayout, sf);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String name, String imageUrl, String mbid, String plays, String listeners) {
        Intent i = new Intent(getApplicationContext(), ArtistProfile.class);
        i.putExtra("name", name);
        i.putExtra("image_url", imageUrl);
        i.putExtra("mbid", mbid);
        i.putExtra("plays", plays);
        i.putExtra("listeners", listeners);
        startActivity(i);
    }

    public  void onTrackInteraction(String trackName){
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setAction(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.setComponent(new ComponentName(
                "com.spotify.music",
                "com.spotify.music.MainActivity"));
        intent.putExtra(SearchManager.QUERY, trackName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(myArtistFragment != null){
            myArtistFragment.onResume();
        }
    }

    class RetrieveArtistChartTask extends AsyncTask<String, Void, String> {
        private  final int animationDelay = 350;

        protected String doInBackground(String... urls) {
            return new Requester().getJson(urls[0]);
        }
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(String response) {
            try {
                Artist[] art = new JsonMapper().mapArtistFromJson(response);

                if(topArtistFragment == null)
                    return;

                for (Artist a: art){
                    topArtistFragment.addArtist(a);
                }

                mainProgressBar.setAlpha(1f);
                mainProgressBar.animate().alpha(0f).setDuration(animationDelay).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mainProgressBar.setVisibility(View.GONE);
                    }
                });

                topArtistFragment.getView().setAlpha(0f);
                topArtistFragment.getView().animate().alpha(1f).setDuration(animationDelay);

            }catch (Exception ex){
                Log.d("PARSE", ex.toString());
            }
        }
    }


    class RetrieveTracksChartTask extends AsyncTask<String, Void, String> {
        private  final int animationDelay = 350;

        protected String doInBackground(String... urls) {
            return new Requester().getJson(urls[0]);
        }
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(String response) {
            try {
                Track[] tr = new JsonMapper().mapTracksFromJson(response);
                //ListAdapter la = new ChartTrackAdapter(_context, tr);

                for(Track t: tr){
                    topTracksFragment.addTrack(t);
                }

                mainProgressBar.setAlpha(1f);
                mainProgressBar.animate().alpha(0f).setDuration(animationDelay).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mainProgressBar.setVisibility(View.GONE);
                    }
                });

                topTracksFragment.getView().setAlpha(0f);
                topTracksFragment.getView().animate().alpha(1f).setDuration(animationDelay);

            }catch (Exception ex){
                Log.d("PARSE", ex.toString());
            }
        }
    }
    class RetrieveTagsChartTask extends AsyncTask<String, Void, String> {
        private final int animationDelay = 350;

        protected String doInBackground(String... urls) {
            return new Requester().getJson(urls[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(String response) {
            try {
                Tag[] tags = new JsonMapper().mapTagsFromJson(response);

                for (Tag t: tags){
                    topTagsFragment.addTag(t);
                }

                ListView topTrackListView = (ListView) findViewById(R.id.listViewTags);
                //topTrackListView.setAdapter(la);

                mainProgressBar.setAlpha(1f);
                mainProgressBar.animate().alpha(0f).setDuration(animationDelay).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mainProgressBar.setVisibility(View.GONE);
                    }
                });

                topTagsFragment.getView().setAlpha(0f);
                topTagsFragment.getView().animate().alpha(1f).setDuration(animationDelay);

            } catch (Exception ex) {
                Log.d("PARSE", ex.toString());
            }
        }

    }
}
