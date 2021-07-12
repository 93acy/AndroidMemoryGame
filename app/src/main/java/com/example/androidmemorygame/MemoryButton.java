package com.example.androidmemorygame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends AppCompatButton {
    protected int row;
    protected int column;
    protected int frontDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    public MemoryButton(Context context, int r, int c,
                        int frontImageDrawableId) {
        super(context);

        row = r;
        column = c;
        frontDrawableId = frontImageDrawableId;

        //noinspection RestrictedApi
        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        //noinspection RestrictedApi
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.cross);

        setBackground(back);

//        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
//        tempParams.width = (int) getResources().getDisplayMetrics().density * 50;
//        tempParams.height = (int) getResources().getDisplayMetrics().density * 50;
//        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public void flip() {
        if (isMatched) {
            return;
        }

        if (isFlipped) {
           setBackground(back);
           isFlipped = false;
        } else {
            setBackground(front);
            isFlipped = true;
        }


    }
}
