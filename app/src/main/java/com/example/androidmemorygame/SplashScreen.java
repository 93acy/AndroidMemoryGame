package com.example.androidmemorygame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer startsound = MediaPlayer.create(SplashScreen.this, R.raw.startsound);
        startsound.start();

    Intent intent = new Intent(this,MainActivity.class);
    startActivity(intent);
    finish();

    }
}