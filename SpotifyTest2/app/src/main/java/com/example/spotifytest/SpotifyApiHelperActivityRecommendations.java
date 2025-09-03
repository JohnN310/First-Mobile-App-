package com.example.spotifytest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class SpotifyApiHelperActivityRecommendations extends AppCompatActivity implements SpotifyApiHelperRecommendations.SpotifyDataListener {

    private ListView listView;
    private SpotifyApiHelperRecommendations spotifyApiHelper;

    private SpotifyApiHelper spot;

    private String accessToken;

    private Button btnCapture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_api_helper_recommendations);

        // Initialize ListView
        listView = findViewById(R.id.listView);

        accessToken = HomePage.publicToken;

        // Initialize SpotifyApiHelper
        spotifyApiHelper = new SpotifyApiHelperRecommendations();

        // Set the data listener
        spotifyApiHelper.mListener = this;
        try {
            spotifyApiHelper.fetchDataFromSpotify(SpotifyApiHelper.topTrackIds, accessToken);
        } catch (JSONException | IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        changeBackgroundBasedOnSpecialDays();

    }

    private void captureScreen() {
        // Get the root view of the activity
        View rootView = getWindow().getDecorView().getRootView();

        // Enable drawing cache
        rootView.setDrawingCacheEnabled(true);

        // Create a bitmap of the rootView
        Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rootView.draw(canvas);

        // Disable drawing cache
        rootView.setDrawingCacheEnabled(false);

        // Use MediaStore to save the screenshot
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "screenshot_" + System.currentTimeMillis() + ".png");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);

        // Save the bitmap to the MediaStore
        try {
            android.net.Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(uri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.close();
                        Toast.makeText(this, "Wrapped saved!", Toast.LENGTH_SHORT).show();
                        Log.d("SCREENSHOT", "SAVED");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Failed to save screenshot.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to capture screenshot.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDataReceived(List<String> trackIds) {
        // Construct endpoint using trackIds
        CustomArrayAdapter adapter = new CustomArrayAdapter(listView.getContext(), trackIds);
        listView.setAdapter(adapter);
    }


    @Override
    public void onError(String errorMessage) {
        // Handle error
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

    public void back(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, SpotifyApiHelperActivityArtists.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}
