package com.example.androidmemorygame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import java.util.Random;

public class gameActivity extends AppCompatActivity implements View.OnClickListener{

    private int numOfElements;
    private MemoryButton[] buttons;
    //the position
    private int [] buttonGraphicLocations;
    //the images
    private int [] buttonGraphics;

    private MemoryButton selected1;
    private MemoryButton selected2;

    private boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        MediaPlayer music = MediaPlayer.create(gameActivity.this, R.raw.backgroundmusic);
        music.start();



        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridlayout4x3);

        int numColumns = gridLayout.getColumnCount();
        int numRow = gridLayout.getRowCount();

        numOfElements = numColumns * numRow;

        buttons = new MemoryButton[numOfElements];

        buttonGraphics = new int [numOfElements/2];

        buttonGraphics[0]= R.drawable.a;
        buttonGraphics[1]= R.drawable.b;
        buttonGraphics[2]= R.drawable.c;
        buttonGraphics[3]= R.drawable.d;
        buttonGraphics[4]= R.drawable.e;
        buttonGraphics[5]= R.drawable.f;

        //let us know which drawable, button should have
        buttonGraphicLocations = new int [numOfElements];

        shuffleButtonsGraphic();

        for(int r = 0; r<numRow; r++){
            for (int c = 0; c<numColumns; c++){
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicLocations[r*numColumns +c]]);
                //let android generate ID to prevent clash
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r *numColumns +c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

    }

    protected void shuffleButtonsGraphic(){
        Random rand = new Random();

        //fill in default values for the button location
        for(int i = 0; i <numOfElements; i++){

            buttonGraphicLocations[i] = i % (numOfElements/2);
        }

        for(int i = 0; i<numOfElements;i++){
            int temp = buttonGraphicLocations[i];
            int swaplocation = rand.nextInt(12);

            buttonGraphicLocations[i] = buttonGraphicLocations[swaplocation];
            buttonGraphicLocations[swaplocation] = temp;

        }
    }

    @Override
    public void onClick(View view) {
        //isbusy prevent crash
        if (isBusy)
            return;

        MemoryButton button = (MemoryButton) view;

        if(button.isMatched)
            return;
        //just clicked 1 button need to store selection
        if(selected1 == null){
            selected1 = button;
            selected1.flip();
            return;
        }
        //if selected same button, ignore
        if(selected1.getId()== button.getId())
        {
            return;
        }
        if (selected1.getFrontImageDrawableId() == button.getFrontImageDrawableId()){
            button.flip();
            button.setMatched(true);
            selected1.setMatched(true);

            MediaPlayer score = MediaPlayer.create(gameActivity.this, R.raw.score);
            score.start();

            //dont let user press again if matched
            selected1.setEnabled(false);


            //return null value of select button 1
            selected1 = null;

            return;
        }
        else{
            selected2 = button;
            selected2.flip();
            isBusy =true;


            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selected2.flip();
                    selected1.flip();
                    selected1 = null;
                    selected2 = null;
                    isBusy = false;
                }
            },200);

        }
    }
}
