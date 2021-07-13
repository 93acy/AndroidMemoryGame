package com.example.androidmemorygame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends androidx.appcompat.widget.AppCompatButton{

        protected int column;
        protected int row;
        protected int frontDrawableId;
        protected boolean isFlipped;
        protected boolean isMatched;

        protected Drawable front;
        protected Drawable back;

        public MemoryButton(Context context, int r,int c , int frontImageDrawableId) {
                super(context);
                row =r;
                column = c;
                frontDrawableId= frontImageDrawableId;


                front = context.getDrawable(frontImageDrawableId);
                back = context.getDrawable(R.drawable.yugioh_back);

                GridLayout.LayoutParams temp_param = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

                setLayoutParams(temp_param);

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

        public void flip(){
                if(isMatched)
                        return;
                if (isFlipped){
                        setBackground(back);
                        isFlipped = false;
                }
                else{
                        setBackground(front);
                        isFlipped=true;
                }
}
}
