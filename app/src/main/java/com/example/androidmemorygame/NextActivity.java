package com.example.androidmemorygame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidmemorygame.R;

import java.util.ArrayList;
import java.util.List;


public class NextActivity extends AppCompatActivity {

    ArrayList<Integer> selected;

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
