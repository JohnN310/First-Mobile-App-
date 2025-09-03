package com.example.spotifytest;//package com.example.spotifytest;
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.os.Handler;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Question1Activity extends AppCompatActivity {
//
//    // Correct song rankings
//    private static List<String> topTracks = SpotifyApiHelper.topTrackNames;
//
//    private final Handler handler = new Handler();
//
//    private static final Map<String, Integer> CORRECT_RANKS = new HashMap<>();
//
//    static {
//        for (int i = 0; i < SpotifyApiHelper.topTrackIds.size(); i++) {
//            if (topTracks.get(i) != null) {
//                CORRECT_RANKS.put(topTracks.get(i), i + 1);
//            }
//        }
//    }
//
//    // Spinners for songs
//    private Spinner song1Spinner, song2Spinner, song3Spinner, song4Spinner, song5Spinner;
//
//    // Button for submission
//    private Button submitButton;
//
//    // TextViews for song names
//    private TextView song1TextView, song2TextView, song3TextView, song4TextView, song5TextView;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.quiz_question_1);
//
//        // Initialize views
//        song1Spinner = findViewById(R.id.song1Spinner);
//        song2Spinner = findViewById(R.id.song2Spinner);
//        song3Spinner = findViewById(R.id.song3Spinner);
//        song4Spinner = findViewById(R.id.song4Spinner);
//        song5Spinner = findViewById(R.id.song5Spinner);
//
//        submitButton = findViewById(R.id.submitButton);
//
//        song1TextView = findViewById(R.id.song1TextView);
//        song2TextView = findViewById(R.id.song2TextView);
//        song3TextView = findViewById(R.id.song3TextView);
//        song4TextView = findViewById(R.id.song4TextView);
//        song5TextView = findViewById(R.id.song5TextView);
//
//        // Shuffle the list of song names
//        Collections.shuffle(SpotifyApiHelper.topTrackNames);
//        int count = 0;
//        // Set song names to TextViews
//        for (int i = 0; i < SpotifyApiHelper.topTrackIds.size(); i++) {
//            if (topTracks.get(i) != null) {
//                int textViewId = getResources().getIdentifier("song" + (i + 1) + "TextView", "id", getPackageName());
//                TextView textView = findViewById(textViewId);
//                if (textView != null) {
//                    textView.setText(topTracks.get(i));
//                    count++;
//                }
//            }
//        }
//        for (int i = count; i < 5; i++) {
//            int textViewId = getResources().getIdentifier("song" + (i) + "TextView", "id", getPackageName());
//            TextView textView = findViewById(textViewId);
//            if (textView != null) {
//                textView.setText(" ");
//            }
//        }
//
//        // Set up spinners with song options
//        if (!song1TextView.getText().toString().equals(" ")) {
//            setUpSpinner(song1Spinner);
//        } else {
//            song1Spinner.setVisibility(View.GONE);
//        }
//        if (!song2TextView.getText().toString().equals(" ")) {
//            setUpSpinner(song2Spinner);
//        } else {
//            song2Spinner.setVisibility(View.GONE);
//        }
//        if (!song3TextView.getText().toString().equals(" ")) {
//            setUpSpinner(song3Spinner);
//        } else {
//            song3Spinner.setVisibility(View.GONE);
//        }
//        if (!song4TextView.getText().toString().equals(" ")) {
//            setUpSpinner(song4Spinner);
//        } else {
//            song4Spinner.setVisibility(View.GONE);
//        }
//        if (!song5TextView.getText().toString().equals(" ")) {
//            setUpSpinner(song5Spinner);
//        } else {
//            song5Spinner.setVisibility(View.GONE);
//        }
//
//        // Set click listener for the submit button
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get selected ranks
//                // Convert selected song strings to integers
//                int selectedRank1 = Integer.parseInt(song1Spinner.getSelectedItem().toString().substring(0,1));
//                int selectedRank2 = Integer.parseInt(song2Spinner.getSelectedItem().toString().substring(0,1));
//                int selectedRank3 = Integer.parseInt(song3Spinner.getSelectedItem().toString().substring(0,1));
//                int selectedRank4 = Integer.parseInt(song4Spinner.getSelectedItem().toString().substring(0,1));
//                int selectedRank5 = Integer.parseInt(song5Spinner.getSelectedItem().toString().substring(0,1));
//                int count = 0;
//                // Check if all ranks are correct
//                if (selectedRank1 == CORRECT_RANKS.get(song1TextView.getText().toString()))
//                    count++;
//                if (selectedRank2 == CORRECT_RANKS.get(song2TextView.getText().toString()))
//                    count++;
//                if (selectedRank3 == CORRECT_RANKS.get(song3TextView.getText().toString()))
//                    count++;
//                if (selectedRank4 == CORRECT_RANKS.get(song4TextView.getText().toString()))
//                    count++;
//                if (selectedRank5 == CORRECT_RANKS.get(song5TextView.getText().toString()))
//                    count++;
//                if (count == 5) {
//                    // Show correct toast message
//                    showPopup("Correct!", true);
//                } else {
//                    // Show incorrect toast message
//                    showPopup("Incorrect! You got " + count + " correct.", false);
//                }
//            }
//        });
//    }
//    // Method to set up spinner with song options
//    private void setUpSpinner(Spinner spinner) {
//        // Create array adapter with song options
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.song_options, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set adapter to spinner
//        spinner.setAdapter(adapter);
//    }
//
//    // Method to show a pop-up window
//    private void showPopup(String message, boolean isCorrect) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.quiz_pop_up_window);
//
//        TextView popupMessage = dialog.findViewById(R.id.popupMessage);
//        popupMessage.setText(message);
//
//        if (isCorrect) {
//            popupMessage.setTextSize(22); // Set text size to 25dp for "Correct!"
//        } else {
//            popupMessage.setTextSize(18); // Set text size to 20dp for other messages
//        }
//
//
//        if (isCorrect) {
//            dialog.getWindow().setBackgroundDrawableResource(R.color.correct_color);
//        } else {
//            dialog.getWindow().setBackgroundDrawableResource(R.color.incorrect_color);
//        }
//
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//
//        Button closeButton = dialog.findViewById(R.id.close_button);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }
//
//    public void question2(View view) {
//        Context context = view.getContext();
//        Intent intent = new Intent(context, Question2Activity.class);
//        context.startActivity(intent);
//    }
//}
//
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Question1Activity extends AppCompatActivity {

    // Correct song rankings
    private static List<String> topTracks = SpotifyApiHelper.topTrackNames;

    // Button for submission
    private Button submitButton;

    // RecyclerView for songs
    private RecyclerView recyclerView;

    // Button for submission
    private RecyclerAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question_1);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        submitButton = findViewById(R.id.submitButton);

        // Prepare data
        List<String> itemList = new ArrayList<>(topTracks);
        Collections.shuffle(itemList);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Set up drag and drop functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                    RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target
            ) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(itemList, from, to);
                recyclerView.getAdapter().notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Not needed for this case
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers();
            }
        });
    }

    // Method to check the answers
    private void checkAnswers() {
        int count = 0;
        for (int i = 0; i < topTracks.size(); i++) {
            if (topTracks.get(i).equals(adapter.getItemAtPosition(i))) {
                count++;
            }
        }
        if (count == topTracks.size()) {
            // All answers are correct
            showPopup("Correct!", true);
        } else {
            // Some answers are incorrect
            showPopup("Incorrect! You got " + count + " correct.", false);
        }
    }

    // Method to show a pop-up window
    private void showPopup(String message, boolean isCorrect) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.quiz_pop_up_window);

        TextView popupMessage = dialog.findViewById(R.id.popupMessage);
        popupMessage.setText(message);

        if (isCorrect) {
            popupMessage.setTextSize(22); // Set text size to 25dp for "Correct!"
        } else {
            popupMessage.setTextSize(18); // Set text size to 20dp for other messages
        }

        if (isCorrect) {
            dialog.getWindow().setBackgroundDrawableResource(R.color.correct_color);
        } else {
            dialog.getWindow().setBackgroundDrawableResource(R.color.incorrect_color);
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Method to start the next question
    public void question2(View view) {
        if (SpotifyApiHelperArtists.topArtists == null) {
            Toast.makeText(this, "You have no favorite artists!", Toast.LENGTH_SHORT).show();
        }
        else {
            Bundle bundle = getIntent().getExtras();
            Context context = view.getContext();
            Intent intent = new Intent(context, Question2Activity.class);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
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
