package com.example.spotifytest;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SeeInvites extends AppCompatActivity {

    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.see_invites);

        Bundle bundle = getIntent().getExtras();
        String invites = bundle.getString("invites");

        ListView invitesListView = findViewById(R.id.invitesList);
        ArrayList<String> invitesList = new ArrayList<>();

        if (invites != null && invites.equals("invites,")) {
            Toast.makeText(SeeInvites.this, "You have no invites", Toast.LENGTH_SHORT).show();

        }


        try {
            if (!invites.equals("invites,")) {
                System.out.println("started if");
                System.out.println(invites);
                int firstComma = 7;
                int secondComma = 0;
                String addFriend;

                while (firstComma != -1) {
                    invites = invites.substring(firstComma + 1);
                    System.out.println(invites);

                    secondComma = invites.indexOf(",");
                    addFriend = invites.substring(0, secondComma);
                    System.out.println(addFriend);

                    invitesList.add(addFriend);
                    System.out.println("added");
                    firstComma = invites.indexOf(",");
                    System.out.println("while loop done");
                }


                //HELP PLZ
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.friends, R.id.friendUsername, invitesList);
                System.out.println("made arrayadapter");
                invitesListView.setAdapter(arrayAdapter);
                System.out.println("set arrayadapter");
            }

        } catch (Exception e) {
            Toast.makeText(SeeInvites.this, "Unable to compile list of invites", Toast.LENGTH_SHORT).show();
        }


    }
}
