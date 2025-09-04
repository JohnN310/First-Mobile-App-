package com.example.spotifytest;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfilePagePlaceholder extends AppCompatActivity
        implements AddFriendDialog.AddFriendDialogInterface, ChangePasswordDialog.ChangePasswordDialogInterface {

    String name, username, password, code, friends, invites;
    List<String> friendList;
    PopupWindow popupWindow;
    List<String> inviteList;
    TextView nameTV;
    TextView usernameTV;
    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(ProfilePagePlaceholder.this);
    YourProfile thisProfile;



    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.profile_page);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
        }

        thisProfile = accountsDatabaseHandler.getAccount(username);

        name = thisProfile.getName();
        password = thisProfile.getPassword();
        code = thisProfile.getCode();
        friends = thisProfile.getFriends();
        invites = thisProfile.getInvites();

        if (!invites.startsWith("invites,")) {
            // Insert comma and space after "invites"
            invites = invites.replaceFirst("invites", "invites,");

            // Add commas between names (if they follow name1, name2 pattern)
            invites = invites.replaceAll("name(\\d+)", "name$1, ").trim();

            // Remove trailing comma if any
            if (invites.endsWith(",")) {
                invites = invites.substring(0, invites.length() - 1);
            }
        }

        System.out.println("1111111111111111111 invites: " + invites);

        inviteList = new ArrayList<>();
        friendList = new ArrayList<>();

        if (invites != null && !invites.isEmpty()) {
            String[] invitesArray = invites.split(",");
            for (String invite : invitesArray) {
                if (!invite.isEmpty()) inviteList.add(invite);
            }
        }
        inviteList.remove(0);

        System.out.println("1111111111111111111 invite list: " + inviteList);

        nameTV = (TextView) findViewById(R.id.name_box);
        nameTV.setText(name);

        usernameTV = (TextView) findViewById(R.id.username_box);
        usernameTV.setText(username);

        ListView friendListView = findViewById(R.id.list_of_friends);

        if (friends != null && !friends.equals("friends,")) {
            friends = friends.substring(8);
            System.out.println("111111111111111111 Friends 1111111111111111: " + friends);
            int index = 0;

            String[] friendArray = friends.split(",");
            for (String addFriend : friendArray) {
                if (!addFriend.isEmpty() && !friendList.contains(addFriend) && !addFriend.equals("invites")) {
                    friendList.add(addFriend);
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.friends, R.id.friendUsername, friendList);
            friendListView.setAdapter(arrayAdapter);
        }

        Button goHome = (Button) findViewById(R.id.go_home);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePagePlaceholder.this, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



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

        Button seeInvites = (Button) findViewById(R.id.see_invites_button);
        seeInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ProfilePagePlaceholder.this, SeeInvites.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("invites", invites);
//                intent.putExtras(bundle);
//                startActivity(intent);

                showInvitesPopup(v);


            }
        });


        Button logOut = (Button) findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePagePlaceholder.this, LoginPage.class);
                startActivity(intent);
            }
        });

        Button deleteAccount = (Button) findViewById(R.id.delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_delete_layout, null);
                popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                // set an elevation value for the popup window
                popupWindow.setElevation(5.0f);

                // set a background drawable for the popup window
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // set focusable true to enable touch events outside of the popup window
                popupWindow.setFocusable(true);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, -150);

                Button acceptButton = popupView.findViewById(R.id.accept_button);

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfilePagePlaceholder.this, LoginPage.class);
                        startActivity(intent);
                        accountsDatabaseHandler.deleteAccount(username);
                        popupWindow.dismiss();
                    }
                });
                Button declineButton = popupView.findViewById(R.id.decline_button);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

            }
        });


    }

    public void showInvitesPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_invites_layout, null);

        // Initialize a new instance of PopupWindow
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // set an elevation value for the popup window
        popupWindow.setElevation(5.0f);

        // set a background drawable for the popup window
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set focusable true to enable touch events outside of the popup window
        popupWindow.setFocusable(true);

        // retrieve the TextView in the popup layout to display invites
        TextView invitesTextView = popupView.findViewById(R.id.invites_text_view);

        // build the string of invites to display
        StringBuilder invitesText = new StringBuilder();
        for (String invite : inviteList) {
            invitesText.append(invite).append("\n");
        }

        // set the invites text to the TextView in the popup layout
        final String[] lastInviteHolder = new String[1];
        if (!inviteList.isEmpty()) {
            lastInviteHolder[0] = inviteList.get(inviteList.size() - 1);
        } else {
            lastInviteHolder[0] = "";
        }
        invitesTextView.setText("Friend invite from " + lastInviteHolder[0]);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -150);

        Button acceptButton = popupView.findViewById(R.id.accept_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lastInvite = lastInviteHolder[0]; // get current value

                if (!friendList.contains(lastInvite) && !lastInvite.isEmpty()) {
                    accountsDatabaseHandler.addFriend(username, lastInvite);
                    accountsDatabaseHandler.addFriend(lastInvite, username);
                    lastInviteHolder[0] = ""; // reset after action
                    inviteList.remove(lastInvite);

                    String updatedInvites = invites.replace(lastInvite + ",", ",");
                    thisProfile.setInvites(updatedInvites);
                    invites = updatedInvites;
                    accountsDatabaseHandler.updateInvites(username, updatedInvites);

                    popupWindow.dismiss();
                    Intent intent = getIntent();
                    finish(); // close current instance
                    startActivity(intent); // start it again

                } else if (friendList.contains(lastInvite)) {
                    Toast.makeText(ProfilePagePlaceholder.this, "This user has already been added to your friend list!", Toast.LENGTH_SHORT).show();
                    lastInviteHolder[0] = ""; // reset
                    inviteList.remove(lastInvite);

                    String updatedInvites = invites.replace(lastInvite + ",", ",");
                    thisProfile.setInvites(updatedInvites);
                    invites = updatedInvites;
                    accountsDatabaseHandler.updateInvites(username, updatedInvites);

                    popupWindow.dismiss();
                } else {
                    Toast.makeText(ProfilePagePlaceholder.this, "You have no pending friend invitations!", Toast.LENGTH_SHORT).show();
                    lastInviteHolder[0] = ""; // reset
                    popupWindow.dismiss();
                }
            }
        });


        Button declineButton = popupView.findViewById(R.id.decline_button);

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inviteList.isEmpty()){
                    inviteList.remove(inviteList.size() - 1);
                }

                String lastInvite = lastInviteHolder[0];
                String updatedInvites = invites.replace(lastInvite + ",", ",");
                thisProfile.setInvites(updatedInvites);
                invites = updatedInvites;
                accountsDatabaseHandler.updateInvites(username, updatedInvites);

                popupWindow.dismiss();
            }
        });
    }


    public YourProfile getThisProfile() {
        return thisProfile;
    }


    @Override
    public void sendNewFriendInput(String friendUsername) {
        if (accountsDatabaseHandler.contains(friendUsername)) {
            accountsDatabaseHandler.addInvite(friendUsername, username);
            Toast.makeText(ProfilePagePlaceholder.this, "Successfully sent invite to " + friendUsername, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProfilePagePlaceholder.this, "That username does not exist!", Toast.LENGTH_SHORT).show();

        }

    }

    public void sendChangePasswordInputs(String oldPassword, String newPassword, String confirmPassword) {
        if (!getThisProfile().getPassword().equals(oldPassword)) {
            Toast.makeText(ProfilePagePlaceholder.this, "Incorrect Old Password", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ProfilePagePlaceholder.this, "Your New Passwords Didn't Match", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("else ran");
            Toast.makeText(ProfilePagePlaceholder.this, "Password successfully changed to " + newPassword, Toast.LENGTH_SHORT).show();
            accountsDatabaseHandler.changePassword(username, newPassword);
        }

    }


}