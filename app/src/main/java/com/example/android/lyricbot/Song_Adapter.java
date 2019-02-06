package com.example.android.lyricbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Song_Adapter extends ArrayAdapter {
    public Song_Adapter(Context context, ArrayList<One_Song> songs) {
        super(context,0,songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View List_Song = convertView;

        if(List_Song == null){
            List_Song = LayoutInflater.from(getContext()).inflate(R.layout.one_song,parent,false);
        }

        One_Song song = (One_Song) getItem(position);

        TextView songname = List_Song.findViewById(R.id.song_name);
        songname.setText(song.getTitle());

        TextView artist = List_Song.findViewById(R.id.song_artist);
        artist.setText(song.getArtist());

        TextView song_year = List_Song.findViewById(R.id.song_year);
        song_year.setText(song.getRating());

        return List_Song;
    }
}
