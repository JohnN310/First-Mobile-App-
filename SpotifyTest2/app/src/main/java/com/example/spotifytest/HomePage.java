//package com.example.spotifytest;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//import android.widget.Button;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.spotify.sdk.android.auth.AuthorizationClient;
//import com.spotify.sdk.android.auth.AuthorizationRequest;
//import com.spotify.sdk.android.auth.AuthorizationResponse;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class HomePage extends AppCompatActivity {
//
//    public static final String CLIENT_ID = "d3136c06706142209a3c65aa74c6b9b7";
//    public static final String REDIRECT_URI = "spotifytest://auth";
//    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
//    public static final int AUTH_CODE_REQUEST_CODE = 1;
//    private final OkHttpClient mOkHttpClient = new OkHttpClient();
//    private String mAccessToken, mAccessCode, mUserProfile;
//    private Call mCall;
//    Button pastMonth, pastSixMonths, pastYear, saved, profilePage, settings;
//
//    public void onCreate(Bundle saveInstanceState) {
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.home_page);
//
//
//        mAccessCode = (new ProfilePagePlaceholder()).getThisProfile().getCode();
//        getToken();
//
//        pastMonth = (Button) findViewById(R.id.pastMonth);
//        pastSixMonths = (Button) findViewById(R.id.pastSixMonths);
//        pastYear = (Button) findViewById(R.id.pastYear);
//        saved = (Button) findViewById(R.id.saved);
//        profilePage = (Button) findViewById(R.id.profilePage);
//        settings = (Button) findViewById(R.id.settings);
//
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, Settings.class);
//                startActivity(intent);
//            }
//        });
//
//        profilePage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomePage.this, ProfilePagePlaceholder.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//
//
//    }
//
//    /**
//     * Get code from Spotify
//     * This method will open the Spotify login activity and get the code
//     * What is code?
//     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
//     */
//    public void getToken() {
//        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
//        AuthorizationClient.openLoginActivity(HomePage.this, AUTH_TOKEN_REQUEST_CODE, request);
//    }
//
//    /**
//     * When the app leaves this activity to momentarily get a token/code, this function
//     * fetches the result of that external activity to get the response from Spotify
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
//
//        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
//            mAccessToken = response.getCode();
//        }
//    }
//
//    /**
//     * Get user profile
//     * This method will get the user profile using the token
//     */
//    public void onGetUserProfileClicked() {
//
//        // Create a request to get the user profile
//        final Request request = new Request.Builder()
//                .url("https://api.spotify.com/v1/me")
//                .addHeader("Authorization", "Bearer " + mAccessToken)
//                .build();
//
////        cancelCall();
//        mCall = mOkHttpClient.newCall(request);
//
//        mCall.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("HTTP", "Failed to fetch data: " + e);
//                Toast.makeText(HomePage.this, "Failed to fetch data, watch Logcat for more details",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    final JSONObject jsonObject = new JSONObject(response.body().string());
//                    mUserProfile = jsonObject.toString(3);
//                } catch (JSONException e) {
//                    Log.d("JSON", "Failed to parse data: " + e);
//                    Toast.makeText(HomePage.this, "Failed to parse data, watch Logcat for more details",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//
//    /**
//     * Get authentication request
//     *
//     * @param type the type of the request
//     * @return the authentication request
//     */
//    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
//        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
//                .setShowDialog(false)
//                .setScopes(new String[] { "user-read-email" }) // <--- Change the scope of your requested token here
//                .setCampaign("your-campaign-token")
//                .build();
//    }
//
//    /**
//     * Gets the redirect Uri for Spotify
//     *
//     * @return redirect Uri object
//     */
//    private Uri getRedirectUri() {
//        return Uri.parse(REDIRECT_URI);
//    }
//
//    private void cancelCall() {
//        if (mCall != null) {
//            mCall.cancel();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        cancelCall();
//        super.onDestroy();
//    }
//
//
//
//
//}


