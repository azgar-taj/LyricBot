package com.example.android.lyricbot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.search_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = findViewById(R.id.search_edit_frame);
                findSongs must = new findSongs();
                must.execute();
            }
        });
    }
    private class findSongs extends AsyncTask<String,Integer,ArrayList<One_Song>>{
        public static final String SONG_SEARCH_URL = "http://api.musixmatch.com/ws/1.1/track.search?apikey=964651e6fa6211d1e802ef2a540707d4&page_size=10&s_track_rating=desc&q_track=";
        @Override
        protected ArrayList<One_Song> doInBackground(String... strings) {
            String song_name = txt.getText().toString().replaceAll(" ","+");
            BufferedReader br = null;
            HttpURLConnection connection = null;
            StringBuilder reqJson = new StringBuilder();

            try {
                URL url = new URL(SONG_SEARCH_URL+song_name);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);

                connection.connect();

                InputStream inx = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(inx, Charset.forName("UTF-8")));
                String line = br.readLine();
                while (line != null) {
                    reqJson.append(line);
                    line = br.readLine();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
                return convertToJSON(reqJson.toString());
            }
        }

        @Override
        protected void onPostExecute(ArrayList<One_Song> one_songs) {
            ListView lst = findViewById(R.id.reqList);
            Song_Adapter adapter = new Song_Adapter(getApplicationContext(),one_songs);
            lst.setAdapter(adapter);
        }
    }
    private ArrayList<One_Song> convertToJSON(String songs){
        ArrayList<One_Song> songsAL = new ArrayList<>();
        try {
            JSONObject songsJSON = new JSONObject(songs);
            JSONObject songs_message = songsJSON.getJSONObject("message");
            JSONObject songs_body = songs_message.getJSONObject("body");
            JSONArray tracks = songs_body.getJSONArray("track_list");
            for(int i = 0; i < tracks.length(); i++){
                JSONObject track1 = tracks.getJSONObject(i);
                JSONObject track = track1.getJSONObject("track");
                One_Song song = new One_Song();
                song.setArtist(track.getString("artist_name"));
                song.setID(track.getInt("track_id")+"");
                song.setTitle(track.getString("track_name"));
                song.setRating(track.getString("track_rating"));

                songsAL.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return songsAL;
        }
    }
}
