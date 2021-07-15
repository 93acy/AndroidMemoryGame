package com.example.androidmemorygame;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

//    ActivityResultLauncher<Intent> rlselected;

    private ArrayList<Integer> selected;

    private int numberOfElements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocations;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private boolean isBusy = false;

    private Chronometer timeElapsed;
    private int numCorrectMatches;
    private TextView correct_matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
//        registerForImages();

        Intent intent= getIntent();
        selected = (ArrayList<Integer>)intent.getIntegerArrayListExtra("selected");


        numCorrectMatches = 0;
        correct_matches = (TextView) findViewById(R.id.num_correct_matches);
        correct_matches.setText(numCorrectMatches + " " + getString(R.string.of_matches));

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout_4x3);

        int numRows = gridLayout.getRowCount();
        int numColumns = gridLayout.getColumnCount();

        numberOfElements = numRows * numColumns;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

        for(int i =0; i<buttonGraphics.length;i++){
            buttonGraphics[i]=selected.get(i);
        }

//        buttonGraphics[0] = R.drawable.b1;
//        buttonGraphics[1] = R.drawable.b2;
//        buttonGraphics[2] = R.drawable.b3;
//        buttonGraphics[3] = R.drawable.b4;
//        buttonGraphics[4] = R.drawable.b5;
//        buttonGraphics[5] = R.drawable.b6;

        buttonGraphicLocations = new int[numberOfElements];

        shuffleButtonGraphics();

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicLocations[r * numColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

        timeElapsed  = (Chronometer) findViewById(R.id.chronom);
        timeElapsed.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });
        timeElapsed.setBase(SystemClock.elapsedRealtime());
        timeElapsed.start();

    }

    protected void shuffleButtonGraphics() {
        Random rand =new Random();

        for (int i = 0; i < numberOfElements; i++) {
            buttonGraphicLocations[i] = i % (numberOfElements / 2);
        }

        for (int i = 0; i < numberOfElements; i++) {
            int temp = buttonGraphicLocations[i];
            int swapIndex = rand.nextInt(12);
            buttonGraphicLocations[i] = buttonGraphicLocations[swapIndex];
            buttonGraphicLocations[swapIndex] = temp;
        }
    }

//    protected void registerForImages(){
//        rlselected = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result->{
//                    if(result.getResultCode()==AppCompatActivity.RESULT_OK){
//                        Intent data= result.getData();
//                        if(data!=null){
//                            selected = data.getIntegerArrayListExtra("selected");
//                        }
//                    }
//                }
//        );
//
//    }


    @Override
    public void onClick(View v) {
        if (isBusy) {
            return;
        }

        MemoryButton button = (MemoryButton) v;

        if (button.isMatched) {
            return;
        }

        if (selectedButton1 == null) {
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if (selectedButton1.getId() == button.getId()) {
            return;
        }

        if (selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()) {
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);
            numCorrectMatches++;
            if (numCorrectMatches == 6) {
                timeElapsed.stop();
            }
            selectedButton1.setEnabled(false);
            button.setEnabled(false);
            selectedButton1 = null;
            correct_matches.setText(numCorrectMatches + " " + getString(R.string.of_matches));
        } else {
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                }
            }, 500);
        }
    }
}
