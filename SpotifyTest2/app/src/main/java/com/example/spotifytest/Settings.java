package com.example.spotifytest;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    Button goToHome;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.settings);

        goToHome = (Button) findViewById(R.id.goToHome);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Context context = v.getContext();
                Intent intent = new Intent(context, HomePage.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });


    }

}
