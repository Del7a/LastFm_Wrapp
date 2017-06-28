package com.example.philip.lastfmwrapper.listAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.R;
import com.example.philip.lastfmwrapper.httpRequester.Requester;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by philip on 23.04.17.
 */

public class ChartArtistAdapter extends ArrayAdapter<Artist>{

    private final String TAG = "ChartArtistAdapter";
    private static String TAGs = "ChartArtistAdapter";

    private ImageView _artistPicture;
    private  ViewGroup _parent;

    public ChartArtistAdapter(Context context, ArrayList<Artist> resource) {
        super(context, R.layout.artist_row, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        _parent = parent;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View artistChartView = inflater.inflate(R.layout.artist_row, parent, false);

        Artist cArt = getItem(position);

        if(cArt != null){
            TextView artistName = (TextView) artistChartView.findViewById(R.id.artistName);
            TextView artistPlays = (TextView) artistChartView.findViewById(R.id.artistPlays);
            TextView artistListeners = (TextView) artistChartView.findViewById(R.id.artistListeners);
            TextView artistPlaysMainText = (TextView) artistChartView.findViewById(R.id.artistPlaysMainText);
            TextView artistListenersMainText = (TextView) artistChartView.findViewById(R.id.artistListenersMainText);
            ImageView artistPicture = (ImageView)  artistChartView.findViewById(R.id.artistImage);
            _artistPicture = artistPicture;

            artistName.setText(cArt.artistName);
            artistPlays.setText(cArt.playcount);
            artistListeners.setText(cArt.listeners);
            artistPicture.setTag(cArt.imageLargeUrl);

            if(cArt.playcount.length() == 0 || cArt.playcount == "0"){
                artistPlaysMainText.setVisibility(View.GONE);
            }
            if(cArt.listeners.length() == 0 || cArt.listeners == "0"){
                artistListenersMainText.setVisibility(View.GONE);
            }


            new RetrieveImageTask().execute(artistPicture);
        }

        return  artistChartView;
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
            result = loadBitmap(getContext(),fileName);
            if (result!=null) {
                return result;
            }
                Bitmap bm = new Requester().getBitmapFromURL(imageNetworkUrl);
                saveFile(getContext(),bm, fileName);
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
