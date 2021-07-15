package com.example.androidmemorygame;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidmemorygame.R;


public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

    }

    @Override
    public void onBackPressed() {

        Intent intent1 = new Intent(NextActivity.this, MainActivity.class);
        startActivity(intent1);
        super.onBackPressed();
    }
}
