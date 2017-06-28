package com.example.philip.lastfmwrapper.utils;

import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.models.Tag;
import com.example.philip.lastfmwrapper.models.Track;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by philip on 23.04.17.
 */

public class JsonMapper {


    public Artist[] mapArtistFromJson(String jsonSrt){
        Artist[] results = null;
        try {
            //jsonSrt = jsonSrt.substring(jsonSrt.indexOf("{"), jsonSrt.lastIndexOf("}") + 1);
            JSONObject json  = new JSONObject(jsonSrt);
            JSONObject artistJsonArr = json.getJSONObject("artists");
            JSONArray artistAr = artistJsonArr.getJSONArray("artist");
            int numberOfArtist = artistAr.length();
            results = new Artist[numberOfArtist];
            for (int i = 0; i < numberOfArtist; i++){
                Artist art = new Artist();
                JSONObject jObj = artistAr.optJSONObject(i);
                art.artistName = jObj.getString("name");
                art.listeners = jObj.getString("listeners");
                art.playcount = jObj.getString("playcount");
                art.mbid = jObj.optString("mbid");
                art.imageLargeUrl = jObj.getJSONArray("image").optJSONObject(3).optString("#text");
                results[i] = art;
            }
        }catch (Exception ex){
            //Log.d("PARSER",ex.toString());
        }

        return results;
    }

    public Artist[] mapSimilarArtist(String jsonSrt){
        Artist[] results = null;
        try {
            //jsonSrt = jsonSrt.substring(jsonSrt.indexOf("{"), jsonSrt.lastIndexOf("}") + 1);
            JSONObject json  = new JSONObject(jsonSrt);
            JSONObject artistJsonArr = json.getJSONObject("artist").getJSONObject("similar");
            JSONArray artistAr = artistJsonArr.getJSONArray("artist");
            int numberOfArtist = artistAr.length();
            results = new Artist[numberOfArtist];
            for (int i = 0; i < numberOfArtist; i++){
                Artist art = new Artist();
                JSONObject jObj = artistAr.optJSONObject(i);
                //JSONObject stats = jObj.optJSONObject("stats");
                art.artistName = jObj.getString("name");
                //Similar artist don't have playcount and listeners stats
                art.listeners = ""; //stats.optString("listeners");
                art.playcount = ""; //stats.optString("playcount");
                art.imageLargeUrl = jObj.getJSONArray("image").optJSONObject(3).optString("#text");
                results[i] = art;
            }
        }catch (Exception ex){
            //Log.d("PARSER",ex.toString());
        }

        return results;
    }

    public Track[] mapTracksFromJson(String jsonStr){
        Track[] results = null;
        try {
            //jsonSrt = jsonSrt.substring(jsonSrt.indexOf("{"), jsonSrt.lastIndexOf("}") + 1);
            JSONObject json  = new JSONObject(jsonStr);
            JSONObject tarackJsonArr = json.getJSONObject("tracks");
            JSONArray artistAr = tarackJsonArr.getJSONArray("track");
            int numberOfTracks = artistAr.length();
            results = new Track[numberOfTracks];
            for (int i = 0; i < numberOfTracks; i++){
                Track tr = new Track();
                JSONObject jObj = artistAr.optJSONObject(i);
                tr.songName = jObj.getString("name");
                tr.listeners = jObj.getString("listeners");
                tr.playcount = jObj.getString("playcount");
                //tr.imageLargeUrl = jObj.getJSONArray("image").optJSONObject(3).getString("#text");
                tr.artistName = jObj.getJSONObject("artist").getString("name");
                results[i] = tr;
            }
        }catch (Exception ex){
            //Log.d("PARSER",ex.toString());
        }

        return results;
    }

    public Tag[] mapTagsFromJson(String jsonStr){
        Tag[] results = null;
        try {
            //jsonSrt = jsonSrt.substring(jsonSrt.indexOf("{"), jsonSrt.lastIndexOf("}") + 1);
            JSONObject json  = new JSONObject(jsonStr);
            JSONObject tagJsonArr = json.getJSONObject("tags");
            JSONArray tagJson = tagJsonArr.getJSONArray("tag");
            int numberOfTags = tagJson.length();
            results = new Tag[numberOfTags];
            for (int i = 0; i < numberOfTags; i++){
                Tag tag = new Tag();
                JSONObject jObj = tagJson.optJSONObject(i);
                tag.tagName = jObj.getString("name");
                tag.reach = jObj.getString("reach");
                tag.taggings = jObj.getString("taggings");
                //tr.imageLargeUrl = jObj.getJSONArray("image").optJSONObject(3).getString("#text");
                JSONObject wikiJson = jObj.getJSONObject("wiki");
                tag.summary = wikiJson.optString("summary", "");
                tag.content = wikiJson.optString("content", "");
                results[i] = tag;
            }
        }catch (Exception ex){
            //Log.d("PARSER",ex.toString());
        }

        return results;
    }


    public Artist[] mapArtistSearchResult(String jsonStr){
        Artist[] results = null;
        try {
            //jsonSrt = jsonSrt.substring(jsonSrt.indexOf("{"), jsonSrt.lastIndexOf("}") + 1);
            JSONObject json  = new JSONObject(jsonStr);
            json = json.getJSONObject("results");
            JSONObject artistJsonArr = json.getJSONObject("artistmatches");
            JSONArray artistAr = artistJsonArr.getJSONArray("artist");
            int numberOfArtist = artistAr.length();
            results = new Artist[numberOfArtist];
            for (int i = 0; i < numberOfArtist; i++){
                Artist art = new Artist();
                JSONObject jObj = artistAr.optJSONObject(i);
                art.artistName = jObj.getString("name");
                art.listeners = jObj.optString("listeners");
                art.playcount = jObj.optString("playcount");
                art.mbid = jObj.optString("mbid");
                art.imageLargeUrl = jObj.getJSONArray("image").optJSONObject(3).optString("#text");
                results[i] = art;
            }
        }catch (Exception ex){
            //Log.d("PARSER",ex.toString());
        }

        return results;
    }
}
