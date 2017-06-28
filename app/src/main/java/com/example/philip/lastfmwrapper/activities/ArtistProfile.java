package com.example.philip.lastfmwrapper.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.database.ArtistContentProvider;
import com.example.philip.lastfmwrapper.database.ArtistTable;
import com.example.philip.lastfmwrapper.httpRequester.Requester;
import com.example.philip.lastfmwrapper.listAdapters.ChartArtistAdapter;
import com.example.philip.lastfmwrapper.utils.JsonMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ArtistProfile extends Activity {

    private ImageView pic;
    private TextView name;
    private TextView bio;
    private String _mbid;
    private Uri artistUri;
    private Button favButton;
    private String listeners;
    private String plays;
    private String imageUrl;
    final static String TAG = "ArtistProfile";
    final static String TAGs = "ArtistProfile";

    private Context _context;
    private Bundle _bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;
        _bundle = savedInstanceState;
        setContentView(R.layout.activity_artist_profile);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        pic = (ImageView) findViewById(R.id.artistMainPicture);
        name = (TextView) findViewById(R.id.profileArtistName);
        bio = (TextView) findViewById(R.id.profileArtistBio);


        Bundle extras = getIntent().getExtras();
        String artistNameIntent = extras.getString("name");
        name.setText(artistNameIntent);

        imageUrl = extras.getString("image_url");
        listeners = extras.getString("listeners");
        plays = extras.getString("plays");


        _mbid = extras.getString("mbid");
        favButton = (Button) findViewById(R.id.artistFave);

        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor c = getContentResolver().query(ArtistContentProvider.CONTENT_URI, null, ArtistTable.COLUMN_NAME + " = " + DatabaseUtils.sqlEscapeString(name.getText().toString()), null, null);
        if(c.getCount() > 0) {
            favButton.setTextColor(Color.parseColor("#00FF00"));
            favButton.setText("unlike");
        }


        pic.setTag(imageUrl);
        new RetrieveImageTask().execute(pic);
        new RetrieveRelatedArtistsTask().execute("artist.getinfo&artist=" + artistNameIntent);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLike(view);
            }
        });


