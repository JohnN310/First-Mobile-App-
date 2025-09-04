package com.example.spotifytest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifytest.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleWelcomePage_Testing extends AppCompatActivity {

    public static final String CLIENT_ID = "d3136c06706142209a3c65aa74c6b9b7";
    public static final String REDIRECT_URI = "spotifytest://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;

    public static String publicToken;
    private TextView tokenTextView, codeTextView, profileTextView;

    private Call mCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_welcome_page);
        // initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);

        tokenTextView = (TextView) findViewById(R.id.token_text_view);

        // set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

    }

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(SimpleWelcomePage_Testing
                .this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(SimpleWelcomePage_Testing.this, AUTH_CODE_REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
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

    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-top-read" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

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
        Context context = view.getContext();
        Intent intent = new Intent(context, SpotifyApiHelperActivitySongs.class);
        context.startActivity(intent);
    }
}