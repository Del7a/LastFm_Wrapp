package com.example.philip.lastfmwrapper.listAdapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.database.ArtistTable;
import com.example.philip.lastfmwrapper.httpRequester.Requester;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by philip on 30.04.17.
 */

public class ArtistCursorAdapter extends CursorAdapter {

    private final String TAG = "ChartArtistAdapter";
    private static String TAGs = "ChartArtistAdapter";

    private ImageView _artistPicture;
    private Context _context;
    private LayoutInflater cursorInflater;

    public ArtistCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        _context = parent.getContext();
        return cursorInflater.inflate(R.layout.artist_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template

        TextView artistName = (TextView) view.findViewById(R.id.artistName);
        TextView artistPlays = (TextView) view.findViewById(R.id.artistPlays);
        TextView artistListeners = (TextView) view.findViewById(R.id.artistListeners);
        ImageView artistPicture = (ImageView)  view.findViewById(R.id.artistImage);
        _artistPicture = artistPicture;

        String aName = cursor.getString(cursor.getColumnIndex(ArtistTable.COLUMN_NAME));
        if (aName != null)
            artistName.setText(aName);
        String aPlays = cursor.getString(cursor.getColumnIndex(ArtistTable.COLUMN_PLAYS));
        if (aPlays != null)
            artistPlays.setText(aPlays);
        String aListeners = cursor.getString(cursor.getColumnIndex(ArtistTable.COLUMN_LISTENERS));
        if ( aListeners != null)
            artistListeners.setText(aListeners);
        String aImgUrl = cursor.getString(cursor.getColumnIndex(ArtistTable.COLUMN_IMG_URL));
        if(aImgUrl != null) {
            artistPicture.setTag(aImgUrl);
            new RetrieveImageTask().execute(artistPicture);
        }
    }

    class RetrieveImageTask extends AsyncTask<ImageView, Void, Bitmap> {

        String imageTag;
        ImageView imageView = _artistPicture;
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
            result = loadBitmap(_context,fileName);
            if (result!=null) {
                return result;
            }
            Bitmap bm = new Requester().getBitmapFromURL(imageNetworkUrl);
            saveFile(_context, bm, fileName);
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