//       fav.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//                artistUri = (_bundle == null) ? null : (Uri) _bundle
//                        .getParcelable(ArtistContentProvider.CONTENT_ITEM_TYPE);
//
//                ContentValues values = new ContentValues();
//                values.put(ArtistTable.COLUMN_NAME, name.getText().toString());
//////
//////                if (artistUri == null) {
//////                    // New artist
//////                    artistUri = getContentResolver().insert(
//////                            ArtistContentProvider.CONTENT_URI, values);
//////                } else {
//////                    // Update todo
//////                    getContentResolver().update(artistUri, values, null, null);
//////                }
//           }
//      });
    }

    public void onLike(View view) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ArtistTable.COLUMN_NAME, name.getText().toString());
        if(_mbid != null) {
            initialValues.put(ArtistTable.COLUMN_LASTFM_ID, _mbid.toString());
        }else{
            initialValues.put(ArtistTable.COLUMN_LASTFM_ID, name.getText().toString());
        }
        initialValues.put(ArtistTable.COLUMN_LISTENERS, listeners != null ? listeners: "0");
        initialValues.put(ArtistTable.COLUMN_IMG_URL, imageUrl);
        initialValues.put(ArtistTable.COLUMN_PLAYS, plays != null ? plays : "0");
        Uri contentUri = ArtistContentProvider.CONTENT_URI;//Uri.withAppendedPath(ArtistContentProvider.CONTENT_URI, ArtistTable.TABLE_ARTIST);

        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor c = getContentResolver().query(contentUri, null, ArtistTable.COLUMN_NAME + " = " + DatabaseUtils.sqlEscapeString(name.getText().toString()), null, null);
        if(c.getCount() == 0) {
            // not found in database
            cr.insert(contentUri, initialValues);
            favButton.setTextColor(Color.parseColor("#00FF00"));
            favButton.setText("unlike");
        }else{
            cr.delete(contentUri,ArtistTable.COLUMN_NAME+"=?", new String[] {name.getText().toString()});
            favButton.setTextColor(Color.parseColor("#00FFFF"));
            favButton.setText("like");
        }

        //ArtistContentProvider acp = new ArtistContentProvider();
        //acp.onCreate();
        //Uri resultUri = acp.insert(contentUri, initialValues);

        Snackbar.make(view, contentUri.toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    class RetrieveRelatedArtistsTask extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {
            return new Requester().getJson(urls[0]);
        }
        protected void onPostExecute(String response) {
            Artist[] art = new JsonMapper().mapSimilarArtist(response);


            ArrayList<Artist> artL = new ArrayList<Artist>( Arrays.asList(art));
            ListAdapter la = new ChartArtistAdapter(_context, artL);


            final ListView similarArtist = new ListView(_context);
            TableRow.LayoutParams tb = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            similarArtist.setBackgroundColor(45654);

            similarArtist.setLayoutParams(tb);

            similarArtist.setAdapter(la);
            similarArtist.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    Artist item = (Artist) similarArtist.getItemAtPosition(position);
                    Toast.makeText(view.getContext(), item.artistName, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(view.getContext(), ArtistProfile.class);
                    i.putExtra("name", item.artistName);
                    i.putExtra("image_url", item.imageLargeUrl);
                    i.putExtra("mbid", item.mbid);

                    startActivity(i);
                }
            });

            //set margin left
            final int dps = 15;
            final float scale = getBaseContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (dps * scale + 0.5f);

            TextView similarText = new TextView(_context);
            String simArtistisText = getResources().getString(R.string.title_similar_artists);
            similarText.setText(simArtistisText);
            similarText.setTextSize(14);
            similarText.setPadding(pixels,0,0,0);

            LinearLayout ll = (LinearLayout) findViewById(R.id.artistProfileLinearLayout);


            int intOrientation = getResources().getConfiguration().orientation;
            if (intOrientation == Configuration.ORIENTATION_PORTRAIT){
                ll.addView(similarText);
            }
            ll.addView(similarArtist);
        }

    }

    class RetrieveImageTask extends AsyncTask<ImageView, Void, Bitmap> {

        String imageTag;
        ImageView imageView = pic;
        @Override
        protected void onPreExecute() {
            imageTag = (String) imageView.getTag();
            imageView.setAlpha(0f);
        }

        protected Bitmap doInBackground(ImageView... urls) {
            imageView = urls[0];
            Bitmap result = null;
            String fileName = imageTag.substring(imageTag.lastIndexOf('/') + 1);
            //String baseDir = "mnt/sdcard/fmWrap";
            String imageNetworkUrl = imageTag;
            //String fullPath = baseDir + '/' + fileName;
            //File picDirectory = new File(baseDir, fileName);
            result = loadBitmap(getApplicationContext(),fileName);
            if (result!=null) {
                return result;
            }
            Bitmap bm = new Requester().getBitmapFromURL(imageNetworkUrl);
            saveFile(getApplicationContext(),bm, fileName);
            result = bm;

            return result;
        }
        protected void onPostExecute(Bitmap response) {
            //TransitionManager.beginDelayedTransition(_parent);
            imageView.setImageBitmap(response);
            imageView.animate().alpha(1f).setDuration(350);
        }

        public void saveFile(Context context, Bitmap b, String picName){
            FileOutputStream fos;
            try {
                fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
            catch (FileNotFoundException e) {
                Log.d(TAGs, "file not found");
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.d(TAGs, "io exception");
                e.printStackTrace();
            }

        }

        public Bitmap loadBitmap(Context context, String picName){
            Bitmap b = null;
            FileInputStream fis;
            try {
                fis = context.openFileInput(picName);
                b = BitmapFactory.decodeStream(fis);
                fis.close();

            }
            catch (FileNotFoundException e) {
                Log.d(TAG, "file not found");
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.d(TAG, "io exception");
                e.printStackTrace();
            }
            return b;
        }
    }
}
