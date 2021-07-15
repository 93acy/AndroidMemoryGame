package com.example.androidmemorygame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TableLayout;

public class MemoryButton  extends androidx.appcompat.widget.AppCompatButton {

    protected int row;
    protected int column;
    protected int frontImageDrawableId;

    protected boolean isFlipped= false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    public MemoryButton(Context context, int r, int c, int frontImageDrawableId)
    {
        super(context);
        row = r;
        column = c;
        this.frontImageDrawableId = frontImageDrawableId;

        front = context.getDrawable(frontImageDrawableId);
        back = context.getDrawable(R.drawable.yugiohback);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        tempParams.width = (int) getResources().getDisplayMetrics().density * 100;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 200;
//        tempParams.height = TableLayout.LayoutParams.WRAP_CONTENT;
//        tempParams.width = TableLayout.LayoutParams.WRAP_CONTENT;
        tempParams.setMargins(40, 100, 5, 20);

        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontImageDrawableId() {
        return frontImageDrawableId;
    }

    //check if matched
    public void flip(){

        //chose something alr done with
        if(isMatched)

            return;
        if(isFlipped){
            setBackground(back);
            isFlipped = false;
        }

        else{
            setBackground(front);
            isFlipped = true;
        }
    }
}

