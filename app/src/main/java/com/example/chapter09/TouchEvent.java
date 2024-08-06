package com.example.chapter09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//342page 예제

public class TouchEvent extends View {

    private List<Circle> circles = new ArrayList<>();
    private Paint paint;

    public TouchEvent(Context context) {
        super(context);
        paint = new Paint();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Circle circle : circles) {
            paint.setColor(circle.color);
            canvas.drawCircle(circle.x, circle.y, circle.radius, paint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Random random = new Random();
            float radius = random.nextInt(300);
            int color = Color.rgb(
                    random.nextInt(256), random.nextInt(256), random.nextInt(256)
            );
            float x = event.getX();
            float y = event.getY();

            circles.add(new Circle(x, y, radius, color));
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }


    private class Circle {
        float x, y, radius;
        int color;

        Circle(float x, float y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }

}