package com.example.androidmemorygame;

import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class game_activity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;

    private MemoryButton[] buttons;

    private int [] buttonGraphicsLocations;

    private int[] buttonGraphics;

    private MemoryButton selected1;
    private MemoryButton selected2;

    private boolean isBusy= false; // prevent app from crash when flipping?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        GridLayout gridLayout =(GridLayout)findViewById(R.id.grid_layout_3x4);

        int numOfColumns= gridLayout.getColumnCount();
        int numOfRows= gridLayout.getRowCount();

        numberOfElements= numOfColumns*numOfRows;

        buttons = new MemoryButton[numberOfElements]; // array of memory buttons

        buttonGraphics= new int[numberOfElements/2];

        buttonGraphics[0]= R.drawable.num1; //insert image here before drawable
        buttonGraphics[1]= R.drawable.num2;
        buttonGraphics[2]= R.drawable.num3;
        buttonGraphics[3]= R.drawable.num4;
        buttonGraphics[4]= R.drawable.num5;
        buttonGraphics[5]= R.drawable.num6;

        buttonGraphicsLocations= new int[numberOfElements]; // actual array to be displayed

        shuffleButtonGraphics();

        for (int r = 0; r<numOfRows; r++)
        {
            for(int c = 0; c<numOfColumns; c++){
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicsLocations[r*numOfColumns+c]]);
                tempButton.setId(View.generateViewId());
                gridLayout.addView(tempButton);
                tempButton.setOnClickListener(this);
                buttons[r*numOfColumns+c]=tempButton;

            }


        }

    }

    protected void shuffleButtonGraphics(){
        Random rand = new Random();

        for (int i = 0; i< numberOfElements; i++){
            buttonGraphicsLocations[i]= i%(numberOfElements/2); //modulus
        } // fill in 12 elements

        for(int i =0; i<numberOfElements; i++){
            int temp = buttonGraphicsLocations[i];
            int swapIndex = rand.nextInt(12);
            buttonGraphicsLocations[i]= buttonGraphicsLocations[swapIndex];
            buttonGraphicsLocations[swapIndex] = temp;
        }

    }


    @Override
    public void onClick(View view) {

        if(isBusy)
            return;
        MemoryButton button  = (MemoryButton) view;

        if(button.isMatched)
            return;

        if(selected1== null){
            selected1 = button;
            selected1.flip();
        }

        if(selected1.getId()== button.getId())
            return;

        if(selected1.getFrontDrawableId()== button.getFrontDrawableId())
        {
            button.flip();
            button.setMatched(true);
            selected1.setEnabled(false);
            selected2.setEnabled(false);

            selected1 =null;
            return;
        }
        else{
            selected2= button;
            selected2.flip();
            isBusy=true;
            final Handler handler =  new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selected1.flip();
                    selected2.flip();
                    selected1= null;
                    selected2= null;
                    isBusy= false;
                }
            },200);
        }





    }
}