package com.example.androidmemorygame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
   private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer startsound = MediaPlayer.create(SplashScreen.this, R.raw.startsound);
        startsound.start();

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent();
                intent.setClass(SplashScreen.this,
                        MainActivity.class);

                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }}
