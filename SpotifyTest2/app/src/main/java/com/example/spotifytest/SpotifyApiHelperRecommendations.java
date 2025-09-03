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
    static SpotifyDataListener mListener; // Listener to handle data received from Spotify API



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
//
//package com.example.spotifytest;
//import android.content.Context;
//import android.util.Log;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class SpotifyApiHelperRecommendations {
//
//    private OkHttpClient mOkHttpClient = new OkHttpClient();
//    private Call mCall;
//    private String mAccessToken;
//
//    public void getRecommendedArtists(String accessToken, ListView listView) {
//        mAccessToken = accessToken;
//
//        if (accessToken == null) {
//            Log.e("SpotifyApiHelper", "Access token is null");
//            // Optionally display a Toast message to the user
//            return;
//        }
//
//        String seedTracks = String.join(",", SpotifyApiHelper.topTrackIds);
//        final Request request = new Request.Builder()
//                .url("https://api.spotify.com/v1/recommendations?limit=20&seed_tracks=" + seedTracks)
//                .addHeader("Authorization", "Bearer " + accessToken)
//                .build();
//
//        cancelCall();
//        mCall = mOkHttpClient.newCall(request);
//
//        mCall.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("SpotifyApiHelper", "Failed to fetch data: " + e.getMessage());
//                // Optionally display a Toast message to the user
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                try {
//                    if (!response.isSuccessful()) {
//                        throw new IOException("Unexpected response code: " + response);
//                    }
//
//                    final JSONObject jsonObject = new JSONObject(response.body().string());
//                    JSONArray items = jsonObject.getJSONArray("items");
//                    List<String> recommendedArtists = new ArrayList<>();
//
//                    for (int i = 0; i < items.length(); i++) {
//                        JSONObject artist = items.getJSONObject(i);
//                        String artistName = artist.getString("name");
//                        recommendedArtists.add(artistName);
//                    }
//
//                    if (listView != null) {
//                        listView.post(() -> {
//                            CustomArrayAdapter adapter = new CustomArrayAdapter(listView.getContext(), recommendedArtists);
//                            listView.setAdapter(adapter);
//                        });
//                    } else {
//                        Log.e("SpotifyApiHelper", "ListView is null");
//                        // Optionally display a Toast message to the user
//                    }
//                } catch (JSONException e) {
//                    Log.e("SpotifyApiHelper", "Failed to parse data: " + e.getMessage());
//                    // Optionally display a Toast message to the user
//                }
//            }
//        });
//    }
//
//    private void cancelCall() {
//        if (mCall != null) {
//            mCall.cancel();
//        }
//    }
//}
