package com.example.spotifytest;
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

public class Question2Activity extends AppCompatActivity {

    // Correct song rankings
    private static List<String> topArtists = SpotifyApiHelperArtists.topArtists;

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
        setContentView(R.layout.quiz_question_2);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        submitButton = findViewById(R.id.submitButton);

        // Prepare data
        List<String> itemList = new ArrayList<>(topArtists);
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
        for (int i = 0; i < topArtists.size(); i++) {
            if (topArtists.get(i).equals(adapter.getItemAtPosition(i))) {
                count++;
            }
        }
        if (count == topArtists.size()) {
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
        dialog.setContentView(R.layout.quiz_pop_up_window_2);

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
    public void backToSongs(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, SpotifyApiHelperActivitySongs.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public void back(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, Question1Activity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}


