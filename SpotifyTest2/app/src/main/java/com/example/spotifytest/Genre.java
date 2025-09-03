package com.example.spotifytest;

public class Genre implements Comparable<Genre> {
    String genreName;
    int personalGenreRank;

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public Genre(String genreName, int personalGenreRank) {
        this(genreName);
        this.personalGenreRank = personalGenreRank;
    }

    public int getPersonalGenreRank() {
        return personalGenreRank;
    }

    public String getGenreName() {
        return genreName;
    }

    @Override
    public int compareTo(Genre o) {
        Genre g = (Genre) o;
        if (this.getPersonalGenreRank() < g.getPersonalGenreRank()) {
            return 1;
        } else if (this.getPersonalGenreRank() > g.getPersonalGenreRank()) {
            return -1;
        }
        return 0;
    }
}
