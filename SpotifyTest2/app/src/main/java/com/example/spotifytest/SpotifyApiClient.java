package com.example.spotifytest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpotifyApiClient {

    private static final String TAG = "SpotifyApiClient";
    private static final String BASE_URL = "http://10.0.2.2:8000/users/";
    private OkHttpClient client;
    public static List<String> tracks = new ArrayList<>();
    public SpotifyApiClient() {
        client = new OkHttpClient();
    }


    //updates the top tracks for a given username.
    public void updateTopTracks(String username, List<String> topTracks) {
        try {
            // Convert topTracks list to JSON
            JSONObject jsonObject = new JSONObject();
            JSONArray tracksArray = new JSONArray(topTracks);
            jsonObject.put("tracks", tracksArray);
            String jsonString = jsonObject.toString();

            // Create request body
            RequestBody body = RequestBody.create(
                    jsonString,
                    MediaType.parse("application/json")
            );

            // Build request
            Request request = new Request.Builder()
                    .url(BASE_URL + username + "/toptracks")
                    .put(body)
                    .build();

            // Execute async call
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to update top tracks", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Top tracks updated successfully: " + response.body().string());
                    } else {
                        Log.e(TAG, "Error updating top tracks: " + response.code() + " " + response.message());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error preparing top tracks JSON", e);
        }
    }



    public void getTopTracks(String username) {
        String url = BASE_URL + username + "/toptracks";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API", "Failed to fetch top tracks", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray tracksArray = jsonObject.getJSONArray("topTracks");

                        tracks.clear();
                        for (int i = 0; i < tracksArray.length(); i++) {
                            tracks.add(tracksArray.getString(i));
                        }

                        // Log or update UI
                        Log.d("API", "Top tracks: " + tracks.toString());


                    } catch (JSONException e) {
                        Log.e("API", "JSON parsing error", e);
                    }
                } else {
                    Log.e("API", "Failed to fetch top tracks: " + response.message());
                }
            }
        });
    }

    public static List<String> returnTopTracks() {
        return new ArrayList<>(tracks); // Return a copy to avoid external modification
    }

    public void createUser(String username, String password, String name) {
        try {
            // Build JSON body
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            json.put("name", name);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            // Build POST request
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(body)
                    .build();

            // Execute async call
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to create user", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "User created successfully: " + response.body().string());
                    } else {
                        Log.e(TAG, "Error creating user: " + response.code() + " " + response.message());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error preparing JSON for user creation", e);
        }
    }

}

