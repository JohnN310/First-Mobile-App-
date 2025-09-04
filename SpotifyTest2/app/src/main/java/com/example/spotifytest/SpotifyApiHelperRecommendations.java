package com.example.spotifytest;

import android.os.AsyncTask;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SpotifyApiHelperRecommendations {

    private static String accessToken;

    private ListView listView;
    static SpotifyDataListener mListener; // listener to handle data received from Spotify API



    public interface SpotifyDataListener {
        void onDataReceived(List<String> recommendedTracks);
        void onError(String errorMessage);
    }

    public void fetchDataFromSpotify(List<String> topTracksIds, String accessToken) throws JSONException, IOException, ExecutionException, InterruptedException {
        this.listView = listView;
        this.accessToken = accessToken;
        getRecommendations(topTracksIds, accessToken);
    }

    public static List<String> getRecommendations(List<String> topTracksIds, String accessToken) throws JSONException, IOException, InterruptedException, ExecutionException {
        String seedTracks = String.join(",", topTracksIds);
        return new FetchRecommendationsTask().execute(seedTracks, accessToken).get();
    }

    private static class FetchRecommendationsTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... seedTracks) {
            List<String> recommendedTracks = new ArrayList<>();
            try {
                String response = fetchWebApi("v1/recommendations?limit=20&seed_tracks=" + seedTracks[0], "GET", null);
                JSONObject jsonResponse = new JSONObject(response);
                return parseRecommendedTracks(jsonResponse);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return recommendedTracks;
        }

        @Override
        protected void onPostExecute(List<String> recommendedTracks) {
            if (mListener != null) {
                mListener.onDataReceived(recommendedTracks);
            }
        }
    }

    private static List<String> parseRecommendedTracks(JSONObject jsonResponse) throws JSONException {
        List<String> recommendedTracks = new ArrayList<>();
        JSONArray tracksArray = jsonResponse.getJSONArray("tracks");
        int index = 0;
        for (int i = 0; i < tracksArray.length(); i++) {
            if (index == 6) {
                break;
            }
            JSONObject track = tracksArray.getJSONObject(i);
            String name = track.getString("name");
            JSONArray artists = track.getJSONArray("artists");
            List<String> artistNames = new ArrayList<>();

            artistNames.add(artists.getJSONObject(0).getString("name"));
            if (!SpotifyApiHelperArtists.topArtists.contains(artists.getJSONObject(0).getString("name")) && !SpotifyApiHelper.topArtistNamesSongs.contains(artists.getJSONObject(0).getString("name"))) {
                recommendedTracks.add(String.join(", ", artistNames));
                index++;
            }
        }
        return recommendedTracks;
    }

    private static String fetchWebApi(String endpoint, String method, JSONObject body) throws IOException {
        String baseUrl = "https://api.spotify.com/";
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        if (body != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        StringBuilder response = new StringBuilder();
        try (InputStream inputStream = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        connection.disconnect();
        return response.toString();
    }

}

