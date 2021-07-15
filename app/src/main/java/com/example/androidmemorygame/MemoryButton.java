package com.example.androidmemorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends AppCompatButton {
    protected int row;
    protected int column;
    protected String frontDrawablePath;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Bitmap front;
    protected Drawable back;

    public MemoryButton(Context context, int r, int c,
                        String frontDrawablePath) {
        super(context);

        row = r;
        column = c;
        this.frontDrawablePath = frontDrawablePath;

        //noinspection RestrictedApi
        front = BitmapFactory.decodeFile(frontDrawablePath);
        //noinspection RestrictedApi
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.cross);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r, 1f), GridLayout.spec(c, 1f));
        tempParams.width = 0;
        tempParams.height = 0;
        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public String getFrontDrawablePath() {
        return frontDrawablePath;
    }

    public void flip() {
        if (isMatched) {
            return;
        }

        if (isFlipped) {
           setBackground(back);
           isFlipped = false;
        } else {
            setBackground(new BitmapDrawable(getResources(), front));
            isFlipped = true;
        }

    }
}
