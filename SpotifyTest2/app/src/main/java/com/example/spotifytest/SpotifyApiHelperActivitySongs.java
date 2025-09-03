package com.example.spotifytest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;




public class SpotifyApiHelperActivitySongs extends AppCompatActivity {

    private ListView listView;
    private SpotifyApiHelper spotifyApiHelper;
    private Button optionsButton;

    private Button optionsButton2;

    private String accessToken;

    private static final int REQUEST_CODE = 101;
    private Button btnCapture;
    private ImageView imageView;

    public static boolean isSaved;
    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(SpotifyApiHelperActivitySongs.this);
    YourProfile thisProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_api_helper_songs);

        // Initialize ListView
        listView = findViewById(R.id.listView);

        // Initialize SpotifyApiHelper
        spotifyApiHelper = new SpotifyApiHelper();

        // Call method to fetch data from Spotify API
//        spotifyApiHelper.fetchDataFromSpotify("v1/me/top/tracks?time_range=long_term&limit=5", "GET", null, listView);

        // Replace "accessToken" with the actual access token obtained during the authentication process
        accessToken = HomePage.publicToken;

        spotifyApiHelper.fetchUserTopTracks(accessToken, "long_term", 5, listView);

        // Initialize options button and set click listener
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

        btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = true;
                saveWrapped();
            }
        });
    }

    private void saveWrapped() {
        Bundle bundle = getIntent().getExtras();
        String username1 = "";
        if (bundle != null) {
            username1 = bundle.getString("username");
        }
        System.out.println(username1);
        List<String> topSongs = SpotifyApiHelper.topTrackNames;
        accountsDatabaseHandler.addSavedWrapped(username1, topSongs);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAa " + topSongs);
        Toast.makeText(this, "Wrapped saved!", Toast.LENGTH_SHORT).show();
    }



    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, optionsButton);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_all_time) {
//                    spotifyApiHelper.fetchDataFromSpotify("v1/me/top/tracks?time_range=long_term&limit=5", "GET", null, listView);
                    spotifyApiHelper.fetchUserTopTracks(accessToken, "long_term", 5, listView);
                    return true;
                } else if (itemId == R.id.menu_6_months) {
//                    spotifyApiHelper.fetchDataFromSpotify("v1/me/top/tracks?time_range=long_term&limit=5", "GET", null, listView);
                    spotifyApiHelper.fetchUserTopTracks(accessToken, "medium_term", 5, listView);
                    return true;
                } else if (itemId == R.id.menu_4_weeks) {
//                    spotifyApiHelper.fetchDataFromSpotify("v1/me/top/tracks?time_range=long_term&limit=5", "GET", null, listView);
                    spotifyApiHelper.fetchUserTopTracks(accessToken, "short_term", 5, listView);
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
                    spotifyApiHelper.fetchUserTopTracks(accessToken, "long_term", 5, listView);                    return true;
                } else if (id == R.id.top10) {
                    // Change background to Summer
                    spotifyApiHelper.fetchUserTopTracks(accessToken, "long_term", 10, listView);                    return true;
                } else if (id == R.id.top15) {
                    // Change background to Fall
                    spotifyApiHelper.fetchUserTopTracks(accessToken, "long_term", 15, listView);                    return true;
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

    public void topArtists(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, SpotifyApiHelperActivityArtists.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    public void back(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, HomePage.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    public void viewSaved(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ViewScreenshotActivity.class);
        context.startActivity(intent);
    }
}
