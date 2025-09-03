package com.example.spotifytest;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;


import android.view.ViewGroup;
import android.widget.Button;

import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfilePage extends AppCompatActivity
        implements AddFriendDialog.AddFriendDialogInterface, ChangePasswordDialog.ChangePasswordDialogInterface {

    String name, username;
    List<String> friendList;
    List<String> inviteList;

    PopupWindow popupWindow; // Declare popupWindow here

    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(ProfilePage.this);
    ArrayList<YourProfile> profiles = accountsDatabaseHandler.readProfiles();
    Bundle bundle = getIntent().getExtras();
    int accountIndex;
    {
        assert bundle != null;
        accountIndex = bundle.getInt("index");
    }

    YourProfile thisProfile = profiles.get(0);

    public int getCurrentProfile() {
        return accountIndex;

    }




    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.profile_page);

        name = thisProfile.getName();
        username = thisProfile.getUsername();

        TextView nameTV = (TextView) findViewById(R.id.name_box);
        nameTV.setText(name);
        TextView usernameTV = (TextView) findViewById(R.id.username_box);
        usernameTV.setText(username);


        friendList = new ArrayList<>();
        inviteList = new ArrayList<>();

        // Check if extras bundle is not null
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            // Retrieve data from extras bundle and set to TextViews
//            name.setText(bundle.getString("name", "Name"));
//            username.setText(bundle.getString("username", "Username"));
//        }


        Button add_friend_button = (Button) findViewById(R.id.addFriendButton);
        add_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendDialog customDialog = new AddFriendDialog();
                customDialog.show(getSupportFragmentManager(), "Add Friend");
            }
        });

        Button changePasswordButton = (Button) findViewById(R.id.editPassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog customDialog = new ChangePasswordDialog();
                customDialog.show(getSupportFragmentManager(), "Change Password");
            }
        });

        Button seeInvitesButton = findViewById(R.id.see_invites_button);
        seeInvitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvitesPopup(v);
            }
        });




    }

    public void changePassword(String newPassword) {
        thisProfile.setPassword(newPassword);

    }


    public YourProfile getThisProfile() {

        return thisProfile;

    }


    @Override
    public void sendNewFriendInput(String friendUsername) {
        inviteList.add(friendUsername);
    }

    public void showInvitesPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Initialize a new instance of PopupWindow
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for the popup window
        popupWindow.setElevation(5.0f);

        // Set a background drawable for the popup window
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set focusable true to enable touch events outside of the popup window
        popupWindow.setFocusable(true);

        // Retrieve the TextView in the popup layout to display invites
        TextView invitesTextView = popupView.findViewById(R.id.invites_text_view);

        // Build the string of invites to display
        StringBuilder invitesText = new StringBuilder();
        for (String invite : inviteList) {
            invitesText.append(invite).append("\n");
        }

        // Set the invites text to the TextView in the popup layout
        invitesTextView.setText(invitesText.toString());

        // Show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -150);
    }

    public void closeShowInvites(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void sendChangePasswordInputs(String oldPassword, String newPassword, String confirmPassword) {

    }


//Notes: fix add friends, fix the text view for add friends, update inviteList every time
// an invite is sent;

    }