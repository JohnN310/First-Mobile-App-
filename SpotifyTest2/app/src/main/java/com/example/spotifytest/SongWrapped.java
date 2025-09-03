package com.example.spotifytest;

import java.util.List;

public class SongWrapped {
    private String date;
    private String timeFrame;
    private List<String> songs;
    public SongWrapped(String date, String timeFrame, List<String> top5tracks) {
        this.date = date;
        this.timeFrame = timeFrame;
        this.songs = top5tracks;
    }

    public List<String> getSongs() {
        return songs;
    }

    public String getDate() {
        return date;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

}