package com.example.spotifytest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifytest.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePage extends AppCompatActivity {

    public static final String CLIENT_ID = "d3136c06706142209a3c65aa74c6b9b7";
    public static final String REDIRECT_URI = "spotifytest://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode, mUserProfile;
    Button pastMonth, pastSixMonths, pastYear, saved, profilePage, settings;
    AccountsDatabaseHandler accountsDatabaseHandler = new AccountsDatabaseHandler(this);
    String username;

    public static String publicToken;
    private TextView tokenTextView, codeTextView, profileTextView;

    private YourProfile thisProfile;


        //mAccessCode = thisProfile.getCode();
        //getToken();
  
    private Call mCall;

    Button my_accountBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_welcome_page);
        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
        }
        thisProfile = accountsDatabaseHandler.getAccount(username);

        Button settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Intent intent = new Intent(HomePage.this, Settings.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button profile = (Button) findViewById(R.id.my_account_btn);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    username = bundle.getString("username");
                }
                Intent intent = new Intent(HomePage.this, ProfilePagePlaceholder.class);
//                Intent intent = new Intent(HomePage.this, ProfilePage.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });


        my_accountBtn = (Button) findViewById(R.id.my_account_btn);

        // set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });
        Button viewSaved = findViewById(R.id.viewButton);
        viewSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show top tracks popup menu
                showTopTracksPopup(v);
            }
        });

    }

    private void showTopTracksPopup(View view) {
        List<String> topTrackNames = accountsDatabaseHandler.getSavedWrapped(username);
        System.out.println("AAAAAAA " + topTrackNames);
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        Menu menu = popupMenu.getMenu();
//        if (topTrackNames.size() > 1) {
//        // Add top track names to the popup menu
//        for (int i = 1; i < topTrackNames.size(); i++) {
//            menu.add(Menu.NONE, i, Menu.NONE, topTrackNames.get(i));
//        }
//            popupMenu.show();
//        } else {
//            Toast.makeText(getApplicationContext(), "You have no saved tracks", Toast.LENGTH_SHORT).show();
//        }
        // inflate the layout for the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);
        // initialize views from the popup layout
        TextView yourNameTextView = popupView.findViewById(R.id.your_name_text);
        TextView invitesTextView = popupView.findViewById(R.id.invites_text_view);
        Button closeButton = popupView.findViewById(R.id.close_button);

        // set text for the TextViews
        yourNameTextView.setText("Top Track Names:");
        invitesTextView.setText(""); // Clear previous text

        // append top track names to the invitesTextView
        StringBuilder sb = new StringBuilder();
        for (String trackName : topTrackNames) {
            if (trackName.equals("topTracks") || trackName.equals("")) {
                continue;
            }
            sb.append(trackName).append("\n\n"); // append track name with new lines
        }
        invitesTextView.setText(sb.toString().trim());
        invitesTextView.setTextSize(20); // set the text size to 20sp (adjust as needed)

        // create a PopupWindow instance
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        // set a background drawable for the popup window
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set focusable true to enable touch events outside of the popup window
        popupWindow.setFocusable(true);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // set focusable to true to enable touch events outside the popup window to dismiss it
        popupWindow.setFocusable(true);

        // show the popup window at the center of the anchor view
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -200);

    }



     // Get token from Spotify

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(HomePage
                .this, AUTH_TOKEN_REQUEST_CODE, request);

    }


     // Get code from Spotify
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(HomePage.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /*
      When the app leaves this activity to momentarily get a token/code, this function
     fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
//            setTextAsync(mAccessToken, tokenTextView);
            publicToken = mAccessToken;
        }
        Log.d("SpotifyApiHelper", "Token: "+publicToken);
        if (publicToken != null) {
            Button tokenBtn1 = findViewById(R.id.token_btn);
            toSongs(tokenBtn1);
        }
    }

    /*
     * creates a UI thread to update a TextView in the background
     * reduces UI latency and makes the system perform more consistently
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    /*
     * get authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-top-read" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    /*
     * gets the redirect Uri for Spotify
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }

    public void toSongs(View view) {
        Bundle bundle = getIntent().getExtras();
        Context context = view.getContext();
        Intent intent = new Intent(context, SpotifyApiHelperActivitySongs.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    public void myAccount(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, LoginPage.class);
        context.startActivity(intent);
    }

    public void settings(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, Settings.class);
        context.startActivity(intent);
    }

    public void viewSaved(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ViewScreenshotActivity.class);
        context.startActivity(intent);
    }
}







