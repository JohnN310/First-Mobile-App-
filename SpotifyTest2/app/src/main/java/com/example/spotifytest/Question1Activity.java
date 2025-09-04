package com.example.spotifytest;//package com.example.spotifytest;

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

    // correct song rankings
    private static List<String> topTracks = SpotifyApiHelper.topTrackNames;

    // button for submission
    private Button submitButton;

    // recyclerView for songs
    private RecyclerView recyclerView;

    // button for submission
    private RecyclerAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question_1);

        // initialize views
        recyclerView = findViewById(R.id.recyclerView);
        submitButton = findViewById(R.id.submitButton);

        // prepare data
        List<String> itemList = new ArrayList<>(topTracks);
        Collections.shuffle(itemList);

        // set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // set up drag and drop functionality
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
                // not needed for this case
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers();
            }
        });
    }

    // method to check the answers
    private void checkAnswers() {
        int count = 0;
        for (int i = 0; i < topTracks.size(); i++) {
            if (topTracks.get(i).equals(adapter.getItemAtPosition(i))) {
                count++;
            }
        }
        if (count == topTracks.size()) {
            // all answers are correct
            showPopup("Correct!", true);
        } else {
            // some answers are incorrect
            showPopup("Incorrect! You got " + count + " correct.", false);
        }
    }

    // method to show a pop-up window
    private void showPopup(String message, boolean isCorrect) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.quiz_pop_up_window);

        TextView popupMessage = dialog.findViewById(R.id.popupMessage);
        popupMessage.setText(message);

        if (isCorrect) {
            popupMessage.setTextSize(22); // set text size to 25dp for "Correct!"
        } else {
            popupMessage.setTextSize(18); // set text size to 20dp for other messages
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

    // method to start the next question
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
