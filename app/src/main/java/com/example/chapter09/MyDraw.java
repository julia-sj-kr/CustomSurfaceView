package com.example.chapter09;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyDraw extends View {
    private Paint paint = new Paint();
    private Path path=new Path();
    private int paintColor=0xFF000000;//검정색
    //private int paintColor=0xFFFFFFFF;//하얀색
    private Paint canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    //AttributeSet: XML 레이아웃 파일에서 정의된 속성 값을 포함하는 객체입니다. 이 객체를 사용하여 XML에서 설정한 속성 값을 가져와서 사용자 정의 뷰를 초기화할 수 있습니다.
    public MyDraw(Context context, AttributeSet attrs){
        super(context,attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(paintColor);
        //Paint.Style 열거형은 다음과 같은 세 가지 스타일을 제공합니다:
        //1.Paint.Style.FILL: 도형을 채우는 스타일
        //2.Paint.Style.STROKE: 도형의 테두리만 그리는 스타일
        //3.Paint.Style.FILL_AND_STROKE:  도형을 채우고 테두리도 그리는 스타일
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        canvasBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas=new Canvas(canvasBitmap);
        canvasPaint=new Paint(Paint.DITHER_FLAG);
    }

    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap,0,0,canvasPaint);
        canvas.drawPath(path,paint);
    }
    public boolean onTouchEvent(MotionEvent event){
        float touchX=event.getX();
        float touchY=event.getY();
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            path.moveTo(touchX,touchY);
        } else if (event.getAction()==MotionEvent.ACTION_MOVE){
            path.lineTo(touchX,touchY);
        } else if (event.getAction()==MotionEvent.ACTION_UP){
            drawCanvas.drawPath(path,paint);
            path.reset();
        } else return false;
        invalidate();
        return true;
    }

    public void setColor(String newColor){
        invalidate();
        paintColor= Color.parseColor(newColor);
        paint.setColor(paintColor);
    }
}
