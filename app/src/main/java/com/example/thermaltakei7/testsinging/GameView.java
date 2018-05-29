package com.example.thermaltakei7.testsinging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Class which creates the arrow that shows Singer's pitch and moves it when needed
 */

public class GameView extends View {

    Paint linePaint = new Paint();

    private int canvasWidth;
    private int canvasHeight;
    private int singerArrowX = 170;



    public GameView(Context context) {
        super(context);

        linePaint.setColor(Color.BLACK);


    }


    /**
     * Logic to move the singer arrow, which is not complete at the moment
     */
    @Override
    protected void onDraw(Canvas canvas) {

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawLine(singerArrowX + 15, 0, singerArrowX + 15, canvasHeight, linePaint);

    }

}

