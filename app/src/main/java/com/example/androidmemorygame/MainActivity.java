package com.example.androidmemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;

        if (id == R.id.startGame) {
            intent = new Intent(MainActivity.this, gameActivity.class);
        }
        if (intent != null) {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

    }
}