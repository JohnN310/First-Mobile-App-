//package com.example.spotifytest;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class LoginPage extends AppCompatActivity  {
//
//    EditText inputUsername;
//    EditText inputPassword;
//    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(LoginPage.this);
//
//
//    public void onCreate(Bundle saveInstanceState) {
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.activity_login);
//
//        inputUsername = (EditText) findViewById(R.id.login_username);
//        inputPassword = (EditText) findViewById(R.id.login_password);
//
//
//        Button loginAttempt = (Button) findViewById(R.id.login_button);
//        loginAttempt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int result = accountsDatabaseHandler.authenticate(inputUsername.getText().toString(), inputPassword.getText().toString());
//               if (result == 3) {
//                   Intent intent = new Intent(LoginPage.this, ProfilePagePlaceholder.class);
//                   Bundle bundle = new Bundle();
//                   bundle.putString("username", inputUsername.getText().toString());
//                   intent.putExtras(bundle);
//                   System.out.println("Here we are");
//                   startActivity(intent);
//               } else if (result == 2) {
//                   Toast.makeText(LoginPage.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
//               } else if (result == 1) {
//                   Toast.makeText(LoginPage.this, "This account does not exist!", Toast.LENGTH_SHORT).show();
//               }
//
//            }
//        });
//
//
//        Button signUp = (Button) findViewById(R.id.signup_button);
//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginPage.this, CreateAccount.class);
//                startActivity(intent);
//            }
//        });
//
//
//    }
//
//
//
//
//}


package com.example.spotifytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity  {

    EditText inputUsername;
    EditText inputPassword;
    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(LoginPage.this);


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.login_username);
        inputPassword = (EditText) findViewById(R.id.login_password);


        Button loginAttempt = (Button) findViewById(R.id.login_button);
        loginAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int result = accountsDatabaseHandler.authenticate(inputUsername.getText().toString(), inputPassword.getText().toString());
                if (result == 3) {
                    Intent intent = new Intent(LoginPage.this, ProfilePagePlaceholder.class);
//                    Intent intent = new Intent(LoginPage.this, ProfilePage.class);
                    Intent intent2 = new Intent(LoginPage.this, HomePage.class);
                    Intent intent3 = new Intent(LoginPage.this, SpotifyApiHelperActivitySongs.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", inputUsername.getText().toString());
                    intent.putExtras(bundle);
                    intent2.putExtras(bundle);
                    intent3.putExtras(bundle);
                    System.out.println("Here we are");
                    startActivity(intent2);
                } else if (result == 2) {
                    Toast.makeText(LoginPage.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                } else if (result == 1) {
                    Toast.makeText(LoginPage.this, "This account does not exist!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Button makeAccount = (Button) findViewById(R.id.signup_button);
        makeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, CreateAccount.class);
                startActivity(intent);
            }
        });



    }




}