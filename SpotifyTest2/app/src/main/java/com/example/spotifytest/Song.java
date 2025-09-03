package com.example.spotifytest;

public class Song implements Comparable<Song> {
    private String songName; //name of song
    private int numListens; //number of listens (total)
    private int yourListens; //number of listens (personal)
    private int personalSongRank; //the rank of the song in your library

    public Song(String songName, int personalSongRank) {
        this.songName = songName;
        this.numListens = 0;
        this.yourListens = 0;
    }

    public Song(String songName, int personalSongRank, int numListens, int yourListens) {
        this(songName, personalSongRank);
        this.numListens = numListens;
        this.yourListens = yourListens;
    }

    @Override
    public int compareTo(Song o) {
        Song comparedSong = (Song) o;
        int result = 0;
        if (this.personalSongRank < comparedSong.getPersonalSongRank()) {
            result = 1;
        } else if (this.personalSongRank == comparedSong.getPersonalSongRank()) {
            result = 0;
        } else {
            result = -1;
        }
        return result;
    }

    public int getPersonalSongRank() {
        return personalSongRank;
    }
}
