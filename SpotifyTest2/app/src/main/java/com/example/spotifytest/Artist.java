package com.example.spotifytest;

public class Artist implements Comparable<Artist> {
    private String artistName;
    private int monthlyListeners; //number of monthly listeners the artist has

    private int personaArtistRank; //the rank of the song in your library
    public Artist(String artistName, int monthlyListeners) {
        this.artistName = artistName;
        this.monthlyListeners = monthlyListeners;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getMonthlyListeners() {
        return monthlyListeners;
    }

    public int getPersonaArtistRank() {
        return personaArtistRank;
    }

    @Override
    public int compareTo(Artist o) {
        if (this.getPersonaArtistRank() < o.getPersonaArtistRank()) {
            return 1;
        } else if (this.getPersonaArtistRank() > o.getPersonaArtistRank()) {
            return -1;
        }
        return 0;
    }
}
