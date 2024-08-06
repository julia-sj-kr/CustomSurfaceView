package com.example.chapter09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SingleTouchView extends View {
    private Paint paint=new Paint();
    private Path path=new Path();

    public SingleTouchView(Context context, AttributeSet attrs){
        super(context,attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    protected void onDraw(Canvas canvas){
        canvas.drawPath(path,paint);
    }

    public boolean onTouchEvent(MotionEvent event){
        float eventX=event.getX();
        float eventY=event.getY();

         if (event.getAction()==MotionEvent.ACTION_DOWN){
             path.moveTo(eventX,eventY);
             return true;
         }
         else if (event.getAction()==MotionEvent.ACTION_MOVE) {
             path.lineTo(eventX,eventY);
         }

         else if (event.getAction()==MotionEvent.ACTION_UP) {

         }
         else {return false;}

         invalidate();
         return true;
    }
}



















