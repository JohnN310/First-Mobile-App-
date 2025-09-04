// 03-30-24 ta logic
package com.example.spotifytest;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyApiHelperArtists {
    public static List<String> topArtists;

    public static List<String> topGenres;

    private String mAccessToken;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    public void fetchUserTopArtists(String accessToken, String timeRange, int limit, ListView listView) {
        if (accessToken == null) {
            Toast.makeText(listView.getContext(), "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAccessToken = accessToken;

        String url = "https://api.spotify.com/v1/me/top/artists?time_range=" + timeRange + "&limit=" + limit;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall(); // cancel previous call if any

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(listView.getContext(), "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray items = jsonObject.getJSONArray("items");

                        List<String> artistNames = new ArrayList<>();
                        topGenres = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject artist = items.getJSONObject(i);
                            JSONArray genresArray = artist.getJSONArray("genres");
                            String artistName = artist.getString("name");
                            artistNames.add(artistName);
                            for (int j = 0; j < genresArray.length(); j++) {
                                String genre = genresArray.getString(j);
                                // Add the genre to the list of all genres
                                if (!topGenres.contains(genre)) {
                                    topGenres.add(genre);
                                }
                            }
                        }
                        topArtists = artistNames;

                        listView.post(() -> {
                            CustomArrayAdapter adapter = new CustomArrayAdapter(listView.getContext(), topArtists);
                            listView.setAdapter(adapter);
                        });
                    } else {
                        // Handling unsuccessful response
                        Log.d("HTTP", "Failed to fetch data: " + response.code());
                        listView.post(() -> Toast.makeText(listView.getContext(), "Failed to fetch data: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                } catch (JSONException e) {
                    // Handling JSON parsing error
                    Log.d("JSON", "Failed to parse data: " + e);
                    listView.post(() -> Toast.makeText(listView.getContext(), "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    private void cancelCall() {
        mOkHttpClient.dispatcher().cancelAll();
    }
}
