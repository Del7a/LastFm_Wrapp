package com.example.philip.lastfmwrapper;

import com.example.philip.lastfmwrapper.models.Artist;
import com.example.philip.lastfmwrapper.httpRequester.Requester;
import com.example.philip.lastfmwrapper.utils.JsonMapper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void GetJsonIsCorrect() throws Exception {
        String jSOn = new Requester().getJson("chart.gettopartists");

        assertNotNull(jSOn);
    }

    @Test
    public void ParseArtistJson() throws Exception {
        String jSOn = new Requester().getJson("chart.gettopartists&limit=3");
        Artist[] artistsArr = new JsonMapper().mapArtistFromJson(jSOn);
        assertNotNull(jSOn);
    }
}