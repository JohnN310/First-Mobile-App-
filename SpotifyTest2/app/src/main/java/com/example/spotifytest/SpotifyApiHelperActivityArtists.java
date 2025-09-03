//package com.example.spotifytest;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SpotifyApiHelperActivityArtists extends AppCompatActivity implements SpotifyApiHelperArtists.SpotifyDataListener {
//
//    private ListView listView;
//    private SpotifyApiHelperArtists spotifyApiHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.spotify_api_helper_artists);
//
//        // Initialize ListView
//        listView = findViewById(R.id.listView);
//
//        // Initialize SpotifyApiHelper
//        spotifyApiHelper = new SpotifyApiHelperArtists();
//
//        // Call method to fetch data from Spotify API
//        spotifyApiHelper.fetchDataFromSpotify("v1/me/top/artists?time_range=long_term&limit=5", "GET", null, listView);
//    }
//
//    // Implement the method to handle data received from Spotify API
//    @Override
//    public void onDataReceived(JSONObject data) {
//        try {
//            JSONArray items = data.getJSONArray("items");
//            List<String> artistNames = new ArrayList<>();
//            for (int i = 0; i < items.length(); i++) {
//                JSONObject artist = items.getJSONObject(i);
//                String name = artist.getString("name");
//                artistNames.add(name);
//            }
//            // Update ListView with artist names
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, artistNames);
//            listView.setAdapter(adapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Implement the method to handle errors
//    @Override
//    public void onError(String errorMessage) {
//        // Handling of errors
//    }
//}


package com.example.spotifytest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SpotifyApiHelperActivityArtists extends AppCompatActivity {

    private ListView listView;
    private SpotifyApiHelperArtists spotifyApiHelper;
    private Button optionsButton;
    private Button optionsButton2;
    private SpotifyApiHelper spot;

    private String accessToken;

    private String encodedGenres;

    private Button btnCapture;

    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(SpotifyApiHelperActivityArtists.this);
    YourProfile thisProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_api_helper_artists);


        // Initialize ListView
        listView = findViewById(R.id.listView);

        accessToken = HomePage.publicToken;

        // Initialize SpotifyApiHelper
        spotifyApiHelper = new SpotifyApiHelperArtists();

        // Call method to fetch data from Spotify API
//        spotifyApiHelper.fetchDataFromSpotify("v1/me/top/artists?time_range=long_term&limit=5", "GET", null, listView);
        spotifyApiHelper.fetchUserTopArtists(accessToken, "long_term", 5, listView);
        ListView listView2 = findViewById(R.id.listView2);
        spot = new SpotifyApiHelper();

//        spot.fetchDataFromSpotify("v1/me/top/tracks?time_range=long_term&limit=5", "GET", null, listView2);
        spot.fetchUserTopTracks(accessToken, "long_term", 5, listView2);

        optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show options menu
                showPopupMenu();
            }
        });

        optionsButton2 = findViewById(R.id.optionsButton2);
        optionsButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu2(v);
            }
        });
        changeBackgroundBasedOnSpecialDays();
//        Bundle bundle = getIntent().getExtras();
//        String username1 = "";
//        if (bundle != null) {
//            username1 = bundle.getString("username");
//        }
//        System.out.println(username1);
//        List<String> topSongs = SpotifyApiHelper.topTrackNames;
//        accountsDatabaseHandler.addSavedWrapped(username1, topSongs);
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAa " + topSongs);
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, optionsButton);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_all_time) {
//                    spotifyApiHelper.fetchDataFromSpotify("v1/me/top/artists?time_range=long_term&limit=5", "GET", null, listView);
                    spotifyApiHelper.fetchUserTopArtists(accessToken, "long_term", 5, listView);
                    return true;
                } else if (itemId == R.id.menu_6_months) {
//                    spotifyApiHelper.fetchDataFromSpotify("v1/me/top/artists?time_range=medium_term&limit=5", "GET", null, listView);
                    spotifyApiHelper.fetchUserTopArtists(accessToken, "medium_term", 5, listView);
                    return true;
                } else if (itemId == R.id.menu_4_weeks) {
//                    spotifyApiHelper.fetchDataFromSpotify("v1/me/top/artists?time_range=short_term&limit=5", "GET", null, listView);
                    spotifyApiHelper.fetchUserTopArtists(accessToken, "short_term", 5, listView);
                    return true;
                }
                return false;
            }
        });
        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.options_menu, popupMenu.getMenu());

        // Show the popup menu
        popupMenu.show();
    }

    private void showPopupMenu2(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                if (id == R.id.top5) {
                    // Change background to Spring
                    spotifyApiHelper.fetchUserTopArtists(accessToken, "long_term", 5, listView);
                    return true;
                } else if (id == R.id.top10) {
                    // Change background to Summer
                    spotifyApiHelper.fetchUserTopArtists(accessToken, "long_term", 10, listView);
                    return true;
                } else if (id == R.id.top15) {
                    // Change background to Fall
                    spotifyApiHelper.fetchUserTopArtists(accessToken, "long_term", 15, listView);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.inflate(R.menu.options_2_menu);
        popupMenu.show();
    }


    private void changeBackgroundBasedOnSpecialDays() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        Drawable backgroundDrawable = null;

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Get the current month and day as strings
        String monthString = new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime());
        String dayOfMonthString = String.valueOf(dayOfMonth);

        // Check for special days
        if (monthString.equals("January") && dayOfMonthString.equals("1")) { // New Year's Day
            backgroundDrawable = getResources().getDrawable(R.drawable.summer_background);
        } else if (monthString.equals("February") && dayOfMonthString.equals("14")) { // Valentine's Day
            backgroundDrawable = getResources().getDrawable(R.drawable.valentines2);
        } else if (monthString.equals("March") && dayOfMonthString.equals("17")) { // St. Patrick's Day
            backgroundDrawable = getResources().getDrawable(R.drawable.stpatricks);
        }
        else if (monthString.equals("October") && dayOfMonthString.equals("31")) { // halloween
            backgroundDrawable = getResources().getDrawable(R.drawable.halloween);
        }
        else if (month == Calendar.NOVEMBER && dayOfMonth >= 22 && dayOfMonth <= 28) { //thanksginving
            backgroundDrawable = getResources().getDrawable(R.drawable.thanksgiving);
        }
        else if (monthString.equals("December") && dayOfMonthString.equals("25")) {
            backgroundDrawable = getResources().getDrawable(R.drawable.christmasnew);
        }
        else if (monthString.equals("December") && dayOfMonthString.equals("24")) {
            backgroundDrawable = getResources().getDrawable(R.drawable.christmasnew);
        }
        else if (monthString.equals("April") && dayOfMonthString.equals("8")) {
            backgroundDrawable = getResources().getDrawable(R.drawable.newyears2);
        }
            // Set background
        if (backgroundDrawable != null) {
            constraintLayout.setBackground(backgroundDrawable);
        }
    }

    public void recommendedArtists(View view) {
        if (SpotifyApiHelperArtists.topArtists == null || SpotifyApiHelper.topTrackIds == null) {
            Toast.makeText(this, "You have no favorite tracks or artists!", Toast.LENGTH_SHORT).show();
        }
        else {
            Bundle bundle = getIntent().getExtras();
            Context context = view.getContext();
            Intent intent = new Intent(context, SpotifyApiHelperActivityRecommendations.class);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    public void quizMe(View view) {
        if (SpotifyApiHelper.topTrackNames == null) {
            Toast.makeText(this, "You have no favorite tracks!", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = getIntent().getExtras();
            Context context = view.getContext();
            Intent intent = new Intent(context, Question1Activity.class);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    public void genreAnalysis(View view){
        Toast toast = Toast.makeText(getApplicationContext(), "Analyzing...", Toast.LENGTH_LONG);
        toast.show();
        List<String> genres = SpotifyApiHelperArtists.topGenres;
        try {
            encodedGenres = URLEncoder.encode(String.join(",", genres), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro",    /* apiKey */ "AIzaSyABtkfxfxDV9PGDWhXkcGbM7iWmuWEVyDU");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder()
                .addText("Briefly describe how a person who listens to all these genres tend to act, dress, and think. Use bullet points. Guess what the student's major is at Georgia Tech. Guess what their favorite color is. Be consistent with your response, if I give you a different list of genres, the response should get" +
                        " a different color or major. Max characters: 300: "+ encodedGenres)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(resultText);

// Process bold text
                int startIndex = 0;
                int endIndex;
                while ((startIndex = resultText.indexOf("**", startIndex)) != -1) {
                    endIndex = resultText.indexOf("**", startIndex + 2);
                    if (endIndex != -1) {
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        startIndex = endIndex + 2;
                    } else {
                        break;
                    }
                }

// Process italic text
                startIndex = 0;
                while ((startIndex = resultText.indexOf("*", startIndex)) != -1) {
                    endIndex = resultText.indexOf("*", startIndex + 1);
                    if (endIndex != -1) {
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        startIndex = endIndex + 1;
                    } else {
                        break;
                    }
                }
                toast.cancel();
                showPopup(spannableStringBuilder);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        }, this.getMainExecutor());
    }

    private void showPopup(SpannableStringBuilder text) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.genre_analysis_popup);

        TextView textView = dialog.findViewById(R.id.popup_text);
        textView.setText(text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Close the dialog when the button is clicked
            }
        });

        dialog.show();
    }

    public void back(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, SpotifyApiHelperActivitySongs.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}
