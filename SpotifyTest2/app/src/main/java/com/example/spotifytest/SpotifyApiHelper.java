//package com.example.spotifytest;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.viewpager.widget.ViewPager;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SpotifyApiHelper {
//
//    // Your Spotify access token
//    private static final String TOKEN = "BQCD7HOi9KR_Bjisa4sMCJ6QxmjjEu_xGzfaWNmmejsqoLTTikz0gm__FNB2O2aDV-nrlvS-FHmeO-pTDJQGNAj6chT9Txj3D3ILVTuPHCFIECbzADI25NNO_Pd4UfDwVjDJy_wdFfhEwC1PXCxSeeQ92zAiI0z_cpjXkXxfsrBnaf7tZ2t9rKucOYm9phx3YM0EopwAXG13BFc084pWbKrhuLd65A75WO9Azk6HJRghOs2-O-EZVhKf_kJXyHA7QA-snZeK0hcEs6ScgJrD95ro";
//    // Reference to ListView
//    private ListView listView;
//    public static List<String> topTrackIds;
//
//    public static List<String> topTrackNames;
//
//    // Method to fetch data from Spotify Web API
//    // Interface to handle data received from Spotify API
//    public interface SpotifyDataListener {
//        void onDataReceived(JSONObject data);
//        void onError(String errorMessage);
//    }
//    public void fetchDataFromSpotify(String endpoint, String method, JSONObject body, ListView listView) {
//        this.listView = listView;
//        new FetchDataTask().execute(endpoint, method, body != null ? body.toString() : null);
//    }
//
//    // AsyncTask to perform network operations in the background
//    private class FetchDataTask extends AsyncTask<String, Void, JSONObject> {
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            String endpoint = params[0];
//            String method = params[1];
//            String body = params[2];
//            String response = null;
//            try {
//                Log.d("Token", TOKEN);
//                URL url = new URL("https://api.spotify.com/" + endpoint);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod(method);
//                urlConnection.setRequestProperty("Authorization", "Bearer " + TOKEN);
//
//                if (method.equals("POST") || method.equals("PUT")) {
//                    urlConnection.setDoOutput(true);
//                    urlConnection.getOutputStream().write(body.getBytes());
//                }
//
//                InputStream in = urlConnection.getInputStream();
//                response = convertStreamToString(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (response != null) {
//                try {
//                    return new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//
//
//        @Override
//        protected void onPostExecute(JSONObject result) {
//            if (result != null) {
//                try {
//                    JSONArray items = result.getJSONArray("items");
//                    List<String> trackNames = new ArrayList<>();
//                    List<String> trackIds1 = new ArrayList<>();
//                    for (int i = 0; i < items.length(); i++) {
//                        JSONObject track = items.getJSONObject(i);
//                        String name = track.getString("name");
//                        String id = track.getString("id");
//                        JSONArray artists = track.getJSONArray("artists");
//                        StringBuilder artistNames = new StringBuilder();
//                        for (int j = 0; j < artists.length(); j++) {
//                            if (j > 0) {
//                                artistNames.append(", ");
//                            }
//                            artistNames.append(artists.getJSONObject(j).getString("name"));
//                        }
//                        trackNames.add(name + " by " + artistNames.toString());
//                        trackIds1.add(id);
//                    }
//                    topTrackIds = trackIds1;
//                    topTrackNames = trackNames;
//                    // Update ListView with track names
//                    CustomArrayAdapter adapter = new CustomArrayAdapter(listView.getContext(), trackNames);
//                    listView.setAdapter(adapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        private String convertStreamToString(InputStream is) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//
//            String line;
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line).append('\n');
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return sb.toString();
//        }
//
//    }
//}
//
package com.example.spotifytest;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class SpotifyApiHelper {
    public static List<String> topTrackIds;

    public static List<String> topTrackNames;

    public static List<String> topArtistNamesSongs;
    private String mAccessToken;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    public void fetchUserTopTracks(String accessToken, String timeRange, int limit, ListView listView) {
        if (accessToken == null) {
            Toast.makeText(listView.getContext(), "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAccessToken = accessToken;

        String url = "https://api.spotify.com/v1/me/top/tracks?time_range="+timeRange+"&limit="+limit;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall(); // Cancel previous call if any

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

                        List<String> trackNames = new ArrayList<>();
                        topTrackNames = new ArrayList<>();
                        topTrackIds = new ArrayList<>();
                        topArtistNamesSongs = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject track = items.getJSONObject(i);
                            JSONArray artists = track.getJSONArray("artists");
                            String trackName = track.getString("name");
                            String id = track.getString("id");
                            StringBuilder artistNames = new StringBuilder();
                            for (int j = 0; j < artists.length(); j++) {
                              if (j > 0) {
                                 artistNames.append(", ");
                             }
                              artistNames.append(artists.getJSONObject(j).getString("name"));
                              topArtistNamesSongs.add(artists.getJSONObject(j).getString("name"));
                            }
                            trackNames.add(trackName + " - " + artistNames.toString());
                            topTrackIds.add(id);

                        }
                        topTrackNames = trackNames;

                        listView.post(() -> {
                            CustomArrayAdapter adapter = new CustomArrayAdapter(listView.getContext(), trackNames);
                            listView.setAdapter(adapter);
                        });
                    } else {
                        // Handling unsuccessful response
                        Log.d("HTTP", "Failed to fetch data: " + response.code()+ " Token:  " + SimpleWelcomePage_Testing.publicToken);
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(listView.getContext(), "Failed to fetch data: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    // Handling JSON parsing error
                    Log.d("JSON", "Failed to parse data: " + e);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(listView.getContext(), "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void cancelCall() {
        mOkHttpClient.dispatcher().cancelAll();
    }
}
