package com.example.omar.quizbot;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button startButton = (Button) findViewById(R.id.btnTryAgain);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mp = MediaPlayer.create(MainScreenActivity.this, R.raw.lightsaber_ignite);
                mp.start();
                Intent i = new Intent(MainScreenActivity.this, EnterNameActivity.class);
                startActivity(i);
            }
        });
    }
}
