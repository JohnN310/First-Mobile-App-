package com.example.spotifytest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangePasswordDialog extends AppCompatDialogFragment {



    ChangePasswordDialogInterface changePasswordDialogInterface;
//    ProfilePagePlaceholder profilePagePlaceholder = new ProfilePagePlaceholder();

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.change_password_dialog, null);

        EditText oldPass = (EditText) view.findViewById(R.id.oldPass);
        EditText newPass = (EditText) view.findViewById(R.id.newPass);
        EditText confirmNewPass = (EditText) view.findViewById(R.id.confirmNewPass);


        builder.setView(view)
                .setTitle("Change Password")
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                            changePasswordDialogInterface.sendChangePasswordInputs(oldPass.getText().toString(), newPass.getText().toString(), confirmNewPass.getText().toString());
                            getDialog().dismiss();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                });


        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        changePasswordDialogInterface = (ChangePasswordDialogInterface) context;

    }


    public interface ChangePasswordDialogInterface {
        void sendChangePasswordInputs(String oldPassword, String newPassword, String confirmPassword);
    }
}
