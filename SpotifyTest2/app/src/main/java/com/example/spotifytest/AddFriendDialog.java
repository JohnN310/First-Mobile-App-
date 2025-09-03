package com.example.spotifytest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddFriendDialog extends AppCompatDialogFragment {



    AddFriendDialogInterface dialogInterface;


    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_friend_dialog, null);

        EditText add_friend_username = (EditText) view.findViewById(R.id.add_friend_username);


        builder.setView(view)
                .setTitle("Add Friend")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String friend = add_friend_username.getText().toString();
                        dialogInterface.sendNewFriendInput(friend);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });



        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        dialogInterface = (AddFriendDialogInterface) context;

    }
    public interface AddFriendDialogInterface {
        void sendNewFriendInput(String friendUsername);
    }

}