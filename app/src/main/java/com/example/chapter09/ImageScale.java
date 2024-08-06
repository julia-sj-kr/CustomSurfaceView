package com.example.chapter09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;

public class ImageScale extends View {
    private Drawable image;
    private ScaleGestureDetector gestureDetector;
    private float scale=1.0f;

    public ImageScale(Context context) {
        super(context);
        image=context.getResources().getDrawable(R.drawable.movie1);
        setFocusable(true);
        image.setBounds(0,0,image.getIntrinsicWidth(),image.getIntrinsicHeight());
        gestureDetector=new ScaleGestureDetector(context, new ScaleListener());
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //현재 캔버스의 크기와 중심점의 정보를 저장하고
        canvas.save();
        //그리는것
        canvas.scale(scale,scale);
        //스케일은 아래와 같이 4개의 인자로도 호출할 수 있다. x,y축의 확대 또는 축소, x,y축의 위치
        //canvas.scale(scale*2,scale,5f,5f);
        image.draw(canvas);
        canvas.restore();
    }

    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            scale*=detector.getScaleFactor();

            if (scale<0.1f) scale=0.1f;
            if (scale>10.0f) scale=10.0f;
            Thread.dumpStack();

            invalidate();
            return true;
        }
    }
}

